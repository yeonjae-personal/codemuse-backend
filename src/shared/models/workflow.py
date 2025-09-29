"""
워크플로우 모델 정의
"""

from datetime import datetime
from typing import List, Dict, Optional, Any
from pydantic import BaseModel, Field
from enum import Enum


class WorkflowStatus(str, Enum):
    """워크플로우 상태"""
    PENDING = "pending"
    RUNNING = "running"
    COMPLETED = "completed"
    FAILED = "failed"
    CANCELLED = "cancelled"


class StepStatus(str, Enum):
    """단계 상태"""
    PENDING = "pending"
    RUNNING = "running"
    COMPLETED = "completed"
    FAILED = "failed"
    SKIPPED = "skipped"


class WorkflowStep(BaseModel):
    """워크플로우 단계 모델"""
    id: str = Field(..., description="단계 고유 ID")
    name: str = Field(..., description="단계 이름")
    service: str = Field(..., description="담당 서비스")
    status: StepStatus = Field(default=StepStatus.PENDING, description="단계 상태")
    input_data: Dict[str, Any] = Field(default_factory=dict, description="입력 데이터")
    output_data: Dict[str, Any] = Field(default_factory=dict, description="출력 데이터")
    error_message: Optional[str] = Field(None, description="에러 메시지")
    started_at: Optional[datetime] = Field(None, description="시작 시간")
    completed_at: Optional[datetime] = Field(None, description="완료 시간")
    
    class Config:
        json_encoders = {
            datetime: lambda v: v.isoformat()
        }


class WorkflowState(BaseModel):
    """워크플로우 상태 모델"""
    id: str = Field(..., description="워크플로우 고유 ID")
    user_id: str = Field(..., description="사용자 ID")
    session_id: str = Field(..., description="세션 ID")
    name: str = Field(..., description="워크플로우 이름")
    status: WorkflowStatus = Field(default=WorkflowStatus.PENDING, description="워크플로우 상태")
    steps: List[WorkflowStep] = Field(default_factory=list, description="워크플로우 단계들")
    context: Dict[str, Any] = Field(default_factory=dict, description="컨텍스트 정보")
    created_at: datetime = Field(default_factory=datetime.utcnow, description="생성 시간")
    updated_at: datetime = Field(default_factory=datetime.utcnow, description="수정 시간")
    completed_at: Optional[datetime] = Field(None, description="완료 시간")
    
    class Config:
        json_encoders = {
            datetime: lambda v: v.isoformat()
        }
