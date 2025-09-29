"""
Workflow Orchestrator

워크플로우 오케스트레이터 핵심 서비스
"""

import uuid
import time
import asyncio
from typing import Dict, Any, List, Optional
from datetime import datetime

from .service_client import ServiceClient
from .workflow_planner import WorkflowPlanner
from ..models.orchestration_request import OrchestrationRequest, OrchestrationResponse
from ..models.workflow_step import WorkflowStep, WorkflowState


class WorkflowOrchestrator:
    """워크플로우 오케스트레이터"""
    
    def __init__(self):
        self.service_client = ServiceClient()
        self.workflow_planner = WorkflowPlanner()
        self.active_workflows: Dict[str, WorkflowState] = {}
    
    async def orchestrate(self, request: OrchestrationRequest) -> OrchestrationResponse:
        """워크플로우 오케스트레이션 실행"""
        request_id = str(uuid.uuid4())
        start_time = time.time()
        
        # 워크플로우 계획 수립
        workflow_steps = self.workflow_planner.plan_workflow(
            request.workflow_type,
            request.user_input,
            request.context
        )
        
        # 워크플로우 상태 생성
        workflow_state = WorkflowState(
            request_id=request_id,
            user_input=request.user_input,
            workflow_type=request.workflow_type,
            current_step=0,
            total_steps=len(workflow_steps),
            steps=workflow_steps,
            context=request.context or {},
            created_at=datetime.now(),
            updated_at=datetime.now()
        )
        
        self.active_workflows[request_id] = workflow_state
        
        try:
            # 워크플로우 실행
            final_result = await self._execute_workflow(workflow_state)
            
            # 성공 응답
            return OrchestrationResponse(
                request_id=request_id,
                status="completed",
                workflow_steps=[self._step_to_dict(step) for step in workflow_steps],
                final_result=final_result,
                total_processing_time=time.time() - start_time,
                services_used=list(set(step.service_name for step in workflow_steps))
            )
            
        except Exception as e:
            # 실패 응답
            return OrchestrationResponse(
                request_id=request_id,
                status="failed",
                workflow_steps=[self._step_to_dict(step) for step in workflow_steps],
                error_message=str(e),
                total_processing_time=time.time() - start_time,
                services_used=list(set(step.service_name for step in workflow_steps))
            )
    
    async def _execute_workflow(self, workflow_state: WorkflowState) -> Dict[str, Any]:
        """워크플로우 실행"""
        results = {}
        
        for i, step in enumerate(workflow_state.steps):
            workflow_state.current_step = i + 1
            workflow_state.updated_at = datetime.now()
            
            try:
                # 단계 실행
                step_result = await self._execute_step(step, results)
                results[f"step_{i+1}"] = step_result
                
            except Exception as e:
                step.status = "failed"
                step.error_message = str(e)
                step.end_time = datetime.now()
                if step.start_time:
                    step.execution_time = (step.end_time - step.start_time).total_seconds()
                
                raise Exception(f"단계 {i+1} 실행 실패: {str(e)}")
        
        return results
    
    async def _execute_step(self, step: WorkflowStep, previous_results: Dict[str, Any]) -> Dict[str, Any]:
        """단계 실행"""
        step.status = "running"
        step.start_time = datetime.now()
        
        try:
            # 매개변수에 이전 결과 반영
            parameters = self._merge_parameters(step.parameters, previous_results)
            
            # 서비스 호출
            if step.service_name == "document_generator":
                result = await self._call_document_generator(step.action, parameters)
            elif step.service_name == "rag_engine":
                result = await self._call_rag_engine(step.action, parameters)
            elif step.service_name == "llm_chat":
                result = await self._call_llm_chat(step.action, parameters)
            elif step.service_name == "web_interface":
                result = await self._call_web_interface(step.action, parameters)
            else:
                raise ValueError(f"지원하지 않는 서비스: {step.service_name}")
            
            # 성공 처리
            step.status = "completed"
            step.result = result
            step.end_time = datetime.now()
            step.execution_time = (step.end_time - step.start_time).total_seconds()
            
            return result
            
        except Exception as e:
            # 실패 처리
            step.status = "failed"
            step.error_message = str(e)
            step.end_time = datetime.now()
            if step.start_time:
                step.execution_time = (step.end_time - step.start_time).total_seconds()
            raise
    
    def _merge_parameters(self, parameters: Dict[str, Any], previous_results: Dict[str, Any]) -> Dict[str, Any]:
        """매개변수에 이전 결과 병합"""
        merged = parameters.copy()
        
        # 이전 결과를 컨텍스트로 추가
        if previous_results:
            merged["context"] = previous_results
        
        return merged
    
    async def _call_document_generator(self, action: str, parameters: Dict[str, Any]) -> Dict[str, Any]:
        """Document Generator 서비스 호출"""
        if action == "analyze_code":
            return await self.service_client.call_service(
                "document_generator",
                "/api/v1/documents/generate",
                "POST",
                parameters
            )
        else:
            raise ValueError(f"지원하지 않는 Document Generator 액션: {action}")
    
    async def _call_rag_engine(self, action: str, parameters: Dict[str, Any]) -> Dict[str, Any]:
        """RAG Engine 서비스 호출"""
        if action == "search_documents":
            return await self.service_client.call_service(
                "rag_engine",
                "/api/v1/search",
                "POST",
                parameters
            )
        else:
            raise ValueError(f"지원하지 않는 RAG Engine 액션: {action}")
    
    async def _call_llm_chat(self, action: str, parameters: Dict[str, Any]) -> Dict[str, Any]:
        """LLM Chat 서비스 호출"""
        if action in ["chat", "generate_code", "suggest_fix"]:
            # 세션 ID가 없으면 새 세션 생성
            session_id = parameters.get("session_id")
            if not session_id:
                session_response = await self.service_client.call_service(
                    "llm_chat",
                    "/api/v1/chat/sessions",
                    "POST",
                    {"title": "Orchestrator Session"}
                )
                session_id = session_response["id"]
                parameters["session_id"] = session_id
            
            return await self.service_client.call_service(
                "llm_chat",
                f"/api/v1/chat/sessions/{session_id}/messages",
                "POST",
                {
                    "message": parameters["message"],
                    "model": parameters.get("model", "gpt-3.5-turbo"),
                    "context": parameters.get("context")
                }
            )
        else:
            raise ValueError(f"지원하지 않는 LLM Chat 액션: {action}")
    
    async def _call_web_interface(self, action: str, parameters: Dict[str, Any]) -> Dict[str, Any]:
        """Web Interface 서비스 호출"""
        if action == "execute_workflow":
            return await self.service_client.call_service(
                "web_interface",
                "/api/v1/workflow/execute",
                "POST",
                parameters
            )
        else:
            raise ValueError(f"지원하지 않는 Web Interface 액션: {action}")
    
    def _step_to_dict(self, step: WorkflowStep) -> Dict[str, Any]:
        """워크플로우 단계를 딕셔너리로 변환"""
        return {
            "step_id": step.step_id,
            "step_name": step.step_name,
            "service_name": step.service_name,
            "action": step.action,
            "status": step.status,
            "result": step.result,
            "error_message": step.error_message,
            "execution_time": step.execution_time
        }
    
    def get_workflow_status(self, request_id: str) -> Optional[WorkflowState]:
        """워크플로우 상태 조회"""
        return self.active_workflows.get(request_id)
    
    def list_active_workflows(self) -> List[WorkflowState]:
        """활성 워크플로우 목록"""
        return list(self.active_workflows.values())
    
    async def health_check(self) -> Dict[str, Any]:
        """헬스 체크"""
        services_health = await self.service_client.health_check_all()
        
        return {
            "orchestrator": "healthy",
            "services": services_health,
            "active_workflows": len(self.active_workflows),
            "timestamp": datetime.now().isoformat()
        }
