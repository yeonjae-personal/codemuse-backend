"""
Document Generator Service

문서 생성 서비스 API
"""

import os
import uuid
import uvicorn
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import Dict, List, Any, Optional
from datetime import datetime

from src.document_generator.services.document_generator import DocumentGeneratorService
from src.document_generator.models.generation_request import GenerationRequest, GenerationResponse

app = FastAPI(title="CodeMuse Document Generator")
document_generator = DocumentGeneratorService()

@app.get("/")
def read_root():
    """서비스 상태 확인"""
    return {
        "service": "CodeMuse Document Generator",
        "version": "1.0.0",
        "status": "running"
    }

@app.get("/health")
def health_check():
    """헬스 체크 엔드포인트"""
    return {"status": "ok"}

class GenerationRequestAPI(BaseModel):
    """문서 생성 요청 API 모델"""
    project_path: str
    template_type: str = "standard"
    output_format: str = "markdown"
    include_metrics: bool = True
    include_dependencies: bool = True
    include_issues: bool = True
    generate_individual_files: bool = False

@app.post("/api/v1/documents/generate", response_model=GenerationResponse)
def generate_document(request: GenerationRequestAPI):
    """문서 생성 API 엔드포인트"""
    try:
        # API 요청을 내부 모델로 변환
        gen_request = GenerationRequest(
            project_path=request.project_path,
            template_type=request.template_type,
            output_format=request.output_format,
            include_metrics=request.include_metrics,
            include_dependencies=request.include_dependencies,
            include_issues=request.include_issues,
            generate_individual_files=request.generate_individual_files
        )
        
        # 문서 생성 서비스 호출
        response = document_generator.generate_document(gen_request)
        return response
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

if __name__ == "__main__":
    # 서비스 실행
    uvicorn.run(app, host="0.0.0.0", port=8001)