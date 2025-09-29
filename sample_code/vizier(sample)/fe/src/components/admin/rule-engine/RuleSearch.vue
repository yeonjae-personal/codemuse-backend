<template>
  <div class="bg-white relative pt-6 pb-3 rounded-lg h-full">
    <div class="flex flex-col h-full">
      <div class="pl-6 pr-6 pb-4">
        <div class="flex justify-between items-center">
          <div class="flex align-center gap-2 items-end">
            <h1 class="font-medium text-base text-text-base tracking-[0.5px]">
              {{ $t("product_platform.ruleSearch") }}
            </h1>
          </div>

          <div class="flex">
            <SearchAndRefreshButton
              @handle-search="handleSearch"
              @handle-refresh="handleResetSearch"
            />
          </div>
        </div>

        <div class="filter mt-2 gap-2">
          <div class="d-flex items-center gap-2 mt-2">
            <BaseSelectScroll
              v-model="searchBy"
              :default-item-select-all="false"
              :options="searchByOptions"
              class="!w-[165px] flex-shrink-0"
              :height="48"
            />
            <BaseInputSearch
              v-model.trim="searchName"
              density="comfortable"
              variant="solo"
              hide-details
              single-line
              rounded="4"
              @handle-search="handleSearch"
            />
          </div>
        </div>
      </div>
      <LocomotiveComponent
        v-if="listRules.length > 0"
        scroll-container-class="h-full px-6"
        scroll-content-class="h-full flex flex-col gap-4 pb-4"
        :number-scroll-y="currentY"
        :is-scroll-when-add-new="isScrollWhenAddNew"
        is-dynamic-scroll
      >
        <div
          v-for="category in listRules"
          :key="category.categoryId"
          class="group-rule"
        >
          <div class="group-name-wrapper">
            <div class="rule-count rule-count-red">
              {{ countCategoryLength(category) }}
            </div>
            <div
              class="group-name"
              @click="() => handleToggleExpanded(category.categoryId)"
            >
              <BaseInputText
                v-show="category.categoryId === selectedCate"
                :ref="
                  (el: any) => {
                    setInputRef(el?.inputElement, category.categoryId, 'cate');
                  }
                "
                v-model="category.categoryName"
                required
                styles="input-edit"
                :rules="useInputValidation({ maxLength: 30 })"
                :maxlength="30"
                @blur="isPressEnter ? null : handleUpdateCategoryName(category)"
                @keypress="
                  (event) => {
                    handleKeyPress(event, category, 'cate');
                  }
                "
              />
              <span
                v-show="category.categoryId !== selectedCate"
                v-html="
                  searchBy !== 'rule'
                    ? highlightText(category.categoryName, searchName)
                    : category.categoryName
                "
              >
              </span>
            </div>
            <BasePopover
              :options="listCategoryActions(category)"
              custom-location="bottom-left"
              class="flex-initial"
            >
              <template #activator>
                <DotsVerticalIcon />
              </template>
            </BasePopover>
          </div>
          <div
            v-show="listExpanedRule.includes(category.categoryId)"
            class="group-rule-part"
          >
            <div class="rule-part-wrapper">
              <div
                v-for="subCate in category.subCategories"
                :key="subCate.subCategoryId"
                class="rule-part"
              >
                <div
                  :ref="`subcate-${subCate.subCategoryId}`"
                  class="part-name-wrapper"
                >
                  <div class="rule-count rule-count-blue">
                    {{
                      subCate.rules.length < 10
                        ? `0${subCate.rules.length}`
                        : subCate.rules.length
                    }}
                  </div>
                  <div
                    class="part-name"
                    @click="() => handleToggleExpanded(subCate.subCategoryId)"
                  >
                    <BaseInputText
                      v-show="subCate.subCategoryId === selectedSubCate"
                      :ref="
                        (el: any) => {
                          setInputRef(
                            el?.inputElement,
                            subCate.subCategoryId,
                            'subcate'
                          );
                        }
                      "
                      v-model="subCate.subCategoryName"
                      required
                      styles="input-edit"
                      :rules="useInputValidation({ maxLength: 30 })"
                      :maxlength="30"
                      @blur="
                        isPressEnter
                          ? null
                          : handleUpdateSubCategoryName(subCate)
                      "
                      @keypress="
                        (event) => {
                          handleKeyPress(event, subCate, 'subcate');
                        }
                      "
                    />
                    <span
                      v-show="subCate.subCategoryId !== selectedSubCate"
                      v-html="
                        searchBy !== 'rule'
                          ? highlightText(subCate.subCategoryName, searchName)
                          : subCate.subCategoryName
                      "
                    ></span>
                  </div>
                  <BasePopover
                    :options="listRuleActions(subCate, category)"
                    custom-location="bottom-left"
                    class="flex-initial"
                  >
                    <template #activator>
                      <DotsVerticalIcon />
                    </template>
                  </BasePopover>
                </div>
                <div
                  v-if="subCate.rules.length > 0"
                  v-show="listExpanedRule.includes(subCate.subCategoryId)"
                  class="rule-list"
                >
                  <div
                    v-for="rule in subCate.rules"
                    :key="rule.ruleId"
                    class="relative"
                  >
                    <div class="hidden-item"></div>
                    <div
                      :ref="`rule-${rule.ruleId}`"
                      class="rule-item zoom-animation"
                      :class="[
                        rule.ruleId === selectedRule
                          ? `!border-[${BORDER_CONFIG.ACTIVE}] border-[2px]`
                          : '!border-lighter border-[1px]',
                        {
                          'is-unused': !rule.useYn,
                        },
                      ]"
                      @click="handleSelectRule(rule)"
                    >
                      <span
                        class="rule-name"
                        v-html="
                          !rule.ruleName && rule.isAddNew
                            ? rule.ruleName || 'New Rule'
                            : searchBy !== 'category'
                              ? highlightText(rule.ruleName, searchName)
                              : rule.ruleName
                        "
                      ></span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </LocomotiveComponent>
      <NoData v-else />
    </div>
    <BasePopup
      v-if="isShowPopupSave"
      v-model="isShowPopupSave"
      :content="t('product_platform.desc_update')"
      :icon="DialogIconType.Info"
      :cancel-button-text="t('product_platform.btn_no')"
      :submit-button-text="t('product_platform.btn_yes')"
      @on-close="handleClosePopupSave"
      @on-submit="handleSubmitPopupSave"
    />
    <BasePopup
      v-if="isShowPopupDeleteConfirm"
      v-model="isShowPopupDeleteConfirm"
      :content="t('product_platform.deleteCategory')"
      :icon="DialogIconType.Info"
      :cancel-button-text="t('product_platform.btn_no')"
      :submit-button-text="t('product_platform.btn_yes')"
      @on-close="handleClosePopupDelete"
      @on-submit="handleSubmitPopupDelete"
    />
  </div>
