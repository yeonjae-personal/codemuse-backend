"""
스트리밍 생성기

ValidationResult를 스트리밍 청크로 변환합니다.
"""

import asyncio
import logging
from typing import AsyncGenerator, Optional

from .stream_models import ChunkType, StreamingChunk, StreamingOptions
from .utils.progress_tracker import ProgressTracker


# 상대 import를 절대 import로 변경
try:
    from ..formatters import FormattingOptions, TextFormatter
except ImportError:
    # 상대 import가 실패할 경우 절대 import 시도
    try:
        from formatters import FormattingOptions, TextFormatter
    except ImportError:
        # 모든 import가 실패할 경우 더미 클래스 사용
        class TextFormatter:
            def __init__(self, options=None):
                pass

            def format(self, result):
                return str(result)

        class FormattingOptions:
            def __init__(self):
                pass


class StreamGenerator:
    """
    스트리밍 생성기

    ValidationResult를 스트리밍 청크로 변환하여 전송합니다.
    """

    def __init__(self, options: Optional[StreamingOptions] = None):
        """
        스트리밍 생성기 초기화

        Args:
            options: 스트리밍 옵션
        """
        self.logger = logging.getLogger(__name__)
        self.options = options or StreamingOptions()
        self.progress_tracker = ProgressTracker()

    async def generate_stream(
        self, validation_result, chunk_delay: float = 0.1
    ) -> AsyncGenerator[StreamingChunk, None]:
        """
        분석 결과를 스트리밍 청크로 생성

        Args:
            validation_result: ValidationResult 객체
            chunk_delay: 청크 간 지연 시간 (초)

        Yields:
            StreamingChunk 객체
        """
        try:
            self.logger.info("스트리밍 데이터 생성 시작")

            # 1. 헤더 청크
            header_chunk = self._create_header_chunk(validation_result)
            yield header_chunk
            await asyncio.sleep(chunk_delay)

            # 2. 기본 정보 청크
            basic_chunk = self._create_basic_info_chunk(validation_result)
            yield basic_chunk
            await asyncio.sleep(chunk_delay)

            # 3. 이슈 정보 청크들
            if hasattr(validation_result, 'issues') and validation_result.issues:
                for i, issue in enumerate(validation_result.issues):
                    issue_chunk = self._create_issue_chunk(issue, i)
                    yield issue_chunk
                    await asyncio.sleep(chunk_delay)

            # 4. 구조 정보 청크
            if hasattr(validation_result, 'structure'):
                structure_chunk = self._create_structure_chunk(
                    validation_result.structure
                )
                yield structure_chunk
                await asyncio.sleep(chunk_delay)

            # 5. 성능 메트릭 청크
            if hasattr(validation_result, 'performance_metrics'):
                performance_chunk = self._create_performance_chunk(
                    validation_result.performance_metrics
                )
                yield performance_chunk
                await asyncio.sleep(chunk_delay)

            # 6. 품질 메트릭 청크
            if hasattr(validation_result, 'quality_metrics'):
                quality_chunk = self._create_quality_chunk(
                    validation_result.quality_metrics
                )
                yield quality_chunk
                await asyncio.sleep(chunk_delay)

            # 7. 완료 청크
            completion_chunk = self._create_completion_chunk(validation_result)
            yield completion_chunk

            self.logger.info("스트리밍 데이터 생성 완료")

        except Exception as e:
            self.logger.error(f"스트리밍 생성 중 오류: {str(e)}")
            error_chunk = self._create_error_chunk(str(e))
            yield error_chunk

    def _create_header_chunk(self, validation_result) -> StreamingChunk:
        """헤더 청크 생성"""
        # rule_summary가 없는 경우 summary 또는 기본값 사용
        summary = getattr(validation_result, 'rule_summary', None)
        if not summary:
            summary = getattr(validation_result, 'summary', '분석 완료')

        content = f"룰 분석 결과: {summary}"

        return StreamingChunk(
            type=ChunkType.ANALYSIS_TEXT,
            content=content,
            timestamp=self._get_timestamp(),
            progress=0.0,
            total_chunks=self._get_total_items(validation_result),
            metadata={
                "rule_id": getattr(validation_result, 'rule_id', 'unknown'),
                "analysis_version": getattr(
                    validation_result.report_metadata, 'analysis_version', '1.0.0'
                ),
            },
        )

    def _create_basic_info_chunk(self, validation_result) -> StreamingChunk:
        """기본 정보 청크 생성"""
        content = f"""
📊 기본 분석 정보
================
✅ 유효성: {'유효함' if validation_result.is_valid else '유효하지 않음'}
📝 요약: {validation_result.summary or '요약 없음'}
🚨 총 이슈: {validation_result.get_total_issues()}개
⚠️ 치명적 이슈: {len(validation_result.get_critical_issues())}개
📈 복잡도 점수: {validation_result.complexity_score}/100
"""

        return StreamingChunk(
            type=ChunkType.ANALYSIS_TEXT,
            content=content,
            timestamp=self._get_timestamp(),
            progress=0.1,
            total_chunks=self._get_total_items(validation_result),
            metadata={
                "is_valid": validation_result.is_valid,
                "total_issues": validation_result.get_total_issues(),
                "critical_issues": len(validation_result.get_critical_issues()),
            },
        )

    def _create_issue_chunk(self, issue, index: int) -> StreamingChunk:
        """이슈 정보 청크 생성"""
        # detected_at가 문자열인 경우 그대로 사용, 객체인 경우 isoformat() 호출
        detected_at = getattr(issue, 'detected_at', 'N/A')
        if hasattr(detected_at, 'isoformat'):
            detected_at = detected_at.isoformat()

        content = f"""
🚨 이슈 {index + 1}: {getattr(issue, 'issue_type', 'unknown')}
   심각도: {getattr(issue, 'severity', 'unknown')}
   설명: {getattr(issue, 'explanation', '설명 없음')}
   제안: {getattr(issue, 'suggestion', '제안사항 없음')}
   발견 시간: {detected_at}
"""

        return StreamingChunk(
            type=ChunkType.ANALYSIS_TEXT,
            content=content,
            timestamp=detected_at,
            progress=0.2 + (index * 0.1),
            total_chunks=self._get_total_items(None),  # 이슈 개수는 별도로 계산
            metadata={
                "issue_type": getattr(issue, 'issue_type', 'unknown'),
                "severity": getattr(issue, 'severity', 'unknown'),
                "index": index,
            },
        )

    def _create_structure_chunk(self, structure) -> StreamingChunk:
        """구조 정보 청크 생성"""
        content = f"""
🏗️ 구조 분석
============
   최대 깊이: {getattr(structure, 'depth', 'N/A')}
   조건 개수: {getattr(structure, 'condition_count', 'N/A')}
   액션 개수: {getattr(structure, 'action_count', 'N/A')}
   복잡도 점수: {getattr(structure, 'complexity_score', 'N/A')}/100
   패턴: {', '.join(getattr(structure, 'detected_patterns', [])) if getattr(structure, 'detected_patterns', []) else '없음'}
"""

        return StreamingChunk(
            type=ChunkType.ANALYSIS_TEXT,
            content=content,
            timestamp=self._get_timestamp(),
            progress=0.7,
            total_chunks=self._get_total_items(None),
            metadata={
                "depth": getattr(structure, 'depth', 'N/A'),
                "complexity_score": getattr(structure, 'complexity_score', 'N/A'),
                "pattern_count": len(getattr(structure, 'detected_patterns', [])),
            },
        )

    def _create_performance_chunk(self, performance) -> StreamingChunk:
        """성능 메트릭 청크 생성"""
        # estimated_execution_time_ms가 숫자인지 확인
        execution_time = getattr(performance, 'estimated_execution_time_ms', 'N/A')
        if execution_time != 'N/A' and execution_time is not None:
            execution_time_str = f"{execution_time:.2f}ms"
        else:
            execution_time_str = "N/A"

        content = f"""
⚡ 성능 메트릭
==============
   성능 점수: {getattr(performance, 'performance_score', 'N/A')}/100
   예상 실행 시간: {execution_time_str}
   복잡도 등급: {getattr(performance, 'complexity_rating', 'N/A')}
   최적화 제안: {len(getattr(performance, 'optimization_suggestions', []))}개
"""

        return StreamingChunk(
            type=ChunkType.ANALYSIS_TEXT,
            content=content,
            timestamp=self._get_timestamp(),
            progress=0.8,
            total_chunks=self._get_total_items(None),
            metadata={
                "performance_score": getattr(performance, 'performance_score', 'N/A'),
                "execution_time": getattr(
                    performance, 'estimated_execution_time_ms', 'N/A'
                ),
                "optimization_count": len(
                    getattr(performance, 'optimization_suggestions', [])
                ),
            },
        )

    def _create_quality_chunk(self, quality) -> StreamingChunk:
        """품질 메트릭 청크 생성"""
        content = f"""
🎯 품질 메트릭
==============
   전체 점수: {getattr(quality, 'overall_score', 'N/A')}/100
   품질 등급: {getattr(quality, 'quality_grade', 'N/A')}
   유지보수성: {getattr(quality, 'maintainability_score', 'N/A')}/100
   가독성: {getattr(quality, 'readability_score', 'N/A')}/100
   완성도: {getattr(quality, 'completeness_score', 'N/A')}/100
   일관성: {getattr(quality, 'consistency_score', 'N/A')}/100
"""

        return StreamingChunk(
            type=ChunkType.ANALYSIS_TEXT,
            content=content,
            timestamp=self._get_timestamp(),
            progress=0.9,
            total_chunks=self._get_total_items(None),
            metadata={
                "overall_score": getattr(quality, 'overall_score', 'N/A'),
                "quality_grade": getattr(quality, 'quality_grade', 'N/A'),
                "maintainability": getattr(quality, 'maintainability_score', 'N/A'),
            },
        )

    def _create_completion_chunk(self, validation_result) -> StreamingChunk:
        """완료 청크 생성"""
        timestamp = getattr(
            validation_result.report_metadata, 'analysis_timestamp', 'N/A'
        )
        generation_time = getattr(
            validation_result.report_metadata, 'report_generation_time', 'N/A'
        )
        generated_by = getattr(
            validation_result.report_metadata, 'report_generated_by', 'N/A'
        )
        environment = getattr(validation_result.report_metadata, 'environment', 'N/A')

        content = f"""
✅ 분석 완료!
============
   총 소요 시간: {generation_time}초
   생성자: {generated_by}
   환경: {environment}
   완료 시간: {timestamp}
"""

        return StreamingChunk(
            type=ChunkType.ANALYSIS_COMPLETE,
            content=content,
            timestamp=timestamp,
            progress=1.0,
            total_chunks=self._get_total_items(validation_result),
            metadata={
                "generation_time": generation_time,
                "generated_by": generated_by,
                "status": "completed",
            },
        )

    def _create_error_chunk(self, error_message: str) -> StreamingChunk:
        """에러 청크 생성"""
        return StreamingChunk(
            type=ChunkType.ANALYSIS_ERROR,
            content=f"❌ 분석 중 오류 발생: {error_message}",
            timestamp=self._get_timestamp(),
            progress=1.0,
            total_chunks=1,
            metadata={"error": True, "error_message": error_message, "status": "error"},
        )

    def _get_timestamp(self) -> str:
        """현재 타임스탬프 반환"""
        from datetime import datetime

        return datetime.now().isoformat()

    def _get_total_items(self, validation_result) -> int:
        """총 아이템 개수 반환"""
        if not validation_result:
            return 1

        base_count = 5  # 헤더, 기본정보, 구조, 성능, 품질, 완료

        if hasattr(validation_result, 'issues') and validation_result.issues:
            base_count += len(validation_result.issues)

        return base_count
