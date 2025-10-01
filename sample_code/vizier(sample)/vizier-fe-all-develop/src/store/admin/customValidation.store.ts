import {
  IAttributeItem,
  ICustomValidationItem,
} from "@/interfaces/admin/admin";
import cloneDeep from "lodash-es/cloneDeep";
import moment from "moment-timezone";

import { v4 as uuidv4 } from "uuid";
import {
  getListCustomValidationApi,
  saveCustomValidationApi,
} from "@/api/prod/customValidationApi";
import { DATE_FORMAT, SPACE, VIEW_MODE } from "@/constants/";
import { Item } from "@/types/catalog/component/ComponentSearch";
import { useI18n } from "vue-i18n";
import { DETAIL_COMPONENT_NAME } from "@/constants/index";
import { RequiredFieldType } from "@/enums/customValidation";
import { checkDuplicateEdges, DFSDetectCycle } from "@/utils/extend-utils";
import { getUserInfor } from "@/constants/userInfor";

const createTempCustomValidationItem = (type) => ({
  id: uuidv4(),
  sort: 1,
  type: type,
  value: "",
  conditions: [],
  actions: [],
  selected: false,
  disabled: false,
  temp: true,
  isEdit: false,
  startDate: moment().startOf("day").format(DATE_FORMAT.DATE_TYPE),
  endDate: "",
});

const createTempAttributeItem = (attrItem) => ({
  id: attrItem.id,
  name: attrItem.name,
  code: attrItem.code || "att1",
  attrType: attrItem.attrType,
  type: attrItem.type,
  data: "",
  value: "",
  selected: false,
  disabled: false,
  temp: true,
  startDate: moment().startOf("day").format(DATE_FORMAT.DATE_TYPE),
  endDate: "",
  multiSelectField: [],
  rangeStartVal: "",
  rangeEndVal: "",
  labelId: "",
  textCntn: "",
  ...attrItem,
});

const initState: ICustomValidationItem[] = [
  {
    id: uuidv4(),
    sort: 1,
    type: "validation",
    value: "",
    conditions: [],
    actions: [],
    selected: false,
    disabled: false,
    temp: true,
    isEdit: false,
    startDate: "",
    endDate: "",
  },
];
interface IOption {
  title: string;
  value: string;
  parentCode?: string;
  cmcdDetlId?: string;
  cmcdDetlNm?: string;
}

