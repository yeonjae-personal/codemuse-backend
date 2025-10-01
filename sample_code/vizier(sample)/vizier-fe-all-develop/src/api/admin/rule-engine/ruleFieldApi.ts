import { httpClient } from "@/utils/http-common";
import { API_RULE_ENGINE_RULE_FIELD } from "./ruleEnginePath";
import type {
  IRuleFieldSearchParams,
  IRuleFieldSearchList,
  IFieldItem,
} from "@/interfaces/admin/rule-field";

export const getRuleFieldList = (params: IRuleFieldSearchParams) =>
  httpClient.get<IRuleFieldSearchList>(API_RULE_ENGINE_RULE_FIELD, { params });

export const createNewField = (data: IFieldItem) =>
  httpClient.post(API_RULE_ENGINE_RULE_FIELD, data);

export const updateField = (data: IFieldItem) =>
  httpClient.post(API_RULE_ENGINE_RULE_FIELD, data);

export const deleteField = (params: { labelId: string }) =>
  httpClient.delete(API_RULE_ENGINE_RULE_FIELD, { params });
