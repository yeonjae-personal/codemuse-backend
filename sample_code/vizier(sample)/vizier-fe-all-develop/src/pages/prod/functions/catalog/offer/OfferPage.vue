<template>
  <div
    class="rounded-lg border border-[#666] h-full bg-white grid grid-cols-4"
    @dragover.prevent=""
  >
    <div
      :class="[
        'relative z-10 rounded-lg min-h-[300px]',
        showProductDetail ? 'col-span-1' : 'col-span-4',
      ]"
    >
      <div class="pt-6 pb-3 rounded h-full flex flex-col">
        <div class="flex px-6 z-30 flex-wrap justify-between items-center">
          <div
            :class="[
              'flex flex-nowrap justify-between items-center gap-4',
              { 'w-full': showProductDetail },
            ]"
          >
            <div class="flex flex-nowrap items-center w-full">
              <div :class="showProductDetail ? 'w-full' : 'w-[292px] mr-2'">
                <BaseSelectScroll
                  ref="selectScroll"
                  v-model="itemTypeSelected"
                  :options="itemsType || []"
                  :item-value="ITEM_VALUE"
                  :item-title="ITEM_TITLE"
                  :rules="[{ required: true }]"
                  :height="48"
                  :show-error-massage="true"
                  :default-item-select-all="false"
                  :placeholder="$t('product_platform.Type')"
                  required
                />
              </div>
              <div v-if="!showProductDetail" class="flex h-[48px]">
                <div :class="showProductDetail ? 'w-[44px]' : 'w-[120px]'">
                  <BaseSelectScroll
                    v-model="selectedValue"
                    :options="options || []"
                    :item-value="ITEM_VALUE"
                    :item-title="ITEM_TITLE"
                    :height="48"
                    :show-error-massage="false"
                    :default-item-select-all="false"
                    :show-option-null="false"
                  />
                </div>
                <div class="filter flex items-center gap-2 ml-2 pt-[1px]">
                  <BaseValidationInputText
                    v-model="inputText"
                    styles="catalog-input offer-search-long"
                    :placeholder="$t('product_platform.inputToSearch')"
                    hide-details
                    @keyup.enter.prevent="handleSearch"
                  />
                </div>
              </div>
              <div class="ml-2">
                <SearchAndRefreshButton
                  @handle-search="handleSearch"
                  @handle-refresh="handleResetSearch"
                />
              </div>
              <div v-if="!showProductDetail" class="ml-2">
                <SearchDetailButton
                  class="bg-white"
                  :is-active="isApplied"
                  :disabled="isDisableAdvancedButton"
                  @click="handleSearchDetail"
                />
              </div>
            </div>
          </div>
          <div
            v-if="showProductDetail"
            class="grid grid-cols-[1fr_2fr] gap-2 h-[48px] mt-2 flex-1"
          >
            <BaseSelectScroll
              v-model="selectedValue"
              :options="options || []"
              :item-value="ITEM_VALUE"
              :item-title="ITEM_TITLE"
              :height="48"
              :show-error-massage="false"
              :default-item-select-all="false"
              :show-option-null="false"
            />
            <BaseInputSearch
              v-model="inputText"
              density="comfortable"
              label="search"
              variant="solo"
              hide-details
              single-line
              rounded="4"
              @keyup.enter="handleSearch"
            />
          </div>
          <div
            :class="[
              'flex justify-center items-center ml-auto',
              { 'show-details w-100 mt-1': showProductDetail },
            ]"
          >
            <FileAction
              v-if="
                viewMode === VIEW_MODE.LIST &&
                !showProductDetail &&
                itemTypeSearch
              "
              class-name="mr-2"
              title="Upload Offers"
              description="Please upload Offer excel file that you have downloaded."
              :is-downloading="downloading"
              @upload-file="handleUploadOffer"
              @download-file="handleDownloadDataTable"
            />
            <switch-view-table
              v-if="
                viewMode === VIEW_MODE.LIST ||
                (!showProductDetail &&
                  (computedProducts?.length || productsTable?.length))
              "
              v-model="viewMode"
              class="ms-auto"
            />
          </div>
        </div>
        <div
          v-if="
            viewMode === VIEW_MODE.GRID && itemTypeSelected && totalSearchItems
          "
          class="mx-6 text-[13px] flex justify-end mr-6 py-2"
        >
          <BaseTotalSearchResult
            :total-search="totalSearchItems"
            :total-items="totalItems"
          />
        </div>
        <template v-if="viewMode === VIEW_MODE.GRID">
          <DetailSearchPane
            v-if="showProductDetail"
            ref="offerSearchPane"
            :list-item="computedProducts"
            :pagination="pagination"
            :item-height="68"
            :item-per-row="1"
            :is-loading="loadingPagination"
            scroll-content-class="grid grid-cols-1"
            container-class="!h-[calc(100vh_-_320px)]"
            @change-page="handleChangePage"
            @search="handleFillData"
          >
            <template #element="{ item, index }">
              <CardItem
                :key="item.objUUID"
                :item="item"
                :search-text="inputText"
                :search-field="selectedValue"
                :offer-type="item.itemCode"
                :actions="listActions(item)"
                :expired="isExpired(item?.validEndDtm)"
                :is-show-detail-infor="false"
                :is-show-actions="
                  !item?.duplicateItem && !item?.isDuplicatedItem
                "
                :code-cmcd-list="codeCmcdList"
                :selected-product="selectedProduct"
                @view-detail="confirmViewDetail($event, index)"
              />
            </template>
          </DetailSearchPane>
          <DetailSearchPane
            v-else
            ref="offerSearchPane"
            :list-item="computedProducts"
            :pagination="pagination"
            :item-height="195"
            :item-width="330"
            :is-loading="loadingPagination"
            scroll-content-class="flex flex-wrap"
            container-class="!h-[calc(100vh_-_260px)]"
            @change-page="handleChangePage"
            @search="handleFillData"
          >
            <template #element="{ item, index }">
              <CardItem
                :key="item.objUUID"
                :item="item"
                :search-text="inputText"
                :search-field="selectedValue"
                :offer-type="item.itemCode"
                :actions="listActions(item)"
                :expired="isExpired(item?.validEndDtm)"
                :is-show-actions="
                  !item?.duplicateItem && !item?.isDuplicatedItem
                "
                :width="330"
                :code-cmcd-list="codeCmcdList"
                :selected-product="selectedProduct"
                @view-detail="confirmViewDetail($event, index)"
              />
            </template>
          </DetailSearchPane>
        </template>
        <div v-else class="flex-grow rounded-lg">
          <DataTableOffer
            v-if="itemTypeSearch && !showProductDetail"
            v-model:pageSize="paginationTable.pageSize"
            v-model:current-page="paginationTable.currentPage"
            class="h-[calc(100vh_-_238px)]"
            :product-type="itemTypeSelected"
            :data="productsTable"
            :total-pages="paginationTable.totalPages || 0"
            :total-search-items="totalSearchItems"
            :text-search="inputText"
            :search-field="selectedValue"
            :disable-change="loadingPagination"
            @click-detail="viewDetail"
            @update:current-page="handleChangePage"
          />
        </div>
      </div>
    </div>
    <div v-if="showProductDetail" class="rounded-lg col-span-3">
      <ProductStructure
        :list-structure="structureData"
        :product-detail="productDetails"
        :screen="screenValue"
        :type="selectedProduct?.itemCode"
        @close-detail="closeDetail"
        @view-detail="viewDetail"
        @view-detail-duplicate="viewDetailDuplicate"
        @refresh-page="refreshPage"
        @handle-click-drop="handleClickDrop"
      />
    </div>
  </div>

  <BasePopup
    v-model="openPopupDuplicate"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.desc_exit_mode_duplicate')"
    :icon="DialogIconType.Warning"
    :submit-button-text="$t('product_platform.btn_yes')"
    @on-close="closePopupExitDuplicate"
    @on-submit="handleExitDuplicate"
  />
  <BasePopup
    v-model="openPopupModifiedStructure"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.modified_structre')"
    :icon="DialogIconType.Warning"
    :submit-button-text="$t('product_platform.btn_yes')"
    @on-close="closePopupModifiedStructure"
    @on-submit="handlePopupModifiedStructure"
  />
  <AdvancedSearch
    v-if="openSearchDetail"
    v-model="openSearchDetail"
    class-custom="search-detail-offer"
    :type="localSelectedType"
    :model-list="advencedSearchList"
    @on-close="closePopupSearchDetail"
    @on-submit="applyPopupSearchDetail"
    @on-reset="handleResetSearchDetail"
  />
