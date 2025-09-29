"""
Integration Request/Response Models
"""

from typing import Dict, Any, Optional, List
from pydantic import BaseModel


class IntegrationRequest(BaseModel):
    """통합 요청"""
    action: str  # analyze_code, generate_document, search_docs, chat_with_llm
    parameters: Dict[str, Any]
    session_id: Optional[str] = None


class IntegrationResponse(BaseModel):
    """통합 응답"""
    success: bool
    data: Optional[Dict[str, Any]] = None
    error: Optional[str] = None
    service_used: str
    response_time: float
