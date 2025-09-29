"""
사용자 모델 정의
"""

from datetime import datetime
from typing import List, Optional
from pydantic import BaseModel, Field, EmailStr
from enum import Enum


class UserRole(str, Enum):
    """사용자 역할"""
    ADMIN = "admin"
    DEVELOPER = "developer"
    VIEWER = "viewer"


class UserStatus(str, Enum):
    """사용자 상태"""
    ACTIVE = "active"
    INACTIVE = "inactive"
    SUSPENDED = "suspended"


class User(BaseModel):
    """사용자 모델"""
    id: str = Field(..., description="사용자 고유 ID")
    email: EmailStr = Field(..., description="이메일")
    username: str = Field(..., description="사용자명")
    full_name: str = Field(..., description="전체 이름")
    role: UserRole = Field(default=UserRole.DEVELOPER, description="사용자 역할")
    status: UserStatus = Field(default=UserStatus.ACTIVE, description="사용자 상태")
    created_at: datetime = Field(default_factory=datetime.utcnow, description="생성 시간")
    updated_at: datetime = Field(default_factory=datetime.utcnow, description="수정 시간")
    last_login: Optional[datetime] = Field(None, description="마지막 로그인 시간")
    preferences: dict = Field(default_factory=dict, description="사용자 설정")
    
    class Config:
        json_encoders = {
            datetime: lambda v: v.isoformat()
        }
