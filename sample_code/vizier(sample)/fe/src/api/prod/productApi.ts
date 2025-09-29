import * as API_PATH from "@/api/prod/path";
import {
  ParamsProductStructureDetailRoot,
  IOfferDetailInfoParam,
  IResourceDetailInfoParam,
  IDiscountDetailParam,
} from "@/interfaces/prod/offer";
import { httpClient } from "@/utils/http-common";
import { ParamUIHistoryTab } from "@/interfaces/prod/HistoryTab";
import {
  removeUndefinedProperties,
  trimAndRemoveEmptyProperties,
} from "@/utils/format-data";

const getProductsApi = (params: any) => {
  const filteredParams = removeUndefinedProperties(params);
  return httpClient.get(API_PATH.SEARCH_PRODUCT_PATH, {
    params: trimAndRemoveEmptyProperties(filteredParams),
  });
};

const getProductStructureDetailRootApi = (
  params: ParamsProductStructureDetailRoot
) => {
  return httpClient.get(API_PATH.PRODUCT_DETAIL_PATH, {
    params,
  });
};

const updateProductAttributesApi = (data: any) => {
  return httpClient.put(API_PATH.PRODUCT_STRUCTURE_DETAIL_ROOT_PATH, {
    data,
  });
};

const updateProductStructureApi = (data: any) => {
  return httpClient.put(API_PATH.STRUCTURE_PATH, data);
};

const getOfferDetailForAddOnPricePlanApi = (params: IOfferDetailInfoParam) => {
  return httpClient.get(API_PATH.OFFER_DETAIL_PATH, { params });
};

const getOfferDetailForDiscountApi = (params: IDiscountDetailParam) => {
  return httpClient.get(API_PATH.COMPONENT_DISCOUNT_DETAIL_PATH, { params });
};

const getResourceDetailApi = (params: IResourceDetailInfoParam) => {
  return httpClient.get(API_PATH.RESOURCE_DETAIL_PATH, { params });
};
const updateProductApi = (data: any) => {
  return httpClient.put(API_PATH.OFFER_DETAIL_PATH, data);
};

const initProductCreateApi = (params: any) => {
  return httpClient.get(API_PATH.INIT_OFFER_CREATE_PATH, { params });
};

const createProductApi = (data: any) => {
  return httpClient.post(API_PATH.OFFER_CREATE_PATH, data);
};

const getUiHistoryTab = (params: ParamUIHistoryTab) => {
  return httpClient.get(API_PATH.UI_HISTORY_TAB, { params });
};

export {
  getProductsApi,
  getProductStructureDetailRootApi,
  updateProductAttributesApi,
  updateProductStructureApi,
  getOfferDetailForAddOnPricePlanApi,
  getOfferDetailForDiscountApi,
  getResourceDetailApi,
  updateProductApi,
  initProductCreateApi,
  createProductApi,
  getUiHistoryTab,
};
