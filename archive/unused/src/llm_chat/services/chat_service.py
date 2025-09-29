"""
Chat Service

채팅 서비스 (LLM + 세션 관리 통합)
"""

from typing import List, Optional, Dict, Any
from datetime import datetime

from .llm_service import LLMService
from .session_manager import SessionManager
from ..models.chat_request import ChatRequest, ChatResponse
from ..models.chat_session import ChatSession, Message


class ChatService:
    """채팅 서비스"""
    
    def __init__(self):
        self.llm_service = LLMService()
        self.session_manager = SessionManager()
    
    async def send_message(self, request: ChatRequest) -> ChatResponse:
        """메시지 전송 및 응답"""
        # 세션 확인 또는 생성
        session_id = request.session_id
        if not session_id:
            session = self.session_manager.create_session()
            session_id = session.id
            request.session_id = session_id
        
        # 사용자 메시지를 세션에 추가
        self.session_manager.add_message(
            session_id=session_id,
            role="user",
            content=request.message
        )
        
        # LLM에게 요청 전송
        response = await self.llm_service.chat(request)
        
        # AI 응답을 세션에 추가
        self.session_manager.add_message(
            session_id=session_id,
            role="assistant",
            content=response.message,
            metadata=response.metadata
        )
        
        return response
    
    def create_session(self, user_id: Optional[str] = None, title: str = "New Chat") -> ChatSession:
        """새 채팅 세션 생성"""
        return self.session_manager.create_session(user_id, title)
    
    def get_session(self, session_id: str) -> Optional[ChatSession]:
        """세션 조회"""
        return self.session_manager.get_session(session_id)
    
    def get_messages(self, session_id: str) -> List[Message]:
        """세션의 메시지 목록 조회"""
        return self.session_manager.get_messages(session_id)
    
    def list_sessions(self, user_id: Optional[str] = None) -> List[ChatSession]:
        """세션 목록 조회"""
        return self.session_manager.list_sessions(user_id)
    
    def delete_session(self, session_id: str) -> bool:
        """세션 삭제"""
        return self.session_manager.delete_session(session_id)
    
    def get_available_models(self) -> Dict[str, List[str]]:
        """사용 가능한 모델 목록"""
        return self.llm_service.get_available_models()
