<template>
  <SearchPane
    ref="searchPane"
    title="product_platform.factorTypeSearch"
    :container-class="'rounded-[12px]'"
    :model-list="factorsType"
    :model-param="{ searchKey: paramFilter.factorTypeName }"
    :pane-type="SearchPaneType.FactorTable"
    :pagination="pagination"
    :pane-col="ColNumber.One"
    :selected-item="factorTypeSelected"
    :open-popup="openPopup"
    :popup-content="popupContent"
    :item-height="80"
    :show-pagination="false"
    @on-click-item="onSelectedFactor"
    @on-search="handleSearch"
    @on-reset="handleResetSearch"
    @close-popup="
      () => {
        openPopup = false;
      }
    "
    @submit-popup="handleCancel"
    @on-lazy-load="handleLazyLoad"
  >
    <template #custom-form>
      <BaseInputSearch
        v-model="paramFilter.factorTypeName"
        density="comfortable"
        :label="'search'"
        variant="solo"
        hide-details
        single-line
        rounded="4"
        class="mt-2"
        @keyup.enter="handleEnterSearch"
      />
    </template>
  </SearchPane>
</template>

<script setup lang="ts">
import useFactorStore from "@/store/admin/factor.store";
import { ColNumber, SearchPaneType } from "@/enums";
import { TABS_NAME_COLLECTION } from "@/constants/index";
import { useI18n } from "vue-i18n";

const { t } = useI18n();
const {
  currentTab,
  factorsType,
  factorsTypeTotal,
  paramFilter,
  factorTypeSelected,
  factorSelected,
  paramFilterDetail,
  factorDetail,
  factorTypeDetail,
  isEditFactorTypeDetail,
  isEditFactorDetail,
  isCreateFactorDetail,
  isAddNewFactorChild,
} = storeToRefs(useFactorStore());
const { getListFactorsType, resetParamFactorType, getDetailFactorType } =
  useFactorStore();

const openPopup = ref(false);
const tempItemSelected = ref(null);
const popupContent = computed(() => t("product_platform.updatingConfirmSaved"));
const searchPane = ref();

const pagination = computed<any>(() => ({
  totalSearchItems: factorsTypeTotal.value,
  totalItems: factorsTypeTotal.value,
}));

const handleLazyLoad = async () => {
  if (paramFilter.value.size <= factorsTypeTotal?.value) {
    paramFilter.value.size = paramFilter.value?.size * 2;
    await handleResponse();
  }
};

const handleCancel = async () => {
  openPopup.value = false;
  factorTypeSelected.value = tempItemSelected.value;
  paramFilterDetail.value.factorName = "";
  paramFilterDetail.value.factorTypeCode =
    factorTypeSelected.value.factorTypeCode;
  paramFilterDetail.value.size = 8;
  factorDetail.value = null;
  factorSelected.value = null;
  isEditFactorDetail.value = false;
  isEditFactorTypeDetail.value = false;
  isCreateFactorDetail.value = false;
  isAddNewFactorChild.value = false;
  currentTab.value = TABS_NAME_COLLECTION.GENERAL;
  await handleSearch();
  await getDetailFactorType();
};

const handleSearch = async (size = 14, isClick = false) => {
  paramFilter.value.size = size;
  if (isClick) {
    paramFilter.value.factorTypeCode = undefined;
  }
  await handleResponse();
};
const handleResetSearch = async (size = 14) => {
  resetParamFactorType();
  paramFilter.value.size = size;
  await handleResponse();
};

const onSelectedFactor = async (item) => {
  if (item?.factorTypeCode !== factorTypeSelected.value?.factorTypeCode) {
    if (isEditFactorTypeDetail.value) {
      tempItemSelected.value = item;
      openPopup.value = true;
    } else {
      currentTab.value = TABS_NAME_COLLECTION.GENERAL;
      factorDetail.value = null;
      factorSelected.value = null;
      factorTypeSelected.value = item;
      if (item?.factorTypeCode) {
        paramFilterDetail.value.factorTypeCode = item?.factorTypeCode;
        paramFilterDetail.value.factorName = "";
        paramFilterDetail.value.factorCode = "";
        paramFilterDetail.value.size = 8;
        paramFilterDetail.value.page = 1;
        await getDetailFactorType();
      }
    }
  }
};

const handleResponse = async () => {
  await getListFactorsType();
};

const handleEnterSearch = () => {
  if (searchPane.value) {
    searchPane.value?.handleSearch?.();
  }
};

watch(
  () => factorTypeDetail.value,
  (newVal) => {
    if (newVal) {
      factorsType.value = factorsType.value?.map((type) => {
        if (type.factorTypeCode === newVal.factorTypeCode) {
          return {
            ...type,
            factorTypeName: newVal?.factorTypeName,
            useYn: newVal?.useYn,
          };
        }
        return type;
      });
    }
  },
  { deep: true }
);

onMounted(async () => {
  if (searchPane.value) {
    searchPane.value?.calcTotalItem?.();
    paramFilter.value.size = searchPane.value?.totalItem || 14;
  }
  await handleResponse();
});
</script>

<style lang="scss" scoped></style>
