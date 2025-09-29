"""
Integration Service

다른 서비스들과의 통합 서비스
"""

import time
import httpx
from typing import Dict, Any, Optional
from datetime import datetime

from ..models.integration_request import IntegrationRequest, IntegrationResponse


class IntegrationService:
    """통합 서비스"""
    
    def __init__(self):
        self.service_urls = {
            "document_generator": "http://localhost:8002",
            "rag_engine": "http://localhost:8003", 
            "llm_chat": "http://localhost:8004",
            "workflow_orchestrator": "http://localhost:8006"
        }
    
    async def execute_action(self, request: IntegrationRequest) -> IntegrationResponse:
        """액션 실행"""
        start_time = time.time()
        
        try:
            if request.action == "analyze_code":
                return await self._analyze_code(request)
            elif request.action == "generate_document":
                return await self._generate_document(request)
            elif request.action == "search_docs":
                return await self._search_documents(request)
            elif request.action == "chat_with_llm":
                return await self._chat_with_llm(request)
            else:
                return IntegrationResponse(
                    success=False,
                    error=f"지원하지 않는 액션: {request.action}",
                    service_used="web_interface",
                    response_time=time.time() - start_time
                )
                
        except Exception as e:
            return IntegrationResponse(
                success=False,
                error=str(e),
                service_used="web_interface",
                response_time=time.time() - start_time
            )
    
    async def _analyze_code(self, request: IntegrationRequest) -> IntegrationResponse:
        """코드 분석"""
        async with httpx.AsyncClient() as client:
            response = await client.post(
                f"{self.service_urls['document_generator']}/api/v1/documents/generate",
                json={
                    "project_path": request.parameters.get("project_path"),
                    "output_format": "markdown",
                    "include_metrics": True,
                    "include_dependencies": True,
                    "include_issues": True
                }
            )
            
            if response.status_code == 200:
                data = response.json()
                return IntegrationResponse(
                    success=True,
                    data=data,
                    service_used="document_generator",
                    response_time=time.time() - time.time()
                )
            else:
                return IntegrationResponse(
                    success=False,
                    error=f"Document Generator 오류: {response.status_code}",
                    service_used="document_generator",
                    response_time=time.time() - time.time()
                )
    
    async def _generate_document(self, request: IntegrationRequest) -> IntegrationResponse:
        """문서 생성"""
        async with httpx.AsyncClient() as client:
            response = await client.post(
                f"{self.service_urls['document_generator']}/api/v1/documents/generate",
                json=request.parameters
            )
            
            if response.status_code == 200:
                data = response.json()
                return IntegrationResponse(
                    success=True,
                    data=data,
                    service_used="document_generator",
                    response_time=time.time() - time.time()
                )
            else:
                return IntegrationResponse(
                    success=False,
                    error=f"Document Generator 오류: {response.status_code}",
                    service_used="document_generator",
                    response_time=time.time() - time.time()
                )
    
    async def _search_documents(self, request: IntegrationRequest) -> IntegrationResponse:
        """문서 검색"""
        async with httpx.AsyncClient() as client:
            response = await client.post(
                f"{self.service_urls['rag_engine']}/api/v1/search",
                json={
                    "query": request.parameters.get("query"),
                    "limit": request.parameters.get("limit", 10),
                    "threshold": request.parameters.get("threshold", 0.7)
                }
            )
            
            if response.status_code == 200:
                data = response.json()
                return IntegrationResponse(
                    success=True,
                    data=data,
                    service_used="rag_engine",
                    response_time=time.time() - time.time()
                )
            else:
                return IntegrationResponse(
                    success=False,
                    error=f"RAG Engine 오류: {response.status_code}",
                    service_used="rag_engine",
                    response_time=time.time() - time.time()
                )
    
    async def _chat_with_llm(self, request: IntegrationRequest) -> IntegrationResponse:
        """LLM 채팅"""
        async with httpx.AsyncClient() as client:
            response = await client.post(
                f"{self.service_urls['llm_chat']}/api/v1/chat/sessions/{request.session_id}/messages",
                json={
                    "message": request.parameters.get("message"),
                    "model": request.parameters.get("model", "gpt-3.5-turbo"),
                    "context": request.parameters.get("context")
                }
            )
            
            if response.status_code == 200:
                data = response.json()
                return IntegrationResponse(
                    success=True,
                    data=data,
                    service_used="llm_chat",
                    response_time=time.time() - time.time()
                )
            else:
                return IntegrationResponse(
                    success=False,
                    error=f"LLM Chat 오류: {response.status_code}",
                    service_used="llm_chat",
                    response_time=time.time() - time.time()
                )
    
    async def health_check_services(self) -> Dict[str, bool]:
        """서비스 헬스 체크"""
        health_status = {}
        
        async with httpx.AsyncClient() as client:
            for service_name, url in self.service_urls.items():
                try:
                    response = await client.get(f"{url}/health", timeout=5.0)
                    health_status[service_name] = response.status_code == 200
                except:
                    health_status[service_name] = False
        
        return health_status
