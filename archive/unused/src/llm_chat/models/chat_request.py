"""
Chat Request/Response Models
"""

from typing import List, Dict, Any, Optional
from pydantic import BaseModel


class ChatRequest(BaseModel):
    """채팅 요청"""
    message: str
    session_id: Optional[str] = None
    context: Optional[Dict[str, Any]] = None
    model: str = "gpt-3.5-turbo"
    temperature: float = 0.7
    max_tokens: Optional[int] = None


class ChatResponse(BaseModel):
    """채팅 응답"""
    message: str
    session_id: str
    model: str
    usage: Dict[str, int]
    response_time: float
    metadata: Dict[str, Any]
