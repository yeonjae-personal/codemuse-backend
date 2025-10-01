<template>
  <div class="flex justify-between items-center h-[40px] gap-3 w-full my-2">
    <BaseInputSearch
      v-model="tableTypeDetailParams.tableName"
      density="comfortable"
      label="search"
      variant="solo"
      hide-details
      single-line
      rounded="4"
      @keyup.enter="handleSearch"
    />
    <SearchAndRefreshButton
      @handle-search="handleSearch"
      @handle-refresh="handleResetSearch"
    />
  </div>
  <BaseTotalSearchResult
    v-if="tableSearchTotal > 0"
    class-name="pt-2"
    :total-search="tableSearchTotal"
    :total-items="tableSearchTotal"
  />
  <div
    id="locoTypeSwapper"
    class="relative h-full w-full"
    :class="[tableSearchTotal ? 'mt-2' : 'mt-4']"
  >
    <LocomotiveComponent
      :scroll-container-class="['!px-0 max-h-[calc(100vh-385px)]']"
      @call-lazy-load="handleLazyLoad"
    >
      <div class="content h-full overflow-hidden">
        <div class="flex flex-col gap-4 items-center px-[10px] my-1">
          <div
            v-for="item in tableTabList"
            :key="item?.tableName"
            ref="factorTypeItems"
            class="w-full"
          >
            <FactorItem
              :item="item"
              :title="item?.tableName"
              :active="item?.tableName === tableSelected?.tableName"
              :is-new="item?.isAdded"
              :disable="item?.useYn === 'N'"
              :search-text="tableTypeDetailParams.tableName"
              :is-show-expand="false"
              @selected-item="handleClickTable(item)"
            >
              <template #appendIcon>
                <TableIcon />
              </template>
            </FactorItem>
          </div>
        </div>
      </div>
    </LocomotiveComponent>
    <div
      v-if="tableTabList?.length === 0"
      class="flex-1 w-full flex align-center h-full"
    >
      <NoData />
    </div>
  </div>
  <base-popup
    v-model="openPopup"
    :icon="DialogIconType.Warning"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.updatingConfirmSaved')"
    @on-close="
      () => {
        openPopup = false;
      }
    "
    @on-submit="handleConfirm"
  />
</template>

<script setup lang="ts">
import { DialogIconType } from "@/enums";
import useTableStructureStore from "@/store/admin/tableStructure.store";

const {
  loading,
  tableSelected,
  tableTypeDetailParams,
  tableTabList,
  tableSearchTotal,
  isGetAllTable,
  tableTypeGeneralInit,
  isEditTable,
  isHasBeenCalled,
} = storeToRefs(useTableStructureStore());

const {
  getListTable,
  resetTableSearch,
  resetTableSearchData,
  resetTableListParams,
} = useTableStructureStore();
const openPopup = ref(false);
const tempItem = ref<any>();
const isReset = ref(false);

const handleLazyLoad = async () => {
  if (!loading.value && !isGetAllTable.value) {
    tableTypeDetailParams.value.page = tableTypeDetailParams.value.page + 1;
    await fetchListTable();
  }
};

const handleClickTable = async (item) => {
  if (isEditTable.value) {
    tempItem.value = item;
    openPopup.value = true;
    return;
  }
  if (tableSelected.value?.tableName !== item?.tableName) {
    tableSelected.value = item;
    isHasBeenCalled.value = false;
    resetTableListParams();
  }
};

const handleSearch = async () => {
  resetTableSearchData();
  tableTypeDetailParams.value.page = 1;
  tableTypeDetailParams.value.tableTypeCode =
    tableTypeGeneralInit.value?.tableTypeCode;
  await fetchListTable();
};

const handleResetSearch = async () => {
  if (isEditTable.value) {
    isReset.value = true;
    openPopup.value = true;
    return;
  }
  resetTableSearch();
  tableTypeDetailParams.value.tableTypeCode =
    tableTypeGeneralInit.value?.tableTypeCode;
  await fetchListTable();
};

const fetchListTable = async () => {
  loading.value = true;
  await getListTable();
  loading.value = false;
};

const handleConfirm = async () => {
  isEditTable.value = false;
  openPopup.value = false;
  tableSelected.value = tempItem.value;
  if (isReset.value) {
    tableSelected.value = null;
    resetTableListParams();
    resetTableSearch();
    tableTypeDetailParams.value.tableTypeCode =
      tableTypeGeneralInit.value?.tableTypeCode;
    await fetchListTable();
    isReset.value = false;
  }
};
</script>

<style lang="scss" scoped></style>
