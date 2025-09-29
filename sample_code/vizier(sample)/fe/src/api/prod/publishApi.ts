import { httpClient } from "@/utils/http-common";
import * as API_PATH from "@/api/prod/path";

const getListPublishSearch = (params) => {
  return httpClient.get(API_PATH.PUBLISH_PACKAGE_SEARCH, { params });
};
const getListPublishItemsSearch = (params) => {
  return httpClient.get(API_PATH.PUBLISH_ITEMS_SEARCH, { params });
};
const getPublishDetail = (params) => {
  return httpClient.get(`${API_PATH.PUBLISH_PACKAGE_DETAIL}/${params}`);
};
const postPublishPackage = (params) => {
  return httpClient.post(API_PATH.PUBLISH_PACKAGE_CREATE, params);
};
const putPublishPackage = (params) => {
  return httpClient.put(API_PATH.PUBLISH_PACKAGE_CREATE, params);
};
const postPublishApproval = (params) => {
  return httpClient.post(API_PATH.PUBLISH_PACKAGE_APPROVAL, params);
};
const putApproveRejectAction = (params) => {
  return httpClient.put(API_PATH.PUBLISH_APPROVE_OR_REJECT, params);
};
const postApprovalRequestAction = (params) => {
  return httpClient.post(API_PATH.PUBLISH_APPROVE_REQUEST, params);
};
const postPublishRequestAction = (params) => {
  return httpClient.post(API_PATH.PUBLISH_REQUEST, params);
};
const postPublishCompose = (params) => {
  return httpClient.post(API_PATH.COMPOSE_PACKAGE_CREATE, params);
};
const postPublishValidate = (params) => {
  return httpClient.post(API_PATH.COMPOSE_PACKAGE_VALIDATE, params);
};
const getLoadDataProduction = () => {
  //TODO: Action load on production environment
  console.log("Trigger load action");

  return true;
};

export {
  getListPublishSearch,
  postPublishPackage,
  getPublishDetail,
  postPublishApproval,
  putPublishPackage,
  getListPublishItemsSearch,
  putApproveRejectAction,
  postPublishCompose,
  postApprovalRequestAction,
  postPublishValidate,
  postPublishRequestAction,
  getLoadDataProduction,
};
