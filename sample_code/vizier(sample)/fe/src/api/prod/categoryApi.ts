import { httpClient } from "@/utils/http-common";
import * as API_PATH from "@/api/prod/path";
import {
  ParamsUiCategoryOffer,
  ParamsUiCategoryProducts,
  ParamsUiCategorySearch,
  ParamsUiCategoryTree,
  CategoryOfferTreeSearchParam,
  ParamsUiCategoryDescription,
} from "@/interfaces/prod/CategoryInterface";

const getUiCategoryOffer = (params: ParamsUiCategoryOffer) => {
  return httpClient.get(API_PATH.UI_CATEGORY_OFFER_PATH, { params });
};
const getListTabs = () => {
  return httpClient.get(API_PATH.UI_CATEGORY_TAB_PATH);
};
const getUiCategoryProducts = (params: ParamsUiCategoryProducts) => {
  return httpClient.get(API_PATH.UI_CATEGORY_PRODUCTS_PATH, { params });
};
const getUiCategorySearch = (params: ParamsUiCategorySearch) => {
  return httpClient.get(API_PATH.UI_CATEGORY_SEARCH_PATH, { params });
};
const getUiCategoryTree = (params: ParamsUiCategoryTree) => {
  return httpClient.get(API_PATH.UI_CATEGORY_TREE_PATH, { params });
};
const getUiCategoryLevelDescription = (params: ParamsUiCategoryDescription) => {
  return httpClient.get(API_PATH.UI_CATEGORY_LEVEL_DESCRIPTION_PATH, {
    params,
  });
};
const getOfferTreeSearch = (params: CategoryOfferTreeSearchParam) => {
  return httpClient.get(API_PATH.UI_CATEGORY_OFFER_TREE_SEARCH_PATH, {
    params,
  });
};
const postUiCategoryTree = (params: any) => {
  return httpClient.post(API_PATH.UI_CATEGORY_TREE_PATH, params);
};
const getOfferListTree = (params: String) => {
  return httpClient.get(API_PATH.UI_CATEGORY_OFFER_LIST_TREE_PATH, {
    params,
  });
};
const getCtgPath = (params: any) => {
  return httpClient.get(API_PATH.UI_CATEGORY_PATH_INFO_PATH, {
    params,
  });
};
export {
  getUiCategoryOffer,
  getUiCategoryProducts,
  getUiCategorySearch,
  getUiCategoryTree,
  getUiCategoryLevelDescription,
  getOfferTreeSearch,
  getOfferListTree,
  getListTabs,
  postUiCategoryTree,
  getCtgPath
};
