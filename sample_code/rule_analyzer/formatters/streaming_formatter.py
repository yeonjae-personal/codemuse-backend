"""
스트리밍 포맷터

스트리밍 전송을 위한 포맷터입니다.
"""

import logging
from typing import Any, Dict, List, Optional

from .options.formatting_options import FormattingOptions


class StreamingFormatter:
    """
    스트리밍 포맷터

    분석 결과를 스트리밍 전송에 적합한 형태로 포맷팅합니다.
    """

    def __init__(self, options: Optional[FormattingOptions] = None):
        """
        스트리밍 포맷터 초기화

        Args:
            options: 포맷팅 옵션
        """
        self.logger = logging.getLogger(__name__)
        self.options = options or FormattingOptions()

    def format_for_streaming(self, validation_result) -> List[Dict[str, Any]]:
        """
        분석 결과를 스트리밍용 청크들로 포맷팅

        Args:
            validation_result: ValidationResult 객체

        Returns:
            스트리밍 청크 리스트
        """
        try:
            chunks = []

            # 1. 헤더 청크
            chunks.append(self._create_header_chunk(validation_result))

            # 2. 기본 정보 청크
            chunks.append(self._create_basic_info_chunk(validation_result))

            # 3. 이슈 정보 청크들
            if hasattr(validation_result, 'issues') and validation_result.issues:
                chunks.extend(self._create_issues_chunks(validation_result.issues))

            # 4. 구조 정보 청크
            if hasattr(validation_result, 'structure'):
                chunks.append(self._create_structure_chunk(validation_result.structure))

            # 5. 성능 메트릭 청크
            if hasattr(validation_result, 'performance_metrics'):
                chunks.append(
                    self._create_performance_chunk(
                        validation_result.performance_metrics
                    )
                )

            # 6. 품질 메트릭 청크
            if hasattr(validation_result, 'quality_metrics'):
                chunks.append(
                    self._create_quality_chunk(validation_result.quality_metrics)
                )

            # 7. 완료 청크
            chunks.append(self._create_completion_chunk(validation_result))

            self.logger.debug(f"스트리밍 포맷팅 완료: {len(chunks)}개 청크")
            return chunks

        except Exception as e:
            self.logger.error(f"스트리밍 포맷팅 오류: {str(e)}")
            return [self._create_error_chunk(str(e))]

    def _create_header_chunk(self, validation_result) -> Dict[str, Any]:
        """헤더 청크 생성"""
        return {
            "type": "header",
            "content": f"📊 룰 분석 결과 - {validation_result.report_metadata.analysis_timestamp}",
            "timestamp": validation_result.report_metadata.analysis_timestamp,
            "progress": 0.0,
            "metadata": {
                "rule_id": getattr(validation_result, 'rule_id', 'unknown'),
                "analysis_version": str(
                    validation_result.report_metadata.analysis_version
                ),
            },
        }

    def _create_basic_info_chunk(self, validation_result) -> Dict[str, Any]:
        """기본 정보 청크 생성"""
        return {
            "type": "basic_info",
            "content": f"""
✅ 유효성: {'유효함' if validation_result.is_valid else '유효하지 않음'}
📝 요약: {validation_result.summary or '요약 없음'}
🚨 총 이슈: {validation_result.get_total_issues()}개
⚠️ 치명적 이슈: {len(validation_result.get_critical_issues()) if hasattr(validation_result.get_critical_issues(), '__len__') else 0}개
📈 복잡도 점수: {str(validation_result.complexity_score)}/100
""",
            "timestamp": validation_result.report_metadata.analysis_timestamp,
            "progress": 0.1,
            "metadata": {
                "is_valid": validation_result.is_valid,
                "total_issues": validation_result.get_total_issues(),
                "critical_issues": (
                    len(validation_result.get_critical_issues())
                    if hasattr(validation_result.get_critical_issues(), '__len__')
                    else 0
                ),
            },
        }

    def _create_issues_chunks(self, issues) -> List[Dict[str, Any]]:
        """이슈 정보 청크들 생성"""
        chunks = []

        for i, issue in enumerate(issues):
            chunk = {
                "type": "issue",
                "content": f"""
🚨 이슈 {i+1}: {issue.issue_type}
   심각도: {issue.severity}
   설명: {issue.explanation}
   제안: {issue.suggestion or '제안사항 없음'}
""",
                "timestamp": (
                    issue.detected_at.isoformat()
                    if hasattr(issue.detected_at, 'isoformat')
                    else str(issue.detected_at)
                ),
                "progress": 0.2 + (i * 0.1),
                "metadata": {
                    "issue_type": issue.issue_type,
                    "severity": issue.severity,
                    "index": i,
                },
            }
            chunks.append(chunk)

        return chunks

    def _create_structure_chunk(self, structure) -> Dict[str, Any]:
        """구조 정보 청크 생성"""
        return {
            "type": "structure",
            "content": f"""
🏗️ 구조 분석
   최대 깊이: {str(structure.depth)}
   조건 개수: {str(structure.condition_count)}
   액션 개수: {str(structure.action_count)}
   복잡도 점수: {str(structure.complexity_score)}/100
   패턴: {', '.join(structure.detected_patterns) if structure.detected_patterns and hasattr(structure.detected_patterns, '__iter__') else '없음'}
""",
            "timestamp": "now",
            "progress": 0.7,
            "metadata": {
                "depth": structure.depth,
                "complexity_score": structure.complexity_score,
                "pattern_count": (
                    len(structure.detected_patterns)
                    if hasattr(structure.detected_patterns, '__len__')
                    else 0
                ),
            },
        }

    def _create_performance_chunk(self, performance) -> Dict[str, Any]:
        """성능 메트릭 청크 생성"""
        return {
            "type": "performance",
            "content": f"""
⚡ 성능 메트릭
   성능 점수: {str(performance.performance_score)}/100
   예상 실행 시간: {str(performance.estimated_execution_time_ms)}ms
   복잡도 등급: {str(performance.complexity_rating)}
   최적화 제안: {len(performance.optimization_suggestions) if hasattr(performance.optimization_suggestions, '__len__') else 0}개
""",
            "timestamp": "now",
            "progress": 0.8,
            "metadata": {
                "performance_score": performance.performance_score,
                "execution_time": performance.estimated_execution_time_ms,
                "optimization_count": (
                    len(performance.optimization_suggestions)
                    if hasattr(performance.optimization_suggestions, '__len__')
                    else 0
                ),
            },
        }

    def _create_quality_chunk(self, quality) -> Dict[str, Any]:
        """품질 메트릭 청크 생성"""
        return {
            "type": "quality",
            "content": f"""
🎯 품질 메트릭
   전체 점수: {str(quality.overall_score)}/100
   품질 등급: {str(quality.quality_grade)}
   유지보수성: {str(quality.maintainability_score)}/100
   가독성: {str(quality.readability_score)}/100
   완성도: {str(quality.completeness_score)}/100
   일관성: {str(quality.consistency_score)}/100
""",
            "timestamp": "now",
            "progress": 0.9,
            "metadata": {
                "overall_score": quality.overall_score,
                "quality_grade": quality.quality_grade,
                "maintainability": quality.maintainability_score,
            },
        }

    def _create_completion_chunk(self, validation_result) -> Dict[str, Any]:
        """완료 청크 생성"""
        return {
            "type": "completion",
            "content": f"""
✅ 분석 완료!
   총 소요 시간: {validation_result.report_metadata.report_generation_time:.2f}초
   생성자: {validation_result.report_metadata.report_generated_by}
   환경: {validation_result.report_metadata.environment}
""",
            "timestamp": validation_result.report_metadata.analysis_timestamp,
            "progress": 1.0,
            "metadata": {
                "generation_time": validation_result.report_metadata.report_generation_time,
                "generated_by": validation_result.report_metadata.report_generated_by,
                "status": "completed",
            },
        }

    def _create_error_chunk(self, error_message: str) -> Dict[str, Any]:
        """에러 청크 생성"""
        return {
            "type": "error",
            "content": f"❌ 오류 발생: {error_message}",
            "timestamp": "now",
            "progress": 1.0,
            "metadata": {
                "error": True,
                "error_message": error_message,
                "status": "error",
            },
        }

    def get_chunk_count(self, validation_result) -> int:
        """
        예상 청크 개수 반환

        Args:
            validation_result: ValidationResult 객체

        Returns:
            예상 청크 개수
        """
        base_count = 2  # 헤더, 기본정보, 완료 (항상 생성되는 청크들)

        # 이슈 청크들
        if hasattr(validation_result, 'issues') and validation_result.issues:
            base_count += 1  # 이슈가 있으면 1개 청크 추가
            self.logger.debug(f"이슈 청크 추가: {validation_result.issues}")
        else:
            self.logger.debug(
                f"이슈 청크 추가 안됨: hasattr={hasattr(validation_result, 'issues')}, issues={validation_result.issues}"
            )

        # 구조 정보 청크
        if hasattr(validation_result, 'structure'):
            base_count += 1

        # 성능 메트릭 청크
        if hasattr(validation_result, 'performance_metrics'):
            base_count += 1

        # 품질 메트릭 청크
        if hasattr(validation_result, 'quality_metrics'):
            base_count += 1

        self.logger.debug(
            f"청크 개수 계산: 기본={2}, 이슈={1 if hasattr(validation_result, 'issues') and validation_result.issues else 0}, 구조={1 if hasattr(validation_result, 'structure') else 0}, 성능={1 if hasattr(validation_result, 'performance_metrics') else 0}, 품질={1 if hasattr(validation_result, 'quality_metrics') else 0}, 총계={base_count}"
        )

        return base_count
