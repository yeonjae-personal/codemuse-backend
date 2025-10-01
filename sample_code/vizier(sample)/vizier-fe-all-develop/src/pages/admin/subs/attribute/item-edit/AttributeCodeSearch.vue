<template>
  <SearchPane
    ref="searchPaneAttributeCode"
    title="product_platform.attribute_code_search"
    container-class="rounded-lg pb-3"
    :pane-col="ColNumber.One"
    :item-height="64"
    :model-param="commCodeSearchParams"
    :model-list="listCommCode"
    :pagination="commCodePagination"
    :show-float-icon-left="true"
    :icon-left-class="'right-0'"
    @on-search="handleSearchCommCode"
    @on-reset="handleResetSearchCommCode"
    @on-change-page="handleChangePage"
    @on-click-float-left="handleCloseAttributeCode"
  >
    <template #custom-form="{ pageSize }">
      <div class="d-flex items-center gap-2 mt-2">
        <BaseSelect
          v-model="commCodeSearchParams.type"
          :default-item-select-all="false"
          :items="commCodeOptions"
          class="!w-[120px] flex-shrink-0"
        />
        <BaseInputSearch
          v-model.trim="commCodeSearchParams.value"
          density="comfortable"
          label="search"
          variant="solo"
          hide-details
          single-line
          rounded="4"
          @handle-search="() => handleSearchCommCode(pageSize)"
        />
      </div>
    </template>
    <template #custom-search-item="{ item }: any">
      <AttributeCodeSearchItem
        :key="item.cmcdGrpId"
        :item="item"
        :search-type-obj="{
          field: commCodeSearchParams.type,
          value: commCodeSearchParams.value,
          keysCheck: {
            code: LABEL_SEARCH_TYPE.CODE,
            name: LABEL_SEARCH_TYPE.NAME,
          },
        }"
        @switch-page="moveCommonCodePage($event)"
        @selected="handleSelectedCommCode($event)"
      />
    </template>
    <template #footer>
      <div
        v-if="listCommCode.length > 0"
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
          @click="handleApplyCommCode"
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
import AttributeCodeSearchItem from "./AttributeCodeSearchItem.vue";
import type { IAttributeCode } from "@/interfaces/admin/attribute-management";
import useRedirect from "@/composables/useRedirect";

const { t } = useI18n();
const {
  listCommCode,
  itemDetail,
  selectedCommCode,
  selectedAdditionalId,
  isShowAttributeCodeSearch,
} = storeToRefs(attributeManagementStore());
const {
  commCodeSearchParams,
  commCodePagination,
  getListCommCode,
  handleResetSearchCommCode,
} = attributeManagementStore();
const { showSnackbar } = useSnackbarStore();
const { moveCommonCodePage } = useRedirect();

const isShowPopupCancel = ref<boolean>(false);

const commCodeOptions = computed(() => [
  {
    name: t("product_platform.name"),
    value: LABEL_SEARCH_TYPE.NAME,
  },
  {
    name: t("product_platform.code"),
    value: LABEL_SEARCH_TYPE.CODE,
  },
]);

const handleSearchCommCode = (size, isSearch = false): void => {
  if (isSearch) selectedCommCode.value = null;
  commCodeSearchParams.page = 1;
  commCodeSearchParams.size = size;
  getListCommCode();
};

const handleCloseAttributeCode = (): void => {
  isShowAttributeCodeSearch.value = false;
};

const handleSubmitPopupCancel = (): void => {
  handleCloseAttributeCode();
  handleResetSearchCommCode();
};

const handleSelectedCommCode = (code: IAttributeCode): void => {
  selectedCommCode.value = code;
};

const handleShowPopupCancel = (): void => {
  isShowPopupCancel.value = true;
};

const handleClosePopupCancel = (): void => {
  isShowPopupCancel.value = false;
};

const handleApplyCommCode = (): void => {
  if (!selectedCommCode.value) {
    showSnackbar(t("product_platform.commonAdmin.plsSelectOne"), "error");
    return;
  }
  itemDetail.value.additionalInfo.forEach((item) => {
    if (selectedCommCode.value && item.id === selectedAdditionalId.value) {
      item.attributeCode = selectedCommCode.value?.cmcdGrpId!;
    }
  });
  selectedAdditionalId.value = "";
  handleResetSearchCommCode();
  handleCloseAttributeCode();
};

const handleChangePage = async (page: number): Promise<void> => {
  commCodeSearchParams.page = page;
  await getListCommCode(true);
};
</script>
