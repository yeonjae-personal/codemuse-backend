<template>
  <SearchPane
    ref="searchPaneRef"
    title="product_platform.factorSearch"
    container-class="rounded-lg col-span-1"
    :pane-col="ColNumber.One"
    :item-height="43"
    :model-param="paramFilterFactorSearch"
    :model-list="factorsSearch?.items"
    :pagination="factorsSearch?.pagination"
    :show-float-icon-right="true"
    @on-search="handleSearch"
    @on-reset="handleResetSearch"
    @on-change-page="handleChangePage"
    @on-click-float-right="onClose"
  >
    <template #custom-form>
      <div class="grid grid-cols-2 gap-2 mt-2">
        <BaseSelectScroll
          ref="selectScroll"
          v-model="paramFilterFactorSearch.factorTypeCode"
          :options="factorInfoSearchListCustom"
          :default-item-select-all="false"
          :height="48"
          required
          :placeholder="$t(`product_platform.type`)"
        />

        <BaseInputSearch
          v-model="paramFilterFactorSearch.factorName"
          density="comfortable"
          label="search"
          variant="solo"
          hide-details
          single-line
          rounded="4"
          @keyup.enter="handleSearch()"
        />
      </div>
    </template>
    <template #custom-search-item="{ item }: any">
      <FactorItem
        :item="item"
        :title="item.factorName"
        :active="factorSelected === item.factorCode"
        :disable="checkExist(item)"
        :is-new="item?.isAdded"
        :search-text="paramFilterFactorSearch.factorName"
        is-show-expand
        :draggable="!checkExist(item)"
        @drag-start="handleDragStart"
        @drag-end="handleDragEnd"
        @selected-item="handleClickFactor(item)"
      />
    </template>
  </SearchPane>
</template>

<script setup lang="ts">
import useFactorStore from "@/store/admin/factor.store";
import { useDragStore, useSnackbarStore } from "@/store";
import { useI18n } from "vue-i18n";
import useMatrixStructureStore from "@/store/admin/matrixStructure.store";
import { ColNumber } from "@/enums";
import SearchPane from "@/components/prod/shared/SearchPane.vue";
const useSnackbar = useSnackbarStore();
const { t } = useI18n();

const searchPaneRef = ref<InstanceType<typeof SearchPane>>();
const factorSelected = ref(null);
const selectScroll = ref();
const { dragOfferType, isDragging } = storeToRefs(useDragStore());

const {
  factorsSearchTotal,
  paramFilterFactorSearch,
  factorsSearch,
  factorInfoSearchList,
} = storeToRefs(useFactorStore());
const { getListFactorsSearch, resetParamFactorSearch, getFactorSearchInfo } =
  useFactorStore();

const matrixStructureStore = useMatrixStructureStore();
const { isShowFactorSearch, matrixBuilderFactors } =
  storeToRefs(matrixStructureStore);

const handleClickFactor = (item) => {
  factorSelected.value = item.factorCode;
};
const factorInfoSearchListCustom = computed(() => {
  return factorInfoSearchList.value.map((item) => ({
    ...item,
    cmcdDetlNm: item?.factorTypeName,
    cmcdDetlId: item?.factorTypeCode,
  }));
});
const handleChangePage = async (page) => {
  paramFilterFactorSearch.value.page = page;
  await handleResponse();
};

const checkExist = (item) => {
  return matrixBuilderFactors.value?.some(
    (header) => header.factorCode === item.factorCode
  );
};

const handleSearch = async (size?: number) => {
  if (!size) {
    searchPaneRef.value?.calcTotalItem();
  }
  selectScroll.value.validate();
  if (!paramFilterFactorSearch.value.factorTypeCode) {
    useSnackbar.showSnackbar(
      t("product_platform.required_field_missing"),
      "error"
    );
    return;
  }
  paramFilterFactorSearch.value.page = 1;
  paramFilterFactorSearch.value.size = size
    ? size
    : searchPaneRef.value?.totalItem;
  await handleResponse();
};
const handleResetSearch = () => {
  selectScroll.value.resetValidate();
  resetParamFactorSearch();
  factorsSearch.value = {
    items: [],
    pagination: {
      ...factorsSearch.value.pagination,
      currentPage: 1,
    },
  };
  factorsSearchTotal.value = 0;
};

const onClose = () => {
  // entityDisplayForm.value.groupSearch = false;
  isShowFactorSearch.value = false;
};

const handleResponse = async () => {
  await getListFactorsSearch();
};

const handleDragStart = () => {
  dragOfferType.value = "factor";
  isDragging.value = true;
};

const handleDragEnd = () => {
  dragOfferType.value = "";
  isDragging.value = false;
};

onMounted(() => {
  getFactorSearchInfo();
});
</script>

<style lang="scss" scoped></style>
