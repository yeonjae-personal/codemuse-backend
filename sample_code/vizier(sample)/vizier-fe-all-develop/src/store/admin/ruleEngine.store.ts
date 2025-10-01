import { v4 as uuidv4 } from "uuid";
import head from "lodash-es/head";
import cloneDeep from "lodash-es/cloneDeep";
import {
  addUpdateCategory,
  addUpdateRule,
  deleteSubCategory,
  generateHTMLReport,
  getListRules,
  getRuleStructure,
  ruleValidation,
  saveRuleStructure,
  testRuleStructure,
} from "@/api/admin/rule-engine/ruleEngineApi";
import { getUserInfor } from "@/constants/userInfor";
import {
  type ListRules,
  type ConditionGroup,
  type LogicType,
  type RuleDetail,
  type Condition,
  TABS_RULE_DETAIL,
  SubCategories,
  TestRuleEnginePayload,
  SaveRuleEnginePayload,
  RuleValidationResponse,
} from "@/interfaces/admin/rule-engine";
import type { IFieldItem } from "@/interfaces/admin/rule-field";
import useSnackbarStore from "../snackbar.store";
import { EMPTY_RULE } from "@/constants/admin/rule-engine";

type State = {
  listRules: ListRules[];
  listRulesTemp: ListRules[];
  listExpanedRule: string[];
  ruleStructure: ConditionGroup | null;
  ruleStructureTemp: ConditionGroup | null;
  searchBy: string;
  searchName: string;
  selectedRule: string;
  selectedSubCate: string;
  selectedCate: string;
  selectedTab: TABS_RULE_DETAIL;
  ruleDetail: RuleDetail;
  ruleDetailTemp: RuleDetail;
  isEditRule: boolean;
  selectedNodeId: string;
  isRefresh: boolean;
  isShowRuleStructure: boolean;
  isShowRuleField: boolean;
  isShowRuleTest: boolean;
  isShowRuleList: boolean;
  isShowRuleDetail: boolean;
  isShowRuleReport: boolean;
  testRule: Condition[];
  testRuleTemp: Condition[];
  isEditRuleStructure: boolean;
  ruleMsg: string;
  tempRuleMsg: string;
  failedCondUuids: string[];
  passed: boolean;
  passedCondUuids: string[];
  requiredCondUuids: string[];
  passedMessage: string | null;
  isTested: boolean;
  isDeletedCondition: boolean;
  isExpanded: boolean;
  ruleValidation: RuleValidationResponse | null;
  isShowReport: boolean;
  ruleReportContent: string;
  isLoadingValidate: boolean;
  isLoadingReport: boolean;
};

const initRuleDetail = {
  ruleId: "",
  ruleName: "",
  department: "",
  user: "",
  creationDate: "",
  overview: "",
  isAddNew: false,
  useYn: true,
  subCategoryId: "",
  categoryId: "",
  ruleMsg: "",
};