</template>

<script setup lang="ts">
import { useRoute } from "vue-router";
import { useI18n } from "vue-i18n";
import cloneDeep from "lodash-es/cloneDeep";
import moment from "moment";
import { ACTION_TYPE, DATE_FORMAT, OFFER_TYPE, VIEW_MODE } from "@/constants/";
import {
  useProductsStore,
  useStructureStore,
  useSnackbarStore,
  useExtendCreateStore,
  useMenuStore,
  useOfferDuplicateProcessStore,
  useCategoryStore,
  useDuplicateStructureStore,
  useProductsDuplicateStore,
} from "@/store";
import {
  OPTIONS_SELECT_SEARCH,
  STRUCTURE_ITEM_SCREEN,
} from "@/constants/offer";
import { filterParamsAdvanced, isExpired } from "@/utils/format-data";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { DialogIconType, LargeItemCode, RequiredYn } from "@/enums";
import { useDownloadFile } from "@/composables/useDownloadFIle";
import { DOWNLOAD_EXCEL_OFFER_SEARCH } from "@/api/prod/path";
import { getListItemCodeApi } from "@/api/prod/commonApi";
import { useGroupCode } from "@/composables/useGroupCode";
import { configPath, findMenuItem } from "@/utils/config-path";
import useOfferCreateProcessStore from "@/store/offerCreateProcess.store";
import { DEFAULT_PAGINATION_OFFER } from "@/constants/table";
import { MenuItemID } from "@/enums/redirect";
import { appProvider, AppProvider } from "@/types/common";
import DuplicateIcon from "@/components/prod/icons/DuplicateIcon.vue";
import DetailSearchPane from "@/components/prod/shared/DetailSearchPane.vue";
import type { ActionType } from "@/interfaces/prod";

