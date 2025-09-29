from datetime import datetime
from typing import Any, Dict, List, Optional, Union
from uuid import uuid4

from pydantic import BaseModel, Field


class ConditionIssue(BaseModel):
    """
    룰 조건 이슈 모델 (순수 로직 분석)
    """

    condUuid: Optional[str] = Field(None, description="조건 고유 ID")
    keyName: Optional[str] = Field(None, description="조건 키 이름")
    dispName: Optional[str] = Field(None, description="조건 표시 이름")
    issue_type: str
    severity: str
    location: str = ""
    explanation: str = ""
    suggestion: str = ""

    def to_json(self) -> Dict[str, Any]:
        """
        ConditionIssue를 JSON 딕셔너리로 변환

        Why: 분석 결과를 시스템 간 교환 가능한 표준 JSON으로 제공하기 위함입니다.
        How: Pydantic 모델 필드들을 딕셔너리로 직렬화합니다.
        """
        return {
            "condUuid": self.condUuid,
            "keyName": self.keyName,
            "dispName": self.dispName,
            "issue_type": self.issue_type,
            "severity": self.severity,
            "location": self.location,
            "explanation": self.explanation,
            "suggestion": self.suggestion,
        }


class RuleCondition(BaseModel):
    """
    룰 조건 모델 (순수 로직 분석)
    """

    condUuid: str = Field(
        default_factory=lambda: str(uuid4()), description="조건 고유 ID"
    )
    keyName: Optional[str] = Field(None, description="조건 키 이름")
    dispName: Optional[str] = Field(None, description="조건 표시 이름")
    operator: Optional[str] = Field(None, description="연산자")
    value: Optional[Any] = Field(None, description="조건 값")
    fieldDataType: Optional[str] = Field(
        None, description="필드 데이터 타입 (String, Number 등)"
    )
    logicType: Optional[str] = Field(None, description="논리 타입 (AND, OR)")
    condition: Optional[List["ConditionTreeItem"]] = Field(
        None, description="하위 조건들 (논리 연산자 블록)"
    )
    field: Optional[str] = None
    conditions: Optional[List["RuleCondition"]] = None
    parent_operator: Optional[str] = None

    def __init__(self, **data):
        if "field" in data and "keyName" not in data:
            data["keyName"] = data["field"]
        if "dispName" not in data:
            data["dispName"] = data.get("keyName", "") or ""
        if "operator" not in data and "keyName" in data:
            data["operator"] = "=="
        if "value" not in data and "keyName" in data:
            data["value"] = ""
        if "fieldDataType" not in data and "keyName" in data:
            data["fieldDataType"] = "String"
        super().__init__(**data)


ConditionTreeItem = Union[RuleCondition, "ConditionTree"]


class ConditionTree(BaseModel):
    """
    조건 트리 구조
    """

    logicType: str = Field(..., description="논리 타입 (AND, OR)")
    condition: List[ConditionTreeItem] = Field(
        ..., description="조건 목록 또는 중첩된 조건 트리"
    )


class Rule(BaseModel):
    """
    룰 모델 (순수 로직 분석)
    """

    ruleUuid: str = Field(
        default_factory=lambda: str(uuid4()), description="룰 고유 ID"
    )
    ruleName: str = Field(..., description="룰 이름")
    ruleMsg: str = Field(..., description="룰 메시지")
    conditionTree: Optional[ConditionTree] = Field(None, description="조건 트리")
    name: Optional[str] = None
    description: Optional[str] = None
    conditions: Optional[List[RuleCondition]] = None
    action: Optional[Dict[str, Any]] = None
    id: Optional[str] = None
    priority: int = Field(default=1, description="룰 실행 우선순위 (낮을수록 높음)")
    enabled: bool = Field(default=True, description="룰 활성화 여부")

    class Config:
        from_attributes = True

    def __init__(self, **data):
        if "name" in data and "ruleName" not in data:
            data["ruleName"] = data["name"]
        if "ruleName" in data and "name" not in data:
            data["name"] = data["ruleName"]
        if "id" in data and "ruleUuid" not in data:
            data["ruleUuid"] = data["id"]
        if "ruleUuid" in data and "id" not in data:
            data["id"] = data["ruleUuid"]
        if "description" in data and "ruleMsg" not in data:
            data["ruleMsg"] = data["description"] or ""
        if "ruleMsg" in data and "description" not in data:
            data["description"] = data["ruleMsg"] or ""
        super().__init__(**data)

    def to_json(self) -> Dict[str, Any]:
        """
        Rule을 JSON 딕셔너리로 변환

        Why: 룰 원본/정규화 결과를 외부 시스템과 공유하거나 저장하기 위함입니다.
        How: 중첩 모델은 dict()로, 리스트는 comprehension으로 변환하여 직렬화합니다.
        """
        return {
            "ruleUuid": self.ruleUuid,
            "ruleName": self.ruleName,
            "ruleMsg": self.ruleMsg,
            "conditionTree": self.conditionTree.dict() if self.conditionTree else None,
            "conditions": (
                [cond.dict() for cond in self.conditions] if self.conditions else None
            ),
            "priority": self.priority,
            "enabled": self.enabled,
        }


