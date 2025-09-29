"""
고급 분석기 (AdvancedAnalyzer)

룰의 고급 분석 기능을 담당합니다 (순수 로직):
- 복잡도 분석
- 성능 메트릭 계산
- 품질 메트릭 평가
- 구조 분석
"""

import logging
from typing import Any, Dict, List, Optional

from ..exceptions import AdvancedAnalysisError
from ..models import (
    FieldAnalysis,
    LogicFlow,
    PerformanceMetrics,
    QualityMetrics,
    Rule,
    RuleCondition,
)


class AdvancedAnalyzer:
    """
    고급 분석 기능을 담당하는 클래스 (순수 로직)
    """

    def __init__(self):
        """AdvancedAnalyzer 초기화"""
        self.logger = logging.getLogger(__name__)

    async def perform_advanced_analysis(
        self,
        rule: Rule,
        conditions: List[RuleCondition],
        basic_issues: List,
        complexity_score: int,
    ) -> Dict[str, Any]:
        """
        고급 분석 수행 (순수 로직)

        Why: 기본 검증만으로는 놓칠 수 있는 성능/품질/로직 흐름 이슈를 정량화하여 개선 지점을 제시하기 위함입니다.
        How: 필드 분석→로직 플로우→성능 메트릭→품질 메트릭 순으로 산출하고 결과를 묶어 반환합니다.

        Args:
            rule: 분석할 룰
            conditions: 파싱된 조건들
            basic_issues: 기본 이슈들
            complexity_score: 복잡도 점수

        Returns:
            Dict[str, Any]: 고급 분석 결과
        """
        try:
            # 1. 필드 분석
            field_analysis = self._analyze_fields(conditions, basic_issues)

            # 2. 로직 플로우 분석
            logic_flow = self._analyze_logic_flow(conditions)

            # 3. 성능 메트릭 계산
            performance_metrics = self._calculate_performance_metrics(
                conditions, complexity_score
            )

            # 4. 품질 메트릭 평가
            quality_metrics = self._evaluate_quality_metrics(
                conditions, basic_issues, complexity_score
            )

            return {
                "field_analysis": field_analysis,
                "logic_flow": logic_flow,
                "performance_metrics": performance_metrics,
                "quality_metrics": quality_metrics,
            }

        except Exception as e:
            self.logger.error(f"고급 분석 중 오류: {str(e)}", exc_info=True)
            raise AdvancedAnalysisError(f"고급 분석 실패: {str(e)}")

    def _analyze_fields(
        self, conditions: List[RuleCondition], issues: List
    ) -> List[FieldAnalysis]:
        """
        필드별 상세 분석

        Why: 각 필드의 사용 패턴과 복잡도를 파악하여 최적화 포인트를 찾기 위함입니다.
        How: 조건을 필드별로 그룹화하여 연산자 사용 빈도, 값 범위, 이슈 수를 계산하고 복잡도 점수를 산출합니다.
        """
        field_data = {}

        # 필드별 데이터 수집
        for condition in conditions:
            if condition.keyName and condition.keyName != "placeholder":
                field = condition.keyName
                if field not in field_data:
                    field_data[field] = {
                        "condition_count": 0,
                        "operators_used": set(),
                        "values": [],
                        "condition_uuids": [],
                        "issues_count": 0,
                    }

                field_data[field]["condition_count"] += 1
                field_data[field]["operators_used"].add(condition.operator or "unknown")
                field_data[field]["values"].append(condition.value)
                field_data[field]["condition_uuids"].append(condition.condUuid)

        # 이슈별 필드 카운트
        for issue in issues:
            if issue.keyName and issue.keyName in field_data:
                field_data[issue.keyName]["issues_count"] += 1

        # FieldAnalysis 객체 생성
        field_analyses = []
        for field, data in field_data.items():
            # 값 범위 계산
            values_range = self._calculate_values_range(data["values"])

            # 복잡도 점수 계산
            complexity_score = self._calculate_field_complexity(
                data["condition_count"], len(data["operators_used"]), values_range
            )

            field_analysis = FieldAnalysis(
                keyName=field,
                field_type=self._determine_field_type(data["values"]),
                condition_count=data["condition_count"],
                operators_used=list(data["operators_used"]),
                values_range=values_range,
                issues_count=data["issues_count"],
                complexity_score=complexity_score,
                condition_uuids=data["condition_uuids"],
            )
            field_analyses.append(field_analysis)

        return field_analyses

    def _analyze_logic_flow(self, conditions: List[RuleCondition]) -> LogicFlow:
        """
        로직 플로우 분석

        Why: 룰의 논리 구조와 분기 패턴을 파악하여 복잡성과 잠재적 문제점을 식별하기 위함입니다.
        How: 논리 연산자 사용 빈도, 중첩 레벨 분포, 분기 커버리지를 계산하고 도달 불가능한 코드를 검출합니다.
        """
        logical_operators = {}
        nesting_levels = []

        def analyze_conditions_recursive(cond_list, level=1):
            for condition in cond_list:
                if condition.logicType:
                    logical_operators[condition.logicType] = (
                        logical_operators.get(condition.logicType, 0) + 1
                    )

                nesting_levels.append(level)

                if condition.conditions:
                    analyze_conditions_recursive(condition.conditions, level + 1)

        analyze_conditions_recursive(conditions)

        # 분기 커버리지 계산
        branch_coverage = self._calculate_branch_coverage(conditions)

        # 잠재적 데드 코드 검출
        potential_dead_code = self._detect_potential_dead_code(conditions)

        return LogicFlow(
            logical_operators=logical_operators,
            nesting_levels=nesting_levels,
            branch_coverage=branch_coverage,
            potential_dead_code=potential_dead_code,
        )

    def _calculate_performance_metrics(
        self, conditions: List[RuleCondition], complexity_score: int
    ) -> PerformanceMetrics:
        """
        성능 메트릭 계산

        Why: 룰 실행 시 예상되는 성능 특성을 미리 파악하여 운영 환경에서의 안정성을 보장하기 위함입니다.
        How: 조건 수와 복잡도 점수를 기반으로 실행 시간과 메모리 사용량을 추정하고 최적화 제안을 생성합니다.
        """
        # 조건 수에 따른 실행 시간 추정
        condition_count = self._count_total_conditions(conditions)
        estimated_execution_time_ms = condition_count * 0.5 + complexity_score * 0.1

        # 메모리 사용량 추정
        memory_usage_estimate_kb = condition_count * 2.5 + complexity_score * 0.5

        # 복잡도 등급 결정
        if complexity_score < 20:
            complexity_rating = "low"
        elif complexity_score < 40:
            complexity_rating = "normal"
        elif complexity_score < 60:
            complexity_rating = "high"
        else:
            complexity_rating = "critical"

        # 최적화 제안
        optimization_suggestions = self._generate_optimization_suggestions(
            conditions, complexity_score
        )

        # 병목 조건 식별
        bottleneck_conditions = self._identify_bottleneck_conditions(conditions)

        return PerformanceMetrics(
            estimated_execution_time_ms=estimated_execution_time_ms,
            memory_usage_estimate_kb=memory_usage_estimate_kb,
            complexity_rating=complexity_rating,
            optimization_suggestions=optimization_suggestions,
            bottleneck_conditions=bottleneck_conditions,
        )

    def _evaluate_quality_metrics(
        self, conditions: List[RuleCondition], issues: List, complexity_score: int
    ) -> QualityMetrics:
        """
        품질 메트릭 평가

        Why: 룰의 유지보수성, 가독성, 완성도, 일관성을 정량화하여 품질 개선 방향을 제시하기 위함입니다.
        How: 이슈 수, 복잡도, 조건 구조를 분석하여 각 품질 차원별 점수를 계산하고 전체 점수를 산출합니다.
        """
        # 유지보수성 점수
        maintainability_score = self._calculate_maintainability_score(
            conditions, issues
        )

        # 가독성 점수
        readability_score = self._calculate_readability_score(
            conditions, complexity_score
        )

        # 완성도 점수
        completeness_score = self._calculate_completeness_score(conditions, issues)

        # 일관성 점수
        consistency_score = self._calculate_consistency_score(conditions, issues)

        # 전체 점수
        overall_score = int(
            (
                maintainability_score
                + readability_score
                + completeness_score
                + consistency_score
            )
            / 4
        )

        return QualityMetrics(
            maintainability_score=maintainability_score,
            readability_score=readability_score,
            completeness_score=completeness_score,
            consistency_score=consistency_score,
            overall_score=overall_score,
        )

    # 헬퍼 메서드들
    def _calculate_values_range(self, values: List[Any]) -> Optional[Dict[str, Any]]:
        """값 범위 계산"""
        if not values:
            return None

        numeric_values = []
        string_values = []

        for value in values:
            try:
                if isinstance(value, (int, float)):
                    numeric_values.append(float(value))
                elif isinstance(value, str):
                    string_values.append(value)
            except (ValueError, TypeError):
                continue

        result = {}

        if numeric_values:
            result["min"] = min(numeric_values)
            result["max"] = max(numeric_values)
            result["avg"] = sum(numeric_values) / len(numeric_values)

        if string_values:
            result["unique_strings"] = len(set(string_values))
            result["string_count"] = len(string_values)

        return result if result else None

    def _determine_field_type(self, values: List[Any]) -> str:
        """필드 타입 결정"""
        numeric_count = 0
        string_count = 0

        for value in values:
            try:
                if isinstance(value, (int, float)):
                    numeric_count += 1
                elif isinstance(value, str):
                    string_count += 1
            except Exception:
                string_count += 1

        if numeric_count > string_count:
            return "number"
        else:
            return "string"

    def _calculate_field_complexity(
        self,
        condition_count: int,
        operator_count: int,
        values_range: Optional[Dict[str, Any]],
    ) -> int:
        """필드 복잡도 점수 계산"""
        complexity = condition_count * 5 + operator_count * 3

        if values_range:
            if "unique_strings" in values_range:
                complexity += values_range["unique_strings"] * 2
            if "max" in values_range and "min" in values_range:
                range_size = values_range["max"] - values_range["min"]
                complexity += min(range_size, 50)

        return min(complexity, 100)

    def _calculate_branch_coverage(
        self, conditions: List[RuleCondition]
    ) -> Dict[str, Any]:
        """분기 커버리지 계산"""
        total_branches = 0
        covered_branches = 0

        def count_branches(cond_list):
            nonlocal total_branches, covered_branches
            for condition in cond_list:
                if condition.keyName and condition.keyName != "placeholder":
                    total_branches += 1
                    covered_branches += 1

                if condition.conditions:
                    count_branches(condition.conditions)

        count_branches(conditions)

        coverage_percentage = (
            (covered_branches / total_branches * 100) if total_branches > 0 else 100
        )

        return {
            "total_branches": total_branches,
            "covered_branches": covered_branches,
            "coverage_percentage": coverage_percentage,
        }

    def _detect_potential_dead_code(self, conditions: List[RuleCondition]) -> List[str]:
        """잠재적 데드 코드 검출"""
        dead_code = []

        # 항상 false가 되는 조건 검출
        for condition in conditions:
            if condition.keyName and condition.keyName != "placeholder":
                if condition.operator == "==" and condition.value == "":
                    dead_code.append(f"빈 문자열 비교: {condition.keyName}")
                elif condition.operator == "!=" and condition.value is None:
                    dead_code.append(f"None 비교: {condition.keyName}")

        return dead_code

    def _count_total_conditions(self, conditions: List[RuleCondition]) -> int:
        """총 조건 수 계산"""
        count = 0

        def count_recursive(cond_list):
            nonlocal count
            for condition in cond_list:
                if condition.keyName and condition.keyName != "placeholder":
                    count += 1
                if condition.conditions:
                    count_recursive(condition.conditions)

        count_recursive(conditions)
        return count

    def _generate_optimization_suggestions(
        self, conditions: List[RuleCondition], complexity_score: int
    ) -> List[str]:
        """최적화 제안 생성"""
        suggestions = []

        if complexity_score > 40:
            suggestions.append("조건을 여러 룰로 분할하여 복잡도를 줄이세요.")

        condition_count = self._count_total_conditions(conditions)
        if condition_count > 20:
            suggestions.append("조건 수가 많습니다. 불필요한 조건을 제거하세요.")

        return suggestions

    def _identify_bottleneck_conditions(
        self, conditions: List[RuleCondition]
    ) -> List[str]:
        """병목 조건 식별"""
        bottlenecks = []

        for condition in conditions:
            if condition.keyName and condition.keyName != "placeholder":
                # 복잡한 연산자나 값이 있는 조건 식별
                if condition.operator in ["contains", "regex"]:
                    bottlenecks.append(f"{condition.keyName}: 복잡한 연산자 사용")
                elif isinstance(condition.value, str) and len(condition.value) > 100:
                    bottlenecks.append(f"{condition.keyName}: 긴 문자열 값")

        return bottlenecks

    def _calculate_maintainability_score(
        self, conditions: List[RuleCondition], issues: List
    ) -> int:
        """유지보수성 점수 계산"""
        score = 100

        # 이슈에 따른 감점
        error_count = len([i for i in issues if i.severity == "error"])
        warning_count = len([i for i in issues if i.severity == "warning"])

        score -= error_count * 10
        score -= warning_count * 2

        # 복잡도에 따른 감점
        condition_count = self._count_total_conditions(conditions)
        if condition_count > 15:
            score -= (condition_count - 15) * 2

        return max(score, 0)

    def _calculate_readability_score(
        self, conditions: List[RuleCondition], complexity_score: int
    ) -> int:
        """가독성 점수 계산"""
        score = 100.0

        # 복잡도에 따른 감점
        if complexity_score > 30:
            score -= (complexity_score - 30) * 1.5

        # 조건명의 명확성 검사
        for condition in conditions:
            if condition.keyName and condition.keyName != "placeholder":
                if not condition.dispName or condition.dispName == condition.keyName:
                    score -= 2

        return int(max(score, 0))

    def _calculate_completeness_score(
        self, conditions: List[RuleCondition], issues: List
    ) -> int:
        """완성도 점수 계산"""
        score = 100

        # 누락 조건 이슈에 따른 감점
        missing_issues = [i for i in issues if i.issue_type == "missing_condition"]
        score -= len(missing_issues) * 15

        # 빈 조건 체크
        if not conditions:
            score -= 50

        return max(score, 0)

    def _calculate_consistency_score(
        self, conditions: List[RuleCondition], issues: List
    ) -> int:
        """일관성 점수 계산"""
        score = 100

        # 모순 이슈에 따른 감점
        contradiction_issues = [
            i for i in issues if i.issue_type == "self_contradiction"
        ]
        score -= len(contradiction_issues) * 20

        # 중복 이슈에 따른 감점
        duplicate_issues = [i for i in issues if i.issue_type == "duplicate_condition"]
        score -= len(duplicate_issues) * 10

        return max(score, 0)
