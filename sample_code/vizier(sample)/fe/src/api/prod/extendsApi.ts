import { httpClient } from "@/utils/http-common";
import * as API_PATH from "@/api/prod/path";
import {
  ParamsUIExtendsDependencyAddOffer,
  ParamsUIExtendsDependencyRelation,
  ParamsUIExtendsDependencyTarget,
  ParamsUIExtendsDependencyTargetPost,
} from "@/interfaces/prod/extends";
import { trimAndRemoveEmptyProperties } from "@/utils/format-data";

const getExtendsDependencyTarget = (params: any) => {
  return httpClient.get(API_PATH.UI_EXTENDS_DEPENDENCY_TARGET, { params });
};
const getExtendsDependencyCountTarget = (
  params: ParamsUIExtendsDependencyTarget
) => {
  return httpClient.get(API_PATH.UI_EXTENDS_DEPENDENCY_COUNT_TARGET, {
    params,
  });
};

const postExtendsDependencyTarget = (
  params: ParamsUIExtendsDependencyTargetPost
) => {
  return httpClient.post(API_PATH.UI_EXTENDS_DEPENDENCY_TARGET, params);
};

const getExtendsDependencyLeader = (
  params: ParamsUIExtendsDependencyTarget
) => {
  return httpClient.get(API_PATH.UI_EXTENDS_DEPENDENCY_LEADER, { params });
};

const getExtendsDependencyFollower = (
  params: ParamsUIExtendsDependencyTarget
) => {
  return httpClient.get(API_PATH.UI_EXTENDS_DEPENDENCY_FOLLOWER, { params });
};

const getExtendsDependencyRelation = (
  params: ParamsUIExtendsDependencyRelation
) => {
  return httpClient.get(API_PATH.UI_EXTENDS_DEPENDENCY_RELATION, { params });
};

const putExtendsDependencyValidity = (
  params: ParamsUIExtendsDependencyAddOffer
) => {
  return httpClient.put(API_PATH.UI_EXTENDS_DEPENDENCY_VALIDITY, params);
};

const getExtendsDependencyRelationDefinition = (dpdcRelUuid: string) => {
  return httpClient.get(API_PATH.UI_EXTENDS_DEPENDENCY_RELATION_DEFINITION, {
    params: { dpdcRelUuid },
  });
};

const getExtendsDependencyGroupTarget = (objUuid: string) => {
  return httpClient.get(API_PATH.UI_EXTENDS_DEPENDENCY_RELATION_GROUP_DETAIL, {
    params: { objUuid, itemCode: "OG" },
  });
};

const getGroupDetail = (objUuid: string) => {
  return httpClient.get(API_PATH.UI_GROUP_DETAIL, {
    params: { objUuid },
  });
};

const putGroup = (params: any) => {
  return httpClient.put(API_PATH.UI_GROUP, params);
};

const postGroup = (params: any) => {
  return httpClient.post(API_PATH.UI_GROUP, params);
};

const getGroup = (params: any) => {
  return httpClient.get(API_PATH.UI_GROUP, { params });
};

const getGroupListView = (params: any) => {
  return httpClient.get(API_PATH.UI_GROUP_ADVANCED, { params });
};

const getGroupCreateInfo = (params) => {
  return httpClient.get(API_PATH.UI_GROUP_CREATE_INFO, { params });
};

const getMultiEntitySearchInfo = () => {
  return httpClient.get(API_PATH.UI_MULTI_ENTITY_SEARCH_INFO);
};

const getMultiEntitySearch = (params: any) => {
  const filteredParams = trimAndRemoveEmptyProperties(params);
  return httpClient.get(API_PATH.UI_MULTI_ENTITY, { params: filteredParams });
};

const getMultiEntityDetail = (params: any) => {
  return httpClient.get(API_PATH.UI_MULTI_ENTITY_DETAIL, { params });
};

const putMultiEntityDetail = (params: any) => {
  return httpClient.put(API_PATH.UI_MULTI_ENTITY, params);
};

const postMultiEntityDetail = (params: any) => {
  return httpClient.post(API_PATH.UI_MULTI_ENTITY, params);
};

const getMultiEntityCreateInfo = (params: any) => {
  return httpClient.get(API_PATH.UI_MULTI_ENTITY_CREATE_INFO, { params });
};

const getMultiEntityItemRelation = (params: any) => {
  return httpClient.get(API_PATH.UI_MULTI_ENTITY_ITEM_RELATION, { params });
};

const getOfferGroupRelation = (params: any) => {
  return httpClient.get(API_PATH.UI_OFFER_GROUP_RELATION, { params });
};

const putOfferGroupDuplicate = (params: any) => {
  return httpClient.put(API_PATH.UI_OFFER_GROUP_RELATION, params);
};

const initCreateInfoApi = (params: any) => {
  return httpClient.get(API_PATH.CREATE_INFO_RELATION, { params });
};

const getRelationSearchAdvanced = (params: any) => {
  return httpClient.get(API_PATH.RELATION_SEARCH_ADVANCED, { params });
};

const putRelation = (params: any) => {
  return httpClient.put(API_PATH.RELATION_UPSET, params);
};

const postRelation = (params: any) => {
  return httpClient.post(API_PATH.RELATION_UPSET, params);
};

const getRelationCreateInfo = (params: any) => {
  return httpClient.get(API_PATH.RELATION_CREATE_INFO, { params });
};

const getRelationListDataTable = (params: any) =>
  httpClient.get(API_PATH.GET_LIST_RELATION_DATA_TABLE, { params });

export {
  getExtendsDependencyTarget,
  getExtendsDependencyCountTarget,
  getExtendsDependencyLeader,
  getExtendsDependencyFollower,
  getExtendsDependencyRelation,
  postExtendsDependencyTarget,
  putExtendsDependencyValidity,
  getExtendsDependencyRelationDefinition,
  getExtendsDependencyGroupTarget,
  getGroupDetail,
  putGroup,
  postGroup,
  getGroup,
  getGroupCreateInfo,
  getOfferGroupRelation,
  putOfferGroupDuplicate,
  getMultiEntitySearchInfo,
  getMultiEntitySearch,
  getMultiEntityDetail,
  getMultiEntityCreateInfo,
  putMultiEntityDetail,
  postMultiEntityDetail,
  getMultiEntityItemRelation,
  initCreateInfoApi,
  getRelationSearchAdvanced,
  putRelation,
  postRelation,
  getRelationCreateInfo,
  getGroupListView,
  getRelationListDataTable,
};
