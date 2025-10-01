import { httpClient } from "@/utils/http-common";
import {
  CmcdRequestParam,
  DomainSearchParam,
  SysMsgSearchRequest,
  TerminologySearchParam,
  PermissionSearchParam,
  PermissionGroupSearchParam,
  MenuSearchParam,
  ScreenManagementSearchParam,
  UserManagementSearchParam,
} from "@/interfaces/admin/admin";
import {
  API_ADMIN_CMCD_PATH_LIST,
  API_ADMIN_DOMAIN_PATH_LIST,
  API_ADMIN_SCREEN_PATH_LIST,
  API_ADMIN_SYSMSG_PATH_LIST,
  API_ADMIN_TERMINOLOGY_PATH_LIST,
  API_ADMIN_URL_PATH_LIST,
  API_ADMIN_PERMISSION_PATH_LIST,
  API_ORG_PATH_TREE,
  API_ADMIN_PERMISSION_GROUP_PATH_LIST,
  API_MENU_PATH_TREE,
  API_ADMIN_SCREEN_URL_PATH_LIST,
  API_ADMIN_USER_PATH_LIST,
} from "./path";

export const getDomainApi = (params: DomainSearchParam) => {
  return httpClient.get(API_ADMIN_DOMAIN_PATH_LIST, { params });
};

export const getCmCodeApi = (params: CmcdRequestParam) => {
  return httpClient.get(API_ADMIN_CMCD_PATH_LIST, { params });
};

export const getSysMsgApi = (params: SysMsgSearchRequest) => {
  return httpClient.get(API_ADMIN_SYSMSG_PATH_LIST, { params });
};

export const getTerminologyApi = (params: TerminologySearchParam) => {
  return httpClient.post(API_ADMIN_TERMINOLOGY_PATH_LIST, params);
};

export const getUserListApi = (params?: UserManagementSearchParam) => {
  return httpClient.get(API_ADMIN_USER_PATH_LIST, { params });
};

export const getScreenListApi = (params?: ScreenManagementSearchParam) => {
  return httpClient.get(API_ADMIN_SCREEN_PATH_LIST, { params });
};

export const getUrlListApi = (params: TerminologySearchParam) => {
  return httpClient.get(API_ADMIN_URL_PATH_LIST, { params });
};
export const getPermissionListApi = (
  params?: Partial<PermissionSearchParam>
) => {
  return httpClient.get(API_ADMIN_PERMISSION_PATH_LIST, { params });
};

export const getPermissionGroupListApi = (
  params?: PermissionGroupSearchParam
) => {
  return httpClient.get(API_ADMIN_PERMISSION_GROUP_PATH_LIST, { params });
};

export const getMenuTreeApi = (params?: MenuSearchParam) => {
  return httpClient.get(API_MENU_PATH_TREE, { params });
};
export const getOrgTreeApi = (params) => {
  return httpClient.get(API_ORG_PATH_TREE, { params });
};
export const getOrgDetailApi = (orgCd, params) => {
  return httpClient.get(`/api/comm/org/orgInfo/v1/${orgCd}`, { params });
};

export const getScreenUrlListByScrnIdApi = (scrnId: String) => {
  return httpClient.get(API_ADMIN_SCREEN_URL_PATH_LIST + `${scrnId}`);
};
