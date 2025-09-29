import set from "lodash-es/set";
import cloneDeep from "lodash-es/cloneDeep";
import { ParamsProductStructureDetailRoot } from "@/interfaces/prod/offer";
import { Pagination } from "@/interfaces/prod/table";
import {
  getProductStructureDetailRootApi,
  updateProductAttributesApi,
  updateProductStructureApi,
  initProductCreateApi,
  createProductApi,
  updateProductApi,
} from "@/api/prod/productApi";
import { DEFAULT_PAGINATION_OFFER } from "@/constants/table";
import {
  ADVENCED_SEARCH_PARAMS_DEFAULT,
  DEFAULT_PAGINATION,
  OFFER_TYPE,
  VIEW_MODE,
} from "@/constants/";
import {
  getUiOfferAdvanced,
  getUiOfferAdvancedTable,
} from "@/api/prod/offerApi";
import { removeUndefinedProperties } from "@/utils/format-data";
import { getUiImpactAnalysisChildren } from "@/api/prod/impactAnalysisApi";

const productStoreObj = () => {
  const itemsType = ref<any[]>([]);
  const products = ref<any[]>([]);
  const productsDuplicate = ref<any[]>([]);
  const productsTable = ref<any[]>([]);
  const offerAdvencedParams = ref(cloneDeep(ADVENCED_SEARCH_PARAMS_DEFAULT));
  const error = ref<any>(null);
  const pagination = ref<Pagination>(cloneDeep(DEFAULT_PAGINATION_OFFER));
  const paginationTable = ref<Pagination>(cloneDeep(DEFAULT_PAGINATION));
  const selectedProduct = ref<any>(null);
  const productDetails = ref<any>(null);
  const productType = ref<string>(OFFER_TYPE.PRICEPLAN);
  const showProductDetail = ref<boolean>(false);
  const viewMode = ref<string>(VIEW_MODE.GRID);
  const inputText = ref<any>(undefined);
  const selectedValue = ref<string>("name");
  const itemTypeSelected = ref<any>(null);
  const advencedSearchList = ref<any[]>([]);
  const getDetailFromOtherPage = ref<Boolean>(false);
  const isDuplicate = ref<Boolean>(false);
  const isRemovePage = ref<Boolean>(false);
  const showActionSave = ref<Boolean>(false);
  const offerDuplicateFinishProcess = ref<Boolean>(false);
  const loadingPagination = ref<boolean>(false);
  const actionType = ref<String>();
  const productLineCoordinates = ref<any[]>([]);
  const duplicateItem = ref<any>(null);
  const initData = ref<any>(null);
  const createOfferType = ref<any>(null);
  const itemTypeSearch = ref<string>("");
  const isSearchTableData = ref<boolean>(false);
  const isDuplicatedItemSibling = ref<any[]>([]);

  const getProducts = async (params: any) => {
    loadingPagination.value = true;
    try {
      const res = await getUiOfferAdvanced({
        ...removeUndefinedProperties(params),
        size: params.size || 15,
      });
      if (res?.data) {
        products.value = res.data.elements.map((item) => ({
          ...item,
          objUUID: item.objUuid,
        }));
        pagination.value = {
          ...pagination.value,
          currentPage: res?.data?.page,
          pageSize: res?.data?.size,
          totalSearchItems: res?.data?.totalElements || null,
          totalItems: res?.data?.totalElements || null,
          totalPages: res?.data?.totalPages || null,
        };
        return res;
      } else {
        products.value = [];
        if (showProductDetail.value == true) {
          pagination.value = set(cloneDeep(pagination.value), "pageSize", 7);
        } else {
          pagination.value = cloneDeep(DEFAULT_PAGINATION_OFFER);
        }
      }
    } catch (err: any) {
      error.value = err;
      throw new Error(err);
    } finally {
      loadingPagination.value = false;
    }
  };

  const getProductsTable = async (params: any) => {
    loadingPagination.value = true;
    try {
      const res = await getUiOfferAdvancedTable({
        ...removeUndefinedProperties(params),
        size: params.size || 15,
      });
      isSearchTableData.value = true;
      if (res?.data) {
        productsTable.value = res.data.elements.map((item) => ({
          ...item,
          objUUID: item.objUuid,
        }));
        paginationTable.value = {
          ...paginationTable.value,
          currentPage: res?.data?.page,
          pageSize: res?.data?.size,
          totalSearchItems: res?.data?.totalElements || null,
          totalItems: res?.data?.totalElements || null,
          totalPages: res?.data?.totalPages || null,
        };
        return res;
      } else {
        productsTable.value = [];
        paginationTable.value = cloneDeep(DEFAULT_PAGINATION);
      }
    } catch (err: any) {
      error.value = err;
      throw new Error(err);
    } finally {
      loadingPagination.value = false;
    }
  };

  const getProductStructureDetailRoot = async (
    params: ParamsProductStructureDetailRoot
  ) => {
    try {
      const res = await getProductStructureDetailRootApi(params);
      return res;
    } catch (err) {
      error.value = err;
      throw err;
    }
  };

  const setSelectedProduct = (prod: any) => {
    selectedProduct.value = prod;
  };

  const setAdvencedSearchList = async (type: any) => {
    const { data } = await initProductCreateApi({
      itemCode: type,
    });
    if (data) {
      advencedSearchList.value = [...data?.additional].map((row) => ({
        ...row,
        fieldName: row.attrUuid,
      }));
    }
  };

  const clearSelectedProduct = () => {
    selectedProduct.value = null;
  };

  const updateAttributesProduct = async (params: any) => {
    await updateProductAttributesApi(params);
  };

  const updateProductStructure = async (data: any) => {
    try {
      const res = await updateProductStructureApi(data);
      return res;
    } catch (err) {
      error.value = err;
      throw err;
    }
  };

  const initProductCreate = async (params: any) => {
    try {
      const res = await initProductCreateApi(params);
      initData.value = res.data;
      return res;
    } catch (err) {
      error.value = err;
      throw err;
    }
  };

  const createProduct = async (data: any) => {
    try {
      const res = await createProductApi(data);
      return res;
    } catch (err) {
      error.value = err;
      throw err;
    }
  };

  const updateProduct = async (data: any, duplicate: Boolean = false) => {
    try {
      if (duplicate) {
        data.general.dplcTrgtUuid = data?.general?.objUUID;
        return await createProductApi(data);
      } else {
        return await updateProductApi(data);
      }
    } catch (err) {
      error.value = err;
      throw err;
    }
  };

  const resetProductsList = () => {
    products.value = [];
    if (showProductDetail.value == true) {
      pagination.value = set(cloneDeep(pagination.value), "pageSize", 7);
    } else {
      if (viewMode.value == VIEW_MODE.GRID) {
        pagination.value = cloneDeep(DEFAULT_PAGINATION_OFFER);
      } else {
        paginationTable.value = cloneDeep(DEFAULT_PAGINATION);
      }
    }
  };

  const getSibling = async (objUuid: string) => {
    const { data } = await getUiImpactAnalysisChildren({
      prodUuid: objUuid,
    });
    if (data) {
      isDuplicatedItemSibling.value = data.map((item) => ({
        ...item,
        objName: item.prodItemNm,
        objCode: item.prodItemCd,
        objUuid: item.prodUuid,
        itemCode: selectedProduct.value?.itemCode,
      }));
    }
  };

  const resetOffer = () => {
    isRemovePage.value = true;
    error.value = null;
    // isDuplicate.value = false;
    selectedProduct.value = null;
    showProductDetail.value = false;
    itemTypeSelected.value = null;
    selectedValue.value = "name";
    inputText.value = undefined;
    viewMode.value = VIEW_MODE.GRID;
    productType.value = OFFER_TYPE.PRICEPLAN;
    productDetails.value = null;
    offerDuplicateFinishProcess.value = false;
    initData.value = null;
    productLineCoordinates.value = [];
    createOfferType.value = null;
    productsTable.value = [];
    resetProductsList();
  };

  const resetAdvancedSearchParams = () => {
    offerAdvencedParams.value = cloneDeep(ADVENCED_SEARCH_PARAMS_DEFAULT);
    advencedSearchList.value = [];
  };
  return {
    itemsType,
    products,
    productsDuplicate,
    productsTable,
    error,
    pagination,
    paginationTable,
    selectedProduct,
    showProductDetail,
    productDetails,
    productType,
    viewMode,
    inputText,
    selectedValue,
    itemTypeSelected,
    advencedSearchList,
    getDetailFromOtherPage,
    isDuplicate,
    isRemovePage,
    showActionSave,
    offerDuplicateFinishProcess,
    loadingPagination,
    actionType,
    offerAdvencedParams,
    productLineCoordinates,
    duplicateItem,
    initData,
    createOfferType,
    itemTypeSearch,
    isSearchTableData,
    isDuplicatedItemSibling,
    getProducts,
    getProductStructureDetailRoot,
    setSelectedProduct,
    setAdvencedSearchList,
    clearSelectedProduct,
    updateAttributesProduct,
    updateProductStructure,
    initProductCreate,
    createProduct,
    updateProduct,
    resetOffer,
    resetProductsList,
    resetAdvancedSearchParams,
    getProductsTable,
    getSibling,
  };
};

const useProductsStore = defineStore("productsStore", productStoreObj);
const useProductsDuplicateStore = defineStore(
  "productsDuplicateStore",
  productStoreObj
);
const useProductsCreateStore = defineStore(
  "productsCreateStore",
  productStoreObj
);

export { useProductsStore, useProductsCreateStore, useProductsDuplicateStore };