const route = useRoute();
const { t, locale } = useI18n();
const { ITEM_TITLE, ITEM_VALUE, search, groupCodeData } = useGroupCode();
const productStore = useProductsStore();
const categoryStore = useCategoryStore();
const structureStore = useStructureStore();
const duplicateStructureStore = useDuplicateStructureStore();
const productsDuplicateStore = useProductsDuplicateStore();
const snackbarStore = useSnackbarStore();
const { newOfferTabItem } = storeToRefs(useExtendCreateStore());
const { downloading, downloadFile } = useDownloadFile();
const { menuTree } = storeToRefs(useMenuStore());
const { offerBeClonedUuid, offerBeClonedCode } = storeToRefs(
  useOfferDuplicateProcessStore()
);
const {
  productsDuplicate,
  duplicateItem,
  selectedProduct: selectedDuplicateProduct,
} = storeToRefs(productsDuplicateStore);
const {
  showListStructure: showDuplicateListStructure,
  isDuplicate: isModeDuplicate,
} = storeToRefs(duplicateStructureStore);
const {
  pagination,
  paginationTable,
  products,
  productsTable,
  productDetails,
  productType,
  showProductDetail,
  viewMode,
  selectedProduct,
  inputText,
  selectedValue,
  itemTypeSelected,
  offerAdvencedParams,
  isRemovePage,
  advencedSearchList,
  getDetailFromOtherPage,
  productLineCoordinates,
  offerDuplicateFinishProcess,
  loadingPagination,
  itemsType,
  itemTypeSearch,
} = storeToRefs(productStore);

const { categoryEditModeSelected } = storeToRefs(useOfferCreateProcessStore());
const {
  structureData,
  showComponentDetail,
  selectedComponent,
  selectedStructureData,
  showListStructure,
  showStructureDetail,
  showActionSave,
  offerUuid,
  listComponentDuplicate,
  isPendingFinish,
} = storeToRefs(structureStore);

