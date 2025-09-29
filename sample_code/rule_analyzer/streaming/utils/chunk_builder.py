"""
스트리밍 청크 구성 유틸리티

스트리밍 데이터를 효율적으로 청크로 구성하는 기능을 제공합니다.
"""

import logging
from typing import Any, Dict, List, Optional

from ..stream_models import ChunkType, StreamingChunk


class ChunkBuilder:
    """
    스트리밍 청크 구성 유틸리티

    텍스트 데이터를 적절한 크기의 청크로 분할하고 구성합니다.
    """

    def __init__(self, max_chunk_size: int = 1000):
        """
        청크 빌더 초기화

        Args:
            max_chunk_size: 최대 청크 크기 (문자)
        """
        self.logger = logging.getLogger(__name__)
        self.max_chunk_size = max_chunk_size

    def build_text_chunks(
        self,
        text: str,
        chunk_type: ChunkType = ChunkType.ANALYSIS_TEXT,
        metadata: Optional[Dict[str, Any]] = None,
    ) -> List[StreamingChunk]:
        """
        텍스트를 청크로 분할

        Args:
            text: 분할할 텍스트
            chunk_type: 청크 타입
            metadata: 추가 메타데이터

        Returns:
            StreamingChunk 리스트
        """
        if not text:
            return []

        chunks = []

        # 텍스트가 최대 크기보다 작으면 단일 청크
        if len(text) <= self.max_chunk_size:
            chunk = StreamingChunk(
                type=chunk_type, content=text, metadata=metadata or {}
            )
            chunks.append(chunk)
            return chunks

        # 텍스트를 적절한 크기로 분할
        lines = text.split('\n')
        current_chunk = []
        current_size = 0

        for line in lines:
            line_size = len(line) + 1  # +1 for newline

            # 현재 청크에 추가할 수 있는지 확인
            if current_size + line_size <= self.max_chunk_size:
                current_chunk.append(line)
                current_size += line_size
            else:
                # 현재 청크 완성
                if current_chunk:
                    chunk_content = '\n'.join(current_chunk)
                    chunk = StreamingChunk(
                        type=chunk_type, content=chunk_content, metadata=metadata or {}
                    )
                    chunks.append(chunk)

                # 새 청크 시작
                current_chunk = [line]
                current_size = line_size

        # 마지막 청크 처리
        if current_chunk:
            chunk_content = '\n'.join(current_chunk)
            chunk = StreamingChunk(
                type=chunk_type, content=chunk_content, metadata=metadata or {}
            )
            chunks.append(chunk)

        return chunks

    def build_structured_chunks(
        self, data: Dict[str, Any], chunk_type: ChunkType = ChunkType.ANALYSIS_TEXT
    ) -> List[StreamingChunk]:
        """
        구조화된 데이터를 청크로 변환

        Args:
            data: 구조화된 데이터
            chunk_type: 청크 타입

        Returns:
            StreamingChunk 리스트
        """
        chunks = []

        # 데이터를 텍스트로 변환
        text_content = self._dict_to_text(data)

        # 텍스트를 청크로 분할
        text_chunks = self.build_text_chunks(text_content, chunk_type)

        # 청크에 인덱스 추가
        for i, chunk in enumerate(text_chunks):
            chunk.chunk_index = i
            chunk.total_chunks = len(text_chunks)
            chunks.append(chunk)

        return chunks

    def build_progress_chunk(
        self, progress: float, message: str = ""
    ) -> StreamingChunk:
        """
        진행률 청크 생성

        Args:
            progress: 진행률 (0.0 ~ 1.0)
            message: 진행률 메시지

        Returns:
            진행률 StreamingChunk
        """
        content = f"진행률: {int(progress * 100)}%"
        if message:
            content += f" - {message}"

        return StreamingChunk(
            type=ChunkType.ANALYSIS_PROGRESS, content=content, progress=progress
        )

    def build_completion_chunk(
        self, total_chunks: int, final_result: Optional[Dict[str, Any]] = None
    ) -> StreamingChunk:
        """
        완료 청크 생성

        Args:
            total_chunks: 전체 청크 수
            final_result: 최종 결과 데이터

        Returns:
            완료 StreamingChunk
        """
        metadata = {"total_chunks": total_chunks}
        if final_result:
            metadata["final_result"] = final_result

        return StreamingChunk(
            type=ChunkType.ANALYSIS_COMPLETE,
            content="분석 완료",
            progress=1.0,
            total_chunks=total_chunks,
            metadata=metadata,
        )

    def build_error_chunk(
        self, error_message: str, error_code: Optional[str] = None
    ) -> StreamingChunk:
        """
        에러 청크 생성

        Args:
            error_message: 에러 메시지
            error_code: 에러 코드

        Returns:
            에러 StreamingChunk
        """
        return StreamingChunk(
            type=ChunkType.ANALYSIS_ERROR,
            content=f"오류 발생: {error_message}",
            error_code=error_code,
            error_message=error_message,
            progress=0.0,
        )

    def merge_small_chunks(
        self, chunks: List[StreamingChunk], min_chunk_size: int = 100
    ) -> List[StreamingChunk]:
        """
        작은 청크들을 병합

        Args:
            chunks: 원본 청크 리스트
            min_chunk_size: 최소 청크 크기

        Returns:
            병합된 청크 리스트
        """
        if not chunks:
            return []

        merged_chunks = []
        current_chunk = None

        for chunk in chunks:
            # 첫 번째 청크이거나 현재 청크가 최소 크기보다 작으면 병합
            if current_chunk is None or len(current_chunk.content) < min_chunk_size:

                if current_chunk is None:
                    current_chunk = chunk
                else:
                    # 청크 병합
                    current_chunk.content += "\n" + chunk.content
                    # 메타데이터 병합
                    if chunk.metadata:
                        current_chunk.metadata.update(chunk.metadata)
            else:
                # 현재 청크가 충분히 크면 저장하고 새 청크 시작
                merged_chunks.append(current_chunk)
                current_chunk = chunk

        # 마지막 청크 처리
        if current_chunk:
            merged_chunks.append(current_chunk)

        return merged_chunks

    def _dict_to_text(self, data: Dict[str, Any], indent: int = 0) -> str:
        """
        딕셔너리를 텍스트로 변환

        Args:
            data: 변환할 딕셔너리
            indent: 들여쓰기 레벨

        Returns:
            변환된 텍스트
        """
        if not isinstance(data, dict):
            return str(data)

        lines = []
        indent_str = "  " * indent

        for key, value in data.items():
            if isinstance(value, dict):
                lines.append(f"{indent_str}{key}:")
                lines.append(self._dict_to_text(value, indent + 1))
            elif isinstance(value, list):
                lines.append(f"{indent_str}{key}:")
                for item in value:
                    if isinstance(item, dict):
                        lines.append(self._dict_to_text(item, indent + 1))
                    else:
                        lines.append(f"{indent_str}  - {item}")
            else:
                lines.append(f"{indent_str}{key}: {value}")

        return "\n".join(lines)

    def estimate_chunk_count(self, text: str) -> int:
        """
        텍스트의 예상 청크 수 계산

        Args:
            text: 분석할 텍스트

        Returns:
            예상 청크 수
        """
        if not text:
            return 0

        return max(1, (len(text) + self.max_chunk_size - 1) // self.max_chunk_size)

    def optimize_chunk_size(self, text_length: int, target_chunks: int = 10) -> int:
        """
        최적의 청크 크기 계산

        Args:
            text_length: 전체 텍스트 길이
            target_chunks: 목표 청크 수

        Returns:
            최적 청크 크기
        """
        if text_length <= 0 or target_chunks <= 0:
            return self.max_chunk_size

        optimal_size = text_length // target_chunks

        # 최소/최대 크기 제한
        min_size = 100
        max_size = 5000

        return max(min_size, min(max_size, optimal_size))
