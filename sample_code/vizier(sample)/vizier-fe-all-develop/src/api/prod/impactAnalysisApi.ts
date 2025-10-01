import { httpClient } from "@/utils/http-common";
import * as API_PATH from "@/api/prod/path";
import { ParamsUiImpactAnalysisRelation } from "@/interfaces/prod/ImpactAnalysisInterface";

const getUiImpactAnalysisItems = (params: any) => {
  return httpClient.get(API_PATH.UI_IMPACT_ANALYSIS_ITEMS, { params });
};

const getUiImpactAnalysisRelation = (
  params: ParamsUiImpactAnalysisRelation
) => {
  return httpClient.get(API_PATH.UI_IMPACT_ANALYSIS_RELATION, { params });
};

const getUiImpactAnalysisChildren = (
  params: ParamsUiImpactAnalysisRelation
) => {
  return httpClient.get(API_PATH.UI_IMPACT_ANALYSIS_CHILDREN, { params });
};

const getUiImpactAnalysisListView = (params: any) => {
  return httpClient.get(API_PATH.UI_IMPACT_ANALYSIS_LIST_VIEW, { params });
};

const getUiImpactAnalysisSelectItem = () => {
  return httpClient.get(API_PATH.UI_IMPACT_ANALYSIS_SELECT_ITEM);
};

export {
  getUiImpactAnalysisItems,
  getUiImpactAnalysisListView,
  getUiImpactAnalysisRelation,
  getUiImpactAnalysisSelectItem,
  getUiImpactAnalysisChildren,
};