const replaceTab = inject<any>("replaceTab");
const { onBulkUploadFile } = inject<AppProvider>(appProvider, {
  onBulkUploadFile: async () => {},
});

const localSelectedType = ref();
const localInputText = ref<any>();
const isApplied = ref(false);
const options = OPTIONS_SELECT_SEARCH;
const screenValue = ref(STRUCTURE_ITEM_SCREEN.OFFER);
const selectedOfferIndex = ref(0);
const selectScroll = ref();
const strutureDataItem = ref(null);
const openPopupDuplicate = ref(false);
const openPopupModifiedStructure = ref(false);
const openSearchDetail = ref(false);
const itemDetail = ref<any>(null);
const dplcTrgtUuid = ref(null);
const codeCmcdList = ref(null);
const isClickResetButton = ref(true);
const offerSearchPane = ref<InstanceType<typeof DetailSearchPane>>();

const totalSearchItems = computed(() => {
  return viewMode.value === VIEW_MODE.GRID
    ? pagination.value.totalSearchItems || 0
    : paginationTable.value.totalSearchItems || 0;
});

const totalItems = computed(() => {
  return pagination.value.totalItems || 0;
});

const computedProducts = computed(() => products.value);

const isDisableAdvancedButton = computed<boolean>(
  () => !itemTypeSelected.value?.trim()
);

const handleSearchDetail = async () => {
  if (itemTypeSelected.value) {
    if (!isApplied.value) {
      await productStore.setAdvencedSearchList(itemTypeSelected.value);
    }
    openSearchDetail.value = true;
  } else {
    snackbarStore.showSnackbar(
      t("product_platform.required_field_missing"),
      "error"
    );
  }
};

const applyPopupSearchDetail = async (event) => {
  isApplied.value = true;
  isClickResetButton.value = false;
  localSelectedType.value = itemTypeSelected.value;
  localInputText.value = inputText.value;
  productStore.resetProductsList();
  offerAdvencedParams.value.general = event.general;
  offerAdvencedParams.value.additional = event.additional;
  await getProducts();
  openSearchDetail.value = false;
};

const closePopupSearchDetail = () => {
  openSearchDetail.value = false;
};

const handleResetSearchDetail = async () => {
  isApplied.value = false;
  closePopupSearchDetail();
  await getProducts();
};

const getScreenByItemType = () => {
  switch (itemTypeSelected.value) {
    case OFFER_TYPE.PRICEPLAN:
      return STRUCTURE_ITEM_SCREEN.PRICEPLAN;
    case OFFER_TYPE.DISCOUNT:
      return STRUCTURE_ITEM_SCREEN.DISCOUNT;
    case OFFER_TYPE.ADDON:
      return STRUCTURE_ITEM_SCREEN.ADDON;
    default:
      return STRUCTURE_ITEM_SCREEN.PRICEPLAN;
  }
};

const handleChangePage = async (newPage: number) => {
  const params = {
    ...offerAdvencedParams.value,
    itemCode: localSelectedType.value,
    objName: selectedValue.value === "name" ? localInputText.value : undefined,
    objCode: selectedValue.value === "code" ? localInputText.value : undefined,
    page: newPage,
    size:
      viewMode.value === VIEW_MODE.GRID
        ? pagination.value.pageSize
        : paginationTable.value.pageSize,
  };
  if (viewMode.value === VIEW_MODE.GRID) {
    productStore.getProducts(params);
  } else {
    productStore.getProductsTable(params);
  }
};

const handleFillData = async (page: number = 1, size: number) => {
  const objName =
    selectedValue.value === "name" ? localInputText.value : undefined;
  const objCode =
    selectedValue.value === "code" ? localInputText.value : undefined;
  let params = {
    page,
    size,
    objName,
    objCode,
    itemCode: localSelectedType.value,
  };
  if (isApplied.value) {
    params = {
      ...filterParamsAdvanced(offerAdvencedParams.value),
      ...params,
    };
  }
  await productStore.getProducts(params);
};

