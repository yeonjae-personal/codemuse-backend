"""
Session Manager

채팅 세션 관리 서비스
"""

import uuid
from typing import Dict, List, Optional
from datetime import datetime

from ..models.chat_session import ChatSession, Message


class SessionManager:
    """세션 관리자"""
    
    def __init__(self):
        self.sessions: Dict[str, ChatSession] = {}
    
    def create_session(self, user_id: Optional[str] = None, title: str = "New Chat") -> ChatSession:
        """새 세션 생성"""
        session_id = str(uuid.uuid4())
        
        session = ChatSession(
            id=session_id,
            user_id=user_id,
            title=title,
            created_at=datetime.now(),
            updated_at=datetime.now(),
            messages=[]
        )
        
        self.sessions[session_id] = session
        return session
    
    def get_session(self, session_id: str) -> Optional[ChatSession]:
        """세션 조회"""
        return self.sessions.get(session_id)
    
    def add_message(self, session_id: str, role: str, content: str, metadata: Optional[Dict] = None) -> Optional[Message]:
        """메시지 추가"""
        session = self.sessions.get(session_id)
        if not session:
            return None
        
        message = Message(
            id=str(uuid.uuid4()),
            role=role,
            content=content,
            timestamp=datetime.now(),
            metadata=metadata
        )
        
        session.messages.append(message)
        session.updated_at = datetime.now()
        
        return message
    
    def get_messages(self, session_id: str) -> List[Message]:
        """세션의 메시지 목록 조회"""
        session = self.sessions.get(session_id)
        if not session:
            return []
        
        return session.messages
    
    def list_sessions(self, user_id: Optional[str] = None) -> List[ChatSession]:
        """세션 목록 조회"""
        if user_id:
            return [session for session in self.sessions.values() if session.user_id == user_id]
        else:
            return list(self.sessions.values())
    
    def delete_session(self, session_id: str) -> bool:
        """세션 삭제"""
        if session_id in self.sessions:
            del self.sessions[session_id]
            return True
        return False
    
    def update_session_title(self, session_id: str, title: str) -> bool:
        """세션 제목 업데이트"""
        session = self.sessions.get(session_id)
        if session:
            session.title = title
            session.updated_at = datetime.now()
            return True
        return False
