"""
채팅 세션 및 메시지 모델 정의
"""

from datetime import datetime
from typing import List, Dict, Optional
from pydantic import BaseModel, Field
from enum import Enum


class MessageRole(str, Enum):
    """메시지 역할"""
    USER = "user"
    ASSISTANT = "assistant"
    SYSTEM = "system"


class ChatSessionStatus(str, Enum):
    """채팅 세션 상태"""
    ACTIVE = "active"
    PAUSED = "paused"
    COMPLETED = "completed"
    ARCHIVED = "archived"


class Message(BaseModel):
    """메시지 모델"""
    id: str = Field(..., description="메시지 고유 ID")
    session_id: str = Field(..., description="세션 ID")
    role: MessageRole = Field(..., description="메시지 역할")
    content: str = Field(..., description="메시지 내용")
    timestamp: datetime = Field(default_factory=datetime.utcnow, description="생성 시간")
    metadata: Dict[str, any] = Field(default_factory=dict, description="메타데이터")
    
    class Config:
        json_encoders = {
            datetime: lambda v: v.isoformat()
        }


class ChatSession(BaseModel):
    """채팅 세션 모델"""
    id: str = Field(..., description="세션 고유 ID")
    user_id: str = Field(..., description="사용자 ID")
    title: str = Field(..., description="세션 제목")
    status: ChatSessionStatus = Field(default=ChatSessionStatus.ACTIVE, description="세션 상태")
    created_at: datetime = Field(default_factory=datetime.utcnow, description="생성 시간")
    updated_at: datetime = Field(default_factory=datetime.utcnow, description="수정 시간")
    messages: List[Message] = Field(default_factory=list, description="메시지 목록")
    context: Dict[str, any] = Field(default_factory=dict, description="컨텍스트 정보")
    
    class Config:
        json_encoders = {
            datetime: lambda v: v.isoformat()
        }
