"""
포맷팅 옵션 유효성 검증

사용자가 제공한 포맷팅 옵션의 유효성을 검증하고 에러 메시지를 제공합니다.
"""

from typing import Any, Dict, List, Tuple

from .formatting_options import DetailLevel, Language


class FormattingOptionValidator:
    """
    포맷팅 옵션 유효성 검증 클래스
    """

    def __init__(self):
        """검증기 초기화"""
        self.errors: List[str] = []
        self.warnings: List[str] = []

    def validate(self, options: Dict[str, Any]) -> Tuple[bool, List[str]]:
        """
        포맷팅 옵션 유효성 검증

        Args:
            options: 검증할 옵션 딕셔너리

        Returns:
            (유효성 여부, 에러 메시지 리스트)
        """
        self.errors.clear()
        self.warnings.clear()

        # 기본 검증
        self._validate_required_fields(options)
        self._validate_field_types(options)
        self._validate_field_values(options)
        self._validate_field_combinations(options)

        # 고급 검증
        self._validate_custom_template(options)
        self._validate_custom_settings(options)

        return len(self.errors) == 0, self.errors.copy()

    def _validate_required_fields(self, options: Dict[str, Any]) -> None:
        """필수 필드 검증"""
        required_fields = ["language", "detail_level"]

        for field in required_fields:
            if field not in options:
                self.errors.append(f"필수 필드 '{field}'가 누락되었습니다.")

    def _validate_field_types(self, options: Dict[str, Any]) -> None:
        """필드 타입 검증"""
        type_validations = {
            "include_emojis": bool,
            "language": str,
            "detail_level": str,
            "max_line_length": int,
            "include_timestamps": bool,
            "include_metadata": bool,
            "include_issues": bool,
            "include_performance": bool,
            "include_quality": bool,
        }

        for field, expected_type in type_validations.items():
            if field in options:
                if not isinstance(options[field], expected_type):
                    self.errors.append(
                        f"필드 '{field}'의 타입이 잘못되었습니다. "
                        f"예상: {expected_type.__name__}, "
                        f"실제: {type(options[field]).__name__}"
                    )

    def _validate_field_values(self, options: Dict[str, Any]) -> None:
        """필드 값 검증"""
        # 언어 검증
        if "language" in options:
            try:
                Language(options["language"])
            except ValueError:
                self.errors.append(
                    f"지원하지 않는 언어입니다: {options['language']}. "
                    f"지원 언어: {[lang.value for lang in Language]}"
                )

        # 상세도 레벨 검증
        if "detail_level" in options:
            try:
                DetailLevel(options["detail_level"])
            except ValueError:
                self.errors.append(
                    f"지원하지 않는 상세도 레벨입니다: {options['detail_level']}. "
                    f"지원 레벨: {[level.value for level in DetailLevel]}"
                )

        # 최대 줄 길이 검증
        if "max_line_length" in options:
            value = options["max_line_length"]
            if not isinstance(value, int) or value < 40 or value > 200:
                self.errors.append(
                    f"최대 줄 길이는 40-200 사이의 정수여야 합니다. "
                    f"현재 값: {value}"
                )

    def _validate_field_combinations(self, options: Dict[str, Any]) -> None:
        """필드 조합 검증"""
        # 간단 레벨에서 상세 정보 포함 시 경고
        if options.get("detail_level") == "simple" and any(
            options.get(field, False)
            for field in ["include_performance", "include_quality", "include_metadata"]
        ):
            self.warnings.append(
                "간단 레벨에서 상세 정보를 포함하면 출력이 복잡해질 수 있습니다."
            )

        # 이모지 비활성화 시 경고
        if options.get("include_emojis") is False:
            self.warnings.append("이모지를 비활성화하면 가독성이 떨어질 수 있습니다.")

    def _validate_custom_template(self, options: Dict[str, Any]) -> None:
        """사용자 정의 템플릿 검증"""
        if "custom_template" in options and options["custom_template"]:
            template = options["custom_template"]

            # JSON 형태 검증
            if not isinstance(template, str):
                self.errors.append("사용자 정의 템플릿은 문자열이어야 합니다.")
                return

            # JSON 파싱 검증
            try:
                import json

                json.loads(template)
            except json.JSONDecodeError as e:
                self.errors.append(
                    f"사용자 정의 템플릿이 유효한 JSON이 아닙니다: {str(e)}"
                )

    def _validate_custom_settings(self, options: Dict[str, Any]) -> None:
        """사용자 정의 설정 검증"""
        if "custom_settings" in options:
            custom_settings = options["custom_settings"]

            if not isinstance(custom_settings, dict):
                self.errors.append("사용자 정의 설정은 딕셔너리여야 합니다.")
                return

            # 예약된 키 이름 검증
            reserved_keys = {
                "language",
                "detail_level",
                "include_emojis",
                "max_line_length",
                "include_timestamps",
                "include_metadata",
                "include_issues",
                "include_performance",
                "include_quality",
            }

            conflicting_keys = set(custom_settings.keys()) & reserved_keys
            if conflicting_keys:
                self.warnings.append(
                    f"사용자 정의 설정에서 예약된 키를 사용하면 예상치 못한 동작이 "
                    f"발생할 수 있습니다: {list(conflicting_keys)}"
                )

    def get_warnings(self) -> List[str]:
        """경고 메시지 반환"""
        return self.warnings.copy()

    def has_warnings(self) -> bool:
        """경고가 있는지 확인"""
        return len(self.warnings) > 0

    def get_validation_summary(self) -> Dict[str, Any]:
        """
        검증 결과 요약 반환

        Returns:
            검증 결과 요약 딕셔너리
        """
        return {
            "is_valid": len(self.errors) == 0,
            "error_count": len(self.errors),
            "warning_count": len(self.warnings),
            "errors": self.errors.copy(),
            "warnings": self.warnings.copy(),
        }