class FieldAnalysis(BaseModel):
    """필드별 상세 분석 결과 (순수 로직)"""

    keyName: str = Field(..., description="조건 키 이름")
    field_type: str = Field(..., description="필드 타입 (string/number)")
    condition_count: int = Field(..., description="해당 필드의 조건 수")
    operators_used: List[str] = Field(..., description="사용된 연산자 목록")
    values_range: Optional[Dict[str, Any]] = Field(None, description="값 범위 정보")
    issues_count: int = Field(..., description="해당 필드의 이슈 수")
    complexity_score: int = Field(..., description="필드 복잡도 점수 (0-100)")
    condition_uuids: List[str] = Field(
        default_factory=list, description="해당 필드와 관련된 조건 UUID 목록"
    )


class LogicFlow(BaseModel):
    """로직 플로우 분석 결과 (순수 로직)"""

    logical_operators: Dict[str, int] = Field(..., description="논리 연산자 사용 빈도")
    nesting_levels: List[int] = Field(..., description="중첩 레벨 분포")
    branch_coverage: Dict[str, Any] = Field(..., description="분기 커버리지 정보")
    potential_dead_code: List[str] = Field(..., description="잠재적 데드 코드 목록")


class PerformanceMetrics(BaseModel):
    """성능 메트릭 (순수 로직)"""

    estimated_execution_time_ms: float = Field(
        0.0, description="예상 실행 시간 (밀리초)"
    )
    memory_usage_estimate_kb: float = Field(0.0, description="예상 메모리 사용량 (KB)")
    complexity_rating: str = Field(
        "normal", description="복잡도 등급 (low/normal/high/critical)"
    )
    optimization_suggestions: List[str] = Field(
        default_factory=list, description="최적화 제안 목록"
    )
    bottleneck_conditions: List[str] = Field(
        default_factory=list, description="병목 조건 목록"
    )


class QualityMetrics(BaseModel):
    """품질 메트릭 (순수 로직)"""

    maintainability_score: int = Field(100, ge=0, le=100, description="유지보수성 점수")
    readability_score: int = Field(100, ge=0, le=100, description="가독성 점수")
    completeness_score: int = Field(100, ge=0, le=100, description="완성도 점수")
    consistency_score: int = Field(100, ge=0, le=100, description="일관성 점수")
    overall_score: int = Field(100, ge=0, le=100, description="전체 품질 점수")


class ReportMetadata(BaseModel):
    """리포트 메타데이터 (순수 로직)"""

    analysis_timestamp: str = Field(..., description="분석 타임스탬프")
    ruleUuid: Optional[str] = Field(None, description="룰 고유 ID")
    ruleName: Optional[str] = Field(None, description="룰 이름")
    analysis_version: str = Field("1.0.1", description="분석 버전")
    total_analysis_time_ms: Optional[int] = Field(
        None, description="총 분석 시간 (밀리초)"
    )
    validation_model: Optional[str] = Field(None, description="검증 모델명")
    report_model: Optional[str] = Field(None, description="리포트 모델명")
    report_generated_by: Optional[str] = Field(None, description="리포트 생성자")
    report_generation_time_ms: Optional[int] = Field(
        None, description="리포트 생성 시간 (밀리초)"
    )
    total_processing_time_ms: Optional[int] = Field(
        None, description="총 처리 시간 (밀리초)"
    )


class StructureInfo(BaseModel):
    """구조 정보 (순수 로직)"""

    depth: int = Field(1, description="조건 트리 최대 깊이")
    condition_count: int = Field(0, description="전체 조건 수")
    condition_node_count: int = Field(
        0, description="전체 조건 노드 수 (논리 연산자 포함)"
    )
    field_condition_count: int = Field(0, description="실제 필드가 있는 비교 조건 수")
    unique_fields: List[str] = Field(
        default_factory=list, description="사용된 고유 필드 목록"
    )


