"""
워크플로우 공통 모델들
"""

from pydantic import BaseModel
from typing import Dict, Any, List, Optional
from datetime import datetime


class WorkflowRequest(BaseModel):
    """워크플로우 요청 모델"""
    query: str
    model: str = "gpt-3.5-turbo"
    use_rag: bool = True
    session_id: Optional[str] = None
    context: Optional[Dict[str, Any]] = None


class WorkflowResponse(BaseModel):
    """워크플로우 응답 모델"""
    success: bool
    response: str
    original_query: str
    standardized_query: Optional[str] = None
    keywords: Optional[List[str]] = None
    search_results_count: int = 0
    quality_score: Optional[float] = None
    model_used: str
    processing_time: float
    error: Optional[str] = None


class StandardizationResult(BaseModel):
    """표준화 결과 모델"""
    success: bool
    original_query: str
    standardized_query: str
    model_used: str
    error: Optional[str] = None


class KeywordExtractionResult(BaseModel):
    """키워드 추출 결과 모델"""
    success: bool
    keywords: List[str]
    query: str
    model_used: str
    error: Optional[str] = None


class SearchResult(BaseModel):
    """검색 결과 모델"""
    success: bool
    results: List[Dict[str, Any]]
    query: str
    keywords: List[str]
    strategy: str
    total_found: int
    error: Optional[str] = None


class QualityReviewResult(BaseModel):
    """품질 검토 결과 모델"""
    success: bool
    reviewed_results: List[Dict[str, Any]]
    quality_score: float
    improvements: List[str]
    original_count: int
    improved_count: int
    error: Optional[str] = None


class ResponseGenerationResult(BaseModel):
    """응답 생성 결과 모델"""
    success: bool
    response: str
    original_query: str
    context_sources: int
    model_used: str
    response_length: int
    error: Optional[str] = None
