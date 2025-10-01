<template>
  <SearchPane
    ref="searchPaneRef"
    title="product_platform.label_search"
    container-class="rounded-lg pb-3"
    :pane-col="ColNumber.One"
    :item-height="64"
    :model-param="labelSearchParams"
    :model-list="listLabel"
    :pagination="labelPagination"
    :show-float-icon-left="true"
    :icon-left-class="'right-0'"
    @on-search="handleSearchLabel"
    @on-reset="handleResetSearchLabel"
    @on-change-page="handleChangePage"
    @on-click-float-left="handleCloseLabelSearch"
  >
    <template #search-button-append>
      <BaseButton
        width="126px"
        :color="ButtonColorType.Secondary"
        class="ml-2"
        @click="() => moveLabelManagementPage()"
      >
        {{ t("product_platform.label_page") }}
        <template #append>
          <ArrowNarrowUpRightIcon />
        </template>
      </BaseButton>
    </template>
    <template #custom-form="{ pageSize }">
      <div class="d-flex items-center gap-2 mt-2">
        <BaseSelect
          v-model="labelSearchParams.type"
          :default-item-select-all="false"
          :items="labelSearchTypeOptions"
          class="!w-[120px] flex-shrink-0"
        />
        <BaseInputSearch
          v-model.trim="labelSearchParams.value"
          density="comfortable"
          label="search"
          variant="solo"
          hide-details
          single-line
          rounded="4"
          @handle-search="() => handleSearchLabel(pageSize)"
        />
      </div>
    </template>
    <template #custom-search-item="{ item }: any">
      <LabelSearchItem
        :key="item.labelId"
        :item="item"
        :search-type-obj="{
          field: labelSearchParams.type,
          value: labelSearchParams.value,
          keysCheck: {
            code: LABEL_SEARCH_TYPE.CODE,
            name: LABEL_SEARCH_TYPE.NAME,
          },
        }"
        :disabled="listLabelID.includes(item.labelId)"
        @switch-page="moveLabelManagementPage($event)"
        @selected="handleSelectedLabel($event)"
      >
      </LabelSearchItem>
    </template>
    <template #footer>
      <div
        v-if="listLabel.length > 0"
        class="flex justify-end item-center gap-2 px-6 mt-3"
      >
        <BaseButton
          :color="ButtonColorType.Gray"
          @click="handleShowPopupCancel"
        >
          {{ t("common.btn_cancel") }}
        </BaseButton>
        <BaseButton
          :color="ButtonColorType.Secondary"
          :disabled="listLabelID.includes(selectedLabel?.labelId || '')"
          @click="handleApplyLabel"
        >
          {{ t("product_platform.apply") }}
        </BaseButton>
      </div>
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
import attributeManagementStore from "@/store/admin/attributeManagement.store";
import { useSnackbarStore } from "@/store";
import { ButtonColorType, ColNumber, DialogIconType } from "@/enums";
import { LABEL_SEARCH_TYPE } from "@/constants/admin/label";
import useRedirect from "@/composables/useRedirect";
import { LabelLanguage } from "@/enums/labelManagement";
import LabelSearchItem from "./LabelSearchItem.vue";
import type { ILabelItem } from "@/interfaces/admin/label-management";
import SearchPane from "@/components/prod/shared/SearchPane.vue";

const { locale, t } = useI18n();
const {
  listLabel,
  itemDetail,
  selectedLabel,
  selectedAdditionalId,
  isShowLabelSearch,
  listLabelID,
} = storeToRefs(attributeManagementStore());
const {
  labelSearchParams,
  labelPagination,
  getListLabel,
  handleResetSearchLabel,
} = attributeManagementStore();
const { showSnackbar } = useSnackbarStore();
const { moveLabelManagementPage } = useRedirect();

const searchPaneRef = ref<InstanceType<typeof SearchPane>>();
const isShowPopupCancel = ref<boolean>(false);

const labelSearchTypeOptions = computed(() => [
  {
    name: t("product_platform.name"),
    value: LABEL_SEARCH_TYPE.NAME,
  },
  {
    name: t("product_platform.code"),
    value: LABEL_SEARCH_TYPE.CODE,
  },
]);

onMounted(() => {
  if (listLabel.value.length === 0) {
    searchPaneRef.value?.calcTotalItem();
    labelSearchParams.page = 1;
    labelSearchParams.size = searchPaneRef.value?.totalItem || 14;
    getListLabel();
  }
});

const handleSearchLabel = (size, isSearch = false, page = 1): void => {
  if (isSearch) selectedLabel.value = null;
  labelSearchParams.page = isSearch ? 1 : page;
  labelSearchParams.size = size;
  getListLabel();
};

const handleCloseLabelSearch = (): void => {
  isShowLabelSearch.value = false;
};

const handleSubmitPopupCancel = (): void => {
  handleCloseLabelSearch();
  handleResetSearchLabel();
};

const handleSelectedLabel = (label: ILabelItem): void => {
  selectedLabel.value = label;
};

const handleShowPopupCancel = (): void => {
  isShowPopupCancel.value = true;
};

const handleClosePopupCancel = (): void => {
  isShowPopupCancel.value = false;
};

const handleApplyLabel = (): void => {
  if (!selectedLabel.value) {
    showSnackbar(t("product_platform.commonAdmin.plsSelectOne"), "error");
    return;
  }
  itemDetail.value.additionalInfo.forEach((item) => {
    if (selectedLabel.value && item.id === selectedAdditionalId.value) {
      item.labelId = selectedLabel.value?.labelId!;
      const label = selectedLabel.value.items.find(
        ({ langCode }) => langCode === (locale.value || "en")
      );
      const englishLabel = selectedLabel.value.items.find(
        ({ langCode }) => langCode === LabelLanguage.English
      );
      const labelName =
        label && label.labelName ? label.labelName : englishLabel?.labelName!;
      item.labelName = labelName;
    }
  });
  listLabelID.value = itemDetail.value.additionalInfo.map(
    (item) => item.labelId
  );
  selectedAdditionalId.value = "";
  handleResetSearchLabel();
  handleCloseLabelSearch();
};

const handleChangePage = async (page: number): Promise<void> => {
  labelSearchParams.page = page;
  await getListLabel(true);
};
</script>
