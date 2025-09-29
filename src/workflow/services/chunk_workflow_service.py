"""
최적화된 Chunk 기반 워크플로우 서비스
모듈화된 5단계 워크플로우를 사용합니다.
"""

import asyncio
import time
from typing import Dict, Any, Optional
from pydantic import BaseModel
from .workflow_orchestrator import WorkflowOrchestrator
from .shared.models import WorkflowRequest, WorkflowResponse


class ChunkWorkflowRequest(BaseModel):
    """Chunk 워크플로우 요청"""
    query: str
    model: str = "gpt-3.5-turbo"
    use_rag: bool = True
    session_id: Optional[str] = None


class ChunkWorkflowService:
    """최적화된 Chunk 워크플로우 서비스"""
    
    def __init__(self):
        self.orchestrator = WorkflowOrchestrator()
    
    async def process_chunk_workflow(self, request: ChunkWorkflowRequest, status_callback=None) -> Dict[str, Any]:
        """
        최적화된 Chunk 기반 워크플로우 처리
        
        1. 사용자 질문을 LLM이 용어집 기반으로 표준화
        2. 표준화된 질문에서 키워드 추출
        3. 표준화된 질문+키워드로 RAG 검색 (소스검색+독스트링검색+MD검색)
        4. 검색된 RAG 결과가 잘된건지 LLM 검토 및 품질 개선
        5. 개선된 RAG 데이터로 최종 답변 생성
        
        Args:
            request: 워크플로우 요청
            
        Returns:
            처리 결과
        """
        try:
            # 요청 변환
            workflow_request = WorkflowRequest(
                query=request.query,
                model=request.model,
                use_rag=request.use_rag,
                session_id=request.session_id
            )
            
            # 오케스트레이터 실행
            result = await self.orchestrator.process_workflow(workflow_request, status_callback)
            
            # 결과 변환
            return {
                "success": result.success,
                "response": result.response,
                "query": result.original_query,
                "standardized_query": result.standardized_query,
                "keywords": result.keywords,
                "search_results_count": result.search_results_count,
                "quality_score": result.quality_score,
                "model": result.model_used,
                "processing_time": result.processing_time,
                "error": result.error
            }
            
        except Exception as e:
            return {
                "success": False,
                "response": "워크플로우 처리 중 오류가 발생했습니다.",
                "query": request.query,
                "error": str(e),
                "processing_time": 0.0
            }