const getProducts = async (page = 1) => {
  if (isClickResetButton.value) return;
  offerSearchPane.value?.calcTotalItem();
  const currentPage = offerSearchPane.value?.pageChange
    ? offerSearchPane.value?.pageChange
    : page;
  const pageSize = offerSearchPane.value?.totalItem
    ? offerSearchPane.value?.totalItem
    : pagination.value.pageSize;
  let request = {
    page: currentPage,
    size:
      viewMode.value === VIEW_MODE.GRID
        ? pageSize
        : paginationTable.value.pageSize,
    objName: selectedValue.value === "name" ? localInputText.value : undefined,
    objCode: selectedValue.value === "code" ? localInputText.value : undefined,
    itemCode: localSelectedType.value,
  };

  if (isApplied.value) {
    request = {
      ...filterParamsAdvanced(offerAdvencedParams.value),
      ...request,
    };
  }
  if (viewMode.value === VIEW_MODE.GRID) {
    await productStore.getProducts(request);
  } else {
    await productStore.getProductsTable(request);
  }
};

const refreshPage = async () => {
  const general = selectedStructureData.value.general;
  const offerData = {
    objUuid: general?.find((item) => item.colName == "obj_uuid")?.attrVal,
    objName: general?.find((item) => item.colName == "obj_name")?.attrVal,
    objCode: general?.find((item) => item.colName == "obj_code")?.attrVal,
    itemCode: general?.find((item) => item.colName == "item_code")?.attrVal,
    additional: [
      ...selectedStructureData.value.additional,
      ...selectedStructureData.value.general.filter(
        (item) => item.dispTab == "G"
      ),
    ].map((item) => {
      if (item.fieldTypeCode === COLUMN_FIELD_TYPE.DM) {
        return { ...item, attrVal: JSON.stringify(item.attrVal) };
      }
      return item;
    }),
  };
  await getProducts(pagination.value.currentPage);
  const selectedItem = products.value.find(
    (item: any) => item.objUUID === selectedProduct.value.objUUID
  );
  selectedProduct.value = selectedItem ? selectedItem : offerData;
  productDetails.value = selectedItem ? selectedItem : offerData;
  await structureStore.getStructure({
    objUuid: selectedProduct.value?.objUUID || offerData.objUuid,
  });
};

const handleClickDrop = async () => {
  showListStructure.value = true;
  showComponentDetail.value = false;
  showStructureDetail.value = false;
};

const closePopupExitDuplicate = () => {
  openPopupDuplicate.value = false;
};

const handleExitDuplicate = () => {
  openPopupDuplicate.value = false;
  showActionSave.value = false;
  viewDetail(itemDetail.value, true);
};

const closePopupModifiedStructure = async () => {
  openPopupModifiedStructure.value = false;
};

const handlePopupModifiedStructure = async () => {
  dplcTrgtUuid.value = itemDetail.value?.dplcTrgtUuid;
  if (dplcTrgtUuid.value && dplcTrgtUuid.value !== "-") {
    const response = await structureStore.getStructure({
      objUuid: dplcTrgtUuid.value,
      onlyValidDtm: true,
    });
    strutureDataItem.value = response?.data;
    isPendingFinish.value = true;
  }
  openPopupModifiedStructure.value = false;
  productDetails.value = itemDetail.value;
  selectedProduct.value = itemDetail.value;

  if (viewMode.value === VIEW_MODE.LIST) {
    await getDetailProductList(productDetails.value, itemTypeSelected.value);
  }
  showProductDetail.value = true;
  await nextTick();
  await changeTotalItemSearch(selectedOfferIndex.value);
  await getStructureData(
    selectedProduct.value?.objUUID,
    strutureDataItem.value
  );
  showListStructure.value = true;
  showActionSave.value = true;
};

