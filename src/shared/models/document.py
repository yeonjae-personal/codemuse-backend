"""
문서 모델 정의
"""

from datetime import datetime
from typing import List, Dict, Optional
from pydantic import BaseModel, Field
from enum import Enum


class DocumentType(str, Enum):
    """문서 타입"""
    CODE_ANALYSIS = "code_analysis"
    API_DOCUMENTATION = "api_documentation"
    ARCHITECTURE_DOCUMENTATION = "architecture_documentation"
    DEPENDENCY_GRAPH = "dependency_graph"
    ISSUE_REPORT = "issue_report"


class DocumentStatus(str, Enum):
    """문서 상태"""
    PENDING = "pending"
    PROCESSING = "processing"
    COMPLETED = "completed"
    FAILED = "failed"


class Document(BaseModel):
    """문서 모델"""
    id: str = Field(..., description="문서 고유 ID")
    title: str = Field(..., description="문서 제목")
    content: str = Field(..., description="문서 내용")
    source_file: str = Field(..., description="원본 파일 경로")
    document_type: DocumentType = Field(..., description="문서 타입")
    status: DocumentStatus = Field(default=DocumentStatus.PENDING, description="문서 상태")
    created_at: datetime = Field(default_factory=datetime.utcnow, description="생성 시간")
    updated_at: datetime = Field(default_factory=datetime.utcnow, description="수정 시간")
    metadata: Dict[str, any] = Field(default_factory=dict, description="메타데이터")
    embedding: Optional[List[float]] = Field(None, description="벡터 임베딩")
    
    class Config:
        json_encoders = {
            datetime: lambda v: v.isoformat()
        }
