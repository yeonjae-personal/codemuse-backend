"""
RaaS Rule Analyzer - Formatters Package

분석 결과를 다양한 형태로 포맷팅하는 기능을 제공합니다.
- 텍스트 포맷터: JSON 결과를 읽기 쉬운 텍스트로 변환
- 스트리밍 포맷터: 스트리밍 전송을 위한 포맷터
- 템플릿 기반: 언어별, 상세도별 포맷팅 템플릿 지원
"""

from .options.formatting_options import FormattingOptions
from .options.option_validator import FormattingOptionValidator
from .streaming_formatter import StreamingFormatter
from .text_formatter import TextFormatter


__all__ = [
    "TextFormatter",
    "StreamingFormatter",
    "FormattingOptions",
    "FormattingOptionValidator",
]


# 편의 함수들
def format_analysis_result(validation_result, options: FormattingOptions = None) -> str:
    """
    분석 결과를 텍스트로 포맷팅

    Args:
        validation_result: ValidationResult 객체
        options: 포맷팅 옵션

    Returns:
        포맷팅된 텍스트
    """
    if options is None:
        options = FormattingOptions()

    formatter = TextFormatter(options)
    return formatter.format(validation_result)


def get_formatting_options() -> dict:
    """
    사용 가능한 포맷팅 옵션 반환

    Returns:
        포맷팅 옵션 정보 딕셔너리
    """
    return FormattingOptions.get_available_options()


def validate_formatting_options(options: dict) -> tuple[bool, list[str]]:
    """
    포맷팅 옵션 유효성 검증

    Args:
        options: 검증할 옵션 딕셔너리

    Returns:
        (유효성 여부, 에러 메시지 리스트)
    """
    validator = FormattingOptionValidator()
    return validator.validate(options)