const handleSearch = async () => {
  isRemovePage.value = false;
  isClickResetButton.value = false;
  showProductDetail.value = false;
  await nextTick();
  selectScroll.value.validate();
  if (itemTypeSelected.value) {
    itemTypeSearch.value = itemTypeSelected.value;
    if (productType.value !== itemTypeSelected.value) {
      productStore.resetAdvancedSearchParams();
      isApplied.value = false;
    }
    screenValue.value = getScreenByItemType();
    productType.value = itemTypeSelected.value;
    localSelectedType.value = itemTypeSelected.value;
    localInputText.value = inputText.value;
    await getProducts();
    await handleTypeCmcd();
    productDetails.value = null;
    selectedProduct.value = null;
    selectedComponent.value = null;
    selectedStructureData.value = null;
    structureData.value = null;
    showStructureDetail.value = false;
    showListStructure.value = false;
    showComponentDetail.value = false;
    productLineCoordinates.value = [];
  } else {
    snackbarStore.showSnackbar(
      t("product_platform.required_field_missing"),
      "error"
    );
  }
};

const handleTypeCmcd = async () => {
  if (products.value?.length) {
    let arr = products.value[0]?.additional.filter(
      (item) => item.dispCardYn === RequiredYn.Yes
    );
    const commonCodeList = arr?.reduce((codeArr, item) => {
      if (
        [COLUMN_FIELD_TYPE.DL, COLUMN_FIELD_TYPE.DM].includes(
          item.fieldTypeCode
        ) &&
        item?.commGroupCode
      ) {
        codeArr.push(item.commGroupCode);
      }
      return codeArr;
    }, []);
    if (commonCodeList?.length) {
      await search(commonCodeList);
      codeCmcdList.value = cloneDeep(groupCodeData.value);
    }
  }
};

const handleResetSearch = () => {
  selectScroll.value.resetValidate();
  isClickResetButton.value = true;
  selectedValue.value = cloneDeep("name");
  productStore.resetProductsList();
  inputText.value = undefined;
  localInputText.value = undefined;
  itemTypeSelected.value = "";
  itemTypeSearch.value = "";
  isApplied.value = false;
  pagination.value = cloneDeep(DEFAULT_PAGINATION_OFFER);
  paginationTable.value.totalSearchItems = 0;
  productStore.resetAdvancedSearchParams();
  productDetails.value = null;
  selectedProduct.value = null;
  selectedComponent.value = null;
  selectedStructureData.value = null;
  structureData.value = null;
  showStructureDetail.value = false;
  showListStructure.value = false;
  showComponentDetail.value = false;
  productLineCoordinates.value = [];
  showProductDetail.value = false;
};

