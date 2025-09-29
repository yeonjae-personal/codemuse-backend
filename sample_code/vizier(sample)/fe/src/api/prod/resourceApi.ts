import * as API_PATH from "@/api/prod/path";
import { httpClient } from "@/utils/http-common";
import {
  IParamsGetListResourceAdd,
  IPayloadGetListResource,
  IParamsGetResourceDetail,
} from "@/interfaces/prod/resource";
import { trimAndRemoveEmptyProperties } from "@/utils/format-data";

const getListResourceApi = (params: Partial<IPayloadGetListResource>) => {
  return httpClient.get(API_PATH.RESOURCE_LIST_PATH, {
    params: trimAndRemoveEmptyProperties(params),
  });
};
const getListResourceAdvancedApi = (params: any) => {
  return httpClient.get(API_PATH.RESOURCE_LIST_ADVANCED_PATH, {
    params,
  });
};
const getListResourceAdvancedDetailApi = (params: any) => {
  return httpClient.get(API_PATH.RESOURCE_LIST_ADVANCED_DETAIL_PATH, {
    params,
  });
};

const getListResourceAddApi = (params: IParamsGetListResourceAdd) => {
  return httpClient.get(API_PATH.RESOURCE_LIST_ADD_PATH, {
    params,
  });
};

const getFormatCreateResourceApi = (itemCode: string) => {
  return httpClient.get(API_PATH.RESOURCE_GET_CREATE_PATH, {
    params: {
      itemCode: itemCode,
    },
  });
};
const createResourceApi = (payload: any) => {
  return httpClient.post(API_PATH.RESOURCE_CREATE_PATH, payload);
};
const updateResourceApi = (payload: any) => {
  return httpClient.put(API_PATH.RESOURCE_CREATE_PATH, payload);
};
const getResourceDetailApi = (params: IParamsGetResourceDetail) => {
  return httpClient.get(API_PATH.RESOURCE_DETAIL_PATH, {
    params,
  });
};

export {
  getListResourceApi,
  getFormatCreateResourceApi,
  createResourceApi,
  updateResourceApi,
  getResourceDetailApi,
  getListResourceAddApi,
  getListResourceAdvancedApi,
  getListResourceAdvancedDetailApi,
};