</template>
<script setup lang="ts">
import cloneDeep from "lodash-es/cloneDeep";
import { useI18n } from "vue-i18n";
import useRuleEngineStore from "@/store/admin/ruleEngine.store";
import { useSnackbarStore } from "@/store";
import { DialogIconType } from "@/enums";
import { TABS_RULE_DETAIL } from "@/interfaces/admin/rule-engine";
import { useInputValidation } from "@/composables/useInputValidation";
import { EMPTY_RULE } from "@/constants/admin/rule-engine";
import { highlightText } from "@/utils/format-data";
import DeleteIcon from "@/components/prod/icons/DeleteIcon.vue";
import EditIcon from "@/components/prod/icons/EditIcon.vue";
import PlusLargeIcon from "@/components/prod/icons/PlusLargeIcon.vue";
import BaseSelectScroll from "@/components/prod/common/BaseSelectScroll.vue";
import { BORDER_CONFIG } from "@/constants/index";

const { t } = useI18n();
const { showSnackbar } = useSnackbarStore();
const ruleEngineStore = useRuleEngineStore();
const {
  listRules,
  selectedRule,
  selectedSubCate,
  selectedCate,
  searchBy,
  searchName,
  isAddNewRule,
  isEditRule,
  ruleDetail,
  listRulesTemp,
  ruleDetailTemp,
  listExpanedRule,
  isShowRuleDetail,
  isEditRuleStructure,
  ruleStructureTemp,
  ruleStructure,
  tempRuleMsg,
  ruleMsg,
  ruleReportContent,
  ruleValidation,
  isTested,
  passed,
  failedCondUuids,
  passedMessage,
  passedCondUuids,
  isShowRuleList,
  isShowRuleReport,
  isShowRuleField,
  requiredCondUuids,
  isShowRuleTest,
} = storeToRefs(ruleEngineStore);
const {
  addRule,
  saveRuleStructure,
  addCategory,
  setSelectedSubCate,
  setSelectedCate,
  getListRules,
  setSelectedRule,
  setSelectedTab,
  setRuleDetail,
  getRuleStructureDetail,
  setEditRule,
  removeAddNewRule,
  resetSearch,
  createUpdateSubCategory,
  updateCategoryName,
  createUpdateRule,
  validateRuleDetail,
  removeAddNewCategory,
  deleteCategory,
  updateExpandedRule,
  validateRuleStructure,
  setSelectedNodeId,
} = ruleEngineStore;

