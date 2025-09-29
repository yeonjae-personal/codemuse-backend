"""
HTTP Streaming 프로토콜

HTTP 응답 스트리밍을 통해 분석 결과를 전송합니다.
"""

# json 모듈은 현재 사용되지 않지만 향후 확장을 위해 유지
import logging
from typing import AsyncGenerator, Dict, Optional

from ..stream_models import StreamingChunk, StreamingOptions


class HTTPStreamingProtocol:
    """
    HTTP Streaming 프로토콜 구현

    HTTP 응답 스트리밍을 통해 분석 결과를 전송합니다.
    """

    def __init__(self, options: Optional[StreamingOptions] = None):
        """
        HTTP Streaming 프로토콜 초기화

        Args:
            options: 스트리밍 옵션
        """
        self.logger = logging.getLogger(__name__)
        self.options = options or StreamingOptions()

    async def stream_response(
        self, chunks: AsyncGenerator[StreamingChunk, None]
    ) -> AsyncGenerator[str, None]:
        """
        HTTP 응답 스트리밍

        Args:
            chunks: 스트리밍 청크들

        Yields:
            HTTP 응답 라인들
        """
        # HTTP 헤더 전송
        yield "HTTP/1.1 200 OK\r\n"
        yield "Content-Type: text/plain; charset=utf-8\r\n"
        yield "Transfer-Encoding: chunked\r\n"
        yield "Cache-Control: no-cache\r\n"
        yield "Connection: keep-alive\r\n"
        yield "\r\n"

        try:
            async for chunk in chunks:
                # 청크 데이터 전송
                chunk_data = self._format_chunk(chunk)
                yield f"{len(chunk_data.encode('utf-8')):X}\r\n"
                yield chunk_data
                yield "\r\n"

                # 진행률 업데이트 (옵션에 따라)
                if self.options.include_progress:
                    progress_data = self._format_progress(chunk)
                    yield f"{len(progress_data.encode('utf-8')):X}\r\n"
                    yield progress_data
                    yield "\r\n"

                # 청크 완료 신호
                if chunk.is_complete():
                    break

        except Exception as e:
            self.logger.error(f"HTTP 스트리밍 오류: {str(e)}")
            # 에러 응답 전송
            error_data = self._format_error(str(e))
            yield f"{len(error_data.encode('utf-8')):X}\r\n"
            yield error_data
            yield "\r\n"

        # 스트리밍 완료
        yield "0\r\n\r\n"

    def _format_chunk(self, chunk: StreamingChunk) -> str:
        """
        청크를 HTTP 응답 형식으로 포맷팅

        Args:
            chunk: 스트리밍 청크

        Returns:
            포맷팅된 문자열
        """
        if chunk.type.value == "analysis_text":
            return chunk.content

        elif chunk.type.value == "analysis_complete":
            return f"[완료] {chunk.content}"

        elif chunk.type.value == "analysis_error":
            return f"[오류] {chunk.content}"

        elif chunk.type.value == "analysis_progress":
            return f"[진행률] {chunk.get_progress_percentage()}%"

        else:
            return f"[{chunk.type.value}] {chunk.content}"

    def _format_progress(self, chunk: StreamingChunk) -> str:
        """
        진행률 정보 포맷팅

        Args:
            chunk: 스트리밍 청크

        Returns:
            진행률 문자열
        """
        progress = chunk.get_progress_percentage()
        return f"진행률: {progress}%"

    def _format_error(self, error_message: str) -> str:
        """
        에러 메시지 포맷팅

        Args:
            error_message: 에러 메시지

        Returns:
            에러 문자열
        """
        return f"[오류] {error_message}"

    def get_response_headers(self) -> Dict[str, str]:
        """
        HTTP 응답 헤더 반환

        Returns:
            응답 헤더 딕셔너리
        """
        return {
            "Content-Type": "text/plain; charset=utf-8",
            "Transfer-Encoding": "chunked",
            "Cache-Control": "no-cache",
            "Connection": "keep-alive",
            "X-Content-Type-Options": "nosniff",
        }

    def create_error_response(self, status_code: int, error_message: str) -> str:
        """
        에러 응답 생성

        Args:
            status_code: HTTP 상태 코드
            error_message: 에러 메시지

        Returns:
            에러 응답 문자열
        """
        status_texts = {
            400: "Bad Request",
            500: "Internal Server Error",
            503: "Service Unavailable",
        }

        status_text = status_texts.get(status_code, "Error")

        response = f"HTTP/1.1 {status_code} {status_text}\r\n"
        response += "Content-Type: text/plain; charset=utf-8\r\n"
        response += "Content-Length: {}\r\n".format(len(error_message.encode('utf-8')))
        response += "\r\n"
        response += error_message

        return response
