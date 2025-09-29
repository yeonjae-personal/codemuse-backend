"""
Orchestration Request/Response Models
"""

from typing import Dict, Any, Optional, List
from pydantic import BaseModel


class OrchestrationRequest(BaseModel):
    """오케스트레이션 요청"""
    user_input: str
    session_id: Optional[str] = None
    workflow_type: str = "general"  # general, code_analysis, code_generation, bug_fix
    context: Optional[Dict[str, Any]] = None
    priority: int = 1  # 1-5, 높을수록 우선순위 높음


class OrchestrationResponse(BaseModel):
    """오케스트레이션 응답"""
    request_id: str
    status: str  # pending, processing, completed, failed
    workflow_steps: List[Dict[str, Any]]
    final_result: Optional[Dict[str, Any]] = None
    error_message: Optional[str] = None
    total_processing_time: float
    services_used: List[str]