const isShowPopupSave = ref(false);
const isShowPopupDeleteConfirm = ref(false);
const callbackFunc = ref();
const isPressEnter = ref(false);
const deleteSubCateItem = ref();
const isScrollWhenAddNew = ref<boolean>(false);
const instance = getCurrentInstance();
const currentY = ref<number>(0);
const ruleSelect = ref<any>();

const inputRefsCate = ref<any>({});
const inputRefsSubCate = ref<any>({});
const setInputRef = (el, id, type = "subcate") => {
  if (el) {
    type === "subcate"
      ? (inputRefsSubCate.value[id as string] = el)
      : (inputRefsCate.value[id as string] = el);
  }
};

const focusInput = async (id, type = "subcate") => {
  const el =
    type === "subcate"
      ? inputRefsSubCate.value[id as string]
      : inputRefsCate.value[id as string];
  if (el) {
    await nextTick();
    el.focus();
  }
};

const searchByOptions = computed(() => [
  {
    cmcdDetlNm: t("product_platform.categoryName"),
    cmcdDetlId: "category",
  },
  {
    cmcdDetlNm: t("product_platform.ruleName"),
    cmcdDetlId: "rule",
  },
]);

const countCategoryLength = computed(() => {
  return (category) => {
    let count = 0;
    category.subCategories.forEach((subCate) => {
      subCate.rules.forEach(() => {
        count++;
      });
    });
    return count < 10 ? `0${count}` : count;
  };
});

const handleSearch = () => {
  resetSearch();
  getListRules(searchName.value, searchBy.value);
};

const handleResetSearch = () => {
  searchName.value = "";
  listExpanedRule.value = [];
  resetSearch();
  getListRules("");
};

const handleSelectRule = (rule) => {
  setSelectedNodeId("");
  listRulesTemp.value = cloneDeep(listRules.value);
  if (rule.ruleId === selectedRule.value) {
    isShowRuleDetail.value = true;
    return;
  }
  ruleSelect.value = cloneDeep(rule);
  const funcSelectRule = () => {
    setSelectedRule(ruleSelect.value.ruleId);
    setSelectedTab(TABS_RULE_DETAIL.ATTRIBUTES);
    setRuleDetail({
      ruleId: ruleSelect.value.ruleId,
      ruleName: ruleSelect.value.ruleName,
      department: ruleSelect.value.department,
      user: ruleSelect.value.user,
      overview: ruleSelect.value.overview,
      creationDate: ruleSelect.value.creationDate,
      isAddNew: false,
      useYn: ruleSelect.value.useYn,
      categoryId: ruleSelect.value.categoryId,
      subCategoryId: ruleSelect.value.subCategoryId,
      ruleMsg: ruleSelect.value.ruleMsg || "",
    });
    getRuleStructureDetail(ruleSelect.value.ruleId);
  };
  if (isEditRule.value || isEditRuleStructure.value) {
    isShowPopupSave.value = true;
    callbackFunc.value = funcSelectRule;
    return;
  }
  funcSelectRule();
};

