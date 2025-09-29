"""
스트리밍 관련 데이터 모델

스트리밍 전송에 필요한 데이터 구조와 옵션을 정의합니다.
"""

from datetime import datetime
from enum import Enum
from typing import Any, Dict, Optional

from pydantic import BaseModel, Field


class ChunkType(str, Enum):
    """스트리밍 청크 타입"""

    ANALYSIS_TEXT = "analysis_text"  # 분석 텍스트
    ANALYSIS_PROGRESS = "analysis_progress"  # 진행률 업데이트
    ANALYSIS_COMPLETE = "analysis_complete"  # 분석 완료
    ANALYSIS_ERROR = "analysis_error"  # 분석 에러
    METADATA = "metadata"  # 메타데이터
    HEARTBEAT = "heartbeat"  # 연결 유지 신호


class StreamingStatus(str, Enum):
    """스트리밍 상태"""

    INITIALIZING = "initializing"  # 초기화 중
    STREAMING = "streaming"  # 스트리밍 중
    PAUSED = "paused"  # 일시정지
    COMPLETED = "completed"  # 완료
    ERROR = "error"  # 에러
    CANCELLED = "cancelled"  # 취소됨


class StreamingChunk(BaseModel):
    """
    스트리밍 청크 모델

    스트리밍으로 전송되는 각 데이터 단위를 나타냅니다.
    """

    # 기본 정보
    type: ChunkType = Field(..., description="청크 타입")
    content: str = Field(..., description="실제 텍스트 내용")
    timestamp: str = Field(
        default_factory=lambda: datetime.now().isoformat(), description="타임스탬프"
    )

    # 진행률 정보
    progress: float = Field(
        default=0.0, ge=0.0, le=1.0, description="진행률 (0.0 ~ 1.0)"
    )

    # 메타데이터
    chunk_index: Optional[int] = Field(default=None, description="청크 인덱스 (순서)")

    total_chunks: Optional[int] = Field(default=None, description="전체 청크 수")

    # 추가 정보
    metadata: Optional[Dict[str, Any]] = Field(
        default_factory=dict, description="추가 메타데이터"
    )

    # 에러 정보 (에러 타입인 경우)
    error_code: Optional[str] = Field(default=None, description="에러 코드")

    error_message: Optional[str] = Field(default=None, description="에러 메시지")

    class Config:
        """Pydantic 설정"""

        use_enum_values = True
        validate_assignment = True

    def is_complete(self) -> bool:
        """분석 완료 여부 확인"""
        return self.type == ChunkType.ANALYSIS_COMPLETE

    def is_error(self) -> bool:
        """에러 여부 확인"""
        return self.type == ChunkType.ANALYSIS_ERROR

    def is_progress_update(self) -> bool:
        """진행률 업데이트 여부 확인"""
        return self.type == ChunkType.ANALYSIS_PROGRESS

    def get_progress_percentage(self) -> int:
        """진행률을 퍼센트로 반환"""
        return int(self.progress * 100)

    def to_dict(self) -> Dict[str, Any]:
        """딕셔너리로 변환"""
        return self.dict()


class StreamingOptions(BaseModel):
    """
    스트리밍 옵션 모델

    스트리밍 동작을 제어하는 옵션들을 정의합니다.
    """

    # 기본 스트리밍 옵션
    chunk_delay: float = Field(
        default=0.1, ge=0.01, le=2.0, description="청크 간 딜레이 (초)"
    )

    max_chunk_size: int = Field(
        default=1000, ge=100, le=10000, description="최대 청크 크기 (문자)"
    )

    include_progress: bool = Field(default=True, description="진행률 포함 여부")

    include_metadata: bool = Field(default=True, description="메타데이터 포함 여부")

    # 고급 옵션
    enable_heartbeat: bool = Field(default=True, description="하트비트 활성화 여부")

    heartbeat_interval: float = Field(
        default=30.0, ge=5.0, le=300.0, description="하트비트 간격 (초)"
    )

    # 에러 처리 옵션
    retry_on_error: bool = Field(default=True, description="에러 시 재시도 여부")

    max_retries: int = Field(default=3, ge=0, le=10, description="최대 재시도 횟수")

    # 성능 옵션
    buffer_size: int = Field(default=100, ge=10, le=1000, description="버퍼 크기")

    enable_compression: bool = Field(default=False, description="압축 활성화 여부")

    # 사용자 정의 설정
    custom_settings: Dict[str, Any] = Field(
        default_factory=dict, description="사용자 정의 설정"
    )

    class Config:
        """Pydantic 설정"""

        use_enum_values = True
        validate_assignment = True

    @classmethod
    def get_available_options(cls) -> Dict[str, Any]:
        """
        사용 가능한 스트리밍 옵션 정보 반환

        Returns:
            옵션 정보 딕셔너리
        """
        return {
            "chunk_delay": {"min": 0.01, "max": 2.0, "default": 0.1, "unit": "초"},
            "max_chunk_size": {
                "min": 100,
                "max": 10000,
                "default": 1000,
                "unit": "문자",
            },
            "heartbeat_interval": {
                "min": 5.0,
                "max": 300.0,
                "default": 30.0,
                "unit": "초",
            },
            "max_retries": {"min": 0, "max": 10, "default": 3, "unit": "회"},
            "buffer_size": {"min": 10, "max": 1000, "default": 100, "unit": "개"},
            "defaults": {
                "chunk_delay": 0.1,
                "max_chunk_size": 1000,
                "include_progress": True,
                "include_metadata": True,
                "enable_heartbeat": True,
                "heartbeat_interval": 30.0,
                "retry_on_error": True,
                "max_retries": 3,
                "buffer_size": 100,
                "enable_compression": False,
            },
        }

    def get_effective_chunk_delay(self) -> float:
        """
        실제 적용될 청크 딜레이 반환

        Returns:
            실제 청크 딜레이 (초)
        """
        # 사용자 정의 설정에서 오버라이드 확인
        if "chunk_delay" in self.custom_settings:
            custom_delay = self.custom_settings["chunk_delay"]
            if isinstance(custom_delay, (int, float)) and 0.01 <= custom_delay <= 2.0:
                return float(custom_delay)

        return self.chunk_delay

    def should_include_field(self, field_name: str) -> bool:
        """
        특정 필드를 포함해야 하는지 확인

        Args:
            field_name: 필드명

        Returns:
            포함 여부
        """
        field_mapping = {
            "progress": self.include_progress,
            "metadata": self.include_metadata,
        }

        return field_mapping.get(field_name, True)


