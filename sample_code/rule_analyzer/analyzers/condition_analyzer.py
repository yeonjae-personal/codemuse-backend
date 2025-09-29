"""
조건 분석기 (ConditionAnalyzer)

룰의 조건을 분석하고 파싱하는 기능을 담당합니다.
- 조건 트리 파싱
- 필드 타입 추론
- 조건 구조 분석
- 깊이 및 복잡성 계산
"""

import logging
import re
from typing import Any, Dict, List, Optional

from ..exceptions import ConditionAnalysisError
from ..models import Rule, RuleCondition


class ConditionAnalyzer:
    """
    조건 분석 및 파싱을 담당하는 클래스

    이 클래스는 다음 기능들을 제공합니다:
    - 조건 트리 파싱 및 변환
    - 필드 타입 자동 추론
    - 조건 구조 분석
    - 복잡성 및 깊이 계산
    """

    def __init__(self):
        """ConditionAnalyzer 초기화"""
        self.logger = logging.getLogger(__name__)
        self.field_types: Dict[str, str] = {}
        self.global_condition_index = 0
        self.condition_index_map = {}

        # 타입별 허용 연산자 정의
        self._valid_operators = {
            "string": [
                "==",
                "!=",
                "contains",
                "starts_with",
                "ends_with",
                "in",
                "not_in",
            ],
            "number": ["==", "!=", ">", ">=", "<", "<=", "in", "not_in"],
            "boolean": ["==", "!="],
            "date": ["==", "!=", ">", ">=", "<", "<="],
            "array": ["contains", "in", "not_in"],
            "logical": ["and", "or"],
        }

        # 성능 최적화를 위한 캐시
        self._field_analysis_cache: Dict[str, Any] = {}
        self._complexity_cache: Dict[str, int] = {}
        self._analysis_cache: Dict[str, Any] = {}

    def parse_rule_conditions(self, rule: Rule) -> List[RuleCondition]:
        """
        룰에서 조건들을 파싱하여 RuleCondition 리스트로 변환

        Why: 다양한 입력 구조의 룰에서도 동일한 조건 리스트를 얻어 이후 분석 단계가 일관되게 동작하도록 하기 위함입니다.
        How: dict/객체/트리 형태를 모두 지원하며, 논리 블록/필드 조건을 재귀적으로 펼쳐 표준 모델로 변환합니다.

        Args:
            rule (Rule): 파싱할 룰 객체

        Returns:
            List[RuleCondition]: 파싱된 조건들
        """
        try:
            # 캐시 확인
            rule_id = getattr(rule, "ruleUuid", getattr(rule, "id", "unknown"))
            cache_key = f"parse_{rule_id}"

            if cache_key in self._analysis_cache:
                return self._analysis_cache[cache_key]

            conditions = []

            # 다양한 룰 형식 지원
            if isinstance(rule, dict):
                # 딕셔너리 형태의 룰
                if "conditionTree" in rule and rule["conditionTree"]:
                    self.logger.debug(
                        f"딕셔너리 conditionTree 발견: type={type(rule['conditionTree'])}"
                    )
                    conditions = self._parse_condition_tree(rule["conditionTree"])
                    self.logger.debug(
                        f"딕셔너리 conditionTree 파싱 후 조건 수: {len(conditions)}"
                    )
                elif "conditions" in rule and rule["conditions"]:
                    self.logger.debug(
                        f"딕셔너리 conditions 발견: {len(rule['conditions'])}개"
                    )
                    conditions = self._parse_conditions_list(rule["conditions"])
                else:
                    self.logger.warning(f"딕셔너리 룰에서 조건을 찾을 수 없음: {rule}")
            elif hasattr(rule, "conditionTree") and rule.conditionTree:
                self.logger.debug(
                    f"conditionTree 발견: type={type(rule.conditionTree)}"
                )
                conditions = self._parse_condition_tree(rule.conditionTree)
                self.logger.debug(f"conditionTree 파싱 후 조건 수: {len(conditions)}")
            elif hasattr(rule, "conditions") and rule.conditions:
                self.logger.debug(f"conditions 발견: {len(rule.conditions)}개")
                conditions = self._parse_conditions_list(rule.conditions)
            elif hasattr(rule, "ruleCondition") and getattr(
                rule, "ruleCondition", None
            ):
                self.logger.debug("ruleCondition 발견")
                conditions = self._parse_condition_tree(getattr(rule, "ruleCondition"))
            else:
                self.logger.warning(f"룰에서 조건을 찾을 수 없음: {rule}")

            # 캐시에 저장
            self._analysis_cache[cache_key] = conditions

            # 값 변환 후처리
            self._post_process_conditions(conditions)

            self.logger.debug(f"조건 파싱 완료: {len(conditions)}개 조건")
            return conditions

        except Exception as e:
            self.logger.error(f"조건 파싱 중 오류: {str(e)}", exc_info=True)
            raise ConditionAnalysisError(f"조건 파싱 실패: {str(e)}")

    def _parse_condition_tree(self, tree: Any) -> List[RuleCondition]:
        """
        조건 트리를 파싱하여 조건 리스트로 변환

        Args:
            tree: 조건 트리 객체

        Returns:
            List[RuleCondition]: 파싱된 조건들
        """
        conditions = []

        try:
            if tree is None:
                return conditions

            # 딕셔너리 형태의 트리
            if isinstance(tree, dict):
                self.logger.debug(
                    f"딕셔너리 형태 트리 파싱: {tree.get('keyName', 'unknown')}"
                )
                # 논리 연산자 블록인 경우
                if "logicType" in tree and "condition" in tree:
                    # 하위 조건들을 재귀적으로 파싱하고 직접 추가
                    for nested_data in tree.get("condition", []):
                        conditions.extend(self._parse_condition_tree(nested_data))
                else:
                    # 일반 필드 조건
                    condition = self._parse_dict_condition(tree)
                    if condition:
                        conditions.append(condition)

            # 객체 형태의 트리
            elif hasattr(tree, "__dict__"):
                # ConditionTree 객체인지 RuleCondition 객체인지 구분
                tree_type = type(tree).__name__
                self.logger.debug(f"객체 형태 트리 파싱: {tree_type}")

                if tree_type == "ConditionTree":
                    # ConditionTree 객체인 경우
                    if tree.condition:
                        for item in tree.condition:
                            conditions.extend(self._parse_condition_tree(item))

                elif tree_type == "RuleCondition":
                    # RuleCondition 객체인 경우
                    # keyName이 None이거나 placeholder인 경우 (논리 연산자 블록)
                    if not tree.keyName or tree.keyName == "placeholder":
                        # 논리 연산자 블록이면 하위 조건들을 처리
                        if hasattr(tree, "condition") and tree.condition:
                            for item in tree.condition:
                                conditions.extend(self._parse_condition_tree(item))
                        elif hasattr(tree, "conditions") and tree.conditions:
                            for item in tree.conditions:
                                conditions.extend(self._parse_condition_tree(item))
                    else:
                        # 실제 필드 조건이면 그대로 추가
                        conditions.append(tree)

                else:
                    # 기타 객체의 경우 기존 방식 사용
                    condition = self._parse_object_condition(tree)
                    if condition:
                        conditions.append(condition)

            # 리스트 형태
            elif isinstance(tree, list):
                for item in tree:
                    conditions.extend(self._parse_condition_tree(item))

        except Exception as e:
            self.logger.error(f"조건 트리 파싱 중 오류: {str(e)}", exc_info=True)

        return conditions

    def _parse_dict_condition(self, condition_dict: dict) -> Optional[RuleCondition]:
        """
        딕셔너리 형태의 조건을 RuleCondition으로 변환

        Args:
            condition_dict (dict): 조건 딕셔너리

        Returns:
            Optional[RuleCondition]: 변환된 조건
        """
        try:
            # 일반 필드 조건 처리
            if "keyName" in condition_dict and "operator" in condition_dict:
                # fieldDataType에 따라 값 변환
                raw_value = condition_dict.get("value")
                field_data_type = condition_dict.get("fieldDataType", "String")
                converted_value = self._convert_value_by_type(
                    raw_value, field_data_type
                )

                self.logger.debug(
                    f"_parse_dict_condition: {condition_dict.get('keyName')} - "
                    f"{raw_value} ({type(raw_value).__name__}) -> "
                    f"{converted_value} ({type(converted_value).__name__})"
                )

                return RuleCondition(
                    keyName=condition_dict.get("keyName"),
                    operator=condition_dict.get("operator"),
                    value=converted_value,
                    fieldDataType=field_data_type,
                    condUuid=condition_dict.get("condUuid"),
                )
            # 논리 연산자 블록 처리
            elif "logicType" in condition_dict and "condition" in condition_dict:
                # 하위 조건들을 파싱
                nested_conditions = []
                for nested_data in condition_dict.get("condition", []):
                    nested_conditions.extend(self._parse_condition_tree(nested_data))

                return RuleCondition(
                    keyName="placeholder",
                    operator=condition_dict.get("logicType", "and").lower(),
                    conditions=nested_conditions,
                    logicType=condition_dict.get("logicType"),
                )
            else:
                self.logger.warning(f"알 수 없는 조건 형식: {condition_dict}")
                return None

        except Exception as e:
            self.logger.error(f"딕셔너리 조건 파싱 중 오류: {str(e)}", exc_info=True)
            return None

    def _parse_object_condition(self, condition_obj: Any) -> Optional[RuleCondition]:
        """
        객체 형태의 조건을 RuleCondition으로 변환

        Args:
            condition_obj: 조건 객체

        Returns:
            Optional[RuleCondition]: 변환된 조건
        """
        try:
            # 이미 RuleCondition 객체인 경우
            if isinstance(condition_obj, RuleCondition):
                return condition_obj

            # 다른 객체 타입인 경우 속성 추출
            key_name = getattr(condition_obj, "keyName", None)
            operator = getattr(condition_obj, "operator", None)
            value = getattr(condition_obj, "value", None)
            field_data_type = getattr(condition_obj, "fieldDataType", "String")
            cond_uuid = getattr(condition_obj, "condUuid", None)
            logic_type = getattr(condition_obj, "logicType", None)

            # 하위 조건들 처리
            conditions = getattr(condition_obj, "conditions", None)
            if conditions:
                nested_conditions = []
                for nested_condition in conditions:
                    nested_conditions.extend(
                        self._parse_condition_tree(nested_condition)
                    )
                conditions = nested_conditions

            # 값 변환
            converted_value = self._convert_value_by_type(value, field_data_type)

            return RuleCondition(
                keyName=key_name,
                operator=operator,
                value=converted_value,
                fieldDataType=field_data_type,
                conditions=conditions,
                logicType=logic_type,
                condUuid=cond_uuid,
            )

        except Exception as e:
            self.logger.error(f"객체 조건 파싱 중 오류: {str(e)}", exc_info=True)
            return None

    def _parse_conditions_list(self, conditions_list: List[Any]) -> List[RuleCondition]:
        """
        조건 리스트를 파싱하여 RuleCondition 리스트로 변환

        Args:
            conditions_list (List[Any]): 조건 리스트

        Returns:
            List[RuleCondition]: 파싱된 조건들
        """
        conditions = []

        for condition_item in conditions_list:
            parsed_conditions = self._parse_condition_tree(condition_item)
            conditions.extend(parsed_conditions)

        return conditions

    def infer_field_types(
        self, rule: Rule, conditions: List[RuleCondition]
    ) -> Dict[str, str]:
        """
        조건들로부터 필드 타입을 추론

        Why: 필드 타입 정보가 명시되지 않은 룰에서도 자동으로 타입을 추론하여 검증과 분석의 정확성을 높이기 위함입니다.
        How: fieldDataType 우선 확인 후, 값과 연산자 패턴을 분석하여 string/number/boolean/date/array 타입을 자동 판별합니다.

        Args:
            rule (Rule): 룰 객체
            conditions (List[RuleCondition]): 조건들

        Returns:
            Dict[str, str]: 필드명과 타입 매핑
        """
        try:
            field_types = {}

            def analyze_condition(condition: RuleCondition):
                if condition.keyName and condition.keyName != "placeholder":
                    # fieldDataType이 있으면 우선 사용
                    if condition.fieldDataType:
                        field_types[condition.keyName] = condition.fieldDataType.lower()
                    else:
                        # 값과 연산자로부터 타입 추론
                        inferred_type = self._infer_type_from_value(
                            condition.value, condition.operator
                        )
                        field_types[condition.keyName] = inferred_type

                # 하위 조건들도 처리
                if condition.conditions:
                    for nested_condition in condition.conditions:
                        analyze_condition(nested_condition)

            # 모든 조건 분석
            for condition in conditions:
                analyze_condition(condition)

            # 캐시에 저장
            self.field_types.update(field_types)

            self.logger.debug(f"필드 타입 추론 완료: {len(field_types)}개 필드")
            return field_types

        except Exception as e:
            self.logger.error(f"필드 타입 추론 중 오류: {str(e)}", exc_info=True)
            return {}

    def _infer_type_from_value(self, value: Any, operator: Optional[str] = None) -> str:
        """
        값과 연산자로부터 타입 추론

        Args:
            value: 값
            operator: 연산자

        Returns:
            str: 추론된 타입
        """
        if value is None:
            return "unknown"

        # 불린 타입
        if isinstance(value, bool):
            return "boolean"

        # 숫자 타입
        if isinstance(value, (int, float)):
            return "number"

        # 문자열 타입
        if isinstance(value, str):
            # 날짜 형식 확인
            if self._is_date_string(value):
                return "date"

            # 숫자 문자열 확인
            try:
                float(value)
                return "number"
            except ValueError:
                pass

            # 불린 문자열 확인
            if value.lower() in ["true", "false"]:
                return "boolean"

            return "string"

        # 리스트/배열 타입
        if isinstance(value, (list, tuple)):
            return "array"

        # 기타
        return "unknown"

    def _is_date_string(self, value: str) -> bool:
        """
        문자열이 날짜 형식인지 확인

        Args:
            value (str): 확인할 문자열

        Returns:
            bool: 날짜 형식 여부
        """
        date_patterns = [
            r"^\d{4}-\d{2}-\d{2}$",  # YYYY-MM-DD
            r"^\d{4}/\d{2}/\d{2}$",  # YYYY/MM/DD
            r"^\d{2}/\d{2}/\d{4}$",  # MM/DD/YYYY
            r"^\d{2}-\d{2}-\d{4}$",  # MM-DD-YYYY
            r"^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}",  # ISO 8601
        ]

        for pattern in date_patterns:
            if re.match(pattern, value):
                return True

        return False

    def calculate_structure_metrics(
        self, conditions: List[RuleCondition], rule: Optional[Rule] = None
    ) -> Dict[str, Any]:
        """
        조건들의 구조 메트릭 계산

        Why: 룰의 구조적 복잡성과 깊이를 정량화하여 유지보수성과 성능 영향을 미리 파악하기 위함입니다.
        How: 조건 트리의 깊이, 조건 수, 고유 필드 수, 논리 연산자 분포를 분석하여 복잡도 점수와 구조 정보를 산출합니다.

        Args:
            conditions (List[RuleCondition]): 조건들
            rule (Optional[Rule]): 룰 객체

        Returns:
            Dict[str, Any]: 구조 메트릭
        """
        try:
            # 기본 메트릭 계산
            total_conditions = self._count_all_conditions(conditions)
            field_conditions = self._count_field_conditions(conditions)
            logical_operators = total_conditions - field_conditions
            max_depth = self._calculate_depth(conditions)
            unique_fields = len(self._extract_unique_fields(conditions))
            complexity_score = self._calculate_complexity_score(conditions)

            # 원본 구조 분석 (가능한 경우)
            original_depth = max_depth
            original_conditions = total_conditions

            if rule and hasattr(rule, "conditionTree") and rule.conditionTree:
                try:
                    original_depth, original_conditions = (
                        self._analyze_original_structure(rule.conditionTree)
                    )
                except Exception as e:
                    self.logger.warning(f"원본 구조 분석 실패: {str(e)}")

            # 논리 연산자 분석
            logic_operators = self._count_logical_operators(conditions)

            # 조건 세부사항 추출
            condition_details = self._extract_condition_details(conditions)

            metrics = {
                "total_conditions": total_conditions,
                "field_conditions": field_conditions,
                "logical_operators": logical_operators,
                "max_depth": max_depth,
                "unique_fields": unique_fields,
                "complexity_score": complexity_score,
                "original_depth": original_depth,
                "original_conditions": original_conditions,
                "logic_operators": logic_operators,
                "condition_details": condition_details,
            }

            self.logger.debug(f"구조 메트릭 계산 완료: {metrics}")
            return metrics

        except Exception as e:
            self.logger.error(f"구조 메트릭 계산 중 오류: {str(e)}", exc_info=True)
            return {}

    def _calculate_depth(
        self, conditions: List[RuleCondition], current_depth: int = 1
    ) -> int:
        """
        조건 트리의 최대 깊이 계산

        Args:
            conditions (List[RuleCondition]): 조건들
            current_depth (int): 현재 깊이

        Returns:
            int: 최대 깊이
        """
        if not conditions:
            return current_depth

        max_depth = current_depth

        for condition in conditions:
            # 논리 연산자 블록인 경우에만 깊이 증가
            if condition.conditions and condition.keyName == "placeholder":
                nested_depth = self._calculate_depth(
                    condition.conditions, current_depth + 1
                )
                max_depth = max(max_depth, nested_depth)

        return max_depth

    def _count_all_conditions(self, conditions: List[RuleCondition]) -> int:
        """
        모든 조건의 개수 계산 (재귀)

        Args:
            conditions (List[RuleCondition]): 조건들

        Returns:
            int: 총 조건 개수
        """
        count = 0

        for condition in conditions:
            count += 1
            if condition.conditions:
                count += self._count_all_conditions(condition.conditions)

        return count

    def _count_field_conditions(self, conditions: List[RuleCondition]) -> int:
        """
        필드 조건의 개수 계산 (논리 연산자 제외)

        Args:
            conditions (List[RuleCondition]): 조건들

        Returns:
            int: 필드 조건 개수
        """
        count = 0

        for condition in conditions:
            # 실제 필드 조건인지 확인
            if condition.keyName and condition.keyName != "placeholder":
                count += 1

            # 하위 조건들도 계산
            if condition.conditions:
                count += self._count_field_conditions(condition.conditions)

        return count

    def _extract_unique_fields(self, conditions: List[RuleCondition]) -> List[str]:
        """
        고유한 필드명들 추출

        Args:
            conditions (List[RuleCondition]): 조건들

        Returns:
            List[str]: 고유한 필드명들
        """
        fields = set()

        def extract_fields(condition_list):
            for condition in condition_list:
                if condition.keyName and condition.keyName != "placeholder":
                    fields.add(condition.keyName)
                if condition.conditions:
                    extract_fields(condition.conditions)

        extract_fields(conditions)
        return list(fields)

    def _extract_condition_details(
        self,
        conditions: List[RuleCondition],
        depth_level: int = 0,
        parent_logic: Optional[str] = None,
    ) -> List[Dict[str, Any]]:
        """
        조건 세부사항 추출

        Args:
            conditions (List[RuleCondition]): 조건들
            depth_level (int): 깊이 레벨
            parent_logic (Optional[str]): 부모 논리 연산자

        Returns:
            List[Dict[str, Any]]: 조건 세부사항들
        """
        details = []

        for condition in conditions:
            detail = {
                "keyName": condition.keyName,
                "operator": condition.operator,
                "value": condition.value,
                "fieldDataType": condition.fieldDataType,
                "depth": depth_level,
                "parent_logic": parent_logic,
                "is_logical": condition.keyName == "placeholder"
                or condition.keyName is None,
            }

            details.append(detail)

            # 하위 조건들 처리
            if condition.conditions:
                nested_details = self._extract_condition_details(
                    condition.conditions, depth_level + 1, condition.operator
                )
                details.extend(nested_details)

        return details

    def _calculate_complexity_score(self, conditions: List[RuleCondition]) -> int:
        """
        복잡도 점수 계산

        Args:
            conditions (List[RuleCondition]): 조건들

        Returns:
            int: 복잡도 점수
        """
        score = 0

        # 기본 점수 (조건 개수)
        total_conditions = self._count_all_conditions(conditions)
        score += total_conditions * 2  # 조건당 2점

        # 깊이 점수
        max_depth = self._calculate_depth(conditions)
        score += max_depth * 3  # 깊이당 3점

        # 고유 필드 점수
        unique_fields = len(self._extract_unique_fields(conditions))
        score += unique_fields * 1  # 필드당 1점

        # 논리 연산자 점수
        logic_operators = self._count_logical_operators(conditions)
        for operator, count in logic_operators.items():
            if operator == "or":
                score += count * 2  # OR은 더 복잡 (2점)
            else:
                score += count * 1  # AND는 덜 복잡 (1점)

        # 복잡한 연산자 점수
        for condition in conditions:
            if condition.operator in ["contains", "starts_with", "ends_with"]:
                score += 1
            elif condition.operator in ["in", "not_in"]:
                score += 2

        return score

    def _analyze_original_structure(self, condition_tree: Any) -> tuple[int, int]:
        """
        원본 조건 트리의 구조 분석

        Args:
            condition_tree: 원본 조건 트리

        Returns:
            tuple[int, int]: (최대 깊이, 총 조건 개수)
        """
        try:

            def analyze_recursive(tree, current_depth: int) -> tuple[int, int]:
                if tree is None:
                    return current_depth, 0

                max_depth = current_depth
                total_count = 0

                if isinstance(tree, dict):
                    total_count += 1

                    # 하위 조건들 분석
                    if "condition" in tree and isinstance(tree["condition"], list):
                        for nested_tree in tree["condition"]:
                            nested_depth, nested_count = analyze_recursive(
                                nested_tree, current_depth + 1
                            )
                            max_depth = max(max_depth, nested_depth)
                            total_count += nested_count

                elif isinstance(tree, list):
                    for item in tree:
                        nested_depth, nested_count = analyze_recursive(
                            item, current_depth
                        )
                        max_depth = max(max_depth, nested_depth)
                        total_count += nested_count

                elif hasattr(tree, "__dict__"):
                    total_count += 1

                    # 하위 조건들 확인
                    if hasattr(tree, "condition") and tree.condition:
                        for nested_tree in tree.condition:
                            nested_depth, nested_count = analyze_recursive(
                                nested_tree, current_depth + 1
                            )
                            max_depth = max(max_depth, nested_depth)
                            total_count += nested_count

                return max_depth, total_count

            return analyze_recursive(condition_tree, 1)

        except Exception as e:
            self.logger.error(f"원본 구조 분석 중 오류: {str(e)}", exc_info=True)
            return 1, 0

    def _count_logical_operators(
        self, conditions: List[RuleCondition]
    ) -> Dict[str, int]:
        """
        논리 연산자 개수 계산

        Args:
            conditions (List[RuleCondition]): 조건들

        Returns:
            Dict[str, int]: 논리 연산자별 개수
        """
        operators = {"and": 0, "or": 0}

        for condition in conditions:
            # 논리 연산자 블록인지 확인
            if condition.keyName == "placeholder" or condition.keyName is None:
                if condition.operator:
                    op = condition.operator.lower()
                    if op in operators:
                        operators[op] += 1
                elif condition.logicType:
                    op = condition.logicType.lower()
                    if op in operators:
                        operators[op] += 1

            # 하위 조건들도 계산
            if condition.conditions:
                nested_operators = self._count_logical_operators(condition.conditions)
                for op, count in nested_operators.items():
                    operators[op] += count

        return operators

    def get_field_type(
        self, field: str, condition: Optional[RuleCondition] = None
    ) -> str:
        """
        필드의 타입 반환

        Why: 필드의 정확한 타입 정보를 제공하여 연산자 유효성 검사와 타입 검증의 정확성을 보장하기 위함입니다.
        How: 조건에 명시된 fieldDataType을 우선 확인하고, 없으면 캐시된 타입 정보를 사용하여 표준 타입명으로 매핑합니다.

        Args:
            field (str): 필드명
            condition (Optional[RuleCondition]): 조건 객체 (fieldDataType 확인용)

        Returns:
            str: 필드 타입
        """
        # 1. 조건에 명시된 fieldDataType 우선 사용
        if condition and condition.fieldDataType:
            field_type = condition.fieldDataType.lower()
            # 표준 타입명으로 매핑
            type_mapping = {
                "string": "string",
                "text": "string",
                "number": "number",
                "integer": "number",
                "int": "number",
                "float": "number",
                "boolean": "boolean",
                "bool": "boolean",
                "date": "date",
                "datetime": "date",
                "array": "array",
                "list": "array",
            }
            return type_mapping.get(field_type, field_type)

        # 2. 캐시된 타입 정보 사용
        return self.field_types.get(field, "unknown")

    def is_valid_operator(
        self, field: str, operator: str, condition: Optional[RuleCondition] = None
    ) -> bool:
        """
        필드에 대한 연산자의 유효성 확인

        Why: 필드 타입에 맞지 않는 연산자 사용으로 인한 런타임 오류를 사전에 방지하고 룰의 논리적 정확성을 보장하기 위함입니다.
        How: get_field_type으로 필드 타입을 확인한 후, 해당 타입에 허용된 연산자 목록과 비교하여 유효성을 판단합니다.

        Args:
            field (str): 필드명
            operator (str): 연산자
            condition (Optional[RuleCondition]): 조건 객체 (fieldDataType 확인용)

        Returns:
            bool: 유효성 여부
        """
        field_type = self.get_field_type(field, condition)
        valid_ops = self._valid_operators.get(field_type, [])
        return operator in valid_ops

    def is_valid_type(
        self, field: str, value: Any, condition: Optional[RuleCondition] = None
    ) -> bool:
        """
        필드에 대한 값의 타입 유효성 확인

        Why: 필드에 할당되는 값의 타입이 올바른지 검증하여 데이터 일관성과 룰 실행의 안정성을 보장하기 위함입니다.
        How: get_field_type으로 필드 타입을 확인하고, 값의 실제 타입을 추론한 후 타입 호환성 매트릭스를 통해 유효성을 판단합니다.

        Args:
            field (str): 필드명
            value: 값
            condition (Optional[RuleCondition]): 조건 객체 (fieldDataType 확인용)

        Returns:
            bool: 유효성 여부
        """
        field_type = self.get_field_type(field, condition)
        inferred_type = self._infer_type_from_value(value)

        # 타입 호환성 확인
        compatible_types = {
            "string": ["string", "unknown"],
            "number": ["number", "unknown"],
            "boolean": ["boolean", "unknown"],
            "date": ["date", "string", "unknown"],
            "array": ["array", "unknown"],
        }

        return inferred_type in compatible_types.get(field_type, [])

    def _get_condition_field(self, condition: RuleCondition) -> Optional[str]:
        """
        조건에서 필드명 추출

        Args:
            condition (RuleCondition): 조건

        Returns:
            Optional[str]: 필드명
        """
        if condition.keyName and condition.keyName != "placeholder":
            return condition.keyName
        return None

    def _convert_value_by_type(self, value: Any, field_data_type: str) -> Any:
        """
        필드 데이터 타입에 따라 값 변환

        Args:
            value: 원본 값
            field_data_type (str): 필드 데이터 타입

        Returns:
            Any: 변환된 값
        """
        if value is None:
            return None

        try:
            field_type = field_data_type.lower()

            if field_type in ["number", "integer", "int"]:
                if isinstance(value, str):
                    # 문자열에서 숫자 추출
                    if "." in value:
                        return float(value)
                    else:
                        return int(value)
                return value

            elif field_type in ["boolean", "bool"]:
                if isinstance(value, str):
                    return value.lower() in ["true", "1", "yes", "on"]
                return bool(value)

            elif field_type in ["string", "text"]:
                return str(value)

            elif field_type in ["date", "datetime"]:
                if isinstance(value, str):
                    # 날짜 문자열 그대로 반환
                    return value
                return str(value)

            else:
                # 기타 타입은 그대로 반환
                return value

        except (ValueError, TypeError):
            self.logger.warning(
                f"값 변환 실패: {value} -> {field_data_type}, 원본 반환"
            )
            return value

    def _post_process_conditions(self, conditions: List[RuleCondition]):
        """
        조건들의 후처리

        Args:
            conditions (List[RuleCondition]): 조건들
        """

        def process_condition(condition: RuleCondition):
            # 값 변환 후처리
            if condition.value is not None and condition.fieldDataType:
                condition.value = self._convert_value_by_type(
                    condition.value, condition.fieldDataType
                )

            # 하위 조건들도 처리
            if condition.conditions:
                self._post_process_conditions(condition.conditions)

        for condition in conditions:
            process_condition(condition)

    def is_contradictory(self, cond1, cond2):
        """
        두 조건이 서로 모순되는지 판단 (레거시와 동일한 방식)

        Why: 동일 필드에 모순되는 조건이 있어 룰이 항상 실패하거나 예상치 못한 결과를 초래하는 문제를 사전에 방지하기 위함입니다.
        How: 같은 필드의 조건들을 비교하여 연산자와 값의 모순 관계(==/!=, >/<=, >=/< 등)를 분석하고 모순 여부를 판단합니다.

        Args:
            cond1: 첫 번째 조건
            cond2: 두 번째 조건

        Returns:
            bool: 모순 여부
        """
        # 기본 필드명 확인
        if not hasattr(cond1, "keyName") or not hasattr(cond2, "keyName"):
            return False

        key1 = getattr(cond1, "keyName", None)
        key2 = getattr(cond2, "keyName", None)

        # 같은 필드가 아니면 모순이 아님
        if key1 != key2 or key1 is None or key1 == "placeholder":
            return False

        # 연산자와 값 추출
        op1 = getattr(cond1, "operator", None)
        op2 = getattr(cond2, "operator", None)
        val1 = getattr(cond1, "value", None)
        val2 = getattr(cond2, "value", None)

        if op1 is None or op2 is None:
            return False

        # == 와 != 조건의 자기모순 케이스 (우선순위 높음)
        if (op1 == "==" and op2 == "!=" and str(val1) == str(val2)) or (
            op1 == "!=" and op2 == "==" and str(val1) == str(val2)
        ):
            return True

        # 문자열 동등 비교
        if isinstance(val1, str) and isinstance(val2, str):
            if op1 == "==" and op2 == "==" and val1 != val2:
                return True

        # 숫자 값 변환 시도
        try:
            num_val1 = float(val1)
            num_val2 = float(val2)

            # 숫자 범위 체크
            if (op1 == ">" and op2 == "<=" and num_val1 <= num_val2) or (
                op1 == "<=" and op2 == ">" and num_val1 >= num_val2
            ):
                return True
            elif (op1 == ">=" and op2 == "<" and num_val1 >= num_val2) or (
                op1 == "<" and op2 == ">=" and num_val1 <= num_val2
            ):
                return True
            elif (op1 == ">=" and op2 == "<=" and num_val1 > num_val2) or (
                op1 == "<=" and op2 == ">=" and num_val1 < num_val2
            ):
                return True
            elif (op1 == ">" and op2 == "<" and num_val1 >= num_val2) or (
                op1 == "<" and op2 == ">" and num_val1 <= num_val2
            ):
                return True
            # 같음/같지 않음 체크
            elif op1 == "==" and op2 == "==" and num_val1 != num_val2:
                return True

        except (ValueError, TypeError):
            # 숫자가 아닌 경우 다른 타입 간 비교
            if op1 == "==" and op2 == "==":
                # 서로 다른 값에 대한 == 연산자 사용 시 모순
                return True

        return False

    def is_ambiguous(self, cond1, cond2):
        """
        두 조건이 모호한지 판단 (레거시와 동일한 방식)

        Why: 조건 간의 겹침이나 중복으로 인해 분기 로직이 불명확해지고 예상치 못한 동작을 초래하는 문제를 방지하기 위함입니다.
        How: 같은 필드의 조건들을 비교하여 범위 겹침(>=/==, >/>= 등), 동일 값 처리 등을 분석하고 모호성 여부를 판단합니다.

        Args:
            cond1: 첫 번째 조건
            cond2: 두 번째 조건

        Returns:
            bool: 모호성 여부
        """
        # 기본 필드명 확인
        if not hasattr(cond1, "keyName") or not hasattr(cond2, "keyName"):
            return False

        key1 = getattr(cond1, "keyName", None)
        key2 = getattr(cond2, "keyName", None)

        # 같은 필드가 아니면 모호하지 않음
        if key1 != key2 or key1 is None or key1 == "placeholder":
            return False

        # 연산자와 값 추출
        op1 = getattr(cond1, "operator", None)
        op2 = getattr(cond2, "operator", None)
        val1 = getattr(cond1, "value", None)
        val2 = getattr(cond2, "value", None)

        if op1 is None or op2 is None:
            return False

        # 숫자 값 변환 시도
        try:
            num_val1 = float(val1)
            num_val2 = float(val2)

            # 범위 겹침 검사
            if op1 == ">=" and op2 == ">=":
                # 둘 다 >= 조건인 경우, 더 큰 값이 작은 값을 포함
                if num_val1 <= num_val2:
                    return True  # val1 >= x >= val2 형태로 겹침
                elif num_val2 <= num_val1:
                    return True  # val2 >= x >= val1 형태로 겹침

            elif op1 == ">=" and op2 == "==" and num_val2 >= num_val1:
                return True  # >= val1 조건이 == val2 조건을 포함
            elif op2 == ">=" and op1 == "==" and num_val1 >= num_val2:
                return True  # >= val2 조건이 == val1 조건을 포함

            elif op1 == ">" and op2 == ">=" and num_val1 >= num_val2:
                return True  # > val1 조건이 >= val2 조건을 포함
            elif op2 == ">" and op1 == ">=" and num_val2 >= num_val1:
                return True  # > val2 조건이 >= val1 조건을 포함

            # 동일한 값에 대한 같은 연산자
            elif op1 == op2 and num_val1 == num_val2:
                return True

        except (ValueError, TypeError):
            # 문자열 값인 경우
            if isinstance(val1, str) and isinstance(val2, str):
                if op1 == op2 and val1 == val2:
                    return True

        return False
