"""
메트릭 생성기 (MetricsGenerator)

룰 분석 결과를 바탕으로 다양한 메트릭을 생성합니다.
"""

import logging
from typing import List

from ..exceptions import AdvancedAnalysisError
from ..models import (
    FieldAnalysis,
    PerformanceMetrics,
    QualityMetrics,
    RuleCondition,
)


class MetricsGenerator:
    """
    메트릭 생성을 담당하는 클래스
    """

    def __init__(self):
        """MetricsGenerator 초기화"""
        self.logger = logging.getLogger(__name__)

    def generate_performance_metrics(
        self,
        conditions: List[RuleCondition],
        complexity_score: int,
        field_analysis: List[FieldAnalysis],
    ) -> PerformanceMetrics:
        """
        성능 메트릭 생성

        Why: 룰 실행 시 예상되는 성능 특성을 정량화하여 운영 환경에서의 안정성과 최적화 방향을 제시하기 위함입니다.
        How: 조건 수와 복잡도를 기반으로 실행 시간과 메모리 사용량을 추정하고 병목 조건을 식별하여 최적화 제안을 생성합니다.

        Args:
            conditions: 파싱된 조건들
            complexity_score: 복잡도 점수
            field_analysis: 필드 분석 결과

        Returns:
            PerformanceMetrics: 성능 메트릭
        """
        try:
            # 조건 수에 따른 실행 시간 추정
            condition_count = len(
                [c for c in conditions if c.keyName and c.keyName != "placeholder"]
            )
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
                conditions, complexity_score, field_analysis
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

        except Exception as e:
            self.logger.error(f"성능 메트릭 생성 중 오류: {str(e)}")
            raise AdvancedAnalysisError(f"성능 메트릭 생성 실패: {str(e)}")

    def generate_quality_metrics(
        self,
        conditions: List[RuleCondition],
        issues: List,
        complexity_score: int,
        field_analysis: List[FieldAnalysis],
    ) -> QualityMetrics:
        """
        품질 메트릭 생성

        Why: 룰의 품질을 유지보수성, 가독성, 완성도, 일관성 차원에서 정량화하여 개선 방향을 제시하기 위함입니다.
        How: 이슈 수, 복잡도, 조건 구조를 분석하여 각 품질 차원별 점수를 계산하고 전체 품질 점수를 산출합니다.

        Args:
            conditions: 파싱된 조건들
            issues: 검출된 이슈들
            complexity_score: 복잡도 점수
            field_analysis: 필드 분석 결과

        Returns:
            QualityMetrics: 품질 메트릭
        """
        try:
            # 유지보수성 점수
            maintainability_score = self._calculate_maintainability_score(
                conditions, issues, complexity_score
            )

            # 가독성 점수
            readability_score = self._calculate_readability_score(
                conditions, complexity_score, field_analysis
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

        except Exception as e:
            self.logger.error(f"품질 메트릭 생성 중 오류: {str(e)}")
            raise AdvancedAnalysisError(f"품질 메트릭 생성 실패: {str(e)}")

    def _generate_optimization_suggestions(
        self,
        conditions: List[RuleCondition],
        complexity_score: int,
        field_analysis: List[FieldAnalysis],
    ) -> List[str]:
        """최적화 제안 생성"""
        suggestions = []

        if complexity_score > 40:
            suggestions.append("조건을 여러 룰로 분할하여 복잡도를 줄이세요.")

        condition_count = len(
            [c for c in conditions if c.keyName and c.keyName != "placeholder"]
        )
        if condition_count > 20:
            suggestions.append("조건 수가 많습니다. 불필요한 조건을 제거하세요.")

        # 필드별 최적화 제안
        for field in field_analysis:
            if field.complexity_score > 30:
                suggestions.append(f"{field.keyName} 필드의 복잡도를 줄이세요.")

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
        self, conditions: List[RuleCondition], issues: List, complexity_score: int
    ) -> int:
        """유지보수성 점수 계산"""
        score = 100

        # 이슈에 따른 감점
        error_count = len([i for i in issues if i.severity == "error"])
        warning_count = len([i for i in issues if i.severity == "warning"])

        score -= error_count * 10
        score -= warning_count * 2

        # 복잡도에 따른 감점
        condition_count = len(
            [c for c in conditions if c.keyName and c.keyName != "placeholder"]
        )
        if condition_count > 15:
            score -= (condition_count - 15) * 2

        return max(score, 0)

    def _calculate_readability_score(
        self,
        conditions: List[RuleCondition],
        complexity_score: int,
        field_analysis: List[FieldAnalysis],
    ) -> int:
        """가독성 점수 계산"""
        score = 100

        # 복잡도에 따른 감점
        if complexity_score > 30:
            score -= (complexity_score - 30) * 1.5

        # 조건명의 명확성 검사
        for condition in conditions:
            if condition.keyName and condition.keyName != "placeholder":
                if not condition.dispName or condition.dispName == condition.keyName:
                    score -= 2

        return max(int(score), 0)

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