class StreamingSession(BaseModel):
    """
    스트리밍 세션 모델

    스트리밍 세션의 상태와 정보를 관리합니다.
    """

    # 세션 정보
    session_id: str = Field(..., description="세션 고유 ID")
    created_at: str = Field(
        default_factory=lambda: datetime.now().isoformat(), description="생성 시간"
    )

    # 상태 정보
    status: StreamingStatus = Field(
        default=StreamingStatus.INITIALIZING, description="스트리밍 상태"
    )

    # 진행 정보
    total_chunks: int = Field(default=0, description="전체 청크 수")
    sent_chunks: int = Field(default=0, description="전송된 청크 수")
    current_progress: float = Field(default=0.0, description="현재 진행률")

    # 성능 정보
    start_time: Optional[str] = Field(default=None, description="시작 시간")
    end_time: Optional[str] = Field(default=None, description="종료 시간")
    total_duration: Optional[float] = Field(
        default=None, description="총 소요 시간 (초)"
    )

    # 에러 정보
    error_count: int = Field(default=0, description="에러 발생 횟수")
    last_error: Optional[str] = Field(default=None, description="마지막 에러 메시지")

    # 메타데이터
    metadata: Dict[str, Any] = Field(
        default_factory=dict, description="세션 메타데이터"
    )

    class Config:
        """Pydantic 설정"""

        use_enum_values = True
        validate_assignment = True

    def is_active(self) -> bool:
        """세션이 활성 상태인지 확인"""
        return self.status in [StreamingStatus.INITIALIZING, StreamingStatus.STREAMING]

    def is_completed(self) -> bool:
        """세션이 완료되었는지 확인"""
        return self.status in [
            StreamingStatus.COMPLETED,
            StreamingStatus.ERROR,
            StreamingStatus.CANCELLED,
        ]

    def get_progress_percentage(self) -> int:
        """진행률을 퍼센트로 반환"""
        return int(self.current_progress * 100)

    def update_progress(self, sent_chunks: int, total_chunks: int) -> None:
        """
        진행률 업데이트

        Args:
            sent_chunks: 전송된 청크 수
            total_chunks: 전체 청크 수
        """
        self.sent_chunks = sent_chunks
        self.total_chunks = total_chunks
        if total_chunks > 0:
            self.current_progress = sent_chunks / total_chunks
        else:
            self.current_progress = 0.0

    def mark_completed(self) -> None:
        """세션을 완료 상태로 표시"""
        self.status = StreamingStatus.COMPLETED
        self.end_time = datetime.now().isoformat()
        if self.start_time:
            start_dt = datetime.fromisoformat(self.start_time)
            end_dt = datetime.fromisoformat(self.end_time)
            self.total_duration = (end_dt - start_dt).total_seconds()

    def mark_error(self, error_message: str) -> None:
        """
        세션을 에러 상태로 표시

        Args:
            error_message: 에러 메시지
        """
        self.status = StreamingStatus.ERROR
        self.last_error = error_message
        self.error_count += 1
        self.end_time = datetime.now().isoformat()

    def to_summary(self) -> Dict[str, Any]:
        """
        세션 요약 정보 반환

        Returns:
            세션 요약 딕셔너리
        """
        return {
            "session_id": self.session_id,
            "status": self.status,
            "progress": self.get_progress_percentage(),
            "sent_chunks": self.sent_chunks,
            "total_chunks": self.total_chunks,
            "error_count": self.error_count,
            "duration": self.total_duration,
        }
