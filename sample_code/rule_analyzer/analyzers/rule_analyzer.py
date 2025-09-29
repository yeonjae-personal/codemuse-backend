"""
룰 분석기 (RuleAnalyzer)

룰의 종합적인 분석을 담당하는 메인 분석기입니다 (순수 로직).
- 룰 파싱 및 검증
- 이슈 검출
- 복잡도 분석
- 고급 분석 (성능 메트릭, 품질 메트릭 등)
"""

import asyncio
import logging
from datetime import datetime
from typing import Any, Dict, List, Optional

from ..exceptions import RuleAnalyzerError
from ..models import (
    ReportMetadata,
    Rule,
    StructureInfo,
    ValidationResult,
)
from .advanced_analyzer import AdvancedAnalyzer
from .condition_analyzer import ConditionAnalyzer
from .issue_detector import IssueDetector


logger = logging.getLogger(__name__)


class RuleAnalyzer:
    """
    룰 분석을 담당하는 메인 클래스 (순수 로직)
    """

    def __init__(self):
        """RuleAnalyzer 초기화"""
        self.logger = logging.getLogger(__name__)
        self.condition_analyzer = ConditionAnalyzer()
        self.issue_detector = IssueDetector(self.condition_analyzer)
        self.advanced_analyzer = AdvancedAnalyzer()

    async def analyze_rule(
        self, rule: Rule, analysis_options: Optional[Dict[str, Any]] = None
    ) -> ValidationResult:
        """
        룰을 종합적으로 분석 (순수 로직)

        Why: 룰 정의의 모순/누락/타입 오류/복잡도 과다를 사전에 탐지해 운영 리스크를 줄이기 위함입니다.
        How: 구조 분석 → 조건 파싱 → 복잡도 계산 → 이슈 검출 → 고급 분석(성능/품질/로직 플로우) → 메타데이터 구성 순으로 처리합니다.

        Args:
            rule: 분석할 룰
            analysis_options: 분석 옵션

        Returns:
            ValidationResult: 분석 결과
        """
        start_time = datetime.now().timestamp()

        try:
            # 1. 기본 구조 분석
            structure_info = self._analyze_structure(rule)

            # 2. 조건 파싱
            conditions = self._parse_conditions(rule)

            # 3. 복잡도 계산
            complexity_score = self._calculate_complexity(conditions, structure_info)

            # 4. 이슈 검출
            issues = await self.issue_detector.detect_all_issues(
                rule, conditions, complexity_score
            )

            # 5. 기본 검증 결과 생성
            is_valid = len([i for i in issues if i.severity == "error"]) == 0
            summary = self._generate_summary(rule, issues, complexity_score)
            issue_counts = self._count_issues_by_type(issues)

            # 6. 고급 분석 수행 (순수 로직)
            advanced_results = await self.advanced_analyzer.perform_advanced_analysis(
                rule, conditions, issues, complexity_score
            )

            # 7. 메타데이터 생성
            report_metadata = self._create_report_metadata(rule, start_time)

            # 8. ValidationResult 생성 (순수 로직)
            rule_name_for_summary = (
                rule.get("ruleName", "Unknown")
                if isinstance(rule, dict)
                else getattr(rule, "ruleName", "Unknown")
            )
            # LogicFlow가 None이거나 유효하지 않은 경우 기본값 생성
            logic_flow = advanced_results.get("logic_flow")
            if logic_flow is None or not hasattr(logic_flow, "logical_operators"):
                from ..models import LogicFlow

                logic_flow = LogicFlow(
                    logical_operators={"AND": 0, "OR": 0},
                    nesting_levels=[],
                    branch_coverage={},
                    potential_dead_code=[],
                )

            validation_result = ValidationResult(
                is_valid=is_valid,
                summary=summary,
                issue_counts=issue_counts,
                issues=issues,
                structure=structure_info,
                rule_summary=f"룰 '{rule_name_for_summary}' 분석 완료",
                complexity_score=complexity_score,
                field_analysis=advanced_results.get("field_analysis", []),
                logic_flow=logic_flow,
                performance_metrics=advanced_results.get("performance_metrics"),
                quality_metrics=advanced_results.get("quality_metrics"),
                report_metadata=report_metadata,
            )

            # 로그용 룰명 추출
            rule_name = (
                rule.get("ruleName", "Unknown")
                if isinstance(rule, dict)
                else getattr(rule, "ruleName", "Unknown")
            )
            self.logger.info(f"룰 분석 완료: {rule_name} (복잡도: {complexity_score})")
            return validation_result

        except Exception as e:
            self.logger.error(f"룰 분석 중 오류: {str(e)}", exc_info=True)
            raise RuleAnalyzerError(f"룰 분석 실패: {str(e)}")

    def analyze_rule_sync(
        self, rule: Rule, analysis_options: Optional[Dict[str, Any]] = None
    ) -> ValidationResult:
        """
        룰을 동기적으로 분석 (비동기 래퍼)

        Why: 비동기를 쓰지 않는 코드베이스에서도 동일한 분석 기능을 제공하기 위함입니다.
        How: 이벤트 루프 상태에 따라 run/새 루프 생성 후 await analyze_rule 흐름을 동기적으로 완료합니다.

        Args:
            rule: 분석할 룰
            analysis_options: 분석 옵션

        Returns:
            ValidationResult: 분석 결과
        """
        try:
            return asyncio.run(self.analyze_rule(rule, analysis_options))
        except RuntimeError:
            # 이미 이벤트 루프가 실행 중인 경우
            loop = asyncio.new_event_loop()
            asyncio.set_event_loop(loop)
            try:
                return loop.run_until_complete(
                    self.analyze_rule(rule, analysis_options)
                )
            finally:
                loop.close()

    def _analyze_structure(self, rule: Rule) -> StructureInfo:
        """룰 구조 분석"""
        depth = 1
        condition_count = 0
        condition_node_count = 0
        field_condition_count = 0
        unique_fields = set()

        def analyze_condition_tree(tree, current_depth=1):
            nonlocal depth, condition_count, condition_node_count, field_condition_count

            if not tree:
                return

            depth = max(depth, current_depth)
            condition_node_count += 1

            # condition 필드 처리
            condition_list = None
            if isinstance(tree, dict):
                condition_list = tree.get("condition")
            else:
                condition_list = getattr(tree, "condition", None)

            if condition_list:
                for item in condition_list:
                    # 딕셔너리와 객체 모두 처리
                    key_name = None
                    if isinstance(item, dict):
                        key_name = item.get("keyName")
                    else:
                        key_name = getattr(item, "keyName", None)

                    if key_name and key_name != "placeholder":
                        condition_count += 1
                        field_condition_count += 1
                        unique_fields.add(key_name)
                    elif (
                        hasattr(item, "logicType")
                        and hasattr(item, "condition")
                        and item.condition
                    ):
                        # 논리 연산자 블록인 경우 재귀적으로 분석
                        analyze_condition_tree(item, current_depth + 1)
                    elif (
                        hasattr(item, "logicType")
                        and hasattr(item, "conditions")
                        and item.conditions
                    ):
                        # conditions 필드를 사용하는 경우도 처리
                        analyze_condition_tree(item, current_depth + 1)

            # conditions 필드도 확인 (RuleParser에서 사용)
            conditions_list = None
            if isinstance(tree, dict):
                conditions_list = tree.get("conditions")
            else:
                conditions_list = getattr(tree, "conditions", None)

            if conditions_list:
                for item in conditions_list:
                    # 딕셔너리와 객체 모두 처리
                    key_name = None
                    if isinstance(item, dict):
                        key_name = item.get("keyName")
                    else:
                        key_name = getattr(item, "keyName", None)

                    if key_name and key_name != "placeholder":
                        condition_count += 1
                        field_condition_count += 1
                        unique_fields.add(key_name)
                    elif (
                        hasattr(item, "logicType")
                        and hasattr(item, "condition")
                        and item.condition
                    ):
                        # 논리 연산자 블록인 경우 재귀적으로 분석
                        analyze_condition_tree(item, current_depth + 1)
                    elif (
                        hasattr(item, "logicType")
                        and hasattr(item, "conditions")
                        and item.conditions
                    ):
                        # conditions 필드를 사용하는 경우도 처리
                        analyze_condition_tree(item, current_depth + 1)

        # 딕셔너리 형태의 룰 처리
        if isinstance(rule, dict):
            # conditionTree 분석
            if "conditionTree" in rule and rule["conditionTree"]:
                analyze_condition_tree(rule["conditionTree"])

            # conditions 분석 (레거시 지원)
            if "conditions" in rule and rule["conditions"]:
                for condition in rule["conditions"]:
                    key_name = condition.get("keyName")
                    if key_name and key_name != "placeholder":
                        condition_count += 1
                        field_condition_count += 1
                        unique_fields.add(key_name)

                    if condition.get("conditions"):
                        analyze_condition_tree(condition, 2)
        else:
            # 객체 형태의 룰 처리
            # conditionTree 분석
            if hasattr(rule, "conditionTree") and rule.conditionTree:
                analyze_condition_tree(rule.conditionTree)

            # conditions 분석 (레거시 지원)
            if hasattr(rule, "conditions") and rule.conditions:
                for condition in rule.conditions:
                    if condition.keyName and condition.keyName != "placeholder":
                        condition_count += 1
                        field_condition_count += 1
                        unique_fields.add(condition.keyName)

                    if condition.conditions:
                        analyze_condition_tree(condition, 2)

        return StructureInfo(
            depth=depth,
            condition_count=condition_count,
            condition_node_count=condition_node_count,
            field_condition_count=field_condition_count,
            unique_fields=list(unique_fields),
        )

    def _parse_conditions(self, rule: Rule) -> List:
        """조건 파싱"""
        conditions = []

        # 딕셔너리 형태의 룰 처리
        if isinstance(rule, dict):
            # conditionTree에서 조건 추출
            if "conditionTree" in rule and rule["conditionTree"]:
                conditions.extend(
                    self._extract_conditions_from_tree(rule["conditionTree"])
                )

            # conditions에서 조건 추출 (레거시 지원)
            if "conditions" in rule and rule["conditions"]:
                conditions.extend(rule["conditions"])
        else:
            # 객체 형태의 룰 처리
            # conditionTree에서 조건 추출
            if hasattr(rule, "conditionTree") and rule.conditionTree:
                conditions.extend(
                    self._extract_conditions_from_tree(rule.conditionTree)
                )

            # conditions에서 조건 추출 (레거시 지원)
            if hasattr(rule, "conditions") and rule.conditions:
                conditions.extend(rule.conditions)

        return conditions

    def _extract_conditions_from_tree(self, tree) -> List:
        """조건 트리에서 조건 추출"""
        conditions = []

        # condition 필드 처리
        condition_list = None
        if isinstance(tree, dict):
            condition_list = tree.get("condition")
        else:
            condition_list = getattr(tree, "condition", None)

        if condition_list:
            for item in condition_list:
                # 모든 조건을 추가 (논리 연산자 블록 포함)
                conditions.append(item)

                # 중첩된 논리 연산자 블록의 경우 재귀적으로 처리
                if isinstance(item, dict):
                    if item.get("logicType") and item.get("condition"):
                        # 중첩된 조건들은 이미 item에 포함되어 있으므로 추가하지 않음
                        pass
                else:
                    if (
                        hasattr(item, "logicType")
                        and hasattr(item, "condition")
                        and item.condition
                    ):
                        # 중첩된 조건들은 이미 item에 포함되어 있으므로 추가하지 않음
                        pass

        return conditions

    def _calculate_complexity(
        self, conditions: List, structure_info: StructureInfo
    ) -> int:
        """복잡도 점수 계산"""
        complexity = 0

        # 기본 복잡도
        complexity += structure_info.depth * 5
        complexity += structure_info.condition_count * 3
        complexity += len(structure_info.unique_fields) * 2

        # 조건별 복잡도 (재귀적으로 중첩 조건 포함)
        def calculate_condition_complexity(condition_list):
            nonlocal complexity
            for condition in condition_list:
                # 딕셔너리와 객체 모두 처리
                key_name = None
                operator = None
                value = None

                if isinstance(condition, dict):
                    key_name = condition.get("keyName")
                    operator = condition.get("operator")
                    value = condition.get("value")
                else:
                    key_name = getattr(condition, "keyName", None)
                    operator = getattr(condition, "operator", None)
                    value = getattr(condition, "value", None)

                if key_name and key_name != "placeholder":
                    # 연산자 복잡도
                    if operator in ["contains", "regex", "in"]:
                        complexity += 5
                    elif operator in ["==", "!="]:
                        complexity += 1
                    else:
                        complexity += 3

                    # 값 복잡도
                    if isinstance(value, str) and len(value) > 50:
                        complexity += 2
                    elif isinstance(value, (list, dict)):
                        complexity += 5

                # 중첩된 조건들도 계산
                if isinstance(condition, dict):
                    if condition.get("condition"):
                        calculate_condition_complexity(condition["condition"])
                    elif condition.get("conditions"):
                        calculate_condition_complexity(condition["conditions"])
                else:
                    if hasattr(condition, "condition") and condition.condition:
                        calculate_condition_complexity(condition.condition)
                    elif hasattr(condition, "conditions") and condition.conditions:
                        calculate_condition_complexity(condition.conditions)

        calculate_condition_complexity(conditions)

        return min(complexity, 100)

    def _generate_summary(self, rule: Rule, issues: List, complexity_score: int) -> str:
        """요약 생성"""
        error_count = len([i for i in issues if i.severity == "error"])
        warning_count = len([i for i in issues if i.severity == "warning"])

        # 룰명 추출
        rule_name = ""
        if isinstance(rule, dict):
            rule_name = rule.get("ruleName", "Unknown")
        else:
            rule_name = getattr(rule, "ruleName", "Unknown")

        if error_count == 0 and warning_count == 0:
            return f"룰 '{rule_name}'이 유효합니다. 복잡도: {complexity_score}/100"
        elif error_count == 0:
            return f"룰 '{rule_name}'에 {warning_count}개의 경고가 있습니다. 복잡도: {complexity_score}/100"
        else:
            return (
                f"룰 '{rule_name}'에 {error_count}개의 오류와 {warning_count}개의 경고가 있습니다. "
                f"복잡도: {complexity_score}/100"
            )

    def _count_issues_by_type(self, issues: List) -> Dict[str, int]:
        """이슈 타입별 카운트"""
        counts = {}
        for issue in issues:
            issue_type = issue.issue_type
            counts[issue_type] = counts.get(issue_type, 0) + 1
        return counts

    def _create_report_metadata(self, rule: Rule, start_time: float) -> ReportMetadata:
        """리포트 메타데이터 생성"""
        analysis_time = int((datetime.now().timestamp() - start_time) * 1000)

        # 룰 정보 추출
        rule_uuid = ""
        rule_name = ""
        if isinstance(rule, dict):
            rule_uuid = rule.get("ruleUuid", "Unknown")
            rule_name = rule.get("ruleName", "Unknown")
        else:
            rule_uuid = getattr(rule, "ruleUuid", "Unknown")
            rule_name = getattr(rule, "ruleName", "Unknown")

        return ReportMetadata(
            analysis_timestamp=datetime.now().isoformat(),
            ruleUuid=rule_uuid,
            ruleName=rule_name,
            analysis_version="1.0.1",
            total_analysis_time_ms=analysis_time,
            validation_model="rule-analyzer-v1.0",
            report_model="advanced-analyzer-v1.0",
            report_generated_by="raas-rule-analyzer",
            report_generation_time_ms=analysis_time,
            total_processing_time_ms=analysis_time,
        )

    def validate_rule(self, rule: Rule) -> bool:
        """
        룰 유효성 검증 (간단한 버전)

        Why: 최소한의 스크리닝으로 잘못된 룰을 초기에 걸러 분석 비용을 줄이기 위함입니다.
        How: 필수 메타(ruleName/ruleMsg)와 조건 존재 여부만 빠르게 검증합니다.

        Args:
            rule: 검증할 룰

        Returns:
            bool: 유효성 여부
        """
        try:
            # 기본 검증
            if not rule.ruleName or not rule.ruleMsg:
                return False

            # 조건 존재 여부 확인
            has_conditions = False
            if rule.conditionTree:
                has_conditions = True
            elif rule.conditions:
                has_conditions = len(rule.conditions) > 0

            return has_conditions

        except Exception as e:
            self.logger.error(f"룰 검증 중 오류: {str(e)}")
            return False

    def get_rule_statistics(self, rule: Rule) -> Dict[str, Any]:
        """
        룰 통계 정보 반환

        Why: 룰 규모/복잡도/사용 필드 분포를 빠르게 파악해 유지보수 포인트를 찾기 위함입니다.
        How: 구조 분석 → 조건 파싱 → 복잡도 계산으로 요약 메트릭을 구성합니다.

        Args:
            rule: 분석할 룰

        Returns:
            Dict[str, Any]: 통계 정보
        """
        try:
            structure_info = self._analyze_structure(rule)
            conditions = self._parse_conditions(rule)
            complexity_score = self._calculate_complexity(conditions, structure_info)

            return {
                "rule_name": rule.ruleName,
                "rule_uuid": rule.ruleUuid,
                "total_conditions": structure_info.condition_count,
                "unique_fields": len(structure_info.unique_fields),
                "max_depth": structure_info.depth,
                "complexity_score": complexity_score,
                "field_list": structure_info.unique_fields,
            }

        except Exception as e:
            self.logger.error(f"통계 생성 중 오류: {str(e)}")
            return {}
