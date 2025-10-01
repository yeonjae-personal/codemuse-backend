import { httpClient } from "@/utils/http-common";
import * as API_PATH from "@/api/prod/path";
import type {
  ILabelSearchParams,
  ILabelSearchList,
  ILabelItem,
  ILanguageList,
} from "@/interfaces/admin/label-management";

export const getLabelList = (params: ILabelSearchParams) =>
  httpClient.get<ILabelSearchList>(API_PATH.GET_LABEL_LIST, { params });

export const createNewLabel = (data: ILabelItem) =>
  httpClient.post(API_PATH.UI_ADMIN_LABEL, data);

export const updateLabel = (data: ILabelItem) =>
  httpClient.put(API_PATH.UI_ADMIN_LABEL, data);

export const deleteLabel = (params: { labelId: string }) =>
  httpClient.delete(API_PATH.UI_ADMIN_LABEL, { params });

export const uploadLabelList = (file: File) => {
  const formData = new FormData();
  formData.append("file", file);
  return httpClient.post(API_PATH.UPLOAD_LABEL_LIST_EXCEL, formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });
};

export const getAllLanguages = () =>
  httpClient.get<ILabelSearchList>(API_PATH.GET_ALL_LANGUAGE);

export const getLanguageLabelList = () =>
  httpClient.get<ILanguageList[]>(API_PATH.GET_LANGUAGE_LABEL_LIST);