const useRuleEngineStore = defineStore("ruleEngineStore", {
  state: (): State => ({
    listRules: [],
    listRulesTemp: [],
    listExpanedRule: [],
    ruleStructure: null,
    ruleStructureTemp: null,
    searchBy: "rule",
    searchName: "",
    selectedRule: "",
    selectedSubCate: "",
    selectedCate: "",
    selectedTab: TABS_RULE_DETAIL.ATTRIBUTES,
    ruleDetail: initRuleDetail,
    ruleDetailTemp: initRuleDetail,
    isEditRule: false,
    selectedNodeId: "",
    isRefresh: false,
    isShowRuleStructure: false,
    isShowRuleField: false,
    isShowRuleTest: false,
    isShowRuleList: true,
    isShowRuleDetail: false,
    isShowRuleReport: false,
    testRule: [],
    testRuleTemp: [],
    isEditRuleStructure: false,
    ruleMsg: "",
    tempRuleMsg: "",
    failedCondUuids: [],
    passed: false,
    passedCondUuids: [],
    passedMessage: null,
    isTested: false,
    requiredCondUuids: [],
    isDeletedCondition: false,
    isExpanded: false,
    ruleValidation: null,
    isShowReport: false,
    ruleReportContent: "",
    isLoadingValidate: false,
    isLoadingReport: false,
  }),
  getters: {
    isEmptyStructure(state: State): boolean {
      return !state.ruleStructure;
    },
    isStartAndGroup(state: State): boolean {
      return (
        !!state.ruleStructure &&
        state.ruleStructure.logicType === "AND" &&
        state.ruleStructure.condition.length > 0
      );
    },
    isStartOrGroup(state: State): boolean {
      return (
        !!state.ruleStructure &&
        state.ruleStructure.logicType === "OR" &&
        state.ruleStructure.condition.length > 0
      );
    },
    isAddNewRule(state: State): boolean {
      return state.ruleDetail.isAddNew;
    },
    aiConditionUuids(state: State): string[] {
      return [
        ...new Set(
          state.ruleValidation?.issues.map(
            ({ condUuid }) => condUuid
          ) as string[]
        ),
      ];
    },
  },
  actions: {
    addRule(subCategoryId: string, categoryId: string) {
      const userInfor = getUserInfor();
      this.setSelectedRule("");
      this.isShowRuleStructure = false;
      this.listRulesTemp = cloneDeep(this.listRules);
      const ruleId = uuidv4();
      this.listRules.map((item) => {
        const subCates = item.subCategories.find(
          (subCate) => subCate.subCategoryId === subCategoryId
        );
        if (subCates) {
          subCates.rules.push({
            ruleId: ruleId,
            ruleName: "",
            isAddNew: true,
            department: "",
            user: "",
            overview: "",
            creationDate: "",
            useYn: true,
            categoryId: categoryId,
            subCategoryId: subCategoryId,
            ruleMsg: "",
          });
          this.selectedRule = ruleId;
        }
        return { ...item, subCategories: subCates || item.subCategories };
      });
      this.ruleDetail = {
        ruleId: ruleId,
        ruleName: "",
        department: userInfor.chgDeptName,
        user: userInfor.chgUser,
        overview: "",
        creationDate: "",
        isAddNew: true,
        useYn: true,
        categoryId: categoryId,
        subCategoryId: subCategoryId,
        ruleMsg: "",
      };
      this.ruleStructure = null;
      this.testRuleTemp = [];
      this.testRule = [];
      this.isRefresh = true;
      this.isEditRule = true;
      this.isShowRuleDetail = true;
    },
    addCategory(categoryId: string) {
      this.listRules.map((item) => {
        if (item.categoryId === categoryId) {
          const subCategoryId = uuidv4();
          item.subCategories.push({
            subCategoryId: subCategoryId,
            subCategoryName: "",
            categoryId: categoryId,
            className: "blue",
            isAddNew: true,
            rules: [],
          });
          this.selectedSubCate = subCategoryId;
        }
        return item;
      });
    },
    removeAddNewCategory() {
      this.listRules = this.listRules.map((item) => ({
        ...item,
        subCategories: item.subCategories.filter((sub) => !sub.isAddNew),
      }));
      this.selectedSubCate = "";
    },
    removeAddNewRule() {
      this.isShowRuleStructure = false;
      this.listRules = this.listRules.map((cate) => {
        return {
          ...cate,
          subCategories: cate.subCategories.map((subCate) => ({
            ...subCate,
            rules: subCate.rules.filter((rule) => !rule.isAddNew),
          })),
        };
      });
      this.ruleDetail = initRuleDetail;
      this.ruleStructure = null;
      this.isShowRuleStructure = true;
      this.isRefresh = true;
      this.testRuleTemp = [];
      this.testRule = [];
    },
    setSelectedRule(ruleId: string) {
      this.selectedRule = ruleId;
      this.isShowRuleDetail = !!ruleId;
      this.isShowRuleField = false;
      this.isShowRuleTest = false;
      this.isShowRuleReport = false;
    },
    setSelectedSubCate(subCateId: string) {
      this.selectedSubCate = subCateId;
    },
    setSelectedCate(cateId: string) {
      this.selectedCate = cateId;
    },
    setSelectedTab(tab: TABS_RULE_DETAIL) {
      this.selectedTab = tab;
    },
    setEditRule(isEdit: boolean) {
      this.isEditRule = isEdit;
      if (isEdit) {
        this.listRulesTemp = cloneDeep(this.listRules);
        this.ruleDetailTemp = cloneDeep(this.ruleDetail);
      }
    },
    updateExpandedRule(categoryId: string, isAddNew: boolean = false) {
      if (
        this.listExpanedRule.find((item) => item === categoryId) &&
        !isAddNew
      ) {
        this.listExpanedRule = this.listExpanedRule.filter(
          (item) => item !== categoryId
        );
      } else {
        this.listExpanedRule.push(categoryId);
      }
    },
    updateRuleTest(conditions: Condition[]) {
      const tempMap = new Map(
        this.testRuleTemp.map((temp) => [temp.fieldUuid, temp.value])
      );
      this.testRule = conditions.map((item) => ({
        ...item,
        value: tempMap.get(item.fieldUuid) || "",
      }));
    },
    resetRuleStructure() {
      this.ruleStructure = cloneDeep(this.ruleStructureTemp);
      this.ruleMsg = cloneDeep(this.tempRuleMsg) || "";
      this.passedCondUuids = [];
      this.failedCondUuids = [];
      this.testRule = [];
      this.passedMessage = null;
      this.testRuleTemp = [];
      this.requiredCondUuids = [];
      this.passed = false;
      this.isTested = false;
      this.ruleValidation = null;
      this.ruleReportContent = "";
    },
    async setRuleDetail(ruleDetail: RuleDetail) {
      const { showSnackbar } = useSnackbarStore();
      try {
        this.ruleDetail = ruleDetail;
        this.ruleMsg = cloneDeep(ruleDetail.ruleMsg) || "";
        this.tempRuleMsg = cloneDeep(ruleDetail.ruleMsg) || "";
        this.isRefresh = true;
        this.testRuleTemp = [];
        this.testRule = [];
      } catch (error: any) {
        showSnackbar(error.errorMsg, "error");
      }
    },
    async getRuleStructureDetail(ruleUuid: string): Promise<void> {
      const { showSnackbar } = useSnackbarStore();
      try {
        this.isShowRuleStructure = false;
        const response = await getRuleStructure({ ruleUuid });
        if (JSON.stringify(response.data) === JSON.stringify(EMPTY_RULE)) {
          this.ruleStructure = null;
          this.ruleStructureTemp = null;
        } else {
          this.ruleStructure = response.data;
          this.ruleStructureTemp = cloneDeep(response.data);
        }
        this.isShowRuleStructure = true;
        this.isEditRuleStructure = false;
        this.failedCondUuids = [];
        this.passed = false;
        this.isTested = false;
        this.passedCondUuids = [];
        this.requiredCondUuids = [];
        this.passedMessage = null;
      } catch (error: any) {
        showSnackbar(error.errorMsg, "error");
      }
    },
    async testRuleStructure(
      fields: Record<string, string | number>
    ): Promise<void> {
      const { showSnackbar } = useSnackbarStore();
      try {
        const conditionGroupDto = this.formatPayloadSaveAndTest();
        const payload: TestRuleEnginePayload = {
          ruleName: this.ruleDetail.ruleName,
          ruleMsg: this.ruleMsg,
          factsData: fields,
          conditionGroupDto: conditionGroupDto as ConditionGroup,
        };
        const response = await testRuleStructure(payload);
        this.failedCondUuids = response.data.failedCondUuids;
        this.passed = response.data.passed;
        this.passedCondUuids = response.data.passedCondUuids;
        this.passedMessage = response.data.passedMessage;
        this.isTested = true;
      } catch (error: any) {
        showSnackbar(error.errorMsg, "error");
      }
    },
    async saveRuleStructure(): Promise<boolean> {
      const { showSnackbar } = useSnackbarStore();
      try {
        const conditionGroup = this.formatPayloadSaveAndTest();
        const payload: SaveRuleEnginePayload = {
          ruleName: this.ruleDetail.ruleName,
          ruleMsg: this.ruleMsg,
          ruleUuid: this.ruleDetail.ruleId,
          conditionTree: conditionGroup as ConditionGroup,
        };
        await saveRuleStructure(payload);
        return true;
      } catch (error: any) {
        showSnackbar(error.errorMsg, "error");
        return false;
      }
    },
    async aiValidateRule(): Promise<void> {
      const { showSnackbar } = useSnackbarStore();
      try {
        this.isLoadingValidate = true;
        this.isShowReport = false;
        const conditionGroup = this.formatPayloadSaveAndTest();
        const payload: SaveRuleEnginePayload = {
          ruleName: this.ruleDetail.ruleName,
          ruleMsg: this.ruleMsg,
          ruleUuid: this.ruleDetail.ruleId,
          conditionTree: conditionGroup as ConditionGroup,
        };
        const response = await ruleValidation([payload]);
        this.ruleValidation = response.data;
      } catch (error: any) {
        if (typeof error === "string") {
          showSnackbar(error, "error");
        } else {
          showSnackbar(error.errorMsg, "error");
        }
      } finally {
        this.isLoadingValidate = false;
      }
    },
    async aiReport(): Promise<void> {
      const { showSnackbar } = useSnackbarStore();
      try {
        if (this.ruleValidation) {
          this.isLoadingReport = true;
          const response = await generateHTMLReport(this.ruleValidation);
          this.ruleReportContent = response.data.report || "";
        }
      } catch (error: any) {
        if (typeof error === "string") {
          showSnackbar(error, "error");
        } else {
          showSnackbar(error.errorMsg, "error");
        }
      } finally {
        this.isLoadingReport = false;
      }
    },
    resetSearch() {
      this.selectedCate = "";
      this.selectedSubCate = "";
      this.selectedRule = "";
      this.isEditRule = false;
      this.ruleDetail = initRuleDetail;
      this.ruleStructure = null;
      this.isShowRuleStructure = false;
      this.isShowRuleTest = false;
      this.isShowRuleField = false;
      this.isRefresh = true;
      this.testRuleTemp = [];
      this.testRule = [];
      this.requiredCondUuids = [];
    },
    validateRuleDetail() {
      let message = "";
      if (!this.ruleDetail.ruleName) {
        message = "product_platform.required_field_missing";
      }
      return message;
    },
    updateRuleItem(ruleId: string, name: string, useYn: boolean) {
      this.listRules = this.listRules.map((item) => ({
        ...item,
        subCategories: item.subCategories.map((sub) => ({
          ...sub,
          rules: sub.rules.map((rule) => ({
            ...rule,
            ruleName: rule.ruleId === ruleId ? name : rule.ruleName,
            useYn: rule.ruleId === ruleId ? useYn : rule.useYn,
          })),
        })),
      }));
    },
    async getListRules(name: string, searchBy: string = "") {
      const { showSnackbar } = useSnackbarStore();
      try {
        const response = await getListRules({ name, searchBy });
        this.listRules = response.data.map((item, index) => {
          return {
            categoryId: item.ruleCtgrUuid,
            categoryName: item.ruleCtgrName,
            subCategories: item.children.map((child) => ({
              subCategoryId: child.ruleCtgrUuid,
              subCategoryName: child.ruleCtgrName,
              categoryId: item.ruleCtgrUuid,
              className: index % 2 === 0 ? "blue" : "green",
              isAddNew: false,
              rules: child.rules.map((rule) => ({
                ruleId: rule.ruleUuid,
                ruleName: rule.ruleName,
                overview: rule.ovwCntn,
                department: rule.chgDeptName,
                user: rule.chgUser,
                creationDate: rule.rgstDtm,
                isAddNew: false,
                useYn: rule.useYn === "Y",
                categoryId: item.ruleCtgrUuid,
                subCategoryId: child.ruleCtgrUuid,
                ruleMsg: rule.ruleMsg || "",
              })),
            })),
          };
        });
      } catch (error: any) {
        showSnackbar(error.errorMsg, "error");
      }
    },
    async createUpdateSubCategory(subCate: SubCategories) {
      const { showSnackbar } = useSnackbarStore();
      try {
        const response = await addUpdateCategory({
          ruleCtgrUuid: subCate.isAddNew ? null : subCate.subCategoryId,
          ruleCtgrName: subCate.subCategoryName,
          hpstRuleCtgrUuid: subCate.categoryId,
        });
        return response;
      } catch (error: any) {
        showSnackbar(error.errorMsg, "error");
      }
    },
    async updateCategoryName(cate: ListRules) {
      const { showSnackbar } = useSnackbarStore();
      try {
        const response = await addUpdateCategory({
          ruleCtgrUuid: cate.categoryId,
          ruleCtgrName: cate.categoryName,
          hpstRuleCtgrUuid: null,
        });
        return response;
      } catch (error: any) {
        showSnackbar(error.errorMsg, "error");
      }
    },
    async deleteCategory(categoryId: string) {
      const { showSnackbar } = useSnackbarStore();
      try {
        return await deleteSubCategory(categoryId);
      } catch (error: any) {
        showSnackbar(error.errorMsg, "error");
      }
    },
    async createUpdateRule() {
      const { showSnackbar } = useSnackbarStore();
      try {
        const response = await addUpdateRule({
          ruleUuid: this.ruleDetail.isAddNew ? null : this.ruleDetail.ruleId,
          ruleName: this.ruleDetail.ruleName,
          ruleCtgrUuid: this.ruleDetail.subCategoryId || "",
          chgDeptName: this.ruleDetail.department,
          chgUser: this.ruleDetail.user,
          useYn: this.ruleDetail.useYn ? "Y" : "N",
          ovwCntn: this.ruleDetail.overview,
        });
        const ruleId = response.status === 200 ? response.data.ruleUuid : "";
        if (this.ruleDetail.isAddNew) {
          await this.getRuleStructureDetail(ruleId);
        }
        if (ruleId) {
          this.selectedRule = ruleId;
          this.setRuleDetail({
            ...this.ruleDetail,
            creationDate: response?.data?.rgstDtm || "",
            isAddNew: false,
            ruleId: ruleId,
          });
        }
        return response;
      } catch (error: any) {
        showSnackbar(error.errorMsg, "error");
      }
    },
    createCondition(count?: number): Condition | Condition[] {
      if (!count) {
        return {
          condUuid: uuidv4(),
          keyName: "",
          dispName: "",
          operator: "",
          value: "",
          fieldUuid: "",
          fieldDataType: "",
        };
      }
      return Array.from({ length: count }, () => ({
        condUuid: uuidv4(),
        keyName: "",
        dispName: "",
        operator: "",
        value: "",
        fieldUuid: "",
        fieldDataType: "",
      }));
    },
    getNewGroupCondition(logicType: LogicType, count: number): ConditionGroup {
      return {
        logicType,
        condition: this.createCondition(count) as Condition[],
      };
    },
    createStructureNode(
      logicType: LogicType,
      isFirstNode: boolean = false
    ): void {
      if (!this.ruleStructure) {
        if (logicType === "OR") {
          const ruleStructure = this.getNewGroupCondition(logicType, 2);
          this.ruleStructure = {
            logicType: "AND",
            condition: [ruleStructure],
          };
          this.selectedNodeId = ruleStructure.condition[0].condUuid!;
          return;
        }
        this.ruleStructure = this.getNewGroupCondition(logicType, 1);
        this.selectedNodeId = this.ruleStructure.condition[0].condUuid!;
        return;
      }
      if (isFirstNode) {
        let newStructure: ConditionGroup | null = null;
        if (this.ruleStructure.logicType === "OR") {
          newStructure = {
            logicType: "AND",
            condition: [],
          };
        }
        if (logicType === "AND") {
          if (this.ruleStructure.logicType === "OR") {
            const newCondition: Condition = this.createCondition() as Condition;
            this.ruleStructure = {
              ...newStructure,
              condition: [newCondition, this.ruleStructure],
            } as ConditionGroup;
            this.selectedNodeId = newCondition.condUuid!;
            return;
          }
          const newCondition: Condition = this.createCondition() as Condition;
          this.ruleStructure.condition = [
            newCondition,
            ...this.ruleStructure.condition,
          ];
          this.selectedNodeId = newCondition.condUuid!;
          return;
        }
        if (this.ruleStructure.logicType === "OR") {
          this.ruleStructure = {
            ...newStructure,
            condition: [this.ruleStructure],
          } as ConditionGroup;
        }
        const firstCondition = head(this.ruleStructure.condition) || null;
        if (firstCondition?.logicType === "OR") {
          const newCondition = this.createCondition() as Condition;
          firstCondition.condition = [
            ...(firstCondition.condition ?? []),
            newCondition,
          ] as Condition[];
          this.selectedNodeId = newCondition.condUuid!;
          return;
        }
        const newGroup = this.getNewGroupCondition(logicType, 2);
        this.ruleStructure.condition = [
          newGroup,
          ...this.ruleStructure.condition,
        ];
        this.selectedNodeId = newGroup.condition[0].condUuid!;
      }
    },
    isGroupPass(group: ConditionGroup): boolean {
      if (!group || !group.condition || group.condition.length === 0) {
        return false;
      }
      const logicType = group.logicType || "AND";
      let allPass = true;
      let atLeastOnePass = false;

      for (const item of group.condition) {
        if (!item.condition || item.condition.length === 0) {
          if (this.passedCondUuids.includes(item.condUuid!)) {
            atLeastOnePass = true;
            if (logicType === "OR") return true;
          } else {
            allPass = false;
          }
        } else {
          const nestedPass = this.isGroupPass(item as ConditionGroup);
          if (nestedPass) {
            atLeastOnePass = true;
            if (logicType === "OR") return true;
          } else {
            allPass = false;
          }
        }
      }

      return logicType === "AND" ? allPass : atLeastOnePass;
    },
    isGroupFail(group: ConditionGroup): boolean {
      const hasDirectFail = false;
      let hasNestedCondition = false;
      for (const item of group?.condition) {
        if (!item.condition || item.condition.length === 0) {
          if (this.failedCondUuids.includes(item.condUuid!)) {
            return true;
          }
        } else {
          hasNestedCondition = true;
        }
      }
      if (!hasDirectFail && hasNestedCondition) {
        for (const item of group?.condition) {
          if (item.condition && item.condition.length > 0) {
            if (this.isGroupFail(item as ConditionGroup)) {
              return true;
            }
          }
        }
      }
      return false;
    },
    addConditionBelowTarget(
      group: ConditionGroup,
      targetUuid: string,
      newCondition: Condition
    ): void {
      for (let i = 0; i < group.condition.length; i++) {
        const item = group.condition[i as number];
        if ("condUuid" in item && item.condUuid === targetUuid) {
          group.condition.splice(i + 1, 0, newCondition);
          this.selectedNodeId = newCondition.condUuid!;
          return;
        }
        if ("condition" in item) {
          this.addConditionBelowTarget(
            item as ConditionGroup,
            targetUuid,
            newCondition
          );
        }
      }
    },
    findNextConditionByUuid(
      group: ConditionGroup,
      targetUuid: string
    ): Condition | null {
      for (let i = 0; i < group.condition.length - 1; i++) {
        const item = group.condition[i as number];
        if ("condUuid" in item && item.condUuid === targetUuid) {
          return group.condition[i + 1] as Condition;
        }
        if ("condition" in item) {
          const found = this.findNextConditionByUuid(
            item as ConditionGroup,
            targetUuid
          );
          if (found) return found;
        }
      }
      return null;
    },
    findConditionByUuid(
      group: ConditionGroup,
      targetUuid: string
    ): Condition | null {
      for (const item of group.condition) {
        if ("condUuid" in item && item.condUuid === targetUuid) {
          return item;
        }
        if ("condition" in item) {
          const found = this.findConditionByUuid(
            item as ConditionGroup,
            targetUuid
          );
          if (found) return found;
        }
      }
      return null;
    },
    findConditionGroupByUuid(
      group: ConditionGroup,
      targetUuid: string
    ): ConditionGroup | null {
      for (const item of group.condition) {
        if ("condition" in item) {
          const containsTarget = (item.condition || []).some(
            (subItem) =>
              "condUuid" in subItem && subItem.condUuid === targetUuid
          );
          if (containsTarget) return item as ConditionGroup;
          const foundGroup = this.findConditionGroupByUuid(
            item as ConditionGroup,
            targetUuid
          );
          if (foundGroup) return foundGroup;
        }
      }
      return null;
    },
    replaceConditionByUuid(
      group: ConditionGroup,
      targetUuid: string,
      newGroup: ConditionGroup
    ): void {
      for (let i = 0; i < group.condition.length; i++) {
        const item = group.condition[i as number];
        if ("condUuid" in item && item.condUuid === targetUuid) {
          group.condition[i as number] = newGroup;
          return;
        }
        if ("condition" in item) {
          this.replaceConditionByUuid(
            item as ConditionGroup,
            targetUuid,
            newGroup
          );
        }
      }
    },
    findCurrentGroup(
      group: ConditionGroup,
      targetGroup: ConditionGroup
    ): ConditionGroup | null {
      for (const item of group.condition) {
        if ("condition" in item) {
          if ((item.condition || []).includes(targetGroup)) {
            return group;
          }
          const foundGroup = this.findCurrentGroup(
            item as ConditionGroup,
            targetGroup
          );
          if (foundGroup) return foundGroup;
        }
      }
      return null;
    },
    findNextItem(
      group: ConditionGroup,
      targetGroup: ConditionGroup
    ): Condition | ConditionGroup | null {
      for (let i = 0; i < group.condition.length - 1; i++) {
        const item = group.condition[i as number];
        if (
          "condition" in item &&
          JSON.stringify(item) === JSON.stringify(targetGroup)
        ) {
          return group.condition[i + 1] || null;
        }
        if ("condition" in item) {
          const found = this.findNextItem(item as ConditionGroup, targetGroup);
          if (found) return found;
        }
      }
      return null;
    },
    addConditionBelowGroup(
      group: ConditionGroup,
      targetGroup: ConditionGroup,
      newCondition: Condition
    ): void {
      for (let i = 0; i < group.condition.length; i++) {
        const item = group.condition[i as number];
        if (
          "condition" in item &&
          JSON.stringify(item) === JSON.stringify(targetGroup)
        ) {
          group.condition.splice(i + 1, 0, newCondition);
          return;
        }
        if ("condition" in item) {
          this.addConditionBelowGroup(
            item as ConditionGroup,
            targetGroup,
            newCondition
          );
        }
      }
    },
    deleteConditionByUuid(group: ConditionGroup, targetUuid: string): void {
      for (let i = 0; i < group.condition.length; i++) {
        const item = group.condition[i as number];
        if ("condUuid" in item && item.condUuid === targetUuid) {
          group.condition.splice(i, 1);
        }
        if ("condition" in item) {
          this.deleteConditionByUuid(item as ConditionGroup, targetUuid);
          if (item.condition?.length === 0) {
            group.condition.splice(i, 1);
          }
        }
      }
    },
    setSelectedNodeId(nodeId: string): void {
      this.selectedNodeId = nodeId;
    },
    addSortNo(group: ConditionGroup, startNo: number = 1): void {
      let counter = startNo;
      group.condition.forEach((item) => {
        if ("condition" in item) {
          this.addSortNo(item as ConditionGroup);
        } else {
          item.sortNo = counter++;
        }
      });
    },
    formatPayloadSaveAndTest(): ConditionGroup | null {
      if (this.ruleStructure) {
        this.addSortNo(this.ruleStructure);
        return this.ruleStructure;
      }
      return null;
    },
    collectConditions(group: ConditionGroup): Condition[] {
      const result: Condition[] = [];
      const traverse = (item: Condition | ConditionGroup) => {
        if ("condition" in item) {
          item.condition?.forEach(traverse);
        } else {
          result.push(item);
        }
      };
      traverse(group);
      return cloneDeep(result);
    },
    validateRuleStructure(group: ConditionGroup): Condition[] {
      const requiredFields = ["keyName", "dispName", "operator", "value"];
      const invalidConditions: Condition[] = [];
      const traverse = (item: Condition | ConditionGroup) => {
        if ("condition" in item) {
          (item.condition as Condition[]).forEach(traverse);
        } else {
          for (const field of requiredFields) {
            if (!item[field as keyof Condition]) {
              invalidConditions.push(item);
              return;
            }
          }
        }
      };
      traverse(group);
      return invalidConditions;
    },
    updateFieldByFieldUuid(group: ConditionGroup, field: IFieldItem): void {
      for (const item of group.condition) {
        if ("fieldUuid" in item && item.fieldUuid === field.fieldUuid) {
          let options: string[] = ["==", "!=", "IN", "NOT IN"];
          if (field.fieldDataType === "Number") {
            options = ["==", "!=", "IN", "NOT IN", "<", "<=", ">", ">="];
          }
          if (item.fieldDataType !== field.fieldDataType) {
            item.value = "";
          }
          if (!options.includes(item.operator!)) {
            item.operator = "";
          }
          item.fieldDataType = field.fieldDataType;
          item.dispName = field.fieldDispName;
          item.keyName = field.fieldKeyName;
        }
        if ("condition" in item) {
          this.updateFieldByFieldUuid(item as ConditionGroup, field);
        }
      }
    },
  },
});

export default useRuleEngineStore;
