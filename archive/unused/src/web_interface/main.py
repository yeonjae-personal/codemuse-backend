"""
Web Interface Service Main Application
"""

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
import uvicorn

from .api.routes import router

app = FastAPI(
    title="CodeMuse Web Interface",
    description="사용자 인터페이스 및 통합 API 서비스",
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

# 라우터 등록
app.include_router(router)

@app.get("/")
async def root():
    """루트 엔드포인트"""
    return {
        "service": "CodeMuse Web Interface",
        "version": "1.0.0",
        "status": "running"
    }

@app.get("/health")
async def health():
    """헬스 체크"""
    return {"status": "healthy"}

if __name__ == "__main__":
    uvicorn.run(
        "main:app",
        host="0.0.0.0",
        port=8000,
        reload=True
    )