const handleDownloadDataTable = async () => {
  try {
    let params: any = {
      itemCode: itemTypeSelected.value,
      objName:
        selectedValue.value === "name" ? localInputText.value : undefined,
      objCode:
        selectedValue.value === "code" ? localInputText.value : undefined,
      language: locale.value,
    };
    if (isApplied.value) {
      params = {
        ...offerAdvencedParams.value,
        ...params,
      };
    }
    await downloadFile(DOWNLOAD_EXCEL_OFFER_SEARCH, params, "Offer");
  } catch (err: any) {
    snackbarStore.showSnackbar(
      err?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
};

const handleUploadOffer = async (file: File): Promise<void> => {
  onBulkUploadFile("", file, route.path, handleSearch);
};

const getStructureData = async (objUuid: string, data: any = null) => {
  try {
    let res: any = null;
    if (data) {
      res = data;
    } else {
      res = await structureStore.getStructure({
        objUuid,
      });
    }
    if (res?.data || res) {
      structureData.value = res?.data || res;
      if (dplcTrgtUuid.value) {
        structureData.value.benefit = filterDataValid(
          structureData.value.benefit
        );
        structureData.value.characteristics = filterDataValid(
          structureData.value.characteristics
        );
        structureData.value.price = filterDataValid(structureData.value.price);
        structureData.value.service = filterDataValid(
          structureData.value.service
        );
        listComponentDuplicate.value = [
          ...structureData.value.benefit,
          ...structureData.value.characteristics,
          ...structureData.value.price,
          ...structureData.value.service,
        ];
      } else {
        listComponentDuplicate.value = [];
      }
    } else {
      structureData.value = null;
      listComponentDuplicate.value = [];
    }
  } catch (err: any) {
    snackbarStore.showSnackbar(
      err?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  } finally {
    structureStore.resetDragDrop();
    showStructureDetail.value = false;
    showListStructure.value = false;
    showComponentDetail.value = false;
    selectedComponent.value = null;
    showProductDetail.value = true;
    dplcTrgtUuid.value = null;
  }
};

const filterDataValid = (data: any = []) => {
  const now = new Date();
  const dataValid = data
    .filter(
      (item) =>
        item.relationValidEndDtm === null ||
        new Date(item.relationValidEndDtm) >= now
    )
    .map((element: any) => {
      element.relationValidStartDtm = moment(now).format(DATE_FORMAT.DATE_TYPE);
      if (element?.relationValidEndDtm) {
        element.workTypeCode = ACTION_TYPE.ADD_EXPIRED;
      } else {
        element.workTypeCode = ACTION_TYPE.ADD;
      }
      return element;
    });
  return dataValid;
};

const viewDetail = async (element: any, exitDuplicate: boolean = false) => {
  if (element && selectedProduct.value?.objUUID !== element?.objUUID) {
    // check structure
    const objUuid = element?.objUUID;
    const res = await structureStore.getStructure({
      objUuid,
    });
    let validStructureData = false;
    if (res?.data) {
      res?.data?.forEach((item) => {
        if (item?.componentList?.length) {
          validStructureData = true;
        }
      });
      if (!validStructureData) {
        strutureDataItem.value = res?.data;
        itemDetail.value = element;
        openPopupModifiedStructure.value = true;
        return;
      }
      showProductDetail.value = true;
      await nextTick();
      if (viewMode.value === VIEW_MODE.LIST && showProductDetail) {
        getDetailProductList(element, itemTypeSelected.value);
      }
      // check structure
      await changeTotalItemSearch(selectedOfferIndex.value);

      viewMode.value = VIEW_MODE.GRID;
      productDetails.value = element;
      console.log(productDetails.value);

      selectedProduct.value = element;
      itemDetail.value = element;

      const isInvalidStructureData =
        !res?.data.benefit?.length &&
        !res?.data.characteristics?.length &&
        !res?.data.price?.length &&
        !res?.data.service?.length;
      if (isInvalidStructureData) {
        strutureDataItem.value = res?.data;
        itemDetail.value = element;
      }
    }

    await getStructureData(element?.objUUID, res);
  } else if (exitDuplicate && element) {
    await getStructureData(element?.objUUID);
  } else if (!element) {
    await getStructureData(selectedProduct.value?.objUUID);
  }
};

const getDetailProductList = async (element, selectedType) => {
  const detailProductList = await productStore.getProducts({
    page: 1,
    size: 10,
    objName: undefined,
    objCode: element.objCode,
    itemCode: selectedType,
  });
  localInputText.value = detailProductList?.data?.list?.[0].objCode;
};

const viewDetailDuplicate = async () => {
  await getStructureData(offerUuid.value);
};

const confirmViewDetail = async (element: any, index) => {
  if (element?.duplicateItem) return;
  selectedOfferIndex.value = index + 1;
  categoryEditModeSelected.value = null;
  categoryStore.$reset();
  showActionSave.value = false;
  viewDetail(element);
};

const closeDetail = async () => {
  showProductDetail.value = false;
  productDetails.value = null;
  selectedProduct.value = null;
  selectedComponent.value = null;
  selectedStructureData.value = null;
  structureData.value = null;
  showStructureDetail.value = false;
  showListStructure.value = false;
  showComponentDetail.value = false;
  productLineCoordinates.value = [];
  await nextTick();
  await changeTotalItemSearch();
};

const listActions = (item: any): ActionType[] => {
  return [
    {
      name: t("product_platform.duplicate"),
      icon: DuplicateIcon,
      onClick: async () => {
        isModeDuplicate.value = true;
        productsDuplicate.value = [];
        showDuplicateListStructure.value = false;
        selectedDuplicateProduct.value = cloneDeep(item);
        duplicateItem.value = cloneDeep({
          ...item,
          objUUID: null,
          objName: t("product_platform.offerPage.offerName"),
          validEndDtm: null,
          validStartDtm: null,
          duplicateItem: true,
        });
        productsDuplicate.value = [
          { ...cloneDeep(item), isDuplicatedItem: true },
          duplicateItem.value,
        ];
        offerBeClonedUuid.value = item.objUuid;
        offerBeClonedCode.value = item.objCode;
        const newRedirectObj = findMenuItem(
          menuTree.value,
          MenuItemID.OfferDuplicate
        );
        if (newRedirectObj) {
          newRedirectObj["path"] = configPath(newRedirectObj);
          replaceTab(newRedirectObj);
        }
      },
    },
  ];
};

const calculateNewPage = (
  oldPageNumber: number,
  itemPositionOnPage: number,
  oldPageSize: number,
  newPageSize: number
) => {
  //Find the absolute position of the selected item
  const selectedIndex = (oldPageNumber - 1) * oldPageSize + itemPositionOnPage;
  // Calculate the new page number
  const newPage = Math.ceil(selectedIndex / newPageSize);
  return newPage ? newPage : 1;
};

const changeTotalItemSearch = async (index: number = 0): Promise<void> => {
  offerSearchPane.value?.calcTotalItem();
  await nextTick();
  if (!isRemovePage.value && !getDetailFromOtherPage.value) {
    if (viewMode.value == VIEW_MODE.GRID) {
      const currentPage = pagination.value.currentPage;
      const oldPageSize = pagination.value.pageSize;
      const pageSize = offerSearchPane.value?.totalItem
        ? offerSearchPane.value?.totalItem
        : pagination.value.pageSize;
      const targetUUID = itemDetail?.value?.objUUID;
      if (index) {
        pagination.value.pageSize = pageSize;
        pagination.value.currentPage = calculateNewPage(
          currentPage,
          index,
          oldPageSize,
          pageSize
        );
        await getProducts(pagination.value.currentPage);
      } else {
        const foundIndex =
          computedProducts.value.findIndex(
            ({ objUUID }) => objUUID === targetUUID
          ) + 1;
        pagination.value.currentPage = calculateNewPage(
          currentPage,
          foundIndex,
          oldPageSize,
          pageSize
        );
        await getProducts(pagination.value.currentPage);
      }
      screenValue.value = getScreenByItemType();
      productType.value = itemTypeSelected.value;
    } else {
      pagination.value.pageSize = 10;
      pagination.value.currentPage = 1;
    }
  }
};

watch(
  () => newOfferTabItem.value,
  (element) => {
    confirmViewDetail(element, 0);
    inputText.value = element.objName;
    itemTypeSelected.value = element.itemCode;
    productType.value = element.itemCode;
  },
  { deep: true }
);

watch(
  () => viewMode.value,
  () => {
    if (!showProductDetail.value) {
      handleSearch();
    }
  }
);
watch(
  () => itemTypeSelected.value,
  () => {
    isApplied.value = false;
  },
  { deep: true }
);

watch(
  () => offerDuplicateFinishProcess.value,
  async () => {
    if (offerDuplicateFinishProcess.value) {
      if (inputText.value) {
        localInputText.value = inputText.value;
      }
      if (itemTypeSelected.value) {
        localSelectedType.value = itemTypeSelected.value;
      }
      handleChangePage(1);
      offerDuplicateFinishProcess.value = false;
    }
  },
  { deep: true, immediate: true }
);

watch(
  () => viewMode.value,
  () => {
    if (!showProductDetail.value) {
      handleSearch();
    }
  }
);

watch(
  () => itemTypeSelected.value,
  () => {
    isApplied.value = false;
  },
  { deep: true }
);

onMounted(async () => {
  if (inputText.value) {
    localInputText.value = inputText.value;
  }
  if (itemTypeSelected.value) {
    localSelectedType.value = itemTypeSelected.value;
  }
  try {
    if (!itemsType.value?.length) {
      const { data } = await getListItemCodeApi({
        lItemCode: LargeItemCode.Offer,
      });
      itemsType.value = data;
    }
  } catch (error: any) {
    snackbarStore.showSnackbar(
      error?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
});
</script>
