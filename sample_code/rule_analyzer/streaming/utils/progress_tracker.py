"""
스트리밍 진행률 추적 유틸리티

스트리밍 진행 상황을 추적하고 모니터링하는 기능을 제공합니다.
"""

import logging
# time 모듈은 현재 사용되지 않지만 향후 확장을 위해 유지 (v1.4.1)
from datetime import datetime, timedelta
from typing import Any, Dict, List, Optional

from ..stream_models import StreamingChunk


class ProgressTracker:
    """
    스트리밍 진행률 추적 유틸리티

    스트리밍 진행 상황을 실시간으로 추적하고 통계를 제공합니다.
    """

    def __init__(self):
        """진행률 추적기 초기화"""
        self.logger = logging.getLogger(__name__)

        # 진행률 정보
        self.start_time: Optional[datetime] = None
        self.current_progress: float = 0.0
        self.total_chunks: int = 0
        self.processed_chunks: int = 0

        # 성능 통계
        self.chunk_processing_times: List[float] = []
        self.last_chunk_time: Optional[datetime] = None

        # 예상 완료 시간
        self.estimated_completion_time: Optional[datetime] = None

        # 진행률 히스토리
        self.progress_history: List[Dict[str, Any]] = []

        # 상태 정보
        self.is_started: bool = False
        self.is_completed: bool = False
        self.is_error: bool = False
        self.error_message: Optional[str] = None

    def start_tracking(self, total_chunks: int = 0) -> None:
        """
        진행률 추적 시작

        Args:
            total_chunks: 전체 청크 수 (0이면 자동 계산)
        """
        self.start_time = datetime.now()
        self.total_chunks = total_chunks
        self.current_progress = 0.0
        self.processed_chunks = 0
        self.is_started = True
        self.is_completed = False
        self.is_error = False

        # 진행률 히스토리 초기화
        self.progress_history = [
            {
                "timestamp": self.start_time.isoformat(),
                "progress": 0.0,
                "processed_chunks": 0,
                "total_chunks": total_chunks,
            }
        ]

        self.logger.info(f"진행률 추적 시작: 총 {total_chunks}개 청크")

    def update_progress(
        self, chunk: StreamingChunk, auto_calculate_total: bool = False
    ) -> None:
        """
        진행률 업데이트

        Args:
            chunk: 처리된 청크
            auto_calculate_total: 전체 청크 수 자동 계산 여부
        """
        if not self.is_started:
            self.logger.warning("진행률 추적이 시작되지 않았습니다.")
            return

        # 청크 처리 시간 기록
        current_time = datetime.now()
        if self.last_chunk_time:
            processing_time = (current_time - self.last_chunk_time).total_seconds()
            self.chunk_processing_times.append(processing_time)

        self.last_chunk_time = current_time

        # 진행률 계산
        if chunk.chunk_index is not None:
            self.processed_chunks = chunk.chunk_index + 1

        if chunk.total_chunks and chunk.total_chunks > 0:
            self.total_chunks = chunk.total_chunks
            self.current_progress = chunk.progress
        elif self.total_chunks > 0:
            self.current_progress = self.processed_chunks / self.total_chunks
        else:
            # 전체 청크 수를 모르는 경우 진행률을 청크의 progress 값으로 사용
            self.current_progress = chunk.progress

        # 진행률 히스토리 업데이트
        self.progress_history.append(
            {
                "timestamp": current_time.isoformat(),
                "progress": self.current_progress,
                "processed_chunks": self.processed_chunks,
                "total_chunks": self.total_chunks,
                "chunk_type": chunk.type.value,
                "chunk_content_length": len(chunk.content),
            }
        )

        # 완료 여부 확인
        if chunk.is_complete():
            self.is_completed = True
            self._calculate_final_statistics()

        # 에러 여부 확인
        if chunk.is_error():
            self.is_error = True
            self.error_message = chunk.error_message

        # 예상 완료 시간 업데이트
        self._update_estimated_completion_time()

        self.logger.debug(
            f"진행률 업데이트: {self.get_progress_percentage()}% ({self.processed_chunks}/{self.total_chunks})"
        )

    def get_current_progress(self) -> float:
        """
        현재 진행률 반환

        Returns:
            진행률 (0.0 ~ 1.0)
        """
        return self.current_progress

    def get_progress_percentage(self) -> int:
        """
        현재 진행률을 퍼센트로 반환

        Returns:
            진행률 퍼센트 (0 ~ 100)
        """
        return int(self.current_progress * 100)

    def get_processed_chunks(self) -> int:
        """
        처리된 청크 수 반환

        Returns:
            처리된 청크 수
        """
        return self.processed_chunks

    def get_remaining_chunks(self) -> int:
        """
        남은 청크 수 반환

        Returns:
            남은 청크 수
        """
        if self.total_chunks <= 0:
            return 0
        return max(0, self.total_chunks - self.processed_chunks)

    def get_elapsed_time(self) -> Optional[timedelta]:
        """
        경과 시간 반환

        Returns:
            경과 시간 또는 None
        """
        if not self.start_time:
            return None

        return datetime.now() - self.start_time

    def get_estimated_completion_time(self) -> Optional[datetime]:
        """
        예상 완료 시간 반환

        Returns:
            예상 완료 시간 또는 None
        """
        return self.estimated_completion_time

    def get_estimated_remaining_time(self) -> Optional[timedelta]:
        """
        예상 남은 시간 반환

        Returns:
            예상 남은 시간 또는 None
        """
        if not self.estimated_completion_time:
            return None

        return self.estimated_completion_time - datetime.now()

    def get_processing_speed(self) -> Optional[float]:
        """
        처리 속도 반환 (청크/초)

        Returns:
            처리 속도 또는 None
        """
        if not self.chunk_processing_times:
            return None

        # 최근 10개 청크의 평균 처리 시간
        recent_times = self.chunk_processing_times[-10:]
        avg_time = sum(recent_times) / len(recent_times)

        if avg_time > 0:
            return 1.0 / avg_time

        return None

    def get_progress_summary(self) -> Dict[str, Any]:
        """
        진행률 요약 정보 반환

        Returns:
            진행률 요약 딕셔너리
        """
        elapsed_time = self.get_elapsed_time()
        estimated_completion = self.get_estimated_completion_time()
        estimated_remaining = self.get_estimated_remaining_time()
        processing_speed = self.get_processing_speed()

        summary = {
            "is_started": self.is_started,
            "is_completed": self.is_completed,
            "is_error": self.is_error,
            "current_progress": self.current_progress,
            "progress_percentage": self.get_progress_percentage(),
            "processed_chunks": self.processed_chunks,
            "total_chunks": self.total_chunks,
            "remaining_chunks": self.get_remaining_chunks(),
            "elapsed_time_seconds": (
                elapsed_time.total_seconds() if elapsed_time else None
            ),
            "estimated_completion_time": (
                estimated_completion.isoformat() if estimated_completion else None
            ),
            "estimated_remaining_seconds": (
                estimated_remaining.total_seconds() if estimated_remaining else None
            ),
            "processing_speed_chunks_per_second": processing_speed,
            "average_chunk_processing_time": self._get_average_processing_time(),
            "error_message": self.error_message,
        }

        return summary

    def get_progress_history(self) -> List[Dict[str, Any]]:
        """
        진행률 히스토리 반환

        Returns:
            진행률 히스토리 리스트
        """
        return self.progress_history.copy()

    def reset(self) -> None:
        """진행률 추적기 초기화"""
        self.start_time = None
        self.current_progress = 0.0
        self.total_chunks = 0
        self.processed_chunks = 0
        self.chunk_processing_times = []
        self.last_chunk_time = None
        self.estimated_completion_time = None
        self.progress_history = []
        self.is_started = False
        self.is_completed = False
        self.is_error = False
        self.error_message = None

        self.logger.info("진행률 추적기 초기화 완료")

    def _calculate_final_statistics(self) -> None:
        """최종 통계 계산"""
        if not self.start_time:
            return

        total_time = datetime.now() - self.start_time
        total_seconds = total_time.total_seconds()

        if total_seconds > 0:
            chunks_per_second = self.processed_chunks / total_seconds
            self.logger.info(
                f"스트리밍 완료: 총 {self.processed_chunks}개 청크, "
                f"소요시간 {total_seconds:.2f}초, "
                f"속도 {chunks_per_second:.2f} 청크/초"
            )

    def _update_estimated_completion_time(self) -> None:
        """예상 완료 시간 업데이트"""
        if not self.start_time or self.current_progress <= 0:
            return

        # 현재 진행률을 기반으로 예상 완료 시간 계산
        elapsed_time = datetime.now() - self.start_time
        elapsed_seconds = elapsed_time.total_seconds()

        if self.current_progress > 0:
            total_estimated_seconds = elapsed_seconds / self.current_progress
            remaining_seconds = total_estimated_seconds - elapsed_seconds

            self.estimated_completion_time = datetime.now() + timedelta(
                seconds=remaining_seconds
            )

    def _get_average_processing_time(self) -> Optional[float]:
        """평균 처리 시간 반환"""
        if not self.chunk_processing_times:
            return None

        return sum(self.chunk_processing_times) / len(self.chunk_processing_times)
