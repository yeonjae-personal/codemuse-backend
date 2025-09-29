"""
Server-Sent Events (SSE) 프로토콜

SSE를 통해 분석 결과를 실시간으로 전송합니다.
"""

import json
import logging
from typing import AsyncGenerator, Dict, Optional

from ..stream_models import ChunkType, StreamingChunk, StreamingOptions


class SSEProtocol:
    """
    Server-Sent Events (SSE) 프로토콜 구현

    SSE를 통해 분석 결과를 실시간으로 전송합니다.
    """

    def __init__(self, options: Optional[StreamingOptions] = None):
        """
        SSE 프로토콜 초기화

        Args:
            options: 스트리밍 옵션
        """
        self.logger = logging.getLogger(__name__)
        self.options = options or StreamingOptions()

    async def stream_events(
        self, chunks: AsyncGenerator[StreamingChunk, None]
    ) -> AsyncGenerator[str, None]:
        """
        SSE 이벤트 스트리밍

        Args:
            chunks: 스트리밍 청크들

        Yields:
            SSE 이벤트 라인들
        """
        # SSE 헤더 전송
        yield "HTTP/1.1 200 OK\r\n"
        yield "Content-Type: text/event-stream\r\n"
        yield "Cache-Control: no-cache\r\n"
        yield "Connection: keep-alive\r\n"
        yield "Access-Control-Allow-Origin: *\r\n"
        yield "Access-Control-Allow-Headers: Cache-Control\r\n"
        yield "\r\n"

        # 연결 유지 이벤트
        yield "event: connected\r\n"
        yield "data: {\"status\": \"connected\"}\r\n"
        yield "\r\n"

        try:
            async for chunk in chunks:
                # 청크를 SSE 이벤트로 변환
                event_lines = self._format_sse_event(chunk)
                for line in event_lines:
                    yield line

                # 진행률 업데이트 (옵션에 따라)
                if self.options.include_progress:
                    progress_lines = self._format_progress_event(chunk)
                    for line in progress_lines:
                        yield line

                # 청크 완료 신호
                if chunk.is_complete():
                    break

        except Exception as e:
            self.logger.error(f"SSE 스트리밍 오류: {str(e)}")
            # 에러 이벤트 전송
            error_lines = self._format_error_event(str(e))
            for line in error_lines:
                yield line

        # 완료 이벤트 전송
        yield "event: complete\r\n"
        yield "data: {\"status\": \"complete\"}\r\n"
        yield "\r\n"

    def _format_sse_event(self, chunk: StreamingChunk) -> list[str]:
        """
        청크를 SSE 이벤트 형식으로 포맷팅

        Args:
            chunk: 스트리밍 청크

        Returns:
            SSE 이벤트 라인들
        """
        lines = []

        # 이벤트 타입 결정
        if chunk.type == ChunkType.ANALYSIS_TEXT:
            event_type = "analysis"
        elif chunk.type == ChunkType.ANALYSIS_COMPLETE:
            event_type = "complete"
        elif chunk.type == ChunkType.ANALYSIS_ERROR:
            event_type = "error"
        elif chunk.type == ChunkType.ANALYSIS_PROGRESS:
            event_type = "progress"
        else:
            event_type = "info"

        # 이벤트 데이터 구성
        event_data = {
            "type": str(chunk.type),
            "content": chunk.content,
            "timestamp": chunk.timestamp,
            "progress": chunk.progress,
            "chunk_index": chunk.chunk_index,
            "total_chunks": chunk.total_chunks,
            "metadata": chunk.metadata,
        }

        # 에러 정보 추가
        if chunk.error_code:
            event_data["error_code"] = chunk.error_code
        if chunk.error_message:
            event_data["error_message"] = chunk.error_message

        # SSE 이벤트 형식으로 변환
        lines.append(f"event: {event_type}\r\n")
        lines.append(f"data: {json.dumps(event_data, ensure_ascii=False)}\r\n")
        lines.append("\r\n")

        return lines

    def _format_progress_event(self, chunk: StreamingChunk) -> list[str]:
        """
        진행률 이벤트 포맷팅

        Args:
            chunk: 스트리밍 청크

        Returns:
            진행률 이벤트 라인들
        """
        lines = []

        progress_data = {
            "type": "progress",
            "progress": chunk.progress,
            "percentage": chunk.get_progress_percentage(),
            "chunk_index": chunk.chunk_index,
            "total_chunks": chunk.total_chunks,
            "timestamp": chunk.timestamp,
        }

        lines.append("event: progress\r\n")
        lines.append(f"data: {json.dumps(progress_data, ensure_ascii=False)}\r\n")
        lines.append("\r\n")

        return lines

    def _format_error_event(self, error_message: str) -> list[str]:
        """
        에러 이벤트 포맷팅

        Args:
            error_message: 에러 메시지

        Returns:
            에러 이벤트 라인들
        """
        lines = []

        error_data = {
            "type": "error",
            "message": error_message,
            "timestamp": self._get_current_timestamp(),
        }

        lines.append("event: error\r\n")
        lines.append(f"data: {json.dumps(error_data, ensure_ascii=False)}\r\n")
        lines.append("\r\n")

        return lines

    def _get_current_timestamp(self) -> str:
        """현재 타임스탬프 반환"""
        from datetime import datetime

        return datetime.now().isoformat()

    def get_response_headers(self) -> Dict[str, str]:
        """
        SSE 응답 헤더 반환

        Returns:
            응답 헤더 딕셔너리
        """
        return {
            "Content-Type": "text/event-stream",
            "Cache-Control": "no-cache",
            "Connection": "keep-alive",
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Headers": "Cache-Control",
            "X-Content-Type-Options": "nosniff",
        }

    def create_heartbeat_event(self) -> str:
        """
        하트비트 이벤트 생성

        Returns:
            하트비트 이벤트 문자열
        """
        heartbeat_data = {
            "type": "heartbeat",
            "timestamp": self._get_current_timestamp(),
        }

        lines = [
            "event: heartbeat\r\n",
            f"data: {json.dumps(heartbeat_data, ensure_ascii=False)}\r\n",
            "\r\n",
        ]

        return "".join(lines)

    def create_retry_event(self, retry_ms: int = 3000) -> str:
        """
        재연결 지연 이벤트 생성

        Args:
            retry_ms: 재연결 지연 시간 (밀리초)

        Returns:
            재연결 이벤트 문자열
        """
        return f"retry: {retry_ms}\r\n\r\n"
