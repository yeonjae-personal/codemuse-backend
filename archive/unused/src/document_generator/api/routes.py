"""
Document Generator API Routes
"""

from fastapi import APIRouter, HTTPException, BackgroundTasks
from typing import List
import os

from ..models.generation_request import GenerationRequest, GenerationResponse
from ..models.document_analysis import DocumentAnalysis
from ..services.document_generator import DocumentGeneratorService

router = APIRouter(prefix="/api/v1/documents", tags=["Document Generator"])

# 서비스 인스턴스
document_service = DocumentGeneratorService()


@router.post("/generate", response_model=GenerationResponse)
async def generate_document(request: GenerationRequest):
    """문서 생성 API"""
    try:
        # 프로젝트 경로 검증
        if not os.path.exists(request.project_path):
            raise HTTPException(status_code=400, detail="프로젝트 경로가 존재하지 않습니다")
        
        # 문서 생성
        response = document_service.generate_document(request)
        return response
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"문서 생성 중 오류 발생: {str(e)}")


@router.get("/{document_id}")
async def get_document(document_id: str):
    """문서 조회 API"""
    try:
        # 생성된 문서 파일 찾기
        output_dir = "generated_docs"
        for file in os.listdir(output_dir):
            if file.startswith(document_id):
                file_path = os.path.join(output_dir, file)
                with open(file_path, 'r', encoding='utf-8') as f:
                    content = f.read()
                
                return {
                    "document_id": document_id,
                    "content": content,
                    "file_path": file_path
                }
        
        raise HTTPException(status_code=404, detail="문서를 찾을 수 없습니다")
        
    except FileNotFoundError:
        raise HTTPException(status_code=404, detail="문서를 찾을 수 없습니다")
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"문서 조회 중 오류 발생: {str(e)}")


@router.get("/")
async def list_documents():
    """문서 목록 조회 API"""
    try:
        output_dir = "generated_docs"
        if not os.path.exists(output_dir):
            return {"documents": []}
        
        documents = []
        for file in os.listdir(output_dir):
            if file.endswith('.md'):
                file_path = os.path.join(output_dir, file)
                stat = os.stat(file_path)
                documents.append({
                    "document_id": file.replace('.md', ''),
                    "file_name": file,
                    "created_at": stat.st_ctime,
                    "size": stat.st_size
                })
        
        return {"documents": documents}
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"문서 목록 조회 중 오류 발생: {str(e)}")


@router.delete("/{document_id}")
async def delete_document(document_id: str):
    """문서 삭제 API"""
    try:
        output_dir = "generated_docs"
        for file in os.listdir(output_dir):
            if file.startswith(document_id):
                file_path = os.path.join(output_dir, file)
                os.remove(file_path)
                return {"message": "문서가 삭제되었습니다"}
        
        raise HTTPException(status_code=404, detail="문서를 찾을 수 없습니다")
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"문서 삭제 중 오류 발생: {str(e)}")


@router.get("/health")
async def health_check():
    """헬스 체크 API"""
    return {"status": "healthy", "service": "document-generator"}
