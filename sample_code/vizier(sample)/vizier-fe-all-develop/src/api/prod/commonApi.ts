import { httpClient } from "@/utils/http-common";
import { GET_LIST_OPTION_BY_CODE, GET_LIST_ITEM_CODE } from "./path";
import { IParamsGetListItemCode, ParamsGetListOptions, SubTypeParamsI } from "@/interfaces/prod";

const getListSubTypeCommonApi = (params: SubTypeParamsI | undefined) => {
  return httpClient.get(GET_LIST_ITEM_CODE, { params });
};

const getListItemCodeApi = (params: IParamsGetListItemCode) => {
  return httpClient.get(GET_LIST_ITEM_CODE, { params });
};

const getListOptionsByCodeApi = (params: ParamsGetListOptions) => {
  return httpClient.get(GET_LIST_OPTION_BY_CODE, { params });
};

export { getListSubTypeCommonApi, getListOptionsByCodeApi, getListItemCodeApi };
