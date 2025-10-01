import { httpClient } from "@/utils/http-common";
import {
  API_GENERATE_HTML_REPORT,
  API_GET_RULE_STRUCTURE,
  API_RULE_ENGINE_CATEGORY,
  API_RULE_ENGINE_LIST_RULE,
  API_RULE_ENGINE_RULE,
  API_RULE_VALIDATION,
  API_SAVE_RULE_STRUCTURE,
  API_TEST_RULE_STRUCTURE,
} from "./ruleEnginePath";
import {
  CategoryRequest,
  ConditionGroup,
  ListRulesResponse,
  RuleRequest,
  RuleValidationResponse,
  SaveRuleEnginePayload,
  TestRuleEnginePayload,
  TestRuleResponse,
} from "@/interfaces/admin/rule-engine";

export const getListRules = (params: { name: string; searchBy: string }) => {
  return httpClient.get<ListRulesResponse[]>(API_RULE_ENGINE_LIST_RULE, {
    params,
  });
};

export const addUpdateCategory = (data: CategoryRequest) => {
  return httpClient.post(API_RULE_ENGINE_CATEGORY, data);
};

export const addUpdateRule = (data: RuleRequest) => {
  return httpClient.post(API_RULE_ENGINE_RULE, data);
};

export const deleteSubCategory = (categoryId: string) => {
  return httpClient.delete(`${API_RULE_ENGINE_CATEGORY}/${categoryId}`);
};

export const getRuleStructure = (params: { ruleUuid: string }) => {
  return httpClient.get<ConditionGroup>(API_GET_RULE_STRUCTURE, { params });
};

export const testRuleStructure = (data: TestRuleEnginePayload) => {
  return httpClient.post<TestRuleResponse>(API_TEST_RULE_STRUCTURE, data);
};

export const saveRuleStructure = (data: SaveRuleEnginePayload) => {
  return httpClient.post(API_SAVE_RULE_STRUCTURE, data);
};

export const ruleValidation = (data: Array<SaveRuleEnginePayload>) => {
  return httpClient.post<RuleValidationResponse>(API_RULE_VALIDATION, data);
};

export const generateHTMLReport = (data: RuleValidationResponse) => {
  return httpClient.post(API_GENERATE_HTML_REPORT, data);
};
