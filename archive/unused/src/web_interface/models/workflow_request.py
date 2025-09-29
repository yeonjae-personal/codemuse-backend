"""
Workflow Request/Response Models
"""

from typing import Dict, Any, Optional, List
from pydantic import BaseModel


class WorkflowRequest(BaseModel):
    """워크플로우 요청"""
    user_input: str
    session_id: Optional[str] = None
    workflow_type: str = "code_analysis"  # code_analysis, code_generation, bug_fix
    context: Optional[Dict[str, Any]] = None


class WorkflowResponse(BaseModel):
    """워크플로우 응답"""
    request_id: str
    status: str  # pending, processing, completed, failed
    result: Optional[Dict[str, Any]] = None
    error_message: Optional[str] = None
    processing_time: float
    steps_completed: List[str]
