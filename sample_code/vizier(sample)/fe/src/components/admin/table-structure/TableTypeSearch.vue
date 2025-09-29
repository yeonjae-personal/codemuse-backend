<template>
  <SearchPane
    ref="searchPane"
    title="product_platform.tableTypeSearch"
    :container-class="'rounded-[12px]'"
    :model-list="tableTypeSearchList"
    :model-param="{ searchKey: tableTypeSearchParams.tableTypeName }"
    :pane-type="SearchPaneType.FactorTable"
    :pagination="pagination"
    :pane-col="ColNumber.One"
    :selected-item="selectedTableType"
    :open-popup="openPopup"
    :popup-content="$t('product_platform.updatingConfirmSaved')"
    :item-height="64"
    :show-pagination="false"
    :show-float-icon-left="isShowTableTypeDetail || tableSelected"
    icon-left-class="right-[0px]"
    @on-click-float-left="onCloseTableTypeSearch"
    @on-click-item="onSelectedTableType"
    @on-search="handleSearch"
    @on-reset="handleResetSearch"
    @close-popup="
      () => {
        openPopup = false;
      }
    "
    @submit-popup="handleConfirm"
    @on-lazy-load="handleLazyLoad"
  >
    <template #custom-form>
      <BaseInputSearch
        v-model="tableTypeSearchParams.tableTypeName"
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
import { TABS_NAME_COLLECTION } from "@/constants/index";
import { ColNumber, SearchPaneType } from "@/enums";
import useTableStructureStore from "@/store/admin/tableStructure.store";

const {
  loading,
  currentTab,
  isEditTable,
  isEditTableType,
  tableTypeSearchParams,
  tableTypeSearchList,
  tableTypeSearchTotal,
  selectedTableType,
  isGetAllTableTypeSearch,
  tableTypeDetailParams,
  tableSelected,
  isShowTableTypeDetail,
  isShowTableTypeSearch,
  isRedirectTo,
} = storeToRefs(useTableStructureStore());
const {
  getListTableType,
  getListTableTypeDetail,
  resetTableTypeSearch,
  resetTableSearch,
  resetTableSearchData,
} = useTableStructureStore();

const openPopup = ref(false);
const isReset = ref(false);
const tempItemSelected = ref(null);
const searchPane = ref();

const pagination = computed<any>(() => ({
  totalSearchItems: tableTypeSearchTotal.value,
  totalItems: tableTypeSearchTotal.value,
}));

const handleLazyLoad = async () => {
  if (!loading.value && !isGetAllTableTypeSearch.value) {
    tableTypeSearchParams.value.page = tableTypeSearchParams.value.page + 1;
    await handleResponse();
  }
};

const handleConfirm = async () => {
  if (isReset.value) {
    isEditTableType.value = false;
    isEditTable.value = false;
    isShowTableTypeDetail.value = false;
    tableSelected.value = null;
    resetTableTypeSearch();
    await handleResponse();
    openPopup.value = false;
    return;
  }
  selectedTableType.value = tempItemSelected.value;
  isEditTable.value = false;
  isEditTableType.value = false;
  openPopup.value = false;
  currentTab.value = TABS_NAME_COLLECTION.GENERAL;
  tableSelected.value = null;
  tableTypeDetailParams.value.page = 1;
  tableTypeDetailParams.value.tableTypeCode =
    selectedTableType.value.tableTypeCode;
  resetTableSearchData();
  await getListTableTypeDetail();
};

const handleSearch = async (size = 16, isClick, page = 1) => {
  if (isEditTable.value || isEditTableType.value) {
    openPopup.value = true;
    isReset.value = true;
  } else {
    resetData();
    tableTypeSearchParams.value.size = size;
    tableTypeSearchParams.value.page = isClick ? 1 : page;
    if (isClick) {
      tableTypeSearchParams.value.tableTypeCode = undefined;
    }
    await handleResponse();
  }
};

const handleResetSearch = async (size = 16) => {
  if (isEditTable.value || isEditTableType.value) {
    openPopup.value = true;
    isReset.value = true;
  } else {
    isEditTableType.value = false;
    isEditTable.value = false;
    isShowTableTypeDetail.value = false;
    tableSelected.value = null;
    resetTableTypeSearch();
    tableTypeSearchParams.value.size = size;
    await handleResponse();
  }
};

const onSelectedTableType = async (item) => {
  if (item?.tableTypeCode !== selectedTableType.value?.tableTypeCode) {
    if (isEditTable.value || isEditTableType.value) {
      tempItemSelected.value = item;
      openPopup.value = true;
    } else {
      selectedTableType.value = item;
      if (item?.tableTypeCode) {
        tableTypeDetailParams.value.tableTypeCode = item?.tableTypeCode;
        currentTab.value = TABS_NAME_COLLECTION.GENERAL;
        tableSelected.value = null;
        tableTypeDetailParams.value.page = 1;
        resetTableSearchData();
        await getListTableTypeDetail();
      }
    }
    isShowTableTypeDetail.value = true;
  }
};

const handleResponse = async () => {
  loading.value = true;
  await getListTableType();
  loading.value = false;
};

const resetData = () => {
  tableTypeSearchTotal.value = 0;
  tableTypeSearchList.value = [];
  tableTypeSearchParams.value.page = 1;
  isGetAllTableTypeSearch.value = false;
  resetTableSearch();
};

const onCloseTableTypeSearch = () => {
  isShowTableTypeSearch.value = false;
};

const handleEnterSearch = () => {
  if (searchPane.value) {
    searchPane.value?.handleSearch?.();
  }
};

onMounted(async () => {
  if (!tableTypeSearchList.value?.length && !isRedirectTo.value) {
    if (searchPane.value) {
      searchPane.value?.calcTotalItem?.();
      tableTypeSearchParams.value.size = searchPane.value?.totalItem || 16;
    }
    await handleSearch(tableTypeSearchParams.value.size, false);
  }
});
</script>