class ValidationResult(BaseModel):
    """검증 결과 (순수 로직 분석)"""

    is_valid: bool = Field(..., description="유효성 여부")
    summary: str = Field(..., description="요약 정보")
    issue_counts: Dict[str, int] = Field(
        default_factory=dict, description="이슈 타입별 개수"
    )
    issues: List[ConditionIssue] = Field(..., description="검출된 이슈 목록")
    structure: StructureInfo = Field(..., description="구조 정보")
    rule_summary: str = Field("", description="룰 요약")
    complexity_score: int = Field(0, description="복잡도 점수")
    field_analysis: List[FieldAnalysis] = Field(
        default_factory=list, description="필드별 분석 결과"
    )
    logic_flow: Optional[LogicFlow] = Field(None, description="로직 플로우 분석")
    performance_metrics: Optional[PerformanceMetrics] = Field(
        None, description="성능 메트릭"
    )
    quality_metrics: Optional[QualityMetrics] = Field(None, description="품질 메트릭")
    report_metadata: Optional[ReportMetadata] = Field(
        None, description="리포트 메타데이터"
    )

    def to_json(self) -> Dict[str, Any]:
        """
        ValidationResult를 순수 로직 JSON 딕셔너리로 변환

        Why: 분석 결과를 표준 구조로 내려 UI/레포팅/파이프라인에 재활용하기 위함입니다.
        How: 서브 모델/리스트를 dict()로 풀어 JSON 직렬화 가능한 형태로 변환합니다.
        """
        return {
            "is_valid": self.is_valid,
            "summary": self.summary,
            "issue_counts": self.issue_counts,
            "issues": [issue.to_json() for issue in self.issues],
            "structure": self.structure.dict() if self.structure else None,
            "rule_summary": self.rule_summary,
            "complexity_score": self.complexity_score,
            "field_analysis": [field.dict() for field in self.field_analysis],
            "logic_flow": self.logic_flow.dict() if self.logic_flow else None,
            "performance_metrics": (
                self.performance_metrics.dict() if self.performance_metrics else None
            ),
            "quality_metrics": (
                self.quality_metrics.dict() if self.quality_metrics else None
            ),
            "report_metadata": (
                self.report_metadata.dict() if self.report_metadata else None
            ),
        }


# JSON I/O 지원을 위한 모델들
class RuleJsonInput(BaseModel):
    """JSON 입력을 위한 모델"""

    module: str = Field(default="raas-rule-analyzer", description="모듈명")
    action: str = Field(..., description="실행할 액션")
    version: str = Field(default="1.0.1", description="모듈 버전")
    data: Dict[str, Any] = Field(..., description="실제 데이터")
    options: Optional[Dict[str, Any]] = Field(
        default_factory=dict, description="추가 옵션"
    )


class RuleJsonOutput(BaseModel):
    """JSON 출력을 위한 모델"""

    status: str = Field(..., description="실행 상태 (success/error)")
    module: str = Field(default="raas-rule-analyzer", description="모듈명")
    version: str = Field(default="1.0.1", description="모듈 버전")
    timestamp: str = Field(
        default_factory=lambda: datetime.now().isoformat(), description="타임스탬프"
    )
    result: Optional[Dict[str, Any]] = Field(None, description="실행 결과")
    metadata: Optional[Dict[str, Any]] = Field(
        default_factory=dict, description="메타데이터"
    )
    error: Optional[Dict[str, Any]] = Field(None, description="에러 정보")


class AnalysisOptions(BaseModel):
    """분석 옵션 모델"""

    severity_filter: str = Field(default="all", description="심각도 필터")
    include_performance_metrics: bool = Field(
        default=True, description="성능 메트릭 포함 여부"
    )
    include_complexity_analysis: bool = Field(
        default=True, description="복잡도 분석 포함 여부"
    )


class BatchAnalysisResult(BaseModel):
    """배치 분석 결과 모델"""

    total_rules: int = Field(..., description="총 룰 수")
    valid_rules: int = Field(..., description="유효한 룰 수")
    invalid_rules: int = Field(..., description="무효한 룰 수")
    total_issues: int = Field(..., description="총 이슈 수")
    analysis_results: List[Dict[str, Any]] = Field(
        default_factory=list, description="개별 분석 결과"
    )
    summary: Dict[str, Any] = Field(default_factory=dict, description="요약 정보")


RuleCondition.model_rebuild()
