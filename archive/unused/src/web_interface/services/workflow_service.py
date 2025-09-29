"""
Workflow Service

워크플로우 관리 서비스
"""

import uuid
import time
from typing import Dict, Any, List, Optional
from datetime import datetime

from ..models.workflow_request import WorkflowRequest, WorkflowResponse
from .integration_service import IntegrationService


class WorkflowService:
    """워크플로우 서비스"""
    
    def __init__(self):
        self.integration_service = IntegrationService()
        self.active_workflows: Dict[str, WorkflowResponse] = {}
    
    async def execute_workflow(self, request: WorkflowRequest) -> WorkflowResponse:
        """워크플로우 실행"""
        request_id = str(uuid.uuid4())
        start_time = time.time()
        
        # 워크플로우 초기화
        workflow_response = WorkflowResponse(
            request_id=request_id,
            status="processing",
            processing_time=0.0,
            steps_completed=[]
        )
        
        self.active_workflows[request_id] = workflow_response
        
        try:
            if request.workflow_type == "code_analysis":
                result = await self._execute_code_analysis_workflow(request, workflow_response)
            elif request.workflow_type == "code_generation":
                result = await self._execute_code_generation_workflow(request, workflow_response)
            elif request.workflow_type == "bug_fix":
                result = await self._execute_bug_fix_workflow(request, workflow_response)
            else:
                raise ValueError(f"지원하지 않는 워크플로우 타입: {request.workflow_type}")
            
            # 성공 완료
            workflow_response.status = "completed"
            workflow_response.result = result
            workflow_response.processing_time = time.time() - start_time
            
        except Exception as e:
            # 실패 처리
            workflow_response.status = "failed"
            workflow_response.error_message = str(e)
            workflow_response.processing_time = time.time() - start_time
        
        return workflow_response
    
    async def _execute_code_analysis_workflow(self, request: WorkflowRequest, workflow: WorkflowResponse) -> Dict[str, Any]:
        """코드 분석 워크플로우 실행"""
        steps = []
        
        # 1단계: 코드 분석
        steps.append("코드 분석 시작")
        analysis_request = IntegrationRequest(
            action="analyze_code",
            parameters={"project_path": request.context.get("project_path") if request.context else "."}
        )
        analysis_response = await self.integration_service.execute_action(analysis_request)
        
        if not analysis_response.success:
            raise Exception(f"코드 분석 실패: {analysis_response.error}")
        
        steps.append("코드 분석 완료")
        workflow.steps_completed = steps.copy()
        
        # 2단계: 문서 생성
        steps.append("문서 생성 시작")
        doc_request = IntegrationRequest(
            action="generate_document",
            parameters={
                "project_path": request.context.get("project_path") if request.context else ".",
                "output_format": "markdown"
            }
        )
        doc_response = await self.integration_service.execute_action(doc_request)
        
        if not doc_response.success:
            raise Exception(f"문서 생성 실패: {doc_response.error}")
        
        steps.append("문서 생성 완료")
        workflow.steps_completed = steps.copy()
        
        return {
            "analysis_result": analysis_response.data,
            "document_result": doc_response.data,
            "workflow_type": "code_analysis"
        }
    
    async def _execute_code_generation_workflow(self, request: WorkflowRequest, workflow: WorkflowResponse) -> Dict[str, Any]:
        """코드 생성 워크플로우 실행"""
        steps = []
        
        # 1단계: 관련 문서 검색
        steps.append("관련 문서 검색 시작")
        search_request = IntegrationRequest(
            action="search_docs",
            parameters={
                "query": request.user_input,
                "limit": 5
            }
        )
        search_response = await self.integration_service.execute_action(search_request)
        
        if not search_response.success:
            raise Exception(f"문서 검색 실패: {search_response.error}")
        
        steps.append("관련 문서 검색 완료")
        workflow.steps_completed = steps.copy()
        
        # 2단계: LLM을 통한 코드 생성
        steps.append("코드 생성 시작")
        chat_request = IntegrationRequest(
            action="chat_with_llm",
            parameters={
                "message": request.user_input,
                "context": {
                    "search_results": search_response.data.get("results", [])
                },
                "model": "gpt-4"
            },
            session_id=request.session_id
        )
        chat_response = await self.integration_service.execute_action(chat_request)
        
        if not chat_response.success:
            raise Exception(f"코드 생성 실패: {chat_response.error}")
        
        steps.append("코드 생성 완료")
        workflow.steps_completed = steps.copy()
        
        return {
            "search_results": search_response.data,
            "generated_code": chat_response.data,
            "workflow_type": "code_generation"
        }
    
    async def _execute_bug_fix_workflow(self, request: WorkflowRequest, workflow: WorkflowResponse) -> Dict[str, Any]:
        """버그 수정 워크플로우 실행"""
        steps = []
        
        # 1단계: 코드 분석
        steps.append("코드 분석 시작")
        analysis_request = IntegrationRequest(
            action="analyze_code",
            parameters={"project_path": request.context.get("project_path") if request.context else "."}
        )
        analysis_response = await self.integration_service.execute_action(analysis_request)
        
        if not analysis_response.success:
            raise Exception(f"코드 분석 실패: {analysis_response.error}")
        
        steps.append("코드 분석 완료")
        workflow.steps_completed = steps.copy()
        
        # 2단계: 버그 관련 문서 검색
        steps.append("버그 관련 문서 검색 시작")
        search_request = IntegrationRequest(
            action="search_docs",
            parameters={
                "query": f"bug fix {request.user_input}",
                "limit": 5
            }
        )
        search_response = await self.integration_service.execute_action(search_request)
        
        if not search_response.success:
            raise Exception(f"문서 검색 실패: {search_response.error}")
        
        steps.append("버그 관련 문서 검색 완료")
        workflow.steps_completed = steps.copy()
        
        # 3단계: LLM을 통한 버그 수정 제안
        steps.append("버그 수정 제안 생성 시작")
        chat_request = IntegrationRequest(
            action="chat_with_llm",
            parameters={
                "message": f"다음 버그를 수정하는 방법을 제안해주세요: {request.user_input}",
                "context": {
                    "analysis_results": analysis_response.data,
                    "search_results": search_response.data.get("results", [])
                },
                "model": "gpt-4"
            },
            session_id=request.session_id
        )
        chat_response = await self.integration_service.execute_action(chat_request)
        
        if not chat_response.success:
            raise Exception(f"버그 수정 제안 생성 실패: {chat_response.error}")
        
        steps.append("버그 수정 제안 생성 완료")
        workflow.steps_completed = steps.copy()
        
        return {
            "analysis_results": analysis_response.data,
            "search_results": search_response.data,
            "bug_fix_suggestions": chat_response.data,
            "workflow_type": "bug_fix"
        }
    
    def get_workflow_status(self, request_id: str) -> Optional[WorkflowResponse]:
        """워크플로우 상태 조회"""
        return self.active_workflows.get(request_id)
    
    def list_active_workflows(self) -> List[WorkflowResponse]:
        """활성 워크플로우 목록"""
        return list(self.active_workflows.values())
