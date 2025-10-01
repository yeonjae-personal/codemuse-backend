export interface Rule {
  ruleId: string;
  ruleName: string;
  isAddNew: boolean;
  department: string;
  user: string;
  overview: string;
  creationDate: string;
  useYn: boolean;
  categoryId?: string;
  subCategoryId?: string;
  ruleMsg?: string | null;
}

export interface SubCategories {
  subCategoryId: string;
  subCategoryName: string;
  categoryId: string;
  className: string;
  isAddNew: boolean;
  rules: Rule[];
}

export interface ListRules {
  categoryId: string;
  categoryName: string;
  subCategories: SubCategories[];
}

export interface ListRulesResponse {
  ruleCtgrUuid: string;
  ruleCtgrName: string;
  children: Array<{
    ruleCtgrUuid: string;
    ruleCtgrName: string;
    rules: Array<{
      ruleUuid: string;
      ruleName: string;
      ovwCntn: string;
      chgDeptName: string;
      chgUser: string;
      useYn: string;
      ruleMsg?: string | null;
      rgstDtm: string;
    }>;
  }>;
}

export type OperatorValue =
  | "=="
  | "!="
  | ">"
  | ">="
  | "<="
  | "<"
  | "IN"
  | "NOT IN";

export type LogicType = "AND" | "OR";

export type OperatorOption = {
  value: OperatorValue;
  component: Component;
};

export type FieldValues = {
  uuid: string;
  value: string;
};

export type Condition = {
  condUuid?: string;
  keyName?: string;
  dispName?: string;
  operator?: OperatorValue | "";
  value?: string;
  fieldDataType?: string;
  sortNo?: number;
  fieldUuid?: string;
  logicType?: LogicType;
  condition?: Condition[] | ConditionGroup[];
};

export type ConditionGroup = {
  logicType: LogicType;
  condition: Condition[];
};

export type AddNodeOption = {
  value: LogicType;
  title: string;
};
export interface RuleDetail {
  ruleId: string;
  ruleName: string;
  department: string;
  user: string;
  overview: string;
  creationDate: string;
  isAddNew: boolean;
  useYn: boolean;
  categoryId?: string;
  subCategoryId?: string;
  ruleMsg?: string | null;
}

export enum TABS_RULE_DETAIL {
  ATTRIBUTES = "ATTRIBUTES",
  HISTORY = "HISTORY",
}

export interface CategoryRequest {
  ruleCtgrUuid: string | null;
  ruleCtgrName: string;
  hpstRuleCtgrUuid: string | null;
}

export interface RuleRequest {
  ruleUuid: string | null;
  ruleName: string;
  ruleCtgrUuid: string;
  chgDeptName: string;
  chgUser: string;
  useYn: string;
  ovwCntn: string;
}

export interface IFieldItem {
  fieldDispName: string;
  fieldKeyName: string;
  fieldDataType: string;
  fieldValue: string;
}

export type RuleEngineProvider = {
  isDragging: Ref<boolean>;
};

export const ruleEngineProvider = Symbol() as InjectionKey<RuleEngineProvider>;

export type TestRuleEnginePayload = {
  ruleName: string;
  ruleMsg: string;
  factsData: Record<string, string | number>;
  conditionGroupDto: ConditionGroup;
};

export type TestRuleResponse = {
  failedCondUuids: string[];
  passed: boolean;
  passedCondUuids: string[];
  passedMessage: string | null;
};

export type SaveRuleEnginePayload = {
  ruleName: string;
  ruleMsg: string;
  ruleUuid: string;
  conditionTree: ConditionGroup;
};

interface Issue {
  condUuid: string;
  keyName: string | null;
  issue_type: string;
  severity: string;
  location: string;
  explanation: string;
  suggestion: string;
  ai_explanation: string | null;
  ai_suggestion: string | null;
  impact_level: string | null;
  affected_scenarios: string[] | null;
}

interface FieldAnalysis {
  keyName: string;
  field_type: string;
  condition_count: number;
  operators_used: string[];
  values_range: {
    min?: number;
    max?: number;
    examples: (string | number)[];
  };
  issues_count: number;
  complexity_score: number;
  condition_uuids: string[];
}

interface ImprovementRecommendation {
  priority: string;
  title: string;
  description: string;
  effort: string;
}

interface RiskAssessment {
  risk_level: string;
  risk_score: number;
  risk_message: string;
  critical_issues: string[];
  recommendations: string[];
}

export interface RuleValidationResponse {
  is_valid: boolean;
  summary: string;
  issue_counts: {
    ambiguous_branch: number;
    missing_condition: number;
    complexity_warning: number;
  };
  issues: Issue[];
  structure: {
    depth: number;
    condition_count: number;
    condition_node_count: number;
    field_condition_count: number;
    unique_fields: string[];
  };
  ai_comment: string;
  field_analysis: FieldAnalysis[];
  logic_flow: {
    logical_operators: {
      AND: number;
      OR: number;
    };
    nesting_levels: number[];
    branch_coverage: {
      analyzed: boolean;
      coverage_percentage: number;
    };
    potential_dead_code: string[];
  };
  performance_metrics: {
    estimated_execution_time: string;
    complexity_rating: string;
    optimization_opportunities: string[];
    bottleneck_conditions: string[];
  };
  quality_metrics: {
    maintainability_score: number;
    readability_score: number;
    completeness_score: number;
    consistency_score: number;
    overall_score: number;
  };
  report_metadata: {
    analysis_timestamp: string;
    ruleUuid: string;
    ruleName: string;
    analysis_version: string;
    total_analysis_time_ms: number;
    validation_model?: string;
    validation_ai_latency_ms?: number;
    report_model: string | null;
    report_generated_by: string | null;
    report_generation_time_ms: string | null;
  };
  ai_insights: {
    complexity_analysis: string;
    design_patterns: string[];
    potential_improvements: string[];
    business_impact: string;
  };
  improvement_recommendations: ImprovementRecommendation[];
  risk_assessment: RiskAssessment;
  complexity_score: number;
}