const handleClosePopupSave = () => {
  // Khong luu
  isShowPopupSave.value = false;
  listRules.value = cloneDeep(listRulesTemp.value);
  ruleDetail.value = cloneDeep(ruleDetailTemp.value);
  selectedNextRule();
};

const handleSubmitPopupSave = async () => {
  // luu rule detail
  isShowPopupSave.value = false;
  if (
    !ruleStructure.value ||
    JSON.stringify(ruleStructure.value) === JSON.stringify(EMPTY_RULE)
  ) {
    showSnackbar(t("product_platform.please_add_least_condition"), "error");
    return;
  }
  const invalidConditions = validateRuleStructure(ruleStructure.value!);
  const message = validateRuleDetail();
  if (invalidConditions.length > 0 || message) {
    requiredCondUuids.value = invalidConditions.map(
      ({ condUuid }) => condUuid!
    );
    showSnackbar(t("product_platform.required_field_missing"), "error");
    return;
  }
  const [isSaveDetailSuccess, isSaveStructureSuccess] = await Promise.all([
    handleSaveRuleDetail(),
    handleSaveRuleStructure(),
  ]);

  if (isSaveDetailSuccess && isSaveStructureSuccess) {
    showSnackbar(t("product_platform.saveSuccessfully"), "success");
  } else {
    showSnackbar(t("product_platform.dashboard.saveFailed"), "error");
  }
};

const handleSaveRuleDetail = async (): Promise<boolean> => {
  const response = await createUpdateRule();
  if (response?.status === 200) {
    getListRules(searchName.value, searchBy.value);
    selectedNextRule();
    return true;
  }
  return false;
};

const handleSaveRuleStructure = async (): Promise<boolean> => {
  if (
    JSON.stringify(ruleStructure.value) ===
      JSON.stringify(ruleStructureTemp.value) &&
    ruleMsg.value === tempRuleMsg.value
  ) {
    isEditRuleStructure.value = false;
    isShowRuleField.value = false;
    isShowRuleList.value = true;
    isShowRuleDetail.value = true;
    isShowRuleReport.value = false;
    isShowRuleTest.value = false;
    passedCondUuids.value = [];
    failedCondUuids.value = [];
    passedMessage.value = null;
    passed.value = false;
    isTested.value = false;
    ruleValidation.value = null;
    ruleReportContent.value = "";
    return true;
  }
  requiredCondUuids.value = [];
  const isSaveSuccess = await saveRuleStructure();
  if (isSaveSuccess) {
    isShowRuleField.value = false;
    isShowRuleReport.value = false;
    isShowRuleList.value = true;
    isShowRuleDetail.value = true;
    isEditRuleStructure.value = false;
    passedCondUuids.value = [];
    passedMessage.value = null;
    failedCondUuids.value = [];
    passed.value = false;
    isTested.value = false;
    ruleValidation.value = null;
    ruleReportContent.value = "";
    tempRuleMsg.value = cloneDeep(ruleMsg.value);
    ruleStructureTemp.value = cloneDeep(ruleStructure.value);
  }
  return isSaveSuccess;
};

const selectedNextRule = () => {
  setEditRule(false);
  if (isAddNewRule.value) {
    removeAddNewRule();
  }
  if (callbackFunc.value) {
    callbackFunc.value?.();
    callbackFunc.value = null;
  }
};

const handleUpdateCategoryName = async (cate) => {
  if (!cate.categoryName) {
    showSnackbar("Category name cannot empty", "error");
    return;
  }
  setSelectedCate("");
  const response = await updateCategoryName(cate);
  if (response?.status === 200) {
    getListRules(searchName.value, searchBy.value);
    showSnackbar("Save category successfully", "success");
  }
  isPressEnter.value = false;
};

