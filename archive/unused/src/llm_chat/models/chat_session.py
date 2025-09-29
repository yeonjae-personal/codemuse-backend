"""
Chat Session Models
"""

from typing import List, Dict, Any, Optional
from pydantic import BaseModel
from datetime import datetime


class Message(BaseModel):
    """메시지 모델"""
    id: str
    role: str  # user, assistant, system
    content: str
    timestamp: datetime
    metadata: Optional[Dict[str, Any]] = None


class ChatSession(BaseModel):
    """채팅 세션 모델"""
    id: str
    user_id: Optional[str] = None
    title: str
    created_at: datetime
    updated_at: datetime
    messages: List[Message]
    context: Optional[Dict[str, Any]] = None
