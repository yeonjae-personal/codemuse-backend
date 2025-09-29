import * as API_PATH from "@/api/prod/path";
import { IQueryStructureComponentAddList } from "@/interfaces/prod/offer";
import {
  ParamsStructure,
  ParamsStructureDetail,
} from "@/interfaces/prod/structure";
import { httpClient } from "@/utils/http-common";

const getStructureComponentListAddApi = (
  params: IQueryStructureComponentAddList
) => {
  return httpClient.get(API_PATH.STRUCTURE_COMPONENT_LIST_CAN_ADD, {
    params,
  });
};

const getStructureApi = (params: ParamsStructure) => {
  return httpClient.get(API_PATH.STRUCTURE_PATH, { params });
};

const getStructureComponentDetailApi = (params: ParamsStructureDetail) => {
  return httpClient.get(API_PATH.COMPONENT_GET_DETAIL_PATH, { params });
};

export {
  getStructureComponentListAddApi,
  getStructureApi,
  getStructureComponentDetailApi,
};
