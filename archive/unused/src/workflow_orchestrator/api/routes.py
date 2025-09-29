"""
Workflow Orchestrator API Routes
"""

from fastapi import APIRouter, HTTPException, BackgroundTasks
from typing import List, Dict, Any

from ..models.orchestration_request import OrchestrationRequest, OrchestrationResponse
from ..models.workflow_step import WorkflowState
from ..services.orchestrator import WorkflowOrchestrator

router = APIRouter(prefix="/api/v1/orchestrator", tags=["Workflow Orchestrator"])

# 서비스 인스턴스
orchestrator = WorkflowOrchestrator()


@router.post("/execute", response_model=OrchestrationResponse)
async def execute_orchestration(request: OrchestrationRequest, background_tasks: BackgroundTasks):
    """워크플로우 오케스트레이션 실행"""
    try:
        response = await orchestrator.orchestrate(request)
        return response
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"오케스트레이션 실행 중 오류 발생: {str(e)}")


@router.get("/workflow/{request_id}")
async def get_workflow_status(request_id: str):
    """워크플로우 상태 조회"""
    try:
        workflow = orchestrator.get_workflow_status(request_id)
        if not workflow:
            raise HTTPException(status_code=404, detail="워크플로우를 찾을 수 없습니다")
        
        return {
            "request_id": workflow.request_id,
            "user_input": workflow.user_input,
            "workflow_type": workflow.workflow_type,
            "current_step": workflow.current_step,
            "total_steps": workflow.total_steps,
            "status": "processing" if workflow.current_step < workflow.total_steps else "completed",
            "steps": [
                {
                    "step_id": step.step_id,
                    "step_name": step.step_name,
                    "service_name": step.service_name,
                    "action": step.action,
                    "status": step.status,
                    "execution_time": step.execution_time,
                    "error_message": step.error_message
                }
                for step in workflow.steps
            ],
            "created_at": workflow.created_at.isoformat(),
            "updated_at": workflow.updated_at.isoformat()
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"워크플로우 상태 조회 중 오류 발생: {str(e)}")


@router.get("/workflows")
async def list_workflows():
    """활성 워크플로우 목록 조회"""
    try:
        workflows = orchestrator.list_active_workflows()
        return {
            "workflows": [
                {
                    "request_id": workflow.request_id,
                    "user_input": workflow.user_input,
                    "workflow_type": workflow.workflow_type,
                    "current_step": workflow.current_step,
                    "total_steps": workflow.total_steps,
                    "status": "processing" if workflow.current_step < workflow.total_steps else "completed",
                    "created_at": workflow.created_at.isoformat(),
                    "updated_at": workflow.updated_at.isoformat()
                }
                for workflow in workflows
            ],
            "total_count": len(workflows)
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"워크플로우 목록 조회 중 오류 발생: {str(e)}")


@router.get("/health")
async def health_check():
    """헬스 체크"""
    try:
        health_info = await orchestrator.health_check()
        return health_info
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"헬스 체크 중 오류 발생: {str(e)}")


@router.get("/services/status")
async def get_services_status():
    """서비스 상태 조회"""
    try:
        health_info = await orchestrator.health_check()
        return {
            "services": health_info["services"],
            "overall_status": "healthy" if all(health_info["services"].values()) else "degraded",
            "timestamp": health_info["timestamp"]
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"서비스 상태 조회 중 오류 발생: {str(e)}")


@router.get("/workflow-types")
async def get_workflow_types():
    """지원하는 워크플로우 타입 목록"""
    try:
        return {
            "workflow_types": [
                {
                    "type": "general",
                    "description": "일반적인 질문 및 대화"
                },
                {
                    "type": "code_analysis", 
                    "description": "코드 분석 및 문서 생성"
                },
                {
                    "type": "code_generation",
                    "description": "코드 생성 및 구현"
                },
                {
                    "type": "bug_fix",
                    "description": "버그 수정 및 문제 해결"
                }
            ]
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"워크플로우 타입 조회 중 오류 발생: {str(e)}")
