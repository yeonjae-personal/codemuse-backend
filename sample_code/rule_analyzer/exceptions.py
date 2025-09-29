"""
RAAS Rule Analyzer 예외 클래스들

순수 로직 분석 관련 예외들을 정의합니다.
"""


class RuleAnalyzerError(Exception):
    """룰 분석기 기본 예외"""

    pass


class RuleParsingError(RuleAnalyzerError):
    """룰 파싱 오류"""

    pass


class ConditionAnalysisError(RuleAnalyzerError):
    """조건 분석 오류"""

    pass


class IssueDetectionError(RuleAnalyzerError):
    """이슈 검출 오류"""

    pass


class AdvancedAnalysisError(RuleAnalyzerError):
    """고급 분석 오류"""

    pass


class ValidationError(RuleAnalyzerError):
    """검증 오류"""

    pass


class JSONProcessingError(RuleAnalyzerError):
    """JSON 처리 오류"""

    pass


class ConfigurationError(RuleAnalyzerError):
    """설정 오류"""

    pass


class DataTypeError(RuleAnalyzerError):
    """데이터 타입 오류"""

    pass


class LogicError(RuleAnalyzerError):
    """로직 오류"""

    pass
