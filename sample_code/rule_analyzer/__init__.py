"""
RaaS Rule Analyzer

룰 분석 및 검증을 위한 모듈입니다.
JSON I/O 지원으로 파이프라인 통합이 가능합니다.
"""

from typing import List, Union

from .analyzers import RuleAnalyzer
from .exceptions import RuleAnalyzerError, RuleParsingError
from .json_processor import RuleJsonProcessor
from .models import (
    AnalysisOptions,
    BatchAnalysisResult,
    ConditionIssue,
    ConditionTree,
    Rule,
    RuleCondition,
    RuleJsonInput,
    RuleJsonOutput,
    ValidationResult,
)
from .parser import RuleParser


# 버전은 패키지 메타데이터에서 동기화
try:
    from importlib.metadata import version as _pkg_version

    __version__ = _pkg_version(__package__)
except Exception:  # 런타임 환경에 따라 importlib.metadata가 실패할 수 있어 폴백
    __version__ = "1.1.1"
__author__ = "RaaS Team"
__email__ = "raas@example.com"

# 주요 클래스들
__all__ = [
    # 핵심 모델
    "Rule",
    "ValidationResult",
    "ConditionIssue",
    "RuleCondition",
    "ConditionTree",
    # JSON I/O 모델
    "RuleJsonInput",
    "RuleJsonOutput",
    "AnalysisOptions",
    "BatchAnalysisResult",
    # 핵심 서비스
    "RuleParser",
    "RuleAnalyzer",
    "RuleJsonProcessor",
    # 예외
    "RuleAnalyzerError",
    "RuleParsingError",
]


# 편의 함수들
def analyze_rule_json(json_input: Union[str, list, dict]) -> str:
    """
    JSON으로 룰을 분석하고 JSON 결과를 반환

    Why: 상위 시스템이 표준 JSON만 전달해도 분석/검증 결과를 구조화된 JSON으로 즉시 받을 수 있게 하기 위함입니다.
    How: 입력을 파싱한 뒤 단일/배치 여부를 판별하여 RuleAnalyzer로 분석하고, 결과를 JSON 직렬화하여 반환합니다.

    Args:
        json_input: JSON 입력 (룰 배열, 래퍼 객체, 또는 JSON 문자열)

    Returns:
        JSON 결과 문자열
    """
    processor = RuleJsonProcessor()
    result = processor.process_json_input(json_input)
    return result.json()


async def analyze_rule_json_async(json_input: Union[str, list, dict]) -> str:
    """
    JSON으로 룰을 비동기 분석하고 JSON 결과를 반환

    Why: 대량/동시 요청 환경에서 블로킹 없이 빠르게 분석 파이프라인을 처리하기 위함입니다.
    How: 비동기 파서/분석기를 활용하여 이벤트 루프에서 처리하고, 결과를 JSON으로 직렬화합니다.

    Args:
        json_input: JSON 입력 (룰 배열, 래퍼 객체, 또는 JSON 문자열)

    Returns:
        JSON 결과 문자열
    """
    processor = RuleJsonProcessor()
    result = await processor.process_json_input_async(json_input)
    return result.json()


def analyze_rule(rule_data: Union[dict, list]) -> ValidationResult:
    """
    룰 데이터를 분석하여 ValidationResult를 반환

    Why: 애플리케이션 코드에서 간단히 호출해 즉시 분석 결과 객체를 활용하기 위함입니다.
    How: 입력을 파싱하여 Rule 객체로 만든 뒤 RuleAnalyzer로 검증/이슈/복잡도/품질 메트릭을 계산합니다.

    Args:
        rule_data: 룰 데이터 딕셔너리 또는 룰 배열

    Returns:
        ValidationResult: 분석 결과
    """
    parser = RuleParser()
    analyzer = RuleAnalyzer()

    if isinstance(rule_data, list):
        # 룰 배열인 경우 첫 번째 룰만 분석
        rule = parser.parse_multiple(rule_data)[0]
    else:
        # 단일 룰인 경우
        rule = parser.parse(rule_data)

    return analyzer.analyze_rule_sync(rule)


async def analyze_rule_async(rule_data: Union[dict, list]) -> ValidationResult:
    """
    룰 데이터를 비동기 분석하여 ValidationResult를 반환

    Why: 고부하 환경에서 병렬 분석 처리량을 높이기 위함입니다.
    How: 비동기 RuleAnalyzer를 호출하여 검증/이슈/복잡도/품질 메트릭을 산출합니다.

    Args:
        rule_data: 룰 데이터 딕셔너리 또는 룰 배열

    Returns:
        ValidationResult: 분석 결과
    """
    parser = RuleParser()
    analyzer = RuleAnalyzer()

    if isinstance(rule_data, list):
        # 룰 배열인 경우 첫 번째 룰만 분석
        rule = parser.parse_multiple(rule_data)[0]
    else:
        # 단일 룰인 경우
        rule = parser.parse(rule_data)

    return await analyzer.analyze_rule(rule)


def analyze_rules(rule_data: Union[dict, list]) -> List[ValidationResult]:
    """
    룰 데이터들을 분석하여 ValidationResult 리스트를 반환

    Why: 여러 룰을 한 번에 점검하여 품질/모순/누락을 일괄 확인하기 위함입니다.
    How: 입력을 Rule 리스트로 파싱하고 각 룰에 대해 동기 분석을 수행해 결과를 모읍니다.

    Args:
        rule_data: 룰 데이터 딕셔너리 또는 룰 배열

    Returns:
        List[ValidationResult]: 분석 결과 리스트
    """
    parser = RuleParser()
    analyzer = RuleAnalyzer()

    if isinstance(rule_data, list):
        # 룰 배열인 경우
        rules = parser.parse_multiple(rule_data)
    else:
        # 단일 룰인 경우 배열로 변환
        rules = [parser.parse(rule_data)]

    results = []
    for rule in rules:
        result = analyzer.analyze_rule_sync(rule)
        results.append(result)

    return results


async def analyze_rules_async(rule_data: Union[dict, list]) -> List[ValidationResult]:
    """
    룰 데이터들을 비동기 분석하여 ValidationResult 리스트를 반환

    Why: 배치 분석 처리량을 극대화하고 응답 대기 시간을 줄이기 위함입니다.
    How: 비동기 RuleAnalyzer를 사용해 각 룰을 순차/동시로 처리하고 결과를 수집합니다.

    Args:
        rule_data: 룰 데이터 딕셔너리 또는 룰 배열

    Returns:
        List[ValidationResult]: 분석 결과 리스트
    """
    parser = RuleParser()
    analyzer = RuleAnalyzer()

    if isinstance(rule_data, list):
        # 룰 배열인 경우
        rules = parser.parse_multiple(rule_data)
    else:
        # 단일 룰인 경우 배열로 변환
        rules = [parser.parse(rule_data)]

    results = []
    for rule in rules:
        result = await analyzer.analyze_rule(rule)
        results.append(result)

    return results
