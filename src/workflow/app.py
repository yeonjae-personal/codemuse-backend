from fastapi import FastAPI, HTTPException, Request
from fastapi.responses import StreamingResponse
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import json
import os
from typing import Dict, Any, Optional

# 환경 변수에서 서비스 URL 가져오기
LLM_SERVICE_URL = os.getenv("LLM_SERVICE_URL", "http://localhost:8004")
RAG_SERVICE_URL = os.getenv("RAG_SERVICE_URL", "http://localhost:8003")

# 최적화된 Chunk 워크플로우 서비스 import
from .services.chunk_workflow_service import ChunkWorkflowService, ChunkWorkflowRequest

app = FastAPI(title="Workflow Orchestrator", version="1.0.0")

# CORS 설정
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class WorkflowRequest(BaseModel):
    query: str
    model: str = "gpt-3.5-turbo"
    use_rag: bool = True
    session_id: Optional[str] = None

@app.get("/")
async def root():
    return {"message": "Workflow Orchestrator is running"}

@app.get("/health")
async def health_check():
    return {"status": "healthy", "service": "workflow_orchestrator"}

@app.options("/workflow/stream")
async def options_workflow_stream():
    """CORS preflight 요청 처리"""
    return {"message": "OK"}

@app.post("/workflow/stream")
async def process_workflow_stream(request: WorkflowRequest):
    """SSE로 진행상황 + 최종응답 토큰 스트리밍"""
    import json as _json
    try:
        chunk_service = ChunkWorkflowService()

        def format_sse(event: str, data: str) -> bytes:
            return f"event: {event}\ndata: {data}\n\n".encode("utf-8")

        async def generator():
            # ChunkWorkflowRequest 객체 생성
            chunk_request = ChunkWorkflowRequest(
                query=request.query,
                model=request.model,
                use_rag=request.use_rag,
                session_id=request.session_id
            )
            
            # 1단계: 질문 표준화
            yield format_sse("status", "Step 1: Question standardization in progress...")
            yield format_sse("step", "1")
            
            # 2단계: 키워드 추출
            yield format_sse("status", "Step 2: Keyword extraction in progress...")
            yield format_sse("step", "2")
            
            # 3단계: RAG 검색
            yield format_sse("status", "Step 3: RAG search in progress...")
            yield format_sse("step", "3")
            
            # 4단계: 품질 검토
            yield format_sse("status", "Step 4: Quality review in progress...")
            yield format_sse("step", "4")
            
            # 5단계: 최종 응답 생성
            yield format_sse("status", "Step 5: Final response generation in progress...")
            yield format_sse("step", "5")
            
            # 실제 워크플로우 실행
            result = await chunk_service.process_chunk_workflow(chunk_request)
            
            # 표준화된 질문 전송
            standardized_query = result.get("standardized_query", request.query)
            yield format_sse("standardized_question", standardized_query)
            
            # 결과를 스트리밍으로 전송
            yield format_sse("status", "Response generation completed")
            yield format_sse("started", "")
            yield format_sse("model", result.get("model", request.model))
            
            # 최종 응답 스트리밍
            response_text = result.get("response", "")
            for token in response_text:
                yield format_sse("token", token)
            
            # 메트릭스 전송
            metrics = {
                "processing_time": result.get("processing_time", 0),
                "search_results_count": result.get("search_results_count", 0),
                "quality_score": result.get("quality_score", 0),
                "keywords": result.get("keywords", [])
            }
            yield format_sse("metrics", _json.dumps(metrics))
            yield format_sse("done", "ok")

        headers = {
            "Cache-Control": "no-cache",
            "Content-Type": "text/event-stream",
            "Connection": "keep-alive",
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "GET, POST, OPTIONS",
            "Access-Control-Allow-Headers": "Content-Type, Authorization",
        }
        return StreamingResponse(generator(), headers=headers, media_type="text/event-stream")
    
    except Exception as e:
        print(f"❌ 스트리밍 워크플로우 오류: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8006)