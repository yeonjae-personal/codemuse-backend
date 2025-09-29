import { httpClient } from "@/utils/http-common";
import * as API_PATH from "@/api/prod/path";
import type { ParamHistoryCustomValidation } from "@/interfaces/prod/HistoryCustomValidation";
import type { ValidationActionStructureParams } from "@/interfaces/prod/custom-validation";

const getListCustomValidationApi = (params: any) => {
  return httpClient.get(API_PATH.GET_LIST_CUSTOM_VALIDATION, { params });
};

const saveCustomValidationApi = (payload: any) => {
  return httpClient.post(API_PATH.UI_GET_CUSTOM_VALIDATION, payload);
};

const getCustomValidationHistory = (params: ParamHistoryCustomValidation) => {
  return httpClient.get(API_PATH.GET_HISTORY_CUSTOM_VALIDATION, { params });
};

const getValidationActionStructure = (
  params: ValidationActionStructureParams
) => {
  return httpClient.get(API_PATH.GET_VALIDATION_ACTION_STRUCTURE, { params });
};

export {
  getListCustomValidationApi,
  saveCustomValidationApi,
  getCustomValidationHistory,
  getValidationActionStructure,
};