const handleUpdateSubCategoryName = async (subCate) => {
  if (!subCate.subCategoryName) {
    showSnackbar("Category name cannot empty", "error");
    return;
  }
  setSelectedSubCate("");
  const response = await createUpdateSubCategory(subCate);
  if (response?.status === 200) {
    showSnackbar("Save category successfully", "success");
  }
  getListRules(searchName.value, searchBy.value);
  isPressEnter.value = false;
};

const handleKeyPress = (event, cate, type) => {
  let keyCode = event.keyCode ? event.keyCode : event.which;
  if (keyCode === 13) {
    isPressEnter.value = true;
    if (type === "subcate") {
      handleUpdateSubCategoryName(cate);
    } else {
      handleUpdateCategoryName(cate);
    }
  }
};

const handleClosePopupDelete = () => {
  isShowPopupDeleteConfirm.value = false;
};

const handleSubmitPopupDelete = async () => {
  isShowPopupDeleteConfirm.value = false;
  //delete category
  if (deleteSubCateItem.value?.isAddNew) {
    removeAddNewCategory();
  } else {
    const response = await deleteCategory(
      deleteSubCateItem.value?.subCategoryId
    );
    if (response?.status === 200) {
      showSnackbar("Delete successfully", "success");
      handleSearch();
    } else {
      showSnackbar("Cannot save", "error");
    }
  }
};

const handleToggleExpanded = (categoryId: string) => {
  updateExpandedRule(categoryId);
};

const listCategoryActions = (category) => {
  const actions = [] as any;
  actions.push({
    name: t("product_platform.addBelowCategory"),
    icon: PlusLargeIcon,
    iconProps: {
      class: "text-text-lighter",
    },
    onClick: () => {
      if (isAddNewRule.value || selectedSubCate.value) {
        showSnackbar("Cannot add new Category", "error");
        return;
      }
      isScrollWhenAddNew.value = false;
      addCategory(category.categoryId);
      updateExpandedRule(category.categoryId, true);
      nextTick(() => {
        isScrollWhenAddNew.value = true;
        const element =
          instance?.proxy?.$refs[`subcate-${selectedSubCate.value}`]!?.[0];
        currentY.value = element?.offsetTop + 300;
        setTimeout(() => {
          const input = element.querySelector("input");
          input?.focus();
        }, 1000);
      });
    },
  });
  actions.push({
    name: t("product_platform.editName"),
    icon: EditIcon,
    iconProps: {
      class: "text-text-lighter",
      fill: "#6B6D70",
    },
    onClick: () => {
      if (isAddNewRule.value || selectedCate.value) {
        showSnackbar("Cannot edit name", "error");
        return;
      }
      setSelectedCate(category.categoryId);
      focusInput(category.categoryId, "cate");
    },
  });
  return actions;
};

const listRuleActions = (subCate, cate) => {
  const actions = [] as any;

  if (!subCate.isAddNew) {
    actions.push({
      name: t("product_platform.addRule"),
      icon: PlusLargeIcon,
      iconProps: {
        class: "text-text-lighter",
      },
      onClick: () => {
        if (isAddNewRule.value || selectedSubCate.value) {
          showSnackbar("Cannot add new rule", "error");
          return;
        }
        const fundAddNewRule = () => {
          isScrollWhenAddNew.value = false;
          updateExpandedRule(subCate.subCategoryId, true);
          addRule(subCate.subCategoryId, cate.categoryId);
          nextTick(() => {
            isScrollWhenAddNew.value = true;
            const element =
              instance?.proxy?.$refs[`rule-${selectedRule.value}`]!?.[0];
            currentY.value = element?.offsetTop + 300;
          });
        };
        if (isEditRule.value) {
          isShowPopupSave.value = true;
          callbackFunc.value = fundAddNewRule;
          return;
        }
        fundAddNewRule();
      },
    });
    actions.push({
      name: t("product_platform.editName"),
      icon: EditIcon,
      iconProps: {
        class: "text-text-lighter",
        fill: "#6B6D70",
      },
      onClick: () => {
        if (isAddNewRule.value || selectedSubCate.value) {
          showSnackbar("Cannot edit name", "error");
          return;
        }
        setSelectedSubCate(subCate.subCategoryId);
        focusInput(subCate.subCategoryId, "subcate");
      },
    });
  }
  if (subCate.rules.length === 0) {
    actions.push({
      name: t("product_platform.actionRemove"),
      icon: DeleteIcon,
      iconProps: {
        class: "text-text-lighter",
        fill: "#6B6D70",
      },
      onClick: () => {
        if (subCate.isAddNew && !subCate.subCategoryName) {
          removeAddNewCategory();
        } else {
          isShowPopupDeleteConfirm.value = true;
          deleteSubCateItem.value = subCate;
        }
      },
    });
  }
  return actions;
};

