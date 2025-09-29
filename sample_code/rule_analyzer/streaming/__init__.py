"""
RaaS Rule Analyzer - Streaming Package

분석 결과를 스트리밍으로 전송하는 기능을 제공합니다.
- 스트리밍 관리자: 스트리밍 세션 관리
- 스트리밍 생성기: 스트리밍 데이터 생성
- 다양한 프로토콜 지원: HTTP Streaming, SSE, WebSocket
"""

from .stream_generator import StreamGenerator
from .stream_manager import StreamManager
from .stream_models import StreamingChunk, StreamingOptions, StreamingStatus


__all__ = [
    "StreamManager",
    "StreamGenerator",
    "StreamingChunk",
    "StreamingOptions",
    "StreamingStatus",
]


# 편의 함수들
async def stream_analysis_result(
    validation_result, options=None, chunk_delay: float = 0.1
):
    """
    분석 결과를 스트리밍으로 전송

    Args:
        validation_result: ValidationResult 객체
        options: 스트리밍 옵션
        chunk_delay: 청크 간 딜레이 (초)

    Yields:
        StreamingChunk 객체들
    """
    generator = StreamGenerator(options or {})
    async for chunk in generator.generate_stream(validation_result, chunk_delay):
        yield chunk


def get_streaming_options() -> dict:
    """
    사용 가능한 스트리밍 옵션 반환

    Returns:
        스트리밍 옵션 정보 딕셔너리
    """
    return StreamingOptions.get_available_options()


def validate_streaming_options(options: dict) -> tuple[bool, list[str]]:
    """
    스트리밍 옵션 유효성 검증

    Args:
        options: 검증할 옵션 딕셔너리

    Returns:
        (유효성 여부, 에러 메시지 리스트)
    """
    from .stream_models import StreamingOptions

    try:
        StreamingOptions(**options)
        return True, []
    except Exception as e:
        return False, [str(e)]
