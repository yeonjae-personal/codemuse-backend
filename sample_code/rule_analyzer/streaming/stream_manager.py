"""
스트리밍 세션 관리자

스트리밍 세션의 생명주기를 관리하고 여러 스트리밍을 동시에 처리합니다.
"""

import asyncio
import logging
import uuid
from concurrent.futures import ThreadPoolExecutor
from datetime import datetime, timedelta
from typing import Any, AsyncGenerator, Dict, List, Optional

from .stream_generator import StreamGenerator
from .stream_models import (
    StreamingChunk,
    StreamingSession,
    StreamingStatus,
)


class StreamManager:
    """
    스트리밍 세션 관리자

    여러 스트리밍 세션을 동시에 관리하고 모니터링합니다.
    """

    def __init__(self, max_concurrent_sessions: int = 10):
        """
        스트림 관리자 초기화

        Args:
            max_concurrent_sessions: 최대 동시 세션 수
        """
        self.logger = logging.getLogger(__name__)
        self.max_concurrent_sessions = max_concurrent_sessions

        # 세션 관리
        self.active_sessions: Dict[str, StreamingSession] = {}
        self.completed_sessions: Dict[str, StreamingSession] = {}
        self.session_generators: Dict[str, StreamGenerator] = {}

        # 스레드 풀 (동기 작업용)
        self.thread_pool = ThreadPoolExecutor(max_workers=4)

        # 하트비트 관리
        self.heartbeat_task: Optional[asyncio.Task] = None
        self.heartbeat_interval = 30.0

        # 통계
        self.total_sessions_created = 0
        self.total_sessions_completed = 0
        self.total_sessions_failed = 0

    async def start_streaming(
        self,
        validation_result,
        options: Optional[Dict[str, Any]] = None,
        session_id: Optional[str] = None,
    ) -> str:
        """
        새로운 스트리밍 세션 시작

        Args:
            validation_result: ValidationResult 객체
            options: 스트리밍 옵션
            session_id: 세션 ID (None이면 자동 생성)

        Returns:
            세션 ID

        Raises:
            RuntimeError: 최대 세션 수 초과 시
        """
        # 최대 세션 수 확인
        if len(self.active_sessions) >= self.max_concurrent_sessions:
            raise RuntimeError(
                f"최대 동시 세션 수({self.max_concurrent_sessions})를 초과했습니다."
            )

        # 세션 ID 생성
        if session_id is None:
            session_id = str(uuid.uuid4())

        # 스트림 생성기 생성
        generator = StreamGenerator(options)

        # 세션 생성
        session = StreamingSession(
            session_id=session_id,
            status=StreamingStatus.INITIALIZING,
            start_time=datetime.now().isoformat(),
        )

        # 세션 등록
        self.active_sessions[session_id] = session
        self.session_generators[session_id] = generator
        self.total_sessions_created += 1

        self.logger.info(f"새로운 스트리밍 세션 시작: {session_id}")

        # 하트비트 태스크 시작 (첫 번째 세션인 경우)
        if len(self.active_sessions) == 1:
            await self._start_heartbeat()

        return session_id

    async def get_stream(self, session_id: str) -> AsyncGenerator[StreamingChunk, None]:
        """
        스트리밍 데이터 반환

        Args:
            session_id: 세션 ID

        Yields:
            StreamingChunk 객체들

        Raises:
            ValueError: 세션을 찾을 수 없는 경우
        """
        if session_id not in self.active_sessions:
            raise ValueError(f"세션을 찾을 수 없습니다: {session_id}")

        session = self.active_sessions[session_id]
        generator = self.session_generators[session_id]

        # 세션 상태를 스트리밍으로 변경
        session.status = StreamingStatus.STREAMING

        try:
            # 스트리밍 데이터 생성
            async for chunk in generator.generate_stream(validation_result=None):
                # 세션 진행률 업데이트
                session.update_progress(
                    generator.current_chunk_index, generator.current_chunk_index + 1
                )

                yield chunk

                # 완료 청크인 경우 세션 정리
                if chunk.is_complete():
                    await self._complete_session(session_id)
                    break

        except Exception as e:
            self.logger.error(f"스트리밍 세션 {session_id} 오류: {str(e)}")
            session.mark_error(str(e))
            await self._complete_session(session_id)
            raise

    async def pause_session(self, session_id: str) -> bool:
        """
        세션 일시정지

        Args:
            session_id: 세션 ID

        Returns:
            일시정지 성공 여부
        """
        if session_id not in self.active_sessions:
            return False

        session = self.active_sessions[session_id]
        if session.status == StreamingStatus.STREAMING:
            session.status = StreamingStatus.PAUSED
            self.logger.info(f"세션 일시정지: {session_id}")
            return True

        return False

    async def resume_session(self, session_id: str) -> bool:
        """
        세션 재개

        Args:
            session_id: 세션 ID

        Returns:
            재개 성공 여부
        """
        if session_id not in self.active_sessions:
            return False

        session = self.active_sessions[session_id]
        if session.status == StreamingStatus.PAUSED:
            session.status = StreamingStatus.STREAMING
            self.logger.info(f"세션 재개: {session_id}")
            return True

        return False

    async def cancel_session(self, session_id: str) -> bool:
        """
        세션 취소

        Args:
            session_id: 세션 ID

        Returns:
            취소 성공 여부
        """
        if session_id not in self.active_sessions:
            return False

        session = self.active_sessions[session_id]
        session.status = StreamingStatus.CANCELLED
        session.end_time = datetime.now().isoformat()

        await self._complete_session(session_id)
        self.logger.info(f"세션 취소: {session_id}")

        return True

    async def get_session_status(self, session_id: str) -> Optional[Dict[str, Any]]:
        """
        세션 상태 반환

        Args:
            session_id: 세션 ID

        Returns:
            세션 상태 딕셔너리 또는 None
        """
        # 활성 세션에서 확인
        if session_id in self.active_sessions:
            return self.active_sessions[session_id].to_summary()

        # 완료된 세션에서 확인
        if session_id in self.completed_sessions:
            return self.completed_sessions[session_id].to_summary()

        return None

    def list_active_sessions(self) -> List[Dict[str, Any]]:
        """
        활성 세션 목록 반환

        Returns:
            활성 세션 정보 리스트
        """
        return [session.to_summary() for session in self.active_sessions.values()]

    def list_completed_sessions(self) -> List[Dict[str, Any]]:
        """
        완료된 세션 목록 반환

        Returns:
            완료된 세션 정보 리스트
        """
        return [session.to_summary() for session in self.completed_sessions.values()]

    def get_session_count(self) -> Dict[str, int]:
        """
        세션 수 통계 반환

        Returns:
            세션 수 통계 딕셔너리
        """
        return {
            "active": len(self.active_sessions),
            "completed": len(self.completed_sessions),
            "total_created": self.total_sessions_created,
            "total_completed": self.total_sessions_completed,
            "total_failed": self.total_sessions_failed,
            "max_concurrent": self.max_concurrent_sessions,
        }

    async def cleanup_old_sessions(self, max_age_hours: int = 24) -> int:
        """
        오래된 완료 세션 정리

        Args:
            max_age_hours: 최대 보관 시간 (시간)

        Returns:
            정리된 세션 수
        """
        cutoff_time = datetime.now() - timedelta(hours=max_age_hours)
        sessions_to_remove = []

        for session_id, session in self.completed_sessions.items():
            if session.end_time:
                end_time = datetime.fromisoformat(session.end_time)
                if end_time < cutoff_time:
                    sessions_to_remove.append(session_id)

        # 세션 제거
        for session_id in sessions_to_remove:
            del self.completed_sessions[session_id]

        if sessions_to_remove:
            self.logger.info(f"{len(sessions_to_remove)}개의 오래된 세션 정리 완료")

        return len(sessions_to_remove)

    async def shutdown(self) -> None:
        """스트림 관리자 종료"""
        self.logger.info("스트림 관리자 종료 시작")

        # 하트비트 태스크 중지
        if self.heartbeat_task:
            self.heartbeat_task.cancel()
            try:
                await self.heartbeat_task
            except asyncio.CancelledError:
                pass

        # 모든 활성 세션 취소
        for session_id in list(self.active_sessions.keys()):
            await self.cancel_session(session_id)

        # 스레드 풀 종료
        self.thread_pool.shutdown(wait=True)

        self.logger.info("스트림 관리자 종료 완료")

    async def _complete_session(self, session_id: str) -> None:
        """
        세션 완료 처리

        Args:
            session_id: 세션 ID
        """
        if session_id not in self.active_sessions:
            return

        session = self.active_sessions[session_id]
        # generator 변수는 현재 사용되지 않지만 향후 확장을 위해 유지
        _ = self.session_generators[session_id]

        # 세션 완료 시간 설정
        if not session.end_time:
            session.end_time = datetime.now().isoformat()

        # 세션을 완료된 세션으로 이동
        self.completed_sessions[session_id] = session
        del self.active_sessions[session_id]
        del self.session_generators[session_id]

        # 통계 업데이트
        self.total_sessions_completed += 1
        if session.status == StreamingStatus.ERROR:
            self.total_sessions_failed += 1

        # 하트비트 태스크 중지 (마지막 세션인 경우)
        if len(self.active_sessions) == 0:
            await self._stop_heartbeat()

        self.logger.info(f"세션 완료: {session_id} (상태: {session.status})")

    async def _start_heartbeat(self) -> None:
        """하트비트 태스크 시작"""
        if self.heartbeat_task is None or self.heartbeat_task.done():
            self.heartbeat_task = asyncio.create_task(self._heartbeat_loop())
            self.logger.debug("하트비트 태스크 시작")

    async def _stop_heartbeat(self) -> None:
        """하트비트 태스크 중지"""
        if self.heartbeat_task and not self.heartbeat_task.done():
            self.heartbeat_task.cancel()
            try:
                await self.heartbeat_task
            except asyncio.CancelledError:
                pass
            self.heartbeat_task = None
            self.logger.debug("하트비트 태스크 중지")

    async def _heartbeat_loop(self) -> None:
        """하트비트 루프"""
        while True:
            try:
                await asyncio.sleep(self.heartbeat_interval)

                # 활성 세션이 없으면 종료
                if not self.active_sessions:
                    break

                # 하트비트 로그
                active_count = len(self.active_sessions)
                self.logger.debug(f"하트비트: {active_count}개 활성 세션")

            except asyncio.CancelledError:
                break
            except Exception as e:
                self.logger.error(f"하트비트 루프 오류: {str(e)}")
                await asyncio.sleep(5.0)  # 오류 시 잠시 대기

    def _get_validation_result_for_session(self, session_id: str):
        """
        세션의 ValidationResult 객체 반환

        Args:
            session_id: 세션 ID

        Returns:
            ValidationResult 객체 또는 None
        """
        # 실제 구현에서는 세션별로 ValidationResult를 저장해야 함
        # 현재는 간단한 구현을 위해 None 반환
        return None