onBeforeMount(() => {
  if (listRules.value.length === 0) {
    getListRules("");
  }
});
</script>
<style lang="scss" scoped>
.group-rule {
  display: flex;
  flex-direction: column;
  font-family: "Noto Sans KR";
  width: 100%;
  padding-bottom: 16px;
  .group-name-wrapper {
    width: 100%;
    height: 40px;
    padding: 7px 8px 7px 0;
    display: flex;
    align-items: center;
    column-gap: 8px;
    cursor: pointer;
    .rule-count {
      width: 32px;
      height: 32px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: 500;
      font-size: 13px;
    }

    .group-name {
      font-weight: 500;
      font-size: 13px;
      letter-spacing: 0.25px;
      flex: 1;
      text-overflow: ellipsis;
      overflow: hidden;
      white-space: nowrap;
      color: #3a3b3d;
    }
  }
  .group-rule-part {
    display: flex;
    column-gap: 8px;
    padding-left: 40px;
    .rule-part-wrapper {
      display: flex;
      flex-direction: column;
      width: 100%;
      gap: 16px;
    }
    .rule-part {
      display: flex;
      flex-direction: column;
      width: 100%;
      gap: 12px;
      .part-name-wrapper {
        width: 100%;
        height: 40px;
        padding: 7px 8px 7px 0;
        display: flex;
        align-items: center;
        column-gap: 12px;
        .rule-count {
          width: 32px;
          height: 32px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-weight: 500;
          font-size: 13px;
        }
        .part-name {
          font-weight: 500;
          font-size: 13px;
          letter-spacing: 0.25px;
          flex: 1;
          text-overflow: ellipsis;
          overflow: hidden;
          white-space: nowrap;
          color: #3a3b3d;
          cursor: pointer;
        }
      }
      .rule-list {
        display: flex;
        flex-direction: column;
        gap: 12px;
        .hidden-item {
          width: 100%;
          height: 41px;
          position: absolute;
          border-radius: 8px;
          z-index: 0;
          &:first-child::before {
            height: 16px;
            top: -17px;
          }
          &::before {
            display: block;
            content: "";
            height: 12px;
            width: 1px;
            background-color: #dce0e5;
            position: absolute;
            left: 16px;
            top: -13px;
          }
        }
        .rule-item {
          height: 41px;
          padding: 8px 16px;
          border-radius: 8px;
          box-shadow: 0px -16px 16px 0px #395bc20a inset;
          position: relative;
          z-index: 1;
          background-color: #fff;
          &:hover {
            cursor: pointer;
          }
          .rule-name {
            font-size: 13px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            color: #3a3b3d;
            display: block;
          }
        }
        .is-unused {
          background-color: #e9ebf0;
          .rule-name {
            opacity: 0.4;
          }
        }
      }
    }
  }
  .rule-count-red {
    background: #fff0f2;
    color: #ba1642;
  }
  .rule-count-blue {
    background: #e8f4fc;
    color: #1570ef;
  }
}
.input-edit :deep(.v-field__field) {
  height: 32px;
}
.input-edit :deep(.v-field) {
  height: 32px;
}

.input-edit :deep(.v-field__input) {
  padding: 0 0 0 16px;
  height: 38px;
  min-height: 38px;
}

.input-edit :deep(.custom-text-field .v-input__control) {
  height: 32px;
}

:deep() .highlight {
  background-color: yellow;
}
</style>
