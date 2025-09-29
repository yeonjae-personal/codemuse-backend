"""
CodeMuse Backend Main Application

모든 서비스를 통합하는 메인 애플리케이션
"""

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
import uvicorn

# 각 서비스의 라우터 import
from document_generator.api.routes import router as document_router
from rag_engine.api.routes import router as rag_router
from llm_chat.api.routes import router as llm_router
from web_interface.api.routes import router as web_router
from workflow_orchestrator.api.routes import router as orchestrator_router

app = FastAPI(
    title="CodeMuse Backend",
    description="AI 기반 개발 어시스턴트 플랫폼 - 백엔드 서비스",
    version="1.0.0"
)

# CORS 설정
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 각 서비스 라우터 등록
app.include_router(document_router, prefix="/document-generator")
app.include_router(rag_router, prefix="/rag-engine")
app.include_router(llm_router, prefix="/llm-chat")
app.include_router(web_router, prefix="/web-interface")
app.include_router(orchestrator_router, prefix="/workflow-orchestrator")

@app.get("/")
async def root():
    """루트 엔드포인트"""
    return {
        "service": "CodeMuse Backend",
        "version": "1.0.0",
        "status": "running",
        "services": [
            "Document Generator",
            "RAG Engine", 
            "LLM Chat",
            "Web Interface",
            "Workflow Orchestrator"
        ]
    }

@app.get("/health")
async def health():
    """헬스 체크"""
    return {"status": "healthy"}

@app.get("/services")
async def list_services():
    """서비스 목록"""
    return {
        "services": [
            {
                "name": "Document Generator",
                "description": "소스코드 분석 및 MD 문서 자동 생성",
                "endpoints": ["/document-generator/api/v1/documents/"]
            },
            {
                "name": "RAG Engine", 
                "description": "문서 벡터화 및 의미적 검색",
                "endpoints": ["/rag-engine/api/v1/search", "/rag-engine/api/v1/embeddings/"]
            },
            {
                "name": "LLM Chat",
                "description": "LLM API 연동 및 대화 관리",
                "endpoints": ["/llm-chat/api/v1/chat/"]
            },
            {
                "name": "Web Interface",
                "description": "사용자 인터페이스 및 통합 API",
                "endpoints": ["/web-interface/api/v1/workflow/", "/web-interface/api/v1/integration/"]
            },
            {
                "name": "Workflow Orchestrator",
                "description": "전체 워크플로우 조율 및 서비스 간 통신 관리",
                "endpoints": ["/workflow-orchestrator/api/v1/orchestrator/"]
            }
        ]
    }

if __name__ == "__main__":
    uvicorn.run(
        "main:app",
        host="0.0.0.0",
        port=8000,
        reload=True
    )
