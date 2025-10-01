import { httpClient } from "@/utils/http-common";
import * as API_PATH from "@/api/prod/path";
import {
  IAttributeCodeParams,
  IAttributeCodeSearchList,
  IItemDetail,
  IItemsViewList,
  IItemViewRequest,
  ILoupItems,
} from "@/interfaces/admin/attribute-management";

export const getLisItemsView = () =>
  httpClient.get<IItemsViewList[]>(API_PATH.ATTRIBUTE_ITEMS_VIEW);

export const getListAttributeCode = (params: IAttributeCodeParams) =>
  httpClient.get<IAttributeCodeSearchList>(API_PATH.GET_ATTRIBUTE_COMMON_CODE, {
    params,
  });

export const getItemDetail = (params: {
  code: string;
  largeItemCode: string;
  middleItemCode: string;
}) =>
  httpClient.get<IItemDetail>(API_PATH.GET_ITEM_DETAIL, {
    params,
  });

export const getLoupItems = (params: { largeItem: string }) =>
  httpClient.get<ILoupItems>(API_PATH.GET_LOUP_ITEMS, {
    params,
  });

export const saveItemView = (params: IItemViewRequest) =>
  httpClient.post(API_PATH.ATTRIBUTE_ITEMS_VIEW, params);
