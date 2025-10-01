import { httpClient } from "@/utils/http-common";
import {
  API_FACTOR_TYPE_PATH,
  API_FACTOR_TYPE_DETAIL_PATH,
  API_FACTOR_DETAIL_PATH,
  API_FACTOR_SEARCH_PATH,
  API_FACTOR_SEARCH_INFO_PATH
} from "../path";

const getFactorTypes = (params) => {
  return httpClient.get(API_FACTOR_TYPE_PATH, { params });
};
const getFactorSearch = (params) => {
  return httpClient.get(API_FACTOR_SEARCH_PATH, { params });
};
const getFactorSearchInfo = () => {
  return httpClient.get(API_FACTOR_SEARCH_INFO_PATH);
};
const getFactorTypeDetail = (params) => {
  return httpClient.get(API_FACTOR_TYPE_DETAIL_PATH, { params });
};
const putFactorTypeDetail = (params) => {
  return httpClient.put(API_FACTOR_TYPE_DETAIL_PATH, params);
};
const getFactorDetail = (params) => {
  return httpClient.get(API_FACTOR_DETAIL_PATH, { params });
};
const putFactorDetail = (params) => {
  return httpClient.put(API_FACTOR_DETAIL_PATH, params);
};
const postFactorDetail = (params) => {
  return httpClient.post(API_FACTOR_DETAIL_PATH, params);
};

export {
  getFactorTypes,
  getFactorTypeDetail,
  putFactorTypeDetail,
  getFactorDetail,
  putFactorDetail,
  postFactorDetail,
  getFactorSearch,
  getFactorSearchInfo
};
