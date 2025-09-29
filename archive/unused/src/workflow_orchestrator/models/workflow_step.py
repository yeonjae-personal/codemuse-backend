"""
Workflow Step Models
"""

from typing import Dict, Any, Optional, List
from pydantic import BaseModel
from datetime import datetime


class WorkflowStep(BaseModel):
    """워크플로우 단계"""
    step_id: str
    step_name: str
    service_name: str
    action: str
    parameters: Dict[str, Any]
    status: str  # pending, running, completed, failed
    result: Optional[Dict[str, Any]] = None
    error_message: Optional[str] = None
    start_time: Optional[datetime] = None
    end_time: Optional[datetime] = None
    execution_time: Optional[float] = None


class WorkflowState(BaseModel):
    """워크플로우 상태"""
    request_id: str
    user_input: str
    workflow_type: str
    current_step: int
    total_steps: int
    steps: List[WorkflowStep]
    context: Dict[str, Any]
    created_at: datetime
    updated_at: datetime
