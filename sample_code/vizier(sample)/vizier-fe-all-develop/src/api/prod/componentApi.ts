import * as API_PATH from "@/api/prod/path";
import { httpClient } from "@/utils/http-common";
import {
  RequestGetListComponentSearch,
  RequestGetListSubType,
  RequestGetListOfferSearch,
  IComponentDetailQuery,
  IComponentDetailInfoParams,
} from "@/interfaces/prod/component";
import { trimAndRemoveEmptyProperties } from "@/utils/format-data";

const getListSubTypeApi = (params: RequestGetListSubType) => {
  return httpClient.get(API_PATH.COMPONENT_GET_SUBTYPE_PATH, { params });
};

const getListComponentSearchApi = (params: RequestGetListComponentSearch) => {
  return httpClient.get(API_PATH.COMPONENT_GET_COMPONENT_SEARCH_PATH, {
    params: trimAndRemoveEmptyProperties(params),
  });
};

const getListComponentSearchApiAdvance = (params: any) => {
  return httpClient.get(API_PATH.COMPONENT_GET_COMPONENT_SEARCH_ADVANCE_PATH, {
    params: trimAndRemoveEmptyProperties(params),
  });
};

const getListOfferSearchApi = (params: RequestGetListOfferSearch) => {
  return httpClient.get(API_PATH.COMPONENT_GET_OFFER_SEARCH_PATH, { params });
};

const getComponentDetailApi = (params: IComponentDetailQuery) => {
  return httpClient.get(API_PATH.COMPONENT_GET_DETAIL_PATH, { params });
};

const getComponentDetailInfoApi = (params: IComponentDetailInfoParams) => {
  return httpClient.get(API_PATH.COMPONENT_GET_DETAIL_PATH, { params });
};

const getComponentResourceImpactAnalystApi = (params: {
  resourceUUID: string;
}) => {
  return httpClient.get(API_PATH.COMPONENT_RESOURCE_IMPACT_ANALYST, { params });
};

const getCreateFieldsInfoApi = (params: any) => {
  return httpClient.get(API_PATH.COMPONENT_CREATE_INFO, { params });
};

const createComponentApi = (payload: any) => {
  return httpClient.post(API_PATH.COMPONENT_CREATE_PATH, payload);
};

const updateComponentApi = (payload: any) => {
  return httpClient.put(API_PATH.COMPONENT_CREATE_PATH, payload);
};

const getComponentEntityList = (params: any) => {
  return httpClient.get(API_PATH.COMPONENT_GET_ENTITY_LIST, { params });
};

const getComponentSearchType = () => {
  return httpClient.get(API_PATH.COMPONENT_GET_TYPE_PATH);
};

const getListResourceType = (params) => {
  return httpClient.get(API_PATH.COMPONENT_GET_RESOURCE_BY_TYPE_PATH, {
    params,
  });
};

const getUiComponentAdvancedTable = (params: any) => {
  return httpClient.get(API_PATH.UI_COMPONENT_ADVANCED_DETAIL, { params });
};

export {
  getListSubTypeApi,
  getListComponentSearchApi,
  getListOfferSearchApi,
  getComponentDetailApi,
  getComponentDetailInfoApi,
  getComponentResourceImpactAnalystApi,
  createComponentApi,
  updateComponentApi,
  getCreateFieldsInfoApi,
  getListResourceType,
  getComponentEntityList,
  getComponentSearchType,
  getListComponentSearchApiAdvance,
  getUiComponentAdvancedTable,
};
