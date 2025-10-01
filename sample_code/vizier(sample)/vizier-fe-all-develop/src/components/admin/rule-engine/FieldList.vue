<template>
  <SearchPane
    ref="searchPaneFieldList"
    title="product_platform.fieldList"
    container-class="rounded-lg overflow-hidden h-full"
    :pane-col="ColNumber.One"
    :item-height="48"
    :model-param="searchParams"
    :model-list="listFieldComp"
    :pagination="pagination"
    :show-float-icon-left="true"
    :icon-left-class="'right-0'"
    @on-search="handleSearch"
    @on-reset="handleReset"
    @on-change-page="handleChangePage"
    @on-click-float-left="handleCloseFieldList"
  >
    <template #search-button-append>
      <BaseButton
        class="ml-2"
        :color="ButtonColorType.Secondary"
        :width="WIDTH_BUTTON.AUTO"
        @click="addNewFieldItem"
      >
        <EditIcon class="mr-[6px]" />
        {{ $t("product_platform.createField") }}
      </BaseButton>
    </template>
    <template #custom-form="{ pageSize }">
      <div class="d-flex items-center gap-2 mt-2">
        <BaseSelectScroll
          v-model="searchParams.type"
          :default-item-select-all="true"
          :options="labelSearchTypeOptions"
          class="!w-[120px] flex-shrink-0"
          :height="48"
        />
        <BaseInputSearch
          v-model.trim="searchParams.value"
          density="comfortable"
          label="search"
          variant="solo"
          hide-details
          single-line
          rounded="4"
          @handle-search="() => handleSearch(pageSize, true)"
        />
      </div>
    </template>
    <template #custom-search-item="{ item }">
      <FieldItem
        :key="item.fieldUuid"
        :item="item"
        :search-type-obj="{
          field: searchParams.type,
          value: searchParams.value,
          keysCheck: {
            name: RULE_FIELD_SEARCH_TYPE.NAME,
            key: RULE_FIELD_SEARCH_TYPE.KEY,
          },
        }"
        :is-used="listUsedField.includes(item.fieldKeyName)"
      />
    </template>
  </SearchPane>
  <BasePopup
    v-if="isShowPopupCancel"
    v-model="isShowPopupCancel"
    :content="t('product_platform.desc_cancel')"
    :icon="DialogIconType.Warning"
    :cancel-button-text="t('product_platform.btn_no')"
    :submit-button-text="t('product_platform.btn_yes')"
    @on-close="handleClosePopupCancel"
    @on-submit="handleSubmitPopupCancel"
  />
</template>

<script lang="ts" setup>
import { useI18n } from "vue-i18n";
import uniqBy from "lodash-es/uniqBy";
import { ButtonColorType, ColNumber, DialogIconType } from "@/enums";
import useRuleFieldStore from "@/store/admin/ruleField.store";
import { useSnackbarStore } from "@/store";
import useRuleEngineStore from "@/store/admin/ruleEngine.store";
import { RULE_FIELD_SEARCH_TYPE } from "@/constants/admin/rule-field";
import FieldItem from "./FieldItem.vue";
import BaseSelectScroll from "@/components/prod/common/BaseSelectScroll.vue";
import { WIDTH_BUTTON } from "@/constants/index";
import SearchPane from "@/components/prod/shared/SearchPane.vue";

const { t } = useI18n();

const { getListField, resetStore, addNewFieldItem } = useRuleFieldStore();
const { listField, selectedField, editUuid, searchParams, pagination } =
  storeToRefs(useRuleFieldStore());
const {
  ruleStructure,
  testRule,
  ruleValidation,
  ruleReportContent,
  isShowRuleTest,
  isShowRuleField,
  isShowRuleList,
  isExpanded,
  isShowRuleReport,
} = storeToRefs(useRuleEngineStore());
const { collectConditions, updateRuleTest } = useRuleEngineStore();
const { showSnackbar } = useSnackbarStore();

const searchPaneFieldList = ref<InstanceType<typeof SearchPane>>();
const listUsedField = ref<string[]>([]);
const isShowPopupCancel = ref<boolean>(false);

const listFieldComp = computed(() => listField.value as any);
const labelSearchTypeOptions = computed(() => [
  {
    cmcdDetlNm: t("product_platform.displayName"),
    cmcdDetlId: RULE_FIELD_SEARCH_TYPE.NAME,
  },
  {
    cmcdDetlNm: t("product_platform.keyName"),
    cmcdDetlId: RULE_FIELD_SEARCH_TYPE.KEY,
  },
]);

onMounted(() => {
  if (listField.value.length === 0) {
    searchPaneFieldList.value?.calcTotalItem();
    const size =
      searchPaneFieldList.value?.totalItem || searchParams.value.size;
    searchParams.value.size = size;
    getListField();
  }
});

const handleSearch = (size, isSearch = true, page = 1): void => {
  editUuid.value = null;
  searchParams.value.size = size;
  searchParams.value.page = isSearch ? 1 : page;
  if (isSearch) {
    selectedField.value = null;
  }
  getListField();
};

const handleReset = () => {
  resetStore();
  getListField();
};

const handleSubmitPopupCancel = (): void => {};

const handleCloseFieldList = (): void => {
  if (!!ruleValidation.value || !!ruleReportContent.value) {
    isShowRuleField.value = false;
    isShowRuleReport.value = true;
    return;
  }
  if (testRule.value.length > 0) {
    const conditions = collectConditions(ruleStructure.value!);
    const isEmptyAllCondition = conditions.every((item) => !!item.fieldUuid);
    if (!isEmptyAllCondition) {
      showSnackbar(
        t("product_platform.please_add_fields_for_condition"),
        "error"
      );
      return;
    }
    updateRuleTest(uniqBy(conditions, "keyName"));
    isShowRuleTest.value = true;
  } else if (!isExpanded.value) {
    isShowRuleList.value = true;
  }
  isShowRuleField.value = false;
};

const handleClosePopupCancel = (): void => {
  isShowPopupCancel.value = false;
};

const handleChangePage = async (page: number): Promise<void> => {
  searchParams.value.page = page;
  await getListField(true);
};

watch(
  ruleStructure,
  () => {
    const conditions = collectConditions(ruleStructure.value!);
    listUsedField.value =
      (uniqBy(conditions, "keyName").map((item) => item.keyName) as string[]) ||
      [];
  },
  { deep: true, immediate: true }
);
</script>
