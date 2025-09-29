"""
Web Interface API Routes
"""

from fastapi import APIRouter, HTTPException, BackgroundTasks
from typing import List, Dict, Any

from ..models.workflow_request import WorkflowRequest, WorkflowResponse
from ..models.integration_request import IntegrationRequest, IntegrationResponse
from ..services.workflow_service import WorkflowService
from ..services.integration_service import IntegrationService

router = APIRouter(prefix="/api/v1", tags=["Web Interface"])

# 서비스 인스턴스
workflow_service = WorkflowService()
integration_service = IntegrationService()


@router.post("/workflow/execute", response_model=WorkflowResponse)
async def execute_workflow(request: WorkflowRequest, background_tasks: BackgroundTasks):
    """워크플로우 실행"""
    try:
        response = await workflow_service.execute_workflow(request)
        return response
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"워크플로우 실행 중 오류 발생: {str(e)}")


@router.get("/workflow/{request_id}", response_model=WorkflowResponse)
async def get_workflow_status(request_id: str):
    """워크플로우 상태 조회"""
    try:
        workflow = workflow_service.get_workflow_status(request_id)
        if not workflow:
            raise HTTPException(status_code=404, detail="워크플로우를 찾을 수 없습니다")
        return workflow
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"워크플로우 상태 조회 중 오류 발생: {str(e)}")


@router.get("/workflow", response_model=List[WorkflowResponse])
async def list_workflows():
    """활성 워크플로우 목록 조회"""
    try:
        workflows = workflow_service.list_active_workflows()
        return workflows
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"워크플로우 목록 조회 중 오류 발생: {str(e)}")


@router.post("/integration/execute", response_model=IntegrationResponse)
async def execute_integration(request: IntegrationRequest):
    """통합 액션 실행"""
    try:
        response = await integration_service.execute_action(request)
        return response
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"통합 액션 실행 중 오류 발생: {str(e)}")


@router.get("/services/health")
async def check_services_health():
    """서비스 헬스 체크"""
    try:
        health_status = await integration_service.health_check_services()
        return {
            "services": health_status,
            "overall_status": "healthy" if all(health_status.values()) else "degraded"
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"서비스 헬스 체크 중 오류 발생: {str(e)}")


@router.get("/config")
async def get_config():
    """시스템 설정 조회"""
    try:
        return {
            "available_workflows": [
                "code_analysis",
                "code_generation", 
                "bug_fix"
            ],
            "available_actions": [
                "analyze_code",
                "generate_document",
                "search_docs",
                "chat_with_llm"
            ],
            "service_urls": integration_service.service_urls
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"설정 조회 중 오류 발생: {str(e)}")


@router.get("/health")
async def health_check():
    """헬스 체크"""
    return {"status": "healthy", "service": "web-interface"}