const customValidationStore = defineStore("customValidationStore", () => {
  const { t } = useI18n();
  const userInfor = getUserInfor();

  const customValidationItems = ref<ICustomValidationItem[]>([]);
  const customValidationItemsView = ref<ICustomValidationItem[]>(initState);
  const cloneCustomValidationItems = ref<ICustomValidationItem[]>([]);
  const conditionAttributes = ref<IAttributeItem[]>([]);
  const actionAttributes = ref<IAttributeItem[]>([]);
  const dataCommonGroupCode = ref(null);
  const dragItemType = ref("");
  const showHistory = ref(false);
  const validCode = ref("");
  const isChangeConditionSearch = ref(false);
  const conditionSearchItem = ref<IOption | undefined>();
  const conditionSearchType = ref<IOption | undefined>();
  const conditionSearchSubType = ref<IOption | undefined>();
  const conditionSearchItemAct = ref<string>("");
  const conditionSearchTypeAct = ref<string>("");
  const conditionSearchSubTypeAct = ref<string>("");
  const listRules = ref<any[]>([]);
  const totalAdditional = ref<any[]>([]);
  const totalAdditionalOffer = ref<any[]>([]);
  const totalAdditionalComponent = ref<any[]>([]);
  const totalAdditionalResource = ref<any[]>([]);
  const selectedAttribute = ref();
  const selectedAttr = ref();
  const flagRender = ref(false);
  const isOutSide = ref<boolean>(false);
  const isOutSideDL = ref<boolean>(false);
  const validValue = ref<boolean>(true);
  const listRulesItem = ref<any[]>([]);
  const listRulesOriginal = ref<any[]>([]);
  const listItemTypeStore = ref<IOption[]>([]);
  const listItemSubTypeStore = ref<IOption[]>([]);
  const listComponentTypesStore = ref<Item[]>([]);
  const listCategoryStoreAct = ref<IOption[]>([]);
  const listItemTypeStoreAct = ref<IOption[]>([]);
  const listItemSubTypeStoreAct = ref<IOption[]>([]);
  const listComponentTypesStoreAct = ref<Item[]>([]);
  const error = ref("");
  const isEditMode = ref(false);
  const isEmptyCondition = ref<boolean>(false);
  const isEmptyAction = ref<boolean>(false);
  const viewType = ref(VIEW_MODE.GRID);
  const tableConditions = reactive({
    condition: "C",
    itemType: SPACE,
    type: SPACE,
    subType: SPACE,
    listType: [],
    listSubType: [],
    customValidationItems: [],
  });

  const transformToCustomValidationItem = (data) => {
    if (data.length === 0) {
      customValidationItems.value = [
        {
          id: uuidv4(),
          sort: 1,
          type: "validation",
          value: "",
          conditions: [],
          actions: [],
          selected: false,
          disabled: false,
          temp: true,
          isEdit: false,
          startDate: moment().startOf("day").format(DATE_FORMAT.DATE_TYPE),
          endDate: "",
        },
      ];
      return;
    }
    customValidationItems.value = data
      .map((item) => {
        return {
          id: item.validCode,
          sort: item.seqNo,
          type: item.attributes.length > 0 ? "validation" : "memo",
          value: item.validCntn,
          selected: false,
          disabled: checkDisabledItem(item.validStartDtm, item.validEndDtm),
          temp: false,
          isEdit: false,
          startDate: item.validStartDtm
            ? moment(item.validStartDtm).format(DATE_FORMAT.DATE_TYPE)
            : "",
          endDate: item.validEndDtm
            ? moment(item.validEndDtm).format(DATE_FORMAT.DATE_TYPE)
            : "",
          conditions: item.attributes
            .filter((attr) => attr.condType === "C")
            .map((cond) => ({
              id: cond.attrUuid,
              name: `${cond.labelId}`,
              code: cond.commGroupCode,
              attrType: cond.fieldTypeCode,
              type: "condition",
              data: "",
              value: cond.textCntn,
              selected: false,
              disabled:
                checkDisabledItem(cond.validStartDtm, cond.validEndDtm) ||
                cond.useYn === "N",
              useYn: cond.useYn === "Y",
              temp: false,
              startDate: cond.validStartDtm
                ? moment(cond.validStartDtm).format(DATE_FORMAT.DATE_TYPE)
                : "",
              endDate: cond.validEndDtm
                ? moment(cond.validEndDtm).format(DATE_FORMAT.DATE_TYPE)
                : "",
              startDateField: cond.rangeStartDtm
                ? moment(cond.rangeStartDtm).format(DATE_FORMAT.DATE_TYPE)
                : "",
              endDateField: cond.rangeEndDtm
                ? moment(cond.rangeEndDtm).format(DATE_FORMAT.DATE_TYPE)
                : "",
              numberFromField: cond.rangeStartVal,
              numberToField: cond.rangeEndVal,
              multiSelectField: cond.multipleValues,
              sort: cond.attrNo,
              conditionType: cond.actionItemCode,
              largeItemCode: cond.largeItemCode,
              itemCodeName: cond.itemCodeName,
              requiredYn: cond.requiredYn,
              commGroupCode: cond.commGroupCode,
              attrMaxLength: cond.attrMaxLength,
            }))
            .sort((after, before) => after.sort - before.sort),
          actions: item.attributes
            .filter((attr) => attr.condType === "A")
            .map((cond) => ({
              id: cond.attrUuid,
              name: `${cond.labelId}`,
              code: cond.commGroupCode,
              attrType: cond.fieldTypeCode,
              type: "action",
              data: "",
              value: cond.textCntn,
              selected: false,
              disabled:
                checkDisabledItem(cond.validStartDtm, cond.validEndDtm) ||
                cond.useYn === "N",
              useYn: cond.useYn === "Y",
              temp: false,
              startDate: cond.validStartDtm
                ? moment(cond.validStartDtm).format(DATE_FORMAT.DATE_TYPE)
                : "",
              endDate: cond.validEndDtm
                ? moment(cond.validEndDtm).format(DATE_FORMAT.DATE_TYPE)
                : "",
              numberFromField: cond.rangeStartVal,
              numberToField: cond.rangeEndVal,
              startDateField: cond.rangeStartDtm
                ? moment(cond.rangeStartDtm).format(DATE_FORMAT.DATE_TYPE)
                : "",
              endDateField: cond.rangeEndDtm
                ? moment(cond.rangeEndDtm).format(DATE_FORMAT.DATE_TYPE)
                : "",
              multiSelectField: cond.multipleValues,
              sort: cond.attrNo,
              conditionType: cond.actionItemCode,
              largeItemCode: cond.largeItemCode,
              itemCodeName: cond.itemCodeName,
              requiredYn: cond.requiredYn,
              commGroupCode: cond.commGroupCode,
              attrMaxLength: cond.attrMaxLength,
            }))
            .sort((after, before) => after.sort - before.sort),
        };
      })
      .sort((after, before) => after.sort - before.sort);
  };

  const transformToAttributeItem = (data, action) => {
    const result = data
      .map((item) => ({
        id: item.attrUuid,
        name: item.labelId,
        code: item.commGroupCode,
        attrType: item.fieldTypeCode,
        type: action,
        data: "",
        value: "",
        selected: false,
        disabled: false,
        useYn: true,
        temp: true,
        conditionType: item.itemCode,
        sort: item.sortNo,
        condition: item.types.includes("C"),
        action: item.types.includes("A"),
        largeItemCode: item.largeItemCode,
        itemCodeName: item.itemCodeName,
        dispTab: item.dispTab,
        requiredYn: item.requiredYn,
        attrMaxLength: item.attrMaxLength,
      }))
      .sort((after, before) => after.sort - before.sort);

    if (action === "condition") {
      conditionAttributes.value = result;
    } else {
      actionAttributes.value = result;
    }
  };

  const getTypeOfAttribute = (attrId) => {
    const type: string[] = [];
    customValidationItems.value.forEach((item) => {
      if (item.conditions.find((ac) => ac.id === attrId)) {
        type.push("C");
      }
      if (item.actions.find((ac) => ac.id === attrId)) {
        type.push("A");
      }
    });
    return type;
  };

  const setTotalAdditional = (type: string, add: any) => {
    if (
      type === DETAIL_COMPONENT_NAME.OFFER_SEARCH ||
      type === DETAIL_COMPONENT_NAME.OFFER_CREATE
    ) {
      totalAdditionalOffer.value = add;
    } else if (
      type === DETAIL_COMPONENT_NAME.COMPONENT_SEARCH ||
      type === DETAIL_COMPONENT_NAME.COMPONENT_CREATE
    ) {
      totalAdditionalComponent.value = add;
    } else if (
      type === DETAIL_COMPONENT_NAME.RESOURCE_SEARCH ||
      type === DETAIL_COMPONENT_NAME.RESOURCE_CREATE
    ) {
      totalAdditionalResource.value = add;
    }
  };

  const updateDragItemType = (type) => {
    dragItemType.value = type;
  };

  const updateShowHistory = (value) => {
    showHistory.value = value;
  };

  const setValidCode = (value: any) => {
    validCode.value = value;
  };
  const countValidationItem = computed(() => {
    return customValidationItems.value.length;
  });

  const checkValidationItemEdit = (id) => {
    const editItem = customValidationItems.value.find(
      (item) => item.id !== id && item.isEdit
    );
    if (editItem?.type === "validation") {
      return {
        isValid: editItem.actions.length > 0 || editItem.conditions.length > 0,
        id: editItem.id,
      };
    } else {
      return { isValid: (editItem?.value?.length || 0) > 0, id: editItem?.id };
    }
  };

  const addCustomValidationItem = (index, type) => {
    customValidationItems.value.splice(
      index + 1,
      0,
      createTempCustomValidationItem(type)
    );
    updateSortCustomValidationItem();
  };

  const updateSortCustomValidationItem = () => {
    customValidationItems.value = customValidationItems.value.map(
      (item, index) => ({ ...item, sort: index + 1 })
    );
  };

  const deleteCustomValidationItem = (id) => {
    customValidationItems.value = customValidationItems.value.filter(
      (item) => item.id !== id
    );
    updateSortCustomValidationItem();
  };

  const selectedCustomValidationItem = (id) => {
    if (id) {
      customValidationItems.value = customValidationItems.value.map((item) => ({
        ...item,
        isEdit: item.id === id ? item.isEdit : false,
        selected: item.id === id,
      }));
    } else {
      if (customValidationItems.value.find((item) => item.isEdit)) {
        return;
      }
      customValidationItems.value = customValidationItems.value.map((item) => ({
        ...item,
        selected: false,
      }));
    }
  };

  const updateEditCustomValidationItem = (id, isEdit) => {
    const currentItem = customValidationItems.value.find(
      (item) => item.id === id
    );
    if (currentItem) {
      currentItem.isEdit = isEdit;
      if (isEdit) {
        cloneCustomValidationItems.value.push({
          ...cloneDeep(currentItem),
          isEdit: false,
          clone: true,
        });
      } else {
        const cloneItem = cloneCustomValidationItems.value.find(
          (item) => item.id === id
        );
        if (cloneItem) {
          cloneItem.clone = false;
          customValidationItems.value = customValidationItems.value.map(
            (item) => (item.id === id ? cloneItem : item)
          );
          cloneCustomValidationItems.value = [];
        }
      }
    }
  };

  const moveUpCustomValidationItem = (id) => {
    const currentIndex = customValidationItems.value.findIndex(
      (item) => item.id === id
    );
    if (currentIndex > 0) {
      const currentItem = customValidationItems.value[currentIndex as number];
      const swapItem = customValidationItems.value[currentIndex - 1];
      const tempItem = swapItem;
      customValidationItems.value[currentIndex - 1] = currentItem;
      customValidationItems.value[currentIndex as number] = tempItem;
      updateSortCustomValidationItem();
      currentItem.temp ? null : saveCustomValidationItem(id);
    }
  };

  const moveDownCustomValidationItem = (id) => {
    const currentIndex = customValidationItems.value.findIndex(
      (item) => item.id === id
    );
    if (currentIndex <= customValidationItems.value.length - 1) {
      const currentItem = customValidationItems.value[currentIndex as number];
      const swapItem = customValidationItems.value[currentIndex + 1];
      const tempItem = swapItem;
      customValidationItems.value[currentIndex + 1] = currentItem;
      customValidationItems.value[currentIndex as number] = tempItem;
      updateSortCustomValidationItem();
      currentItem.temp ? null : saveCustomValidationItem(id);
    }
  };

  // attribute item

  const addAttributeItem = (attrItem: IAttributeItem) => {
    const editItem = customValidationItems.value.find(
      (item) => item.isEdit && item.type === "validation"
    );
    if (editItem) {
      if (
        editItem.conditions.find((item) => item.id === attrItem.id) ||
        editItem.actions.find((item) => item.id === attrItem.id)
      ) {
        return t("product_platform.attributeIsExist");
      }
      if (attrItem.type === "condition") {
        if (
          editItem.conditions[0] &&
          editItem.conditions[0].conditionType !== attrItem.conditionType
        ) {
          return t("product_platform.cannotAddIteIsDifferentType");
        }
        if (
          editItem.actions.filter((item) => !item.disabled).length > 1 &&
          editItem.conditions.filter((item) => !item.disabled).length === 1
        ) {
          return t("product_platform.conditionActionCannotBeConfigured");
        }

        if (checkMaxItem(editItem.conditions)) {
          return t("product_platform.cannotAddMoreItem");
        }

        // Check duplicate couple item
        const { invalid, duplicate } = checkInValidRule(attrItem, editItem);
        if (invalid) {
          return t("product_platform.ruleIsNotValid");
        }
        if (duplicate) {
          return t("product_platform.duplicateItem");
        }

        editItem.conditions.push(createTempAttributeItem(attrItem));
        editItem.conditions = editItem.conditions.map((item, index) => ({
          ...item,
          sort: index + 1,
        }));
      } else if (attrItem.type === "action") {
        if (
          editItem.actions[0] &&
          editItem.actions[0]?.conditionType !== attrItem.conditionType
        ) {
          return t("product_platform.cannotAddIteIsDifferentType");
        }
        if (
          editItem.conditions.filter((item) => !item.disabled).length > 1 &&
          editItem.actions.filter((item) => !item.disabled).length === 1
        ) {
          return t("product_platform.conditionActionCannotBeConfigured");
        }
        if (checkMaxItem(editItem.actions)) {
          return t("product_platform.cannotAddMoreItem");
        }
        const { invalid, duplicate } = checkInValidRule(attrItem, editItem);
        if (invalid) {
          return t("product_platform.ruleIsNotValid");
        }
        if (duplicate) {
          return t("product_platform.duplicateItem");
        }
        editItem.actions.push(createTempAttributeItem(attrItem));
        editItem.actions = editItem.actions.map((item, index) => ({
          ...item,
          sort: index + 1,
        }));
      }
      return "";
    } else {
      return t("product_platform.pleaseChooseOneItemToEdit");
    }
  };

  const checkMaxItem = (data) => {
    const maxItem = 3;
    if (data.filter((item) => !item.disabled).length >= maxItem) {
      return true;
    }
    return false;
  };

  const checkInValidRule = (
    attrItem: IAttributeItem | null,
    editItem: ICustomValidationItem
  ) => {
    const { id: itemId, conditions, actions } = editItem;
    const listConditions = [...conditions.filter(({ disabled }) => !disabled)];
    const listActions = [...actions.filter(({ disabled }) => !disabled)];
    const cellBoxItems = customValidationItems.value.filter(
      ({ disabled, id }) => !disabled && id !== itemId
    );
    const filteredItems = cellBoxItems.map(
      ({ actions, conditions, ...item }) => ({
        ...item,
        actions: actions.filter(({ disabled }) => !disabled),
        conditions: conditions.filter(({ disabled }) => !disabled),
      })
    );

    if (attrItem && attrItem.type === "condition") {
      listConditions.push(attrItem);
    } else if (attrItem && attrItem.type === "action") {
      listActions.push(attrItem);
    }

    // check has cycle
    const listRules: Array<string[]> = [];
    filteredItems.forEach((item) => {
      item.conditions.forEach((cond) => {
        item.actions.forEach((act) => {
          listRules.push([cond.id, act.id]);
        });
      });
    });
    listConditions.forEach((cond) => {
      listActions.forEach((act) => {
        listRules.push([cond.id, act.id]);
      });
    });

    return {
      invalid: DFSDetectCycle(listRules),
      duplicate: checkDuplicateEdges(listRules),
    };
  };

  const deleteAttributeItem = (attrItem, parentId) => {
    const parentItem = customValidationItems.value.find(
      (item) => item.id === parentId
    );
    if (parentItem) {
      if (attrItem.type === "condition") {
        parentItem.conditions = parentItem.conditions.filter(
          (item) => item.id !== attrItem.id
        );
      } else if (attrItem.type === "action") {
        parentItem.actions = parentItem.actions.filter(
          (item) => item.id !== attrItem.id
        );
      }
    }
  };

  const setEditMode = (value: boolean) => {
    isEditMode.value = value;
  };

  const checkValidateItem = (id: string) => {
    const currentItem = customValidationItems.value.find(
      (item) => item.id == id
    );

    if (!currentItem) {
      return { checkInValid: false, message: "" };
    }

    if (currentItem.type === "memo" && currentItem.value.trim().length === 0) {
      return { checkInValid: true, message: "" };
    }

    const attributes = [...currentItem.conditions, ...currentItem.actions];
    for (const item of attributes) {
      if (!item.disabled) {
        if (
          ["TF", "TA"].includes(item.attrType) &&
          item.value.trim().length === 0
        ) {
          return { checkInValid: true };
        }
        if (["DM", "DL"].includes(item.attrType)) {
          if (
            item.requiredYn === RequiredFieldType.Yes &&
            item.multiSelectField?.toString().length === 0
          ) {
            return { checkInValid: true };
          }
        }
        if (
          ["NF", "RF"].includes(item.attrType) &&
          (item.numberFromField === null ||
            item.numberFromField === undefined ||
            item.numberFromField?.toString().length === 0) &&
          (item.numberToField === null ||
            item.numberToField === undefined ||
            item.numberToField?.toString().length === 0)
        ) {
          return { checkInValid: true };
        }
        if (
          ["NF", "RF"].includes(item.attrType) &&
          ((item.numberToField &&
            item.numberToField?.toString().length >
              Number(item.attrMaxLength)) ||
            (item.numberFromField &&
              item.numberFromField?.toString().length >
                Number(item.attrMaxLength)))
        ) {
          return {
            checkInValid: true,
            message: `${t("product_platform.validate.maxLengthCharacter")} ${
              item.attrMaxLength
            } ${t("product_platform.validate.characters")}`,
          };
        }
        if (
          ["NF", "RF"].includes(item.attrType) &&
          item.numberToField &&
          item.numberFromField &&
          Number(item.numberToField) < Number(item.numberFromField)
        ) {
          return {
            checkInValid: true,
            message: `${t("product_platform.maxBiggerMin")}`,
          };
        }
        if (
          item.attrType === "DP" &&
          (item.startDateField === null ||
            item.startDateField === undefined ||
            item.startDateField?.length === 0) &&
          (item.endDateField === null ||
            item.endDateField === undefined ||
            item.endDateField?.length === 0)
        ) {
          return { checkInValid: true };
        }
      }
    }

    return { checkInValid: false, message: "" };
  };

  const checkDuplicateAction = (actionId: string): boolean => {
    const currentItem = customValidationItems.value.find(
      ({ id, type }) => id === actionId && type !== "memo"
    );

    if (!currentItem) return false;

    const currentActions = [...currentItem.actions];

    const numberActionDuplicate = customValidationItems.value
      .filter(({ id, disabled }) => id !== actionId && !disabled)
      .reduce((count, { actions }) => {
        return (
          count +
          currentActions.filter(
            ({ id, disabled }) =>
              !disabled &&
              actions.some((action) => !action.disabled && action.id === id)
          ).length
        );
      }, 0);

    return numberActionDuplicate > 0;
  };

  const saveCustomValidationItem = async (id) => {
    const currentItem = customValidationItems.value.find(
      (item) => item.id == id
    );
    if (currentItem) {
      if (
        currentItem.actions.length === 0 &&
        currentItem.conditions.length === 0 &&
        currentItem.type === "validation"
      ) {
        throw {
          errorMsg: t("product_platform.cannotSaveItemWithoutAttributes"),
        };
      }

      const { checkInValid, message } = checkValidateItem(id);
      if (checkInValid) {
        throw {
          errorMsg: message || t("product_platform.required_field_missing"),
        };
      }

      const listCustomValidationItem = customValidationItems.value.filter(
        (item) => item.id === id || !item.temp
      );

      const payload = {
        item: conditionSearchItem.value?.value,
        type: conditionSearchType.value?.value,
        chgUser: userInfor.chgUser,
        chgDeptName: userInfor.chgDeptName,
        subType: conditionSearchSubType.value?.value,
        datas: listCustomValidationItem.map((item) => {
          const listAttributes = [
            ...item.actions.map((item, index) => ({
              ...item,
              sort: item.sort || index + 1,
            })),
            ...item.conditions.map((item, index) => ({
              ...item,
              sort: item.sort || index + 1,
            })),
          ];
          const attributes = listAttributes.map((attrItem) => ({
            validCode: null,
            attrUuid: attrItem.id,
            condType: attrItem.type === "action" ? "A" : "C",
            attrNo: attrItem.sort,
            validStartDtm:
              attrItem.startDate || moment().format(DATE_FORMAT.DATE_TYPE),
            rangeStartVal: attrItem.numberFromField,
            rangeEndVal: attrItem.numberToField,
            rangeStartDtmStr: attrItem.startDateField
              ? moment(attrItem.startDateField).format(DATE_FORMAT.DATE_TYPE)
              : "",
            rangeEndDtmStr: attrItem.endDateField
              ? moment(attrItem.endDateField).format(DATE_FORMAT.DATE_TYPE)
              : "",
            textCntn: attrItem.value,
            validEndDtm: attrItem.endDate || null,
            multipleValues: attrItem.multiSelectField,
            fieldTypeCode: attrItem.attrType,
            isExpired: attrItem.disabled,
            commGroupCode: attrItem.commGroupCode,
          }));
          return {
            validCode: item.temp ? null : item.id,
            validCntn: item.value,
            isUpdated: item.isEdit ? !item.temp : false,
            seqNo: item.sort,
            validStartDtm:
              item.startDate || moment().format(DATE_FORMAT.DATE_TYPE),
            validEndDtm: item.endDate || null,
            attributes: attributes,
          };
        }),
      };
      try {
        const response = await saveCustomValidationApi(payload);
        const validationItem = response?.data?.find((req) => req.created);
        currentItem.id = validationItem?.validCode || currentItem.id;
        currentItem.isEdit = false;
        currentItem.temp = false;
        currentItem.actions = currentItem.actions.map((item) => ({
          ...item,
          temp: false,
        }));
        currentItem.conditions = currentItem.conditions.map((item) => ({
          ...item,
          temp: false,
        }));
        cloneCustomValidationItems.value = [];
      } catch (error) {
        throw error;
      }
    } else {
      throw { errorMsg: t("product_platform.itemDoesNotExits") };
    }
  };

  const saveCustomValidationItemEdit = async (type) => {
    const currentItem = customValidationItems.value.find((item) => item.isEdit);
    if (currentItem) {
      if (type === "save") {
        if (
          currentItem.actions.length === 0 &&
          currentItem.conditions.length === 0 &&
          currentItem.type === "validation"
        ) {
          throw {
            errorMsg: t("product_platform.cannotSaveItemWithoutAttributes"),
          };
        }

        const { checkInValid, message } = checkValidateItem(currentItem.id);
        if (checkInValid) {
          throw {
            errorMsg: message || t("product_platform.required_field_missing"),
          };
        }

        const listCustomValidationItem = customValidationItems.value.filter(
          (item) => item.id === currentItem.id || !item.temp
        );

        const payload = {
          item: conditionSearchItem.value?.value,
          type: conditionSearchType.value?.value,
          chgUser: userInfor.chgUser,
          chgDeptName: userInfor.chgDeptName,
          subType: conditionSearchSubType.value?.value,
          datas: listCustomValidationItem.map((item) => {
            const listAttributes = [
              ...item.actions.map((item, index) => ({
                ...item,
                sort: item.sort || index + 1,
              })),
              ...item.conditions.map((item, index) => ({
                ...item,
                sort: item.sort || index + 1,
              })),
            ];
            const attributes = listAttributes.map((attrItem) => ({
              validCode: null,
              attrUuid: attrItem.id,
              condType: attrItem.type === "action" ? "A" : "C",
              attrNo: attrItem.sort,
              validStartDtm: moment().format(DATE_FORMAT.DATE_TYPE),
              rangeStartVal: attrItem.numberFromField,
              rangeEndVal: attrItem.numberToField,
              rangeStartDtmStr: attrItem.startDateField
                ? moment(attrItem.startDateField).format(DATE_FORMAT.DATE_TYPE)
                : "",
              rangeEndDtmStr: attrItem.endDateField
                ? moment(attrItem.endDateField).format(DATE_FORMAT.DATE_TYPE)
                : "",
              textCntn: attrItem.value,
              validEndDtm: attrItem.endDate || null,
              multipleValues: attrItem.multiSelectField,
              fieldTypeCode: attrItem.attrType,
              isExpired: attrItem.disabled,
              commGroupCode: attrItem.commGroupCode,
            }));
            return {
              validCode: item.temp ? null : item.id,
              validCntn: item.value,
              isUpdated: item.isEdit ? !item.temp : false,
              seqNo: item.sort,
              validStartDtm:
                item.startDate || moment().format(DATE_FORMAT.DATE_TYPE),
              validEndDtm: item.endDate || null,
              attributes: attributes,
            };
          }),
        };

        try {
          const response = await saveCustomValidationApi(payload);
          const validationItem = response?.data?.find((req) => req.created);
          currentItem.id = validationItem?.validCode || currentItem.id;
          currentItem.isEdit = false;
          currentItem.temp = false;
          currentItem.actions = currentItem.actions.map((item) => ({
            ...item,
            temp: false,
          }));
          currentItem.conditions = currentItem.conditions.map((item) => ({
            ...item,
            temp: false,
          }));
          cloneCustomValidationItems.value = [];
        } catch (error) {
          throw error;
        }
      } else if (type === "cancel") {
        const cloneItem = cloneCustomValidationItems.value.find(
          (item) => item.id === currentItem.id
        );
        if (cloneItem) {
          customValidationItems.value = customValidationItems.value.map(
            (item) => (item.id === currentItem.id ? cloneItem : item)
          );
          cloneCustomValidationItems.value = [];
        }
      }
    } else {
      throw { errorMsg: t("product_platform.itemDoesNotExits") };
    }
  };

  const updateCustomValidationItemDate = (id, startDate, endDate) => {
    const currentItem = customValidationItems.value.find(
      (item) => item.id === id
    );
    if (currentItem) {
      try {
        currentItem.startDate = startDate
          ? moment(startDate).format(DATE_FORMAT.DATE_TYPE)
          : "";
        currentItem.endDate = endDate
          ? moment(endDate).format(DATE_FORMAT.DATE_TYPE)
          : "";
        currentItem.disabled = checkDisabledItem(startDate, endDate);
        saveCustomValidationItem(id);
      } catch (error) {
        throw error;
      }
    }
  };

  const setCustomValidationItemsView = (filtered: any[]) => {
    customValidationItemsView.value = filtered.sort(
      (after, before) => after.seqNo - before.seqNo
    );
  };

  const getListCustomValidation = async (params: any) => {
    try {
      const res = await getListCustomValidationApi(params);
      listRulesOriginal.value = res.data;
      listRules.value = res.data.map((rule) => ({
        id: "",
        sort: 1,
        type: "validation",
        value: "",
        selected: true,
        disabled: false,
        temp: false,
        isEdit: false,
        startDate: "",
        endDate: "",
        conditions: rule.datas
          .filter((data) => data.condType === "C")
          .map((data) => ({
            ...data,
            id: data.attrUuid,
            name: data.labelId,
            code: data.commGroupCode,
            attrType: data.fieldTypeCode,
            type: "condition",
            data: "",
            value: data.textCntn,
            startDate: data.validStartDtm,
            endDate: data.validEndDtm,
            multiSelectField: [],
          })),
        actions: rule.datas
          .filter((data) => data.condType === "A")
          .map((data) => ({
            ...data,
            id: data.attrUuid,
            code: data.commGroupCode,
            name: data.labelId,
            attrType: data.fieldTypeCode,
            type: "action",
            data: "",
            value: data.textCntn,
            startDate: data.validStartDtm,
            endDate: data.validEndDtm,
            multiSelectField: [],
          })),
      }));
      listRulesItem.value = listRules.value;
    } catch (err: any) {
      err.value = err;
      throw err;
    }
  };

  const getListCustomValidationItem = async (params: any) => {
    try {
      const res = await getListCustomValidationApi(params);
      listRulesItem.value = res.data.map((rule) => ({
        id: "",
        sort: 1,
        type: "validation",
        value: "",
        selected: true,
        disabled: false,
        temp: false,
        isEdit: false,
        startDate: "",
        endDate: "",
        conditions: rule.datas
          .filter((data) => data.condType === "C")
          .map((data) => ({
            ...data,
            id: data.attrUuid,
            name: data.labelId,
            code: data.commGroupCode,
            attrType: data.fieldTypeCode,
            type: "condition",
            data: "",
            value: data.textCntn,
            startDate: data.validStartDtm,
            endDate: data.validEndDtm,
            multiSelectField: [],
          })),
        actions: rule.datas
          .filter((data) => data.condType === "A")
          .map((data) => ({
            ...data,
            id: data.attrUuid,
            code: data.commGroupCode,
            name: data.labelId,
            attrType: data.fieldTypeCode,
            type: "action",
            data: "",
            value: data.textCntn,
            startDate: data.validStartDtm,
            endDate: data.validEndDtm,
            multiSelectField: [],
          })),
      }));
    } catch (err: any) {
      err.value = err;
      throw err;
    }
  };

  const checkEnableValidationItem = (currentItem) => {
    const { invalid, duplicate } = checkInValidRule(null, currentItem);
    if (invalid) {
      return t("product_platform.ruleIsNotValid");
    }
    if (duplicate) {
      return t("product_platform.duplicateItem");
    }
    return "";
  };

  const checkEnableAttribute = (attrItem, parentId) => {
    const parentItem = customValidationItems.value.find(
      (item) => item.id === parentId
    );
    if (parentItem) {
      if (attrItem.type === "condition") {
        if (
          parentItem.actions.filter((item) => !item.disabled).length > 1 &&
          parentItem.conditions.filter((item) => !item.disabled).length === 1
        ) {
          return t("product_platform.conditionActionCannotBeConfigured");
        }
        if (checkMaxItem(parentItem.conditions)) {
          return t("product_platform.cannotAddMoreItem");
        }
        const { invalid, duplicate } = checkInValidRule(attrItem, parentItem);
        if (invalid) {
          return t("product_platform.ruleIsNotValid");
        }
        if (duplicate) {
          return t("product_platform.duplicateItem");
        }
      } else {
        if (
          parentItem.conditions.filter((item) => !item.disabled).length > 1 &&
          parentItem.actions.filter((item) => !item.disabled).length === 1
        ) {
          return t("product_platform.conditionActionCannotBeConfigured");
        }
        if (checkMaxItem(parentItem.actions)) {
          return t("product_platform.cannotAddMoreItem");
        }
        const { invalid, duplicate } = checkInValidRule(attrItem, parentItem);
        if (invalid) {
          return t("product_platform.ruleIsNotValid");
        }
        if (duplicate) {
          return t("product_platform.duplicateItem");
        }
      }
      return "";
    }
  };

  const updateAttributeItemDate = (attrItem, parentId, startDate, endDate) => {
    const parentItem = customValidationItems.value.find(
      (item) => item.id === parentId
    );
    if (parentItem) {
      if (attrItem.type === "condition") {
        const currentItem = parentItem.conditions.find(
          (item) => item.id === attrItem.id
        );
        if (currentItem) {
          currentItem.startDate = startDate
            ? moment(startDate).format(DATE_FORMAT.DATE_TYPE)
            : "";
          currentItem.endDate = endDate
            ? moment(endDate).format(DATE_FORMAT.DATE_TYPE)
            : "";
          currentItem.disabled = checkDisabledItem(startDate, endDate);
        }
      } else if (attrItem.type === "action") {
        const currentItem = parentItem.actions.find(
          (item) => item.id === attrItem.id
        );
        if (currentItem) {
          currentItem.startDate = startDate
            ? moment(startDate).format(DATE_FORMAT.DATE_TYPE)
            : "";
          currentItem.endDate = endDate
            ? moment(endDate).format(DATE_FORMAT.DATE_TYPE)
            : "";
          currentItem.disabled = checkDisabledItem(startDate, endDate);
        }
      }
    }
  };

  const checkDisabledItem = (startDate, endDate) => {
    //startdate <= now < enddate (enabled)
    const today = moment().endOf("minutes");
    const condition =
      moment(startDate, DATE_FORMAT.DATE_TYPE)
        .endOf("minutes")
        .isAfter(today) ||
      moment(endDate, DATE_FORMAT.DATE_TYPE)
        .endOf("minutes")
        .isSameOrBefore(today);
    return condition;
  };

  const updateSelectedAttributeItem = (attrId, type) => {
    selectedAttribute.value = { attrId, type };
  };

  const setSelectedAttr = (attrId: any) => {
    selectedAttr.value = { attrId };
  };

  const setFlagRender = () => {
    flagRender.value = true;
  };

  const setIsOutside = (value: boolean) => {
    isOutSide.value = value;
  };

  const setIsOutsideDL = (value: boolean) => {
    isOutSideDL.value = value;
  };

  const setValidValue = (value: boolean) => {
    validValue.value = value;
  };

  const resetCustomValidationStore = () => {
    customValidationItems.value = [];
    cloneCustomValidationItems.value = [];
    conditionAttributes.value = [];
    actionAttributes.value = [];
    showHistory.value = false;
    selectedAttribute.value = "";
    listCategoryStoreAct.value = [];
    listItemTypeStoreAct.value = [];
    listItemSubTypeStoreAct.value = [];
    conditionSearchItemAct.value = "";
    conditionSearchTypeAct.value = "";
    conditionSearchSubTypeAct.value = "";
    listItemTypeStore.value = [];
    listItemSubTypeStore.value = [];
    isEmptyCondition.value = false;
    isEmptyAction.value = false;
    customValidationItemsView.value = [];
    dataCommonGroupCode.value = null;
    dragItemType.value = "";
    validCode.value = "";
    isChangeConditionSearch.value = false;
    conditionSearchItem.value = undefined;
    conditionSearchType.value = undefined;
    conditionSearchSubType.value = undefined;
    listRules.value = [];
    totalAdditional.value = [];
    totalAdditionalOffer.value = [];
    totalAdditionalComponent.value = [];
    totalAdditionalResource.value = [];
    selectedAttr.value = null;
    flagRender.value = false;
    isOutSide.value = false;
    isOutSideDL.value = false;
    validValue.value = false;
    listRulesItem.value = [];
    listRulesOriginal.value = [];
    listComponentTypesStore.value = [];
    listComponentTypesStoreAct.value = [];
    error.value = "";
    isEditMode.value = false;
  };

  return {
    customValidationItems,
    cloneCustomValidationItems,
    conditionAttributes,
    actionAttributes,
    customValidationItemsView,
    countValidationItem,
    dataCommonGroupCode,
    dragItemType,
    showHistory,
    isChangeConditionSearch,
    conditionSearchItem,
    conditionSearchType,
    conditionSearchSubType,
    conditionSearchItemAct,
    conditionSearchTypeAct,
    conditionSearchSubTypeAct,
    listRules,
    selectedAttribute,
    listRulesItem,
    listRulesOriginal,
    validCode,
    selectedAttr,
    listItemTypeStore,
    listItemSubTypeStore,
    listComponentTypesStore,
    listItemTypeStoreAct,
    listItemSubTypeStoreAct,
    listComponentTypesStoreAct,
    listCategoryStoreAct,
    flagRender,
    totalAdditional,
    totalAdditionalOffer,
    totalAdditionalComponent,
    totalAdditionalResource,
    error,
    isOutSide,
    isOutSideDL,
    validValue,
    isEditMode,
    isEmptyCondition,
    isEmptyAction,
    viewType,
    tableConditions,
    setSelectedAttr,
    setEditMode,
    setValidValue,
    setIsOutside,
    setIsOutsideDL,
    setTotalAdditional,
    setFlagRender,
    setValidCode,
    checkValidationItemEdit,
    addCustomValidationItem,
    deleteCustomValidationItem,
    selectedCustomValidationItem,
    updateEditCustomValidationItem,
    moveUpCustomValidationItem,
    moveDownCustomValidationItem,
    addAttributeItem,
    deleteAttributeItem,
    updateDragItemType,
    updateShowHistory,
    getListCustomValidation,
    setCustomValidationItemsView,
    saveCustomValidationItem,
    saveCustomValidationItemEdit,
    updateCustomValidationItemDate,
    checkEnableValidationItem,
    checkEnableAttribute,
    updateAttributeItemDate,
    updateSelectedAttributeItem,
    transformToCustomValidationItem,
    resetCustomValidationStore,
    getListCustomValidationItem,
    getTypeOfAttribute,
    checkDisabledItem,
    transformToAttributeItem,
    checkDuplicateAction,
  };
});

export default customValidationStore;
