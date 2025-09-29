"""
데이터 검증 유틸리티

다양한 데이터 타입의 유효성을 검증하는 기능을 제공합니다.
"""

import logging
import re
from datetime import datetime
from typing import Any, Dict, List, Optional, Tuple, Union


class ValidationHelper:
    """
    데이터 검증 도우미

    다양한 데이터 타입의 유효성을 검증합니다.
    """

    def __init__(self):
        """검증 도우미 초기화"""
        self.logger = logging.getLogger(__name__)

        # 이메일 패턴
        self.email_pattern = re.compile(
            r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'
        )

        # URL 패턴
        self.url_pattern = re.compile(
            r'^https?://(?:[-\w.])+(?:\:[0-9]+)?(?:/(?:[\w/_.])*(?:\?(?:[\w&=%.])*)?(?:\#(?:[\w.])*)?)?$'
        )

        # 전화번호 패턴 (한국)
        self.phone_pattern = re.compile(r'^(\+82|0)[1-9][0-9]{7,8}$')

    def validate_email(self, email: str) -> bool:
        """
        이메일 주소 유효성 검증

        Args:
            email: 검증할 이메일 주소

        Returns:
            유효성 여부
        """
        if not email or not isinstance(email, str):
            return False

        return bool(self.email_pattern.match(email.strip()))

    def validate_url(self, url: str) -> bool:
        """
        URL 유효성 검증

        Args:
            url: 검증할 URL

        Returns:
            유효성 여부
        """
        if not url or not isinstance(url, str):
            return False

        return bool(self.url_pattern.match(url.strip()))

    def validate_phone(self, phone: str) -> bool:
        """
        전화번호 유효성 검증 (한국)

        Args:
            phone: 검증할 전화번호

        Returns:
            유효성 여부
        """
        if not phone or not isinstance(phone, str):
            return False

        # 공백과 하이픈 제거
        cleaned_phone = re.sub(r'[\s\-]', '', phone)
        return bool(self.phone_pattern.match(cleaned_phone))

    def validate_required(
        self, value: Any, field_name: str = "필드"
    ) -> Tuple[bool, Optional[str]]:
        """
        필수 값 검증

        Args:
            value: 검증할 값
            field_name: 필드명 (에러 메시지용)

        Returns:
            (유효성 여부, 에러 메시지)
        """
        if value is None:
            return False, f"{field_name}은(는) 필수입니다."

        if isinstance(value, str) and not value.strip():
            return False, f"{field_name}은(는) 비어있을 수 없습니다."

        if isinstance(value, (list, dict)) and len(value) == 0:
            return False, f"{field_name}은(는) 비어있을 수 없습니다."

        return True, None

    def validate_string_length(
        self,
        value: str,
        min_length: int = 0,
        max_length: Optional[int] = None,
        field_name: str = "문자열",
    ) -> Tuple[bool, Optional[str]]:
        """
        문자열 길이 검증

        Args:
            value: 검증할 문자열
            min_length: 최소 길이
            max_length: 최대 길이
            field_name: 필드명

        Returns:
            (유효성 여부, 에러 메시지)
        """
        if not isinstance(value, str):
            return False, f"{field_name}은(는) 문자열이어야 합니다."

        length = len(value)

        if length < min_length:
            return (
                False,
                f"{field_name}은(는) 최소 {min_length}자 이상이어야 합니다. (현재: {length}자)",
            )

        if max_length is not None and length > max_length:
            return (
                False,
                f"{field_name}은(는) 최대 {max_length}자 이하여야 합니다. (현재: {length}자)",
            )

        return True, None

    def validate_number_range(
        self,
        value: Union[int, float],
        min_value: Optional[Union[int, float]] = None,
        max_value: Optional[Union[int, float]] = None,
        field_name: str = "숫자",
    ) -> Tuple[bool, Optional[str]]:
        """
        숫자 범위 검증

        Args:
            value: 검증할 숫자
            min_value: 최소값
            max_value: 최대값
            field_name: 필드명

        Returns:
            (유효성 여부, 에러 메시지)
        """
        if not isinstance(value, (int, float)):
            return False, f"{field_name}은(는) 숫자여야 합니다."

        if min_value is not None and value < min_value:
            return (
                False,
                f"{field_name}은(는) {min_value} 이상이어야 합니다. (현재: {value})",
            )

        if max_value is not None and value > max_value:
            return (
                False,
                f"{field_name}은(는) {max_value} 이하여야 합니다. (현재: {value})",
            )

        return True, None

    def validate_list_length(
        self,
        value: List,
        min_length: int = 0,
        max_length: Optional[int] = None,
        field_name: str = "리스트",
    ) -> Tuple[bool, Optional[str]]:
        """
        리스트 길이 검증

        Args:
            value: 검증할 리스트
            min_length: 최소 길이
            max_length: 최대 길이
            field_name: 필드명

        Returns:
            (유효성 여부, 에러 메시지)
        """
        if not isinstance(value, list):
            return False, f"{field_name}은(는) 리스트여야 합니다."

        length = len(value)

        if length < min_length:
            return (
                False,
                f"{field_name}은(는) 최소 {min_length}개 이상의 항목이 있어야 합니다. (현재: {length}개)",
            )

        if max_length is not None and length > max_length:
            return (
                False,
                f"{field_name}은(는) 최대 {max_length}개 이하의 항목이 있어야 합니다. (현재: {length}개)",
            )

        return True, None

    def validate_dict_keys(
        self,
        value: Dict,
        required_keys: List[str],
        optional_keys: Optional[List[str]] = None,
        field_name: str = "딕셔너리",
    ) -> Tuple[bool, Optional[str]]:
        """
        딕셔너리 키 검증

        Args:
            value: 검증할 딕셔너리
            required_keys: 필수 키 리스트
            optional_keys: 선택적 키 리스트
            field_name: 필드명

        Returns:
            (유효성 여부, 에러 메시지)
        """
        if not isinstance(value, dict):
            return False, f"{field_name}은(는) 딕셔너리여야 합니다."

        # 필수 키 확인
        missing_keys = [key for key in required_keys if key not in value]
        if missing_keys:
            return (
                False,
                f"{field_name}에 필수 키가 누락되었습니다: {', '.join(missing_keys)}",
            )

        # 허용된 키만 있는지 확인
        allowed_keys = set(required_keys)
        if optional_keys:
            allowed_keys.update(optional_keys)

        invalid_keys = [key for key in value.keys() if key not in allowed_keys]
        if invalid_keys:
            return (
                False,
                f"{field_name}에 허용되지 않은 키가 있습니다: {', '.join(invalid_keys)}",
            )

        return True, None

    def validate_date_format(
        self, value: str, format_string: str = "%Y-%m-%d", field_name: str = "날짜"
    ) -> Tuple[bool, Optional[str]]:
        """
        날짜 형식 검증

        Args:
            value: 검증할 날짜 문자열
            format_string: 날짜 형식
            field_name: 필드명

        Returns:
            (유효성 여부, 에러 메시지)
        """
        if not isinstance(value, str):
            return False, f"{field_name}은(는) 문자열이어야 합니다."

        try:
            datetime.strptime(value, format_string)
            return True, None
        except ValueError:
            return (
                False,
                f"{field_name} 형식이 올바르지 않습니다. 예상 형식: {format_string}",
            )

    def validate_regex(
        self, value: str, pattern: str, field_name: str = "문자열"
    ) -> Tuple[bool, Optional[str]]:
        """
        정규표현식 패턴 검증

        Args:
            value: 검증할 문자열
            pattern: 정규표현식 패턴
            field_name: 필드명

        Returns:
            (유효성 여부, 에러 메시지)
        """
        if not isinstance(value, str):
            return False, f"{field_name}은(는) 문자열이어야 합니다."

        try:
            if re.match(pattern, value):
                return True, None
            else:
                return False, f"{field_name}이(가) 패턴과 일치하지 않습니다."
        except re.error as e:
            return False, f"잘못된 정규표현식 패턴: {str(e)}"

    def validate_all(
        self, validations: List[Tuple[bool, Optional[str]]]
    ) -> Tuple[bool, List[str]]:
        """
        여러 검증 결과를 종합하여 최종 결과 반환

        Args:
            validations: 검증 결과 리스트 [(유효성, 에러메시지), ...]

        Returns:
            (전체 유효성 여부, 에러 메시지 리스트)
        """
        errors = []

        for is_valid, error_message in validations:
            if not is_valid and error_message:
                errors.append(error_message)

        return len(errors) == 0, errors

    def validate_json_schema(
        self, data: Any, schema: Dict[str, Any]
    ) -> Tuple[bool, List[str]]:
        """
        JSON 스키마 기반 검증 (간단한 구현)

        Args:
            data: 검증할 데이터
            schema: JSON 스키마

        Returns:
            (유효성 여부, 에러 메시지 리스트)
        """
        errors = []

        # 타입 검증
        if "type" in schema:
            expected_type = schema["type"]
            if expected_type == "string" and not isinstance(data, str):
                errors.append(f"문자열이어야 합니다. (현재: {type(data).__name__})")
            elif expected_type == "number" and not isinstance(data, (int, float)):
                errors.append(f"숫자여야 합니다. (현재: {type(data).__name__})")
            elif expected_type == "boolean" and not isinstance(data, bool):
                errors.append(f"불린 값이어야 합니다. (현재: {type(data).__name__})")
            elif expected_type == "array" and not isinstance(data, list):
                errors.append(f"배열이어야 합니다. (현재: {type(data).__name__})")
            elif expected_type == "object" and not isinstance(data, dict):
                errors.append(f"객체여야 합니다. (현재: {type(data).__name__})")

        # 추가 검증 규칙들...
        # (실제 구현에서는 더 복잡한 JSON 스키마 검증 로직 추가)

        return len(errors) == 0, errors

    def get_validation_summary(
        self, field_name: str, value: Any, validations: List[Tuple[bool, Optional[str]]]
    ) -> Dict[str, Any]:
        """
        검증 결과 요약 반환

        Args:
            field_name: 필드명
            value: 검증된 값
            validations: 검증 결과 리스트

        Returns:
            검증 요약 딕셔너리
        """
        is_valid, errors = self.validate_all(validations)

        return {
            "field_name": field_name,
            "value": value,
            "is_valid": is_valid,
            "error_count": len(errors),
            "errors": errors,
            "validation_time": datetime.now().isoformat(),
        }
