"""
JSON I/O Processor for RaaS Rule Analyzer

JSON 입력을 받아서 처리하고 JSON 출력을 반환하는 기능을 제공합니다.
파이프라인 통합을 위한 표준화된 인터페이스를 제공합니다.
"""

import json
import logging
import time
from datetime import datetime
from typing import Any, Dict, List, Union

from .analyzers import RuleAnalyzer
from .models import (
    AnalysisOptions,
    RuleJsonInput,
    RuleJsonOutput,
)
from .parser import RuleParser


class RuleJsonProcessor:
    """
    JSON I/O 처리를 위한 메인 프로세서

    지원하는 액션들:
    - analyze_rule: 단일 룰 분석
    - analyze_batch_rules: 배치 룰 분석
    - detect_issues: 이슈 감지
    - analyze_conditions: 조건 분석
    - analyze_performance: 성능 분석
    - validate_rule: 룰 검증
    """

    def __init__(self):
        """RuleJsonProcessor 초기화"""
        self.logger = logging.getLogger(__name__)
        self.parser = RuleParser()
        self.analyzer = RuleAnalyzer()

    def process_json_input(self, json_data: Union[str, dict, list]) -> RuleJsonOutput:
        """
        JSON 입력을 처리하여 JSON 출력을 반환

        Why: 외부 시스템은 입력 형태만 맞추면 내부 로직을 몰라도 검증/분석 결과를 일관된 포맷으로 받을 수 있어 통합이 쉬워집니다.
        How: 문자열이면 파싱, dict면 래퍼/배치 여부를 판별하여 단일/배치/래퍼 흐름으로 분기 처리합니다.

        Args:
            json_data: JSON 문자열, 딕셔너리, 또는 룰 배열

        Returns:
            RuleJsonOutput: 처리 결과
        """
        start_time = time.time()

        try:
            # JSON 파싱
            if isinstance(json_data, str):
                input_data = json.loads(json_data)
            else:
                input_data = json_data

            # 입력 형태에 따른 처리
            if isinstance(input_data, list):
                # 기존 시스템과 동일한 형태: 룰 배열 직접
                return self._process_rule_array(input_data, start_time)
            elif isinstance(input_data, dict):
                # 래퍼 형태: module, action 등 포함
                return self._process_wrapper_format(input_data, start_time)
            else:
                return self._create_error_response(
                    "INVALID_INPUT_TYPE",
                    "Input must be a list of rules or a wrapper object",
                    execution_time=0,
                )

        except json.JSONDecodeError as e:
            return self._create_error_response(
                "JSON_PARSE_ERROR", f"Invalid JSON format: {str(e)}", execution_time=0
            )
        except Exception as e:
            execution_time = int((time.time() - start_time) * 1000)
            return self._create_error_response(
                "PROCESSING_ERROR", f"Processing failed: {str(e)}", execution_time
            )

    def _process_rule_array(
        self, rule_array: List[Dict[str, Any]], start_time: float
    ) -> RuleJsonOutput:
        """룰 배열 직접 처리 (기존 시스템과 동일한 형태)

        Why: 레거시 파이프라인의 배치 입력을 그대로 지원해 마이그레이션 비용을 낮춥니다.
        How: RuleParser로 파싱 후 RuleAnalyzer로 각 룰을 분석해 요약 결과를 수집합니다.
        """
        try:
            # 룰들 파싱
            rules = self.parser.parse_multiple(rule_array)

            # 단일 룰인 경우
            if len(rules) == 1:
                rule = rules[0]
                validation_result = self.analyzer.analyze_rule_sync(rule)
                result = validation_result.to_json()
            else:
                # 배치 처리
                results = []
                total_issues = 0
                valid_rules = 0
                invalid_rules = 0

                for rule in rules:
                    try:
                        validation_result = self.analyzer.analyze_rule_sync(rule)
                        results.append(
                            {
                                "ruleUuid": rule.ruleUuid,
                                "ruleName": rule.ruleName,
                                "is_valid": validation_result.is_valid,
                                "summary": validation_result.summary,
                                "issue_count": len(validation_result.issues),
                                "complexity_score": validation_result.complexity_score,
                            }
                        )

                        if validation_result.is_valid:
                            valid_rules += 1
                        else:
                            invalid_rules += 1

                        total_issues += len(validation_result.issues)

                    except Exception as e:
                        self.logger.error(
                            f"Rule analysis failed for {rule.ruleName}: {str(e)}"
                        )
                        results.append(
                            {
                                "ruleUuid": rule.ruleUuid,
                                "ruleName": rule.ruleName,
                                "is_valid": False,
                                "summary": f"Analysis failed: {str(e)}",
                                "issue_count": 0,
                                "complexity_score": 0,
                            }
                        )
                        invalid_rules += 1

                result = {
                    "total_rules": len(rules),
                    "valid_rules": valid_rules,
                    "invalid_rules": invalid_rules,
                    "total_issues": total_issues,
                    "analysis_results": results,
                    "summary": {
                        "total_analysis_time_ms": 0,
                        "average_complexity_score": (
                            sum(r.get("complexity_score", 0) for r in results)
                            / len(results)
                            if results
                            else 0
                        ),
                        "most_common_issues": [],
                    },
                }

            # 실행 시간 계산
            execution_time = int((time.time() - start_time) * 1000)

            # 성공 응답 생성
            return RuleJsonOutput(
                status="success",
                module="raas-rule-analyzer",
                version="1.0.1",
                timestamp=datetime.now().isoformat(),
                result=result,
                metadata={
                    "execution_time_ms": execution_time,
                    "memory_usage_kb": 1024,
                    "analysis_timestamp": datetime.now().isoformat(),
                    "total_analysis_time_ms": execution_time,
                },
            )

        except Exception as e:
            execution_time = int((time.time() - start_time) * 1000)
            return self._create_error_response(
                "PROCESSING_ERROR",
                f"Rule array processing failed: {str(e)}",
                execution_time,
            )

    def _process_wrapper_format(
        self, input_dict: Dict[str, Any], start_time: float
    ) -> RuleJsonOutput:
        """래퍼 형태 처리 (module, action 등 포함)

        Why: API 스타일의 일관된 호출 인터페이스를 제공하기 위함입니다.
        How: Pydantic 모델 검증 후 action에 따라 분기하여 처리합니다.
        """
        try:
            # 입력 검증
            input_model = RuleJsonInput(**input_dict)

            # 액션별 처리
            result = self._process_action(input_model)

            # 실행 시간 계산
            execution_time = int((time.time() - start_time) * 1000)

            # 성공 응답 생성
            return RuleJsonOutput(
                status="success",
                module="raas-rule-analyzer",
                version="1.0.1",
                timestamp=datetime.now().isoformat(),
                result=result,
                metadata={
                    "execution_time_ms": execution_time,
                    "memory_usage_kb": 1024,
                    "analysis_timestamp": datetime.now().isoformat(),
                    "total_analysis_time_ms": execution_time,
                },
            )

        except Exception as e:
            execution_time = int((time.time() - start_time) * 1000)
            return self._create_error_response(
                "PROCESSING_ERROR",
                f"Wrapper format processing failed: {str(e)}",
                execution_time,
            )

    async def process_json_input_async(
        self, json_data: Union[str, dict, list]
    ) -> RuleJsonOutput:
        """
        JSON 입력을 비동기로 처리하여 JSON 출력을 반환

        Why: 고부하 환경에서 I/O 대기 없이 빠르게 처리하기 위함입니다.
        How: 입력 파싱 후 비동기 전용 경로로 분기해 분석을 수행합니다.

        Args:
            json_data: JSON 문자열, 딕셔너리, 또는 룰 배열

        Returns:
            RuleJsonOutput: 처리 결과
        """
        start_time = time.time()

        try:
            # JSON 파싱
            if isinstance(json_data, str):
                input_data = json.loads(json_data)
            else:
                input_data = json_data

            # 입력 형태에 따른 처리
            if isinstance(input_data, list):
                # 기존 시스템과 동일한 형태: 룰 배열 직접
                return await self._process_rule_array_async(input_data, start_time)
            elif isinstance(input_data, dict):
                # 래퍼 형태: module, action 등 포함
                return await self._process_wrapper_format_async(input_data, start_time)
            else:
                return self._create_error_response(
                    "INVALID_INPUT_TYPE",
                    "Input must be a list of rules or a wrapper object",
                    execution_time=0,
                )

        except json.JSONDecodeError as e:
            return self._create_error_response(
                "JSON_PARSE_ERROR", f"Invalid JSON format: {str(e)}", execution_time=0
            )
        except Exception as e:
            execution_time = int((time.time() - start_time) * 1000)
            return self._create_error_response(
                "PROCESSING_ERROR", f"Processing failed: {str(e)}", execution_time
            )

    async def _process_rule_array_async(
        self, rule_array: List[Dict[str, Any]], start_time: float
    ) -> RuleJsonOutput:
        """룰 배열 직접 처리 (비동기, 기존 시스템과 동일한 형태)"""
        try:
            # 룰들 파싱
            rules = self.parser.parse_multiple(rule_array)

            # 단일 룰인 경우
            if len(rules) == 1:
                rule = rules[0]
                validation_result = await self.analyzer.analyze_rule(rule)
                result = validation_result.to_json()
            else:
                # 배치 처리 (비동기)
                results = []
                total_issues = 0
                valid_rules = 0
                invalid_rules = 0

                for rule in rules:
                    try:
                        validation_result = await self.analyzer.analyze_rule(rule)
                        results.append(
                            {
                                "ruleUuid": rule.ruleUuid,
                                "ruleName": rule.ruleName,
                                "is_valid": validation_result.is_valid,
                                "summary": validation_result.summary,
                                "issue_count": len(validation_result.issues),
                                "complexity_score": validation_result.complexity_score,
                            }
                        )

                        if validation_result.is_valid:
                            valid_rules += 1
                        else:
                            invalid_rules += 1

                        total_issues += len(validation_result.issues)

                    except Exception as e:
                        self.logger.error(
                            f"Rule analysis failed for {rule.ruleName}: {str(e)}"
                        )
                        results.append(
                            {
                                "ruleUuid": rule.ruleUuid,
                                "ruleName": rule.ruleName,
                                "is_valid": False,
                                "summary": f"Analysis failed: {str(e)}",
                                "issue_count": 0,
                                "complexity_score": 0,
                            }
                        )
                        invalid_rules += 1

                result = {
                    "total_rules": len(rules),
                    "valid_rules": valid_rules,
                    "invalid_rules": invalid_rules,
                    "total_issues": total_issues,
                    "analysis_results": results,
                    "summary": {
                        "total_analysis_time_ms": 0,
                        "average_complexity_score": (
                            sum(r.get("complexity_score", 0) for r in results)
                            / len(results)
                            if results
                            else 0
                        ),
                        "most_common_issues": [],
                    },
                }

            # 실행 시간 계산
            execution_time = int((time.time() - start_time) * 1000)

            # 성공 응답 생성
            return RuleJsonOutput(
                status="success",
                module="raas-rule-analyzer",
                version="1.0.1",
                timestamp=datetime.now().isoformat(),
                result=result,
                metadata={
                    "execution_time_ms": execution_time,
                    "memory_usage_kb": 1024,
                    "analysis_timestamp": datetime.now().isoformat(),
                    "total_analysis_time_ms": execution_time,
                },
            )

        except Exception as e:
            execution_time = int((time.time() - start_time) * 1000)
            return self._create_error_response(
                "PROCESSING_ERROR",
                f"Rule array processing failed: {str(e)}",
                execution_time,
            )

    async def _process_wrapper_format_async(
        self, input_dict: Dict[str, Any], start_time: float
    ) -> RuleJsonOutput:
        """래퍼 형태 처리 (비동기, module, action 등 포함)"""
        try:
            # 입력 검증
            input_model = RuleJsonInput(**input_dict)

            # 액션별 비동기 처리
            result = await self._process_action_async(input_model)

            # 실행 시간 계산
            execution_time = int((time.time() - start_time) * 1000)

            # 성공 응답 생성
            return RuleJsonOutput(
                status="success",
                module="raas-rule-analyzer",
                version="1.0.1",
                timestamp=datetime.now().isoformat(),
                result=result,
                metadata={
                    "execution_time_ms": execution_time,
                    "memory_usage_kb": 1024,
                    "analysis_timestamp": datetime.now().isoformat(),
                    "total_analysis_time_ms": execution_time,
                },
            )

        except Exception as e:
            execution_time = int((time.time() - start_time) * 1000)
            return self._create_error_response(
                "PROCESSING_ERROR",
                f"Wrapper format processing failed: {str(e)}",
                execution_time,
            )

    def _process_action(self, input_model: RuleJsonInput) -> Dict[str, Any]:
        """액션별 동기 처리"""
        action = input_model.action

        if action == "analyze_rule":
            return self._analyze_single_rule(input_model.data)
        elif action == "analyze_batch_rules":
            return self._analyze_batch_rules(input_model.data)
        elif action == "detect_issues":
            return self._detect_issues(input_model.data)
        elif action == "analyze_conditions":
            return self._analyze_conditions(input_model.data)
        elif action == "analyze_performance":
            return self._analyze_performance(input_model.data)
        elif action == "validate_rule":
            return self._validate_rule(input_model.data)
        else:
            raise ValueError(f"Unsupported action: {action}")

    async def _process_action_async(self, input_model: RuleJsonInput) -> Dict[str, Any]:
        """액션별 비동기 처리"""
        action = input_model.action

        if action == "analyze_rule":
            return await self._analyze_single_rule_async(input_model.data)
        elif action == "analyze_batch_rules":
            return await self._analyze_batch_rules_async(input_model.data)
        elif action == "detect_issues":
            return await self._detect_issues_async(input_model.data)
        elif action == "analyze_conditions":
            return self._analyze_conditions(input_model.data)
        elif action == "analyze_performance":
            return self._analyze_performance(input_model.data)
        elif action == "validate_rule":
            return await self._validate_rule_async(input_model.data)
        else:
            raise ValueError(f"Unsupported action: {action}")

    def _analyze_single_rule(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """단일 룰 분석

        Why: 단일 룰을 빠르게 검증/분석하여 품질 문제를 조기에 파악합니다.
        How: RuleParser→RuleAnalyzer 순으로 호출하여 ValidationResult를 JSON으로 변환합니다.
        """
        try:
            rule_data = data.get("rule", {})

            # 룰 파싱
            rule = self.parser.parse(rule_data)

            # 분석 수행
            validation_result = self.analyzer.analyze_rule_sync(rule)

            # 순수 로직 JSON 형식으로 변환
            result = validation_result.to_json()

            # 추가 메타데이터
            result["analysis_timestamp"] = datetime.now().isoformat()
            result["rule_info"] = {
                "ruleUuid": rule.ruleUuid,
                "ruleName": rule.ruleName,
                "ruleMsg": rule.ruleMsg,
            }

            return result

        except Exception as e:
            self.logger.error(f"단일 룰 분석 실패: {str(e)}")
            return {
                "error": str(e),
                "status": "failed",
                "analysis_timestamp": datetime.now().isoformat(),
            }

    async def _analyze_single_rule_async(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """단일 룰 분석 (비동기)

        Why: 대량의 단건 요청을 비동기로 처리해 처리량을 높입니다.
        How: 비동기 RuleAnalyzer를 호출해 ValidationResult를 생성합니다.
        """
        try:
            rule_data = data.get("rule", {})

            # 룰 파싱
            rule = self.parser.parse(rule_data)

            # 분석 수행
            validation_result = await self.analyzer.analyze_rule(rule)

            # 순수 로직 JSON 형식으로 변환
            result = validation_result.to_json()

            # 추가 메타데이터
            result["analysis_timestamp"] = datetime.now().isoformat()
            result["rule_info"] = {
                "ruleUuid": rule.ruleUuid,
                "ruleName": rule.ruleName,
                "ruleMsg": rule.ruleMsg,
            }

            return result

        except Exception as e:
            self.logger.error(f"단일 룰 분석 실패: {str(e)}")
            return {
                "error": str(e),
                "status": "failed",
                "analysis_timestamp": datetime.now().isoformat(),
            }

    def _analyze_batch_rules(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """
        배치 룰 분석

        Why: 여러 룰을 한 번에 분석하여 처리 효율성을 높이고 일관된 결과를 제공하기 위함입니다.
        How: RuleParser로 모든 룰을 파싱한 후 RuleAnalyzer로 순차 분석하여 요약 통계를 생성합니다.
        """
        rules_data = data.get("rules", [])
        options = data.get("analysis_options", {})

        # 룰들 파싱
        rules = self.parser.parse_multiple(rules_data)

        # 분석 옵션 설정
        analysis_options = AnalysisOptions(**options)

        # 배치 분석
        results = []
        total_issues = 0
        valid_rules = 0
        invalid_rules = 0

        for rule in rules:
            try:
                validation_result = self.analyzer.analyze_rule_sync(
                    rule, analysis_options.include_ai_analysis
                )
                results.append(
                    {
                        "ruleUuid": rule.ruleUuid,
                        "ruleName": rule.ruleName,
                        "is_valid": validation_result.is_valid,
                        "summary": validation_result.summary,
                        "issue_count": len(validation_result.issues),
                        "complexity_score": validation_result.complexity_score,
                    }
                )

                if validation_result.is_valid:
                    valid_rules += 1
                else:
                    invalid_rules += 1

                total_issues += len(validation_result.issues)

            except Exception as e:
                self.logger.error(f"Rule analysis failed for {rule.ruleName}: {str(e)}")
                results.append(
                    {
                        "ruleUuid": rule.ruleUuid,
                        "ruleName": rule.ruleName,
                        "is_valid": False,
                        "summary": f"Analysis failed: {str(e)}",
                        "issue_count": 0,
                        "complexity_score": 0,
                    }
                )
                invalid_rules += 1

        return {
            "total_rules": len(rules),
            "valid_rules": valid_rules,
            "invalid_rules": invalid_rules,
            "total_issues": total_issues,
            "analysis_results": results,
            "summary": {
                "total_analysis_time_ms": 0,  # 실제로는 계산 필요
                "average_complexity_score": (
                    sum(r.get("complexity_score", 0) for r in results) / len(results)
                    if results
                    else 0
                ),
                "most_common_issues": [],  # 실제로는 계산 필요
            },
        }

    async def _analyze_batch_rules_async(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """
        배치 룰 분석 (비동기)

        Why: 대량의 룰을 비동기로 처리하여 처리량을 극대화하고 응답 대기 시간을 줄이기 위함입니다.
        How: 비동기 RuleAnalyzer를 사용하여 각 룰을 순차/동시로 처리하고 결과를 수집합니다.
        """
        rules_data = data.get("rules", [])
        options = data.get("analysis_options", {})

        # 룰들 파싱
        rules = self.parser.parse_multiple(rules_data)

        # 분석 옵션 설정
        analysis_options = AnalysisOptions(**options)

        # 배치 분석 (비동기)
        results = []
        total_issues = 0
        valid_rules = 0
        invalid_rules = 0

        for rule in rules:
            try:
                validation_result = await self.analyzer.analyze_rule(
                    rule, analysis_options.include_ai_analysis
                )
                results.append(
                    {
                        "ruleUuid": rule.ruleUuid,
                        "ruleName": rule.ruleName,
                        "is_valid": validation_result.is_valid,
                        "summary": validation_result.summary,
                        "issue_count": len(validation_result.issues),
                        "complexity_score": validation_result.complexity_score,
                    }
                )

                if validation_result.is_valid:
                    valid_rules += 1
                else:
                    invalid_rules += 1

                total_issues += len(validation_result.issues)

            except Exception as e:
                self.logger.error(f"Rule analysis failed for {rule.ruleName}: {str(e)}")
                results.append(
                    {
                        "ruleUuid": rule.ruleUuid,
                        "ruleName": rule.ruleName,
                        "is_valid": False,
                        "summary": f"Analysis failed: {str(e)}",
                        "issue_count": 0,
                        "complexity_score": 0,
                    }
                )
                invalid_rules += 1

        return {
            "total_rules": len(rules),
            "valid_rules": valid_rules,
            "invalid_rules": invalid_rules,
            "total_issues": total_issues,
            "analysis_results": results,
            "summary": {
                "total_analysis_time_ms": 0,  # 실제로는 계산 필요
                "average_complexity_score": (
                    sum(r.get("complexity_score", 0) for r in results) / len(results)
                    if results
                    else 0
                ),
                "most_common_issues": [],  # 실제로는 계산 필요
            },
        }

    def _detect_issues(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """
        이슈 감지

        Why: 룰의 품질 문제를 체계적으로 검출하여 개선 포인트를 제시하고 운영 리스크를 줄이기 위함입니다.
        How: RuleParser로 룰을 파싱한 후 IssueDetector를 사용하여 모든 이슈 타입을 검출하고 통계를 생성합니다.
        """
        rule_data = data.get("rule", {})

        # 룰 파싱
        rule = self.parser.parse(rule_data)

        # 이슈 감지
        issues = self.analyzer.issue_detector.detect_all_issues_sync(rule)

        return {
            "issues": [issue.to_json() for issue in issues],
            "total_issues": len(issues),
            "issue_counts": self._count_issues_by_type(issues),
        }

    async def _detect_issues_async(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """
        이슈 감지 (비동기)

        Why: 고부하 환경에서 이슈 검출을 비동기로 처리하여 처리량을 높이고 응답성을 개선하기 위함입니다.
        How: 비동기 IssueDetector를 사용하여 이슈 검출을 수행하고 결과를 JSON 형태로 반환합니다.
        """
        rule_data = data.get("rule", {})

        # 룰 파싱
        rule = self.parser.parse(rule_data)

        # 이슈 감지 (비동기)
        issues = await self.analyzer.issue_detector.detect_all_issues(rule)

        return {
            "issues": [issue.to_json() for issue in issues],
            "total_issues": len(issues),
            "issue_counts": self._count_issues_by_type(issues),
        }

    def _analyze_conditions(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """
        조건 분석

        Why: 룰의 조건 구조와 필드 타입을 상세히 분석하여 복잡성과 품질을 파악하기 위함입니다.
        How: ConditionAnalyzer를 사용하여 조건 파싱, 필드 타입 추론, 구조 메트릭 계산을 수행합니다.
        """
        rule_data = data.get("rule", {})

        # 룰 파싱
        rule = self.parser.parse(rule_data)

        # 조건 분석
        conditions = self.analyzer.condition_analyzer.parse_rule_conditions(rule)
        field_types = self.analyzer.condition_analyzer.infer_field_types(
            rule, conditions
        )
        structure_metrics = (
            self.analyzer.condition_analyzer.calculate_structure_metrics(
                conditions, rule
            )
        )

        return {
            "conditions": [cond.dict() for cond in conditions],
            "field_types": field_types,
            "structure_metrics": structure_metrics,
        }

    def _analyze_performance(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """
        성능 분석

        Why: 룰 실행 시 예상되는 성능 특성을 분석하여 운영 환경에서의 안정성과 최적화 방향을 제시하기 위함입니다.
        How: MetricsGenerator를 사용하여 조건 분석, 구조 메트릭 계산, 성능 메트릭 생성을 수행합니다.
        """
        rule_data = data.get("rule", {})

        # 룰 파싱
        rule = self.parser.parse(rule_data)

        # 성능 분석
        conditions = self.analyzer.condition_analyzer.parse_rule_conditions(rule)
        structure_metrics = (
            self.analyzer.condition_analyzer.calculate_structure_metrics(
                conditions, rule
            )
        )
        performance_metrics = (
            self.analyzer.metrics_generator.generate_performance_metrics(
                rule, conditions, structure_metrics
            )
        )

        return performance_metrics.dict()

    def _validate_rule(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """
        룰 검증

        Why: 룰의 기본적인 유효성을 빠르게 검증하여 잘못된 룰을 초기에 걸러내기 위함입니다.
        How: RuleParser로 파싱 후 RuleAnalyzer를 사용하여 기본 검증을 수행하고 결과를 JSON으로 반환합니다.
        """
        rule_data = data.get("rule", {})

        # 룰 파싱
        rule = self.parser.parse(rule_data)

        # 룰 검증
        validation_result = self.analyzer.analyze_rule_sync(
            rule, include_ai_analysis=False
        )

        return validation_result.to_json()

    async def _validate_rule_async(self, data: Dict[str, Any]) -> Dict[str, Any]:
        """
        룰 검증 (비동기)

        Why: 고부하 환경에서 룰 검증을 비동기로 처리하여 처리량을 높이고 응답성을 개선하기 위함입니다.
        How: 비동기 RuleAnalyzer를 사용하여 룰 검증을 수행하고 결과를 JSON으로 반환합니다.
        """
        rule_data = data.get("rule", {})

        # 룰 파싱
        rule = self.parser.parse(rule_data)

        # 룰 검증 (비동기)
        validation_result = await self.analyzer.analyze_rule(
            rule, include_ai_analysis=False
        )

        return validation_result.to_json()

    def _count_issues_by_type(self, issues: List) -> Dict[str, int]:
        """이슈 타입별 개수 계산"""
        issue_counts = {}
        for issue in issues:
            issue_type = getattr(issue, "issue_type", "unknown")
            issue_counts[issue_type] = issue_counts.get(issue_type, 0) + 1
        return issue_counts

    def _create_error_response(
        self, error_code: str, error_message: str, execution_time: int = 0
    ) -> RuleJsonOutput:
        """에러 응답 생성"""
        return RuleJsonOutput(
            status="error",
            module="raas-rule-analyzer",
            version="1.0.1",
            timestamp=datetime.now().isoformat(),
            error={"code": error_code, "message": error_message, "details": {}},
            metadata={"execution_time_ms": execution_time},
        )
