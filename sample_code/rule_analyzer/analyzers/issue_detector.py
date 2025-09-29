"""
이슈 검출기 (IssueDetector)

룰의 다양한 이슈를 검출하고 검증하는 기능을 담당합니다.
- 중복 조건 검사
- 모순 조건 검사
- 타입 불일치 검사
- 잘못된 연산자 검사
- 분기 불명확성 검사
- 누락 조건 검사
"""

import logging
from typing import Any, Dict, List, Optional

from ..exceptions import IssueDetectionError
from ..models import ConditionIssue, Rule, RuleCondition
from .condition_analyzer import ConditionAnalyzer


# 품질 임계값 상수
class QualityThresholds:
    MAX_COMPLEXITY_SCORE = 50
    MAX_DEPTH = 5
    MAX_CONDITIONS_PER_FIELD = 10


class IssueDetector:
    """
    이슈 검출 및 검증을 담당하는 클래스

    7가지 주요 이슈 타입을 검출합니다:
    1. duplicate_condition - 중복 조건
    2. type_mismatch - 타입 불일치
    3. invalid_operator - 잘못된 연산자
    4. self_contradiction - 자기모순
    5. missing_condition - 누락 조건
    6. ambiguous_branch - 분기 불명확
    7. complexity_warning - 복잡성 경고
    """

    def __init__(self, condition_analyzer: ConditionAnalyzer):
        """
        IssueDetector 초기화

        Args:
            condition_analyzer (ConditionAnalyzer): 조건 분석기 인스턴스
        """
        self.logger = logging.getLogger(__name__)
        self.condition_analyzer = condition_analyzer

    async def detect_all_issues(
        self,
        rule: Rule,
        conditions: List[RuleCondition],
        complexity_score: int,
    ) -> List[ConditionIssue]:
        """
        모든 이슈 타입을 검출

        Why: 룰의 품질 문제를 체계적으로 검출하여 운영 전에 오류를 사전 방지하고 개선 포인트를 제시하기 위함입니다.
        How: 7가지 이슈 타입(중복/타입불일치/잘못된연산자/자기모순/누락/분기불명확/복잡성경고)을 순차적으로 검출하고 중복을 제거합니다.

        Args:
            rule (Rule): 분석할 룰
            conditions (List[RuleCondition]): 파싱된 조건들
            complexity_score (int): 복잡성 점수

        Returns:
            List[ConditionIssue]: 검출된 모든 이슈들
        """
        all_issues = []

        try:
            # 1. 중복 조건 검사
            duplicate_issues = self.detect_duplicate_conditions(conditions)
            all_issues.extend(duplicate_issues)

            # 2. 타입 불일치 검사
            type_issues = self.detect_type_mismatch(conditions)
            all_issues.extend(type_issues)

            # 3. 잘못된 연산자 검사
            operator_issues = self.detect_invalid_operators(conditions)
            all_issues.extend(operator_issues)

            # 4. 자기모순 검사
            contradiction_issues = self.detect_self_contradiction(conditions)
            all_issues.extend(contradiction_issues)

            # 5. 누락 조건 검사
            missing_issues = self.detect_missing_conditions(rule, conditions)
            all_issues.extend(missing_issues)
            # debug removed for linter

            # 6. 분기 불명확성 검사
            ambiguous_issues = self.detect_ambiguous_branches(conditions)
            all_issues.extend(ambiguous_issues)
            # debug removed for linter

            # 7. 복잡성 경고
            complexity_issues = self.detect_complexity_warnings(
                conditions, complexity_score
            )
            all_issues.extend(complexity_issues)

            # 8. 중복 이슈 제거
            all_issues = self._remove_duplicate_issues(all_issues)

            self.logger.info(f"이슈 검출 완료: 총 {len(all_issues)}건")
            return all_issues

        except Exception as e:
            self.logger.error(f"이슈 검출 중 오류: {str(e)}", exc_info=True)
            raise IssueDetectionError(f"이슈 검출 실패: {str(e)}")

    def detect_duplicate_conditions(self, conditions: list) -> list:
        """
        중복 조건 검출

        Why: 동일한 조건이 중복되어 룰의 복잡성을 증가시키고 유지보수를 어렵게 만드는 문제를 방지하기 위함입니다.
        How: 조건의 시그니처(필드명+연산자+값)를 생성하여 중복을 식별하고 경고 이슈로 등록합니다.
        """
        issues = []
        condition_signatures = {}

        def check_duplicates(condition_list, path=""):
            for i, condition in enumerate(condition_list):
                if condition is None:
                    continue
                signature = self._create_condition_signature(condition)
                current_path = f"{path}.{i}" if path else str(i)
                if signature and signature != "unknown":
                    if signature in condition_signatures:
                        original_path = condition_signatures[signature]
                        issue = ConditionIssue(
                            condUuid=getattr(condition, "condUuid", None),
                            keyName=getattr(condition, "keyName", None),
                            dispName=self._get_disp_name(condition),
                            issue_type="duplicate_condition",
                            severity="warning",
                            location=f"{original_path}, {current_path}",
                            explanation="동일한 조건이 중복되어 있습니다.",
                            suggestion="중복된 조건을 하나만 남기고 제거하세요.",
                        )
                        issues.append(issue)
                    else:
                        condition_signatures[signature] = current_path
                if hasattr(condition, "condition") and condition.condition:
                    check_duplicates(condition.condition, current_path)
                elif hasattr(condition, "conditions") and condition.conditions:
                    check_duplicates(condition.conditions, current_path)

        check_duplicates(conditions)
        return issues

    def detect_type_mismatch(self, conditions: list) -> list:
        """
        타입 불일치 검출

        Why: 필드 타입과 값이 일치하지 않아 런타임 오류나 예상치 못한 동작을 방지하기 위함입니다.
        How: ConditionAnalyzer의 타입 검증 메서드를 사용하여 각 조건의 타입 호환성을 검사합니다.
        """
        issues = []

        def check_types(condition_list, path=""):
            for i, condition in enumerate(condition_list):
                if (
                    condition is None
                    or not getattr(condition, "keyName", None)
                    or getattr(condition, "keyName", None) == "placeholder"
                ):
                    continue
                current_path = f"{path}.{i}" if path else str(i)
                if not self.condition_analyzer.is_valid_type(
                    getattr(condition, "keyName", None),
                    getattr(condition, "value", None),
                    condition,
                ):
                    issue = ConditionIssue(
                        condUuid=getattr(condition, "condUuid", None),
                        keyName=getattr(condition, "keyName", None),
                        dispName=self._get_disp_name(condition),
                        issue_type="type_mismatch",
                        severity="error",
                        location=current_path,
                        explanation="필드의 타입이 올바르지 않습니다.",
                        suggestion="필드 타입에 맞는 값을 입력하세요.",
                    )
                    issues.append(issue)
                if hasattr(condition, "condition") and condition.condition:
                    check_types(condition.condition, current_path)
                elif hasattr(condition, "conditions") and condition.conditions:
                    check_types(condition.conditions, current_path)

        check_types(conditions)
        return issues

    def detect_invalid_operators(self, conditions: list) -> list:
        """
        잘못된 연산자 검출

        Why: 필드 타입에 맞지 않는 연산자 사용으로 인한 런타임 오류를 사전에 방지하기 위함입니다.
        How: ConditionAnalyzer의 연산자 유효성 검사 메서드를 사용하여 각 조건의 연산자 타입 호환성을 검증합니다.
        """
        issues = []

        def check_operators(condition_list, path=""):
            for i, condition in enumerate(condition_list):
                if (
                    condition is None
                    or not getattr(condition, "keyName", None)
                    or getattr(condition, "keyName", None) == "placeholder"
                ):
                    continue
                current_path = f"{path}.{i}" if path else str(i)
                if getattr(
                    condition, "operator", None
                ) and not self.condition_analyzer.is_valid_operator(
                    getattr(condition, "keyName", None),
                    getattr(condition, "operator", None),
                    condition,
                ):
                    issue = ConditionIssue(
                        condUuid=getattr(condition, "condUuid", None),
                        keyName=getattr(condition, "keyName", None),
                        dispName=self._get_disp_name(condition),
                        issue_type="invalid_operator",
                        severity="error",
                        location=current_path,
                        explanation="필드에 올바르지 않은 연산자가 사용되었습니다.",
                        suggestion="필드 타입에 맞는 연산자를 사용하세요.",
                    )
                    issues.append(issue)
                if hasattr(condition, "condition") and condition.condition:
                    check_operators(condition.condition, current_path)
                elif hasattr(condition, "conditions") and condition.conditions:
                    check_operators(condition.conditions, current_path)

        check_operators(conditions)
        return issues

    def detect_self_contradiction(self, conditions: list) -> list:
        """
        자기모순 조건 검출

        Why: 동일 필드에 모순되는 조건이 있어 룰이 항상 실패하거나 예상치 못한 결과를 초래하는 문제를 방지하기 위함입니다.
        How: 같은 필드의 조건들을 그룹화하여 연산자와 값의 모순 관계를 분석하고 오류 이슈로 등록합니다.
        """
        issues = []

        def collect_field_conditions(condition_list, parent_path=""):
            field_conditions = {}
            for i, condition in enumerate(condition_list):
                if condition is None:
                    continue
                current_path = f"{parent_path}.{i}" if parent_path else str(i)
                if (
                    getattr(condition, "keyName", None)
                    and getattr(condition, "keyName", None) != "placeholder"
                ):
                    field = getattr(condition, "keyName", None)
                    if field not in field_conditions:
                        field_conditions[field] = []
                    field_conditions[field].append((condition, current_path))
                if hasattr(condition, "conditions") and condition.conditions:
                    nested = collect_field_conditions(
                        condition.conditions, current_path
                    )
                    for k, v in nested.items():
                        if k not in field_conditions:
                            field_conditions[k] = []
                        field_conditions[k].extend(v)
            return field_conditions

        field_conditions = collect_field_conditions(conditions)
        for field, conds in field_conditions.items():
            for i in range(len(conds)):
                for j in range(i + 1, len(conds)):
                    cond1, loc1 = conds[i]
                    cond2, loc2 = conds[j]
                    if self.condition_analyzer.is_contradictory(cond1, cond2):
                        issue = ConditionIssue(
                            condUuid=getattr(cond1, "condUuid", None),
                            keyName=getattr(cond1, "keyName", None),
                            dispName=self._get_disp_name(cond1),
                            issue_type="self_contradiction",
                            severity="error",
                            location=f"{loc1}, {loc2}",
                            explanation="자기모순: 동일 필드에 모순되는 조건이 있습니다.",
                            suggestion="충돌하는 조건을 검토하고 수정하세요.",
                        )
                        issues.append(issue)
        return issues

    def _check_contradiction(
        self, cond1: Dict[str, Any], cond2: Dict[str, Any]
    ) -> bool:
        """두 조건 간의 모순 여부 확인"""
        op1, val1 = cond1["operator"], cond1["value"]
        op2, val2 = cond2["operator"], cond2["value"]

        try:
            # 숫자 값인 경우
            if isinstance(val1, (int, float)) and isinstance(val2, (int, float)):
                # 같은 값에 대한 모순 검사
                if val1 == val2:
                    if (op1 == "==" and op2 == "!=") or (op1 == "!=" and op2 == "=="):
                        return True
                    if (op1 == ">" and op2 == "<=") or (op1 == "<=" and op2 == ">"):
                        return True
                    if (op1 == "<" and op2 == ">=") or (op1 == ">=" and op2 == "<"):
                        return True

                # 범위 모순 검사
                if op1 == ">" and op2 == "<" and val1 >= val2:
                    return True
                if op1 == ">=" and op2 == "<=" and val1 > val2:
                    return True

            # 문자열 값인 경우
            elif isinstance(val1, str) and isinstance(val2, str):
                if val1 == val2:
                    if (op1 == "==" and op2 == "!=") or (op1 == "!=" and op2 == "=="):
                        return True
                    if op1 == "contains" and op2 == "not_contains":
                        return True

        except (TypeError, ValueError):
            pass

        return False

    def detect_missing_conditions(self, rule, conditions: list) -> list:
        """
        누락된 조건 검출

        Why: 룰에 조건이 없거나 특정 값 범위가 처리되지 않아 예상치 못한 동작을 방지하기 위함입니다.
        How: 숫자 필드의 경우 0값 처리, 범위 간격, 엣지 케이스 누락을 검사하고 경고 이슈로 등록합니다.
        """
        issues = []

        # 빈 조건 체크
        if not conditions or len(conditions) == 0:
            issue = ConditionIssue(
                condUuid=None,
                keyName=None,
                dispName=None,
                issue_type="missing_condition",
                severity="error",
                location="root",
                explanation="룰에 조건이 정의되지 않았습니다.",
                suggestion="최소 하나 이상의 조건을 추가하세요.",
            )
            issues.append(issue)
            return issues

        field_conditions = {}

        # 필드별로 조건 그룹화 (재귀적으로 중첩 조건 포함)
        def collect_field_conditions_recursive(condition_list):
            for condition in condition_list:
                # 논리 연산자 블록 제외, 실제 필드 조건만 검사
                if (
                    getattr(condition, "keyName", None)
                    and getattr(condition, "keyName", None) != "placeholder"
                ):
                    field = getattr(condition, "keyName", None)
                    if field not in field_conditions:
                        field_conditions[field] = []

                    field_conditions[field].append(
                        {
                            "operator": getattr(condition, "operator", None),
                            "value": getattr(condition, "value", None),
                            "condition_obj": condition,  # RuleCondition 객체 추가
                        }
                    )

                # 중첩된 조건들도 수집
                if hasattr(condition, "condition") and condition.condition:
                    collect_field_conditions_recursive(condition.condition)
                elif hasattr(condition, "conditions") and condition.conditions:
                    collect_field_conditions_recursive(condition.conditions)

        collect_field_conditions_recursive(conditions)

        # 각 필드별로 누락된 조건 검사
        for field, conditions_list in field_conditions.items():
            # 숫자 타입 필드에 대한 범위 누락 검사
            field_type = self.condition_analyzer.get_field_type(field)
            if field_type == "number":
                missing_ranges = self._check_number_field_missing_ranges(
                    field, conditions_list
                )
                issues.extend(missing_ranges)

        # 중첩 조건에 대해서도 검사
        for condition in conditions:
            if hasattr(condition, "condition") and condition.condition:
                nested_issues = self.detect_missing_conditions(
                    rule, condition.condition
                )
                issues.extend(nested_issues)
            elif hasattr(condition, "conditions") and condition.conditions:
                nested_issues = self.detect_missing_conditions(
                    rule, condition.conditions
                )
                issues.extend(nested_issues)

        return issues

    def _check_number_field_missing_ranges(self, field: str, conditions: list) -> list:
        """숫자 필드에 대한 범위 누락 검사 (레거시와 100% 동일)"""
        issues = []
        min_values = []
        max_values = []
        exact_values = set()
        not_exact_values = set()
        for condition in conditions:
            try:
                value = float(condition["value"])
                if condition["operator"] == ">=":
                    min_values.append({"value": value, "inclusive": True})
                elif condition["operator"] == ">":
                    min_values.append({"value": value, "inclusive": False})
                elif condition["operator"] == "<=":
                    max_values.append({"value": value, "inclusive": True})
                elif condition["operator"] == "<":
                    max_values.append({"value": value, "inclusive": False})
                elif condition["operator"] == "==":
                    exact_values.add(value)
                elif condition["operator"] == "!=":
                    not_exact_values.add(value)
            except (ValueError, TypeError):
                continue
        has_zero_condition = 0 in exact_values
        has_zero_in_range = any(v["value"] == 0 and v["inclusive"] for v in min_values)
        if not has_zero_condition and not has_zero_in_range and min_values:
            first_condition_uuid = None
            if conditions:
                first_condition_uuid = getattr(
                    conditions[0]["condition_obj"], "condUuid", None
                )
            issues.append(
                ConditionIssue(
                    condUuid=first_condition_uuid,
                    keyName=field,
                    dispName=getattr(conditions[0]["condition_obj"], "dispName", None)
                    or field,
                    issue_type="missing_condition",
                    severity="warning",
                    location=f"필드 '{field}' 조건",
                    explanation=(
                        f"{field} = 0인 경우는 어떤 조건에도 해당되지 않으므로 누락된 조건 가능성이 있습니다."
                    ),
                    suggestion=(
                        f"'{field}' 필드에 대해 값이 0인 경우의 처리를 규칙에 명시적으로 추가하는 것이 좋습니다."
                    ),
                )
            )
        if exact_values and len(exact_values) >= 2:
            exact_list = sorted(exact_values)
            for i in range(len(exact_list) - 1):
                gap = exact_list[i + 1] - exact_list[i]
                if gap > 1:
                    covered_by_ranges = False
                    for val in range(int(exact_list[i] + 1), int(exact_list[i + 1])):
                        val_covered = False
                        for min_val in min_values:
                            if (min_val["inclusive"] and val >= min_val["value"]) or (
                                not min_val["inclusive"] and val > min_val["value"]
                            ):
                                val_covered = True
                                break
                        if not val_covered:
                            covered_by_ranges = False
                            break
                        covered_by_ranges = True
                    if not covered_by_ranges:
                        first_condition_uuid = None
                        if conditions:
                            first_condition_uuid = getattr(
                                conditions[0]["condition_obj"], "condUuid", None
                            )
                        issues.append(
                            ConditionIssue(
                                condUuid=first_condition_uuid,
                                keyName=field,
                                dispName=getattr(
                                    conditions[0]["condition_obj"], "dispName", None
                                )
                                or field,
                                issue_type="missing_condition",
                                severity="warning",
                                location=f"필드 '{field}' 조건",
                                explanation=(
                                    f"{field} 값이 {exact_list[i]}와 {exact_list[i + 1]} 사이에 "
                                    f"있는 경우에 대한 조건이 누락되었을 수 있습니다."
                                ),
                                suggestion=(
                                    f"'{field}' 필드에 대해 누락된 범위의 값들에 대한 처리를 추가하세요."
                                ),
                            )
                        )
        return issues

    def detect_ambiguous_branches(self, conditions: list) -> list:
        """
        분기 불명확성 검출

        Why: 조건 간의 겹침이나 중복으로 인해 분기 로직이 불명확해지는 문제를 방지하기 위함입니다.
        How: 숫자 필드의 범위 겹침, 문자열 필드의 중복 값 처리를 검사하여 경고 이슈로 등록합니다.
        """
        issues = []

        # 조건들을 필드별로 그룹화
        field_conditions = {}

        def collect_field_conditions(condition_list, parent_path=""):
            for i, condition in enumerate(condition_list):
                if condition is None:
                    continue
                current_path = f"{parent_path}.{i}" if parent_path else str(i)

                if (
                    getattr(condition, "keyName", None)
                    and getattr(condition, "keyName", None) != "placeholder"
                ):
                    field = getattr(condition, "keyName", None)
                    if field not in field_conditions:
                        field_conditions[field] = []

                    field_conditions[field].append(
                        {
                            "operator": getattr(condition, "operator", None),
                            "value": getattr(condition, "value", None),
                            "location": current_path,
                            "original": condition,
                        }
                    )

                if hasattr(condition, "condition") and condition.condition:
                    collect_field_conditions(condition.condition, current_path)
                elif hasattr(condition, "conditions") and condition.conditions:
                    collect_field_conditions(condition.conditions, current_path)

        collect_field_conditions(conditions)

        # 각 필드별로 분기 불명확 검사
        for field, conditions_list in field_conditions.items():
            # 2개 이상의 조건이 있을 때만 검사
            if len(conditions_list) < 2:
                continue

            # 필드 타입 확인 (첫 번째 조건을 전달하여 정확한 타입 추출)
            first_condition = (
                conditions_list[0]["original"] if conditions_list else None
            )
            field_type = self.condition_analyzer.get_field_type(field, first_condition)

            # 타입별 검사
            if field_type == "number":
                ambiguous_issues = self._check_number_field_ambiguity(
                    field, conditions_list
                )
                if ambiguous_issues:
                    if isinstance(ambiguous_issues, list):
                        issues.extend(ambiguous_issues)
                    else:
                        issues.append(ambiguous_issues)
            elif field_type == "string":
                ambiguous_issue = self._check_string_field_ambiguity(
                    field, conditions_list
                )
                if ambiguous_issue:
                    issues.append(ambiguous_issue)

        return issues

    def _check_number_field_ambiguity(
        self, field: str, conditions: list
    ) -> "Optional[ConditionIssue]":
        """숫자 필드에 대한 분기 불명확 검사 (레거시와 100% 동일)"""
        # 내부 디버그 출력 제거(테스트/프로덕션 로그 오염 방지)
        overlapping_conditions = []
        numeric_conditions = []
        for condition in conditions:
            try:
                op = condition["operator"]
                value = float(condition["value"])
                numeric_conditions.append(
                    {
                        "operator": op,
                        "value": value,
                        "location": condition["location"],
                        "original": condition,
                    }
                )
            except (ValueError, TypeError):
                continue

        for i in range(len(numeric_conditions)):
            for j in range(i + 1, len(numeric_conditions)):
                cond1 = numeric_conditions[i]
                cond2 = numeric_conditions[j]
                op1, val1 = cond1["operator"], cond1["value"]
                op2, val2 = cond2["operator"], cond2["value"]

                is_redundant = False
                explanation = ""
                if op1 == ">=" and op2 == "==" and val2 >= val1:
                    is_redundant = True
                    explanation = (
                        f"'{field} == {val2}'는 '{field} >= {val1}'에 이미 포함됩니다"
                    )
                elif op2 == ">=" and op1 == "==" and val1 >= val2:
                    is_redundant = True
                    explanation = (
                        f"'{field} == {val1}'는 '{field} >= {val2}'에 이미 포함됩니다"
                    )
                elif op1 == ">=" and op2 == ">=" and val1 <= val2:
                    is_redundant = True
                    explanation = (
                        f"'{field} >= {val2}'는 '{field} >= {val1}'에 이미 포함됩니다"
                    )
                elif op1 == ">=" and op2 == ">=" and val2 <= val1:
                    is_redundant = True
                    explanation = (
                        f"'{field} >= {val1}'는 '{field} >= {val2}'에 이미 포함됩니다"
                    )
                elif op1 == ">" and op2 == ">=" and val1 >= val2:
                    is_redundant = True
                    explanation = (
                        f"'{field} > {val1}'는 '{field} >= {val2}'에 이미 포함됩니다"
                    )
                elif op2 == ">" and op1 == ">=" and val2 >= val1:
                    is_redundant = True
                    explanation = (
                        f"'{field} > {val2}'는 '{field} >= {val1}'에 이미 포함됩니다"
                    )

                if is_redundant:
                    overlapping_conditions.append((cond1, cond2, explanation))

        # 중복 조건과 0 빠짐을 모두 검출하기 위해 별도 이슈 리스트 생성
        issues = []

        if overlapping_conditions:
            locations = []
            explanations = []
            for cond1, cond2, explanation in overlapping_conditions:
                locations.append(f"{cond1['location']}, {cond2['location']}")
                explanations.append(explanation)
            location_str = "; ".join(locations)
            explanation_str = "; ".join(explanations)
            first_condition_uuid = None
            first_disp_name = None
            if conditions:
                first_condition_uuid = getattr(
                    conditions[0]["original"], "condUuid", None
                )
                first_disp_name = getattr(conditions[0]["original"], "dispName", None)
            issues.append(
                ConditionIssue(
                    condUuid=first_condition_uuid,
                    keyName=field,
                    dispName=first_disp_name or field,
                    issue_type="ambiguous_branch",
                    severity="warning",
                    location=location_str,
                    explanation=f"{field} 필드에 리던던트(중복) 조건이 있습니다: {explanation_str}",
                    suggestion="중복되는 조건을 제거하거나 조건을 명확하게 정의하세요.",
                )
            )

        all_values = set()
        for condition in conditions:
            op = condition["operator"]
            value = condition["value"]
            if op == "==" and isinstance(value, (int, float)):
                all_values.add(value)
        key_values = [0, 1]
        missing_values = []
        for key_value in key_values:
            if key_value not in all_values and all(
                not self._value_matches_condition(key_value, condition)
                for condition in conditions
            ):
                missing_values.append(key_value)
        if missing_values:
            values_str = ", ".join([str(v) for v in missing_values])
            first_condition_uuid = None
            if conditions:
                first_condition_uuid = getattr(
                    conditions[0]["original"], "condUuid", None
                )
            issues.append(
                ConditionIssue(
                    condUuid=first_condition_uuid,
                    keyName=field,
                    dispName=conditions[0].get("dispName") or field,
                    issue_type="missing_condition",
                    severity="warning",
                    location=f"필드 '{field}' 조건",
                    explanation=(
                        f"{field} 필드가 {values_str} 값일 때는 어느 조건에도 "
                        "해당되지 않아 누락된 조건입니다."
                    ),
                    suggestion=f"{field} 필드의 모든 가능한 값에 대한 처리를 정의하세요.",
                )
            )

        # 모든 이슈 반환
        if issues:
            return issues[0] if len(issues) == 1 else issues
        return None

    def _value_matches_condition(self, value: Any, condition: Dict[str, Any]) -> bool:
        """값이 조건에 맞는지 확인 (레거시와 100% 동일)"""
        op = condition["operator"]
        cond_value = condition["value"]

        try:
            # 타입 정규화: 숫자 비교를 위해 모든 값을 float로 변환 시도
            normalized_value: float
            normalized_cond_value: float

            # value 정규화
            if isinstance(value, str):
                normalized_value = float(value)
            elif isinstance(value, (int, float)):
                normalized_value = float(value)
            else:
                # 숫자가 아닌 타입은 문자열 비교로 처리
                return self._compare_non_numeric(value, cond_value, op)

            # cond_value 정규화
            if isinstance(cond_value, str):
                normalized_cond_value = float(cond_value)
            elif isinstance(cond_value, (int, float)):
                normalized_cond_value = float(cond_value)
            else:
                # 숫자가 아닌 타입은 문자열 비교로 처리
                return self._compare_non_numeric(value, cond_value, op)

            # 숫자 비교 연산
            if op == "==":
                return normalized_value == normalized_cond_value
            elif op == "!=":
                return normalized_value != normalized_cond_value
            elif op == ">":
                return normalized_value > normalized_cond_value
            elif op == ">=":
                return normalized_value >= normalized_cond_value
            elif op == "<":
                return normalized_value < normalized_cond_value
            elif op == "<=":
                return normalized_value <= normalized_cond_value

        except (ValueError, TypeError):
            # 숫자 변환 실패 시 문자열 비교로 폴백
            return self._compare_non_numeric(value, cond_value, op)

        return False

    def _compare_non_numeric(self, value: Any, cond_value: Any, op: str) -> bool:
        """숫자가 아닌 값들에 대한 비교 (레거시와 100% 동일)"""
        try:
            str_value: str = str(value)
            str_cond_value: str = str(cond_value)

            if op == "==":
                return str_value == str_cond_value
            elif op == "!=":
                return str_value != str_cond_value
            # 문자열 간의 크기 비교는 사전순으로 처리
            elif op == ">":
                return str_value > str_cond_value
            elif op == ">=":
                return str_value >= str_cond_value
            elif op == "<":
                return str_value < str_cond_value
            elif op == "<=":
                return str_value <= str_cond_value
        except Exception as e:
            # 로깅을 통해 예외 정보 보존
            self.logger.debug(f"문자열 비교 중 예외 발생: {e}")
            return False

        return False

    def _check_string_field_ambiguity(
        self, field: str, conditions: list
    ) -> "Optional[ConditionIssue]":
        string_values = {}
        for condition in conditions:
            op = condition["operator"]
            value = condition["value"]
            if op == "==" and isinstance(value, str):
                if value not in string_values:
                    string_values[value] = []
                string_values[value].append(condition)
        for value, value_conditions in string_values.items():
            if len(value_conditions) > 1:
                parent_ops = set(
                    cond.get("parent_operator") for cond in value_conditions
                )
                if len(parent_ops) > 1:
                    locations = [cond["location"] for cond in value_conditions]
                    location_str = ", ".join(locations)
                    first_condition_uuid = None
                    if value_conditions:
                        first_cond = value_conditions[0]
                        first_condition_uuid = getattr(
                            first_cond["condition_obj"], "condUuid", None
                        )
                    return ConditionIssue(
                        condUuid=first_condition_uuid,
                        keyName=field,
                        dispName=value_conditions[0].get("dispName"),
                        issue_type="ambiguous_branch",
                        severity="warning",
                        location=location_str,
                        explanation=(
                            f"{field} 필드의 '{value}' 값에 대해 여러 분기에서 "
                            "동시에 처리되어 분기가 불명확합니다."
                        ),
                        suggestion=(
                            f"{field} 필드의 '{value}' 값에 대한 처리를 "
                            "하나의 분기로 통합하세요."
                        ),
                    )
        return None

    def detect_complexity_warnings(
        self, conditions: list, complexity_score: int
    ) -> list:
        """
        복잡성 경고 검출

        Why: 룰의 복잡도가 과도하게 높아 유지보수성과 성능이 저하되는 문제를 사전에 경고하기 위함입니다.
        How: 복잡도 점수를 임계값과 비교하여 경고/오류 수준을 결정하고 개선 제안을 포함한 이슈로 등록합니다.
        """
        issues = []
        COMPLEXITY_WARNING_THRESHOLD = 30
        COMPLEXITY_ERROR_THRESHOLD = 50
        COMPLEXITY_MAX_SCORE = 100
        if complexity_score >= COMPLEXITY_WARNING_THRESHOLD:
            severity = (
                "error" if complexity_score >= COMPLEXITY_ERROR_THRESHOLD else "warning"
            )
            issue = ConditionIssue(
                condUuid=None,
                keyName=None,
                dispName=None,
                issue_type="complexity_warning",
                severity=severity,
                location="전체 룰",
                explanation=f"룰의 복잡성이 높습니다 (점수: {complexity_score}/{COMPLEXITY_MAX_SCORE}).",
                suggestion="조건을 단순화하거나 여러 룰로 분할하는 것을 고려하세요.",
            )
            issues.append(issue)
        return issues

    def _calculate_depth(
        self, conditions: List[RuleCondition], current_depth: int = 1
    ) -> int:
        """조건 트리의 최대 깊이 계산"""
        if not conditions:
            return current_depth

        max_depth = current_depth

        for condition in conditions:
            if condition.conditions:
                nested_depth = self._calculate_depth(
                    condition.conditions, current_depth + 1
                )
                max_depth = max(max_depth, nested_depth)

        return max_depth

    def _count_field_conditions(
        self, conditions: List[RuleCondition], field_counts: Dict[str, int]
    ):
        """필드별 조건 개수 계산"""
        for condition in conditions:
            if condition.keyName and condition.keyName != "placeholder":
                field = condition.keyName
                field_counts[field] = field_counts.get(field, 0) + 1

            if condition.conditions:
                self._count_field_conditions(condition.conditions, field_counts)

    def _create_condition_signature(self, condition: "RuleCondition") -> str:
        """조건의 고유 시그니처 생성 (레거시와 100% 동일)"""
        if not condition.keyName or condition.keyName == "placeholder":
            return "unknown"
        if hasattr(condition, "conditions") and condition.conditions:
            return f"logical_{getattr(condition, 'operator', None)}_{len(condition.conditions)}"
        value_str = str(condition.value) if condition.value is not None else "null"
        return f"{condition.keyName}_{getattr(condition, 'operator', None)}_{value_str}"

    def _remove_duplicate_issues(
        self, issues: List[ConditionIssue]
    ) -> List[ConditionIssue]:
        """중복 이슈 제거"""
        unique = {}
        for issue in issues:
            # message -> explanation으로 변경
            identifier = f"{issue.keyName}|{issue.issue_type}|{getattr(issue, 'explanation', '')}"
            if identifier not in unique:
                unique[identifier] = issue
        return list(unique.values())

    def detect_issues_from_rule_direct(self, rule: Rule) -> List[ConditionIssue]:
        """
        룰 JSON을 직접 분석하여 누락된 이슈들을 검출

        Why: 기존 파싱 파이프라인을 거치지 않고 원본 룰 구조에서 직접 이슈를 검출하여 레거시 시스템과의 호환성을 보장하기 위함입니다.
        How: conditionTree를 직접 순회하여 필드별 조건을 수집하고, 숫자 필드에 대해 리던던트 조건과 누락된 엣지 케이스를 검출합니다.
        """
        issues = []
        try:
            # conditionTree에서 직접 조건 추출
            if hasattr(rule, "conditionTree") and rule.conditionTree:
                field_conditions = {}
                self._extract_field_conditions_recursive(
                    rule.conditionTree, field_conditions
                )
                # 모든 필드에 대해 일반화된 이슈 검출
                for field_name, conditions in field_conditions.items():
                    # 숫자 필드인지 확인
                    if self._is_numeric_field(conditions):
                        # 리던던트 조건 검출
                        redundant_issues = self._detect_field_redundant_conditions(
                            field_name, conditions
                        )
                        issues.extend(redundant_issues)
                        # 누락된 조건 검출 (0값, 음수값 등)
                        missing_issues = self._detect_field_missing_edge_cases(
                            field_name, conditions
                        )
                        issues.extend(missing_issues)
        except Exception as e:
            self.logger.error(f"직접 이슈 검출 중 오류: {str(e)}")

        return issues

    def _detect_field_redundant_conditions(
        self, field_name: str, conditions: List[Dict[str, Any]]
    ) -> List[ConditionIssue]:
        """필드의 리던던트 조건 검출 (레거시와 동일)"""
        issues = []
        redundant_pairs = []
        for i, cond1 in enumerate(conditions):
            for j, cond2 in enumerate(conditions[i + 1 :], i + 1):
                try:
                    val1, val2 = float(cond1["value"]), float(cond2["value"])
                    op1, op2 = cond1["operator"], cond2["operator"]
                    # 리던던트 조건 패턴 검사
                    redundant_explanation = self._check_redundant_pattern(
                        field_name, op1, val1, op2, val2
                    )
                    if redundant_explanation:
                        redundant_pairs.append((cond1, cond2, redundant_explanation))
                        break
                except (ValueError, TypeError):
                    continue
        if redundant_pairs:
            for cond1, cond2, explanation in redundant_pairs:
                # 첫 번째 조건에서 condUuid 가져오기 (cond1에서)
                first_condition_uuid = None
                if isinstance(cond1, dict):
                    first_condition_uuid = self._get_condition_uuid(cond1)
                issues.append(
                    ConditionIssue(
                        condUuid=first_condition_uuid,
                        keyName=field_name,
                        dispName=self._get_disp_name(cond1),
                        issue_type="ambiguous_branch",
                        severity="warning",
                        location=f"{field_name} 필드 조건들",
                        explanation=(
                            f"{field_name} 필드에 리던던트(중복) 조건이 있습니다: "
                            f"{explanation}"
                        ),
                        suggestion="중복되는 조건을 제거하거나 조건을 명확하게 정의하세요.",
                    )
                )
        return issues

    def _detect_field_missing_edge_cases(
        self, field_name: str, conditions: List[Dict[str, Any]]
    ) -> List[ConditionIssue]:
        """필드의 누락된 엣지 케이스 검출 (레거시와 동일)"""
        issues = []
        try:
            # 0값 조건 체크
            has_zero_condition = any(
                cond["operator"] == "==" and float(cond["value"]) == 0
                for cond in conditions
            )
            has_zero_range = any(
                cond["operator"] in [">=", ">"] and float(cond["value"]) == 0
                for cond in conditions
            )
            # 최소값 확인
            numeric_conditions = [
                float(cond["value"])
                for cond in conditions
                if (
                    cond["operator"] in [">=", ">"]
                    and self._is_numeric_value(cond["value"])
                )
            ]
            if numeric_conditions:
                min_value = min(numeric_conditions)
                # 0값이 처리되지 않는 경우
                if not has_zero_condition and not has_zero_range and min_value > 0:
                    # 첫 번째 조건에서 condUuid 가져오기
                    first_condition_uuid = None
                    if conditions:
                        first_condition_uuid = self._get_condition_uuid(conditions[0])
                    issues.append(
                        ConditionIssue(
                            condUuid=first_condition_uuid,
                            keyName=field_name,
                            dispName=self._get_disp_name(conditions[0]),
                            issue_type="missing_condition",
                            severity="warning",
                            location=f"{field_name} 필드 조건",
                            explanation=(
                                f"{field_name} = 0인 경우는 어떤 조건에도 "
                                "해당되지 않으므로 누락된 조건 가능성이 있습니다."
                            ),
                            suggestion=(
                                f"{field_name} 필드에 대해 값이 0인 경우의 처리를 "
                                "규칙에 명시적으로 추가하는 것이 좋습니다."
                            ),
                        )
                    )
        except Exception as e:
            self.logger.debug(f"{field_name} 필드 엣지 케이스 검출 중 오류: {str(e)}")
        return issues

    def _get_condition_uuid(self, condition_info) -> Optional[str]:
        """조건의 UUID를 안전하게 추출 (레거시와 100% 동일)"""
        if isinstance(condition_info, dict):
            condition_obj = condition_info.get("condition_obj")
            if condition_obj:
                return getattr(condition_obj, "condUuid", None)
            return condition_info.get("condUuid")
        else:
            return getattr(condition_info, "condUuid", None)

    def _get_disp_name(self, condition_info) -> Optional[str]:
        """조건의 표시명을 안전하게 추출 (레거시와 100% 동일)"""
        if isinstance(condition_info, dict):
            disp_name = condition_info.get("dispName")
            if disp_name:
                return disp_name
            condition_obj = condition_info.get("condition_obj")
            if condition_obj:
                return getattr(condition_obj, "dispName", None)
            return condition_info.get("keyName")
        else:
            disp_name = getattr(condition_info, "dispName", None)
            if disp_name:
                return disp_name
            return getattr(condition_info, "keyName", None)

    def _check_redundant_pattern(
        self, field_name: str, op1: str, val1: float, op2: str, val2: float
    ) -> str:
        """리던던트 조건 패턴 검사 (레거시와 100% 동일)"""
        if op1 == ">=" and op2 == "==" and val2 >= val1:
            return (
                f"'{field_name} == {val2}'는 '{field_name} >= {val1}'에 이미 포함됩니다"
            )
        elif op2 == ">=" and op1 == "==" and val1 >= val2:
            return (
                f"'{field_name} == {val1}'는 '{field_name} >= {val2}'에 이미 포함됩니다"
            )
        elif op1 == ">=" and op2 == ">=" and val1 <= val2:
            return (
                f"'{field_name} >= {val2}'는 '{field_name} >= {val1}'에 이미 포함됩니다"
            )
        elif op1 == ">=" and op2 == ">=" and val2 <= val1:
            return (
                f"'{field_name} >= {val1}'는 '{field_name} >= {val2}'에 이미 포함됩니다"
            )
        elif op1 == ">" and op2 == ">=" and val1 >= val2:
            return (
                f"'{field_name} > {val1}'는 '{field_name} >= {val2}'에 이미 포함됩니다"
            )
        elif op2 == ">" and op1 == ">=" and val2 >= val1:
            return (
                f"'{field_name} > {val2}'는 '{field_name} >= {val1}'에 이미 포함됩니다"
            )
        return ""

    def _is_numeric_value(self, value: Any) -> bool:
        """값이 숫자인지 확인 (레거시와 100% 동일)"""
        try:
            float(value)
            return True
        except (ValueError, TypeError):
            return False

    def _extract_field_conditions_recursive(self, tree, field_conditions, path=""):
        """재귀적으로 필드 조건 추출 (레거시와 100% 동일)"""
        if isinstance(tree, dict):
            # 필드 조건인 경우
            if (
                "keyName" in tree
                and tree["keyName"]
                and tree["keyName"] != "placeholder"
            ):
                field_name = tree["keyName"]
                if field_name not in field_conditions:
                    field_conditions[field_name] = []

                field_conditions[field_name].append(
                    {
                        "operator": tree.get("operator"),
                        "value": tree.get("value"),
                        "condition_obj": tree,  # 원본 객체 저장
                    }
                )

            # 중첩 조건이 있는 경우
            if "conditions" in tree and tree["conditions"]:
                for i, child in enumerate(tree["conditions"]):
                    new_path = f"{path}/{i+1}" if path else f"{i+1}"
                    self._extract_field_conditions_recursive(
                        child, field_conditions, new_path
                    )

        elif isinstance(tree, list):
            # 리스트인 경우 각 항목에 대해 재귀 처리
            for i, item in enumerate(tree):
                new_path = f"{path}/{i+1}" if path else f"{i+1}"
                self._extract_field_conditions_recursive(
                    item, field_conditions, new_path
                )
