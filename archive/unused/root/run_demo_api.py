"""
상무님 데모용 통합 API 서버
"""

import sys
import os
sys.path.append(os.path.join(os.path.dirname(__file__), 'src'))

from fastapi import FastAPI, HTTPException, BackgroundTasks
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import Dict, Any, List
import subprocess
import json
from pathlib import Path
import time

app = FastAPI(title="CodeMuse Demo API", version="1.0.0")

# CORS 설정
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class DocumentUpdateResponse(BaseModel):
    status: str
    message: str
    docs_created: int
    docs_uploaded: int
    processing_time: float

class DemoStatusResponse(BaseModel):
    status: str
    services: Dict[str, str]
    documents_count: int
    last_updated: str

# 전역 상태 저장
demo_status = {
    "last_updated": None,
    "docs_count": 0,
    "processing": False
}

@app.get("/")
async def root():
    return {
        "service": "CodeMuse Demo API", 
        "status": "running",
        "version": "1.0.0",
        "description": "상무님 데모용 통합 API"
    }

@app.get("/health")
async def health():
    return {"status": "healthy", "service": "CodeMuse Demo API"}

@app.get("/api/v1/demo/status", response_model=DemoStatusResponse)
async def get_demo_status():
    """데모 상태 조회"""
    try:
        # 서비스 상태 확인
        services = {}
        
        # RAG 서비스 확인
        try:
            import requests
            response = requests.get("http://localhost:8003/health", timeout=2)
            services["rag_service"] = "healthy" if response.status_code == 200 else "unhealthy"
        except:
            services["rag_service"] = "unavailable"
        
        # LLM 서비스 확인
        try:
            response = requests.get("http://localhost:8004/health", timeout=2)
            services["llm_service"] = "healthy" if response.status_code == 200 else "unhealthy"
        except:
            services["llm_service"] = "unavailable"
        
        # Workflow 서비스 확인
        try:
            response = requests.get("http://localhost:8006/health", timeout=2)
            services["workflow_service"] = "healthy" if response.status_code == 200 else "unhealthy"
        except:
            services["workflow_service"] = "unavailable"
        
        return DemoStatusResponse(
            status="ready" if not demo_status["processing"] else "processing",
            services=services,
            documents_count=demo_status["docs_count"],
            last_updated=demo_status["last_updated"] or "없음"
        )
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"상태 조회 중 오류 발생: {str(e)}")

@app.post("/api/v1/demo/update-documents", response_model=DocumentUpdateResponse)
async def update_documents(background_tasks: BackgroundTasks):
    """문서 업데이트 (소스코드 → MD → RAG)"""
    if demo_status["processing"]:
        raise HTTPException(status_code=409, detail="이미 문서 업데이트가 진행 중입니다.")
    
    # 백그라운드에서 실행
    background_tasks.add_task(process_document_update)
    
    return DocumentUpdateResponse(
        status="started",
        message="문서 업데이트가 시작되었습니다. 완료까지 약 30초 소요됩니다.",
        docs_created=0,
        docs_uploaded=0,
        processing_time=0.0
    )

async def process_document_update():
    """실제 문서 업데이트 처리"""
    start_time = time.time()
    demo_status["processing"] = True
    
    try:
        print("🔄 문서 업데이트 시작...")
        
        # 1단계: 소스코드에서 MD 문서 생성
        print("📝 1단계: 소스코드 → MD 문서 생성")
        docs_created = await generate_docs_from_sample_code()
        
        print(f"✅ {docs_created}개 문서 생성 완료")
        
        # 2단계: RAG에 문서 업로드
        print("📤 2단계: MD 문서 → RAG 저장")
        docs_uploaded = await upload_docs_to_rag()
        
        print(f"✅ {docs_uploaded}개 문서 RAG 업로드 완료")
        
        # 상태 업데이트
        processing_time = time.time() - start_time
        demo_status["docs_count"] = docs_uploaded
        demo_status["last_updated"] = time.strftime("%Y-%m-%d %H:%M:%S")
        demo_status["processing"] = False
        
        print(f"🎉 문서 업데이트 완료! (소요시간: {processing_time:.2f}초)")
        
    except Exception as e:
        print(f"❌ 문서 업데이트 실패: {e}")
        demo_status["processing"] = False
        raise e

async def generate_docs_from_sample_code():
    """실제 Document Generator 서비스를 사용하여 MD 문서 생성"""
    try:
        import requests
        
        # Document Generator 서비스 호출 (파일별 개별 문서 생성)
        response = requests.post(
            "http://localhost:8001/api/v1/documents/generate",
            json={
                "project_path": "sample_code",
                "output_format": "markdown",
                "include_metrics": True,
                "include_dependencies": True,
                "include_issues": True,
                "template_type": "standard",
                "generate_individual_files": True
            },
            timeout=30
        )
        
        if response.status_code == 200:
            result = response.json()
            print(f"✅ Document Generator 서비스로 문서 생성 완료")
            print(f"📊 분석 결과: {result['metadata']['total_files']}개 파일, {result['metadata']['total_lines']}줄")
            
            # 개별 파일 생성 모드인 경우 생성된 파일 수 반환
            if result['metadata'].get('individual_files_generated'):
                return result['metadata']['individual_files_generated']
            else:
                return 1  # 통합 문서 1개 생성
        else:
            print(f"❌ Document Generator 서비스 호출 실패: {response.status_code}")
            return 0
            
    except Exception as e:
        print(f"❌ Document Generator 서비스 오류: {e}")
        # 폴백: 기존 방식 사용
        return await generate_docs_fallback()

async def generate_docs_fallback():
    """폴백: 기존 방식으로 MD 문서 생성"""
    sample_code_dir = Path("sample_code")
    docs_dir = Path("generated_docs")
    docs_dir.mkdir(exist_ok=True)
    
    docs_created = 0
    
    # sample_code 디렉토리의 Python 파일들 스캔
    for py_file in sample_code_dir.glob("*.py"):
        print(f"📄 {py_file.name} 분석 중...")
        
        # 간단한 MD 문서 생성
        with open(py_file, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # 파일명에서 확장자 제거
        doc_name = py_file.stem + ".md"
        doc_path = docs_dir / doc_name
        
        # MD 문서 생성
        md_content = f"""# {py_file.stem}

## 파일 정보
- **파일명**: {py_file.name}
- **경로**: {py_file}
- **언어**: Python

## 코드 내용
```python
{content}
```

## 분석 결과
이 파일은 {py_file.stem} 관련 기능을 담당하는 Python 모듈입니다.

### 주요 구성 요소
- 클래스와 함수들이 포함되어 있습니다.
- 타입 힌트가 적용되어 있습니다.
- 에러 처리가 구현되어 있습니다.

### 사용법
이 모듈은 다른 Python 파일에서 import하여 사용할 수 있습니다.

```python
from {py_file.stem} import *
```
"""
        
        with open(doc_path, 'w', encoding='utf-8') as f:
            f.write(md_content)
        
        docs_created += 1
        print(f"✅ {doc_name} 생성 완료")
    
    return docs_created

async def upload_docs_to_rag():
    """생성된 MD 문서들을 RAG에 업로드"""
    docs_dir = Path("generated_docs")
    docs_uploaded = 0
    
    try:
        import requests
        
        # 1단계: RAG에 저장된 문서 목록 확인
        print("🔍 기존 RAG 문서 확인 중...")
        existing_response = requests.get("http://localhost:8003/documents", timeout=10)
        existing_docs = set()
        
        if existing_response.status_code == 200:
            existing_data = existing_response.json()
            for doc in existing_data.get('documents', []):
                # 메타데이터에서 파일 경로 추출
                metadata = doc.get('metadata', {})
                source = metadata.get('source', '')
                if source:
                    existing_docs.add(source)
            print(f"📊 기존 RAG 문서 수: {len(existing_docs)}개")
        
        # 2단계: 중복 제거 및 새 파일 필터링
        current_time = time.time()
        recent_files = []
        new_files = []
        
        for md_file in docs_dir.rglob("*.md"):
            file_path_str = str(md_file)
            
            # 중복 체크: 이미 RAG에 있는 파일 제외
            if file_path_str in existing_docs:
                print(f"⏭️  중복 건너뛰기: {md_file.name}")
                continue
                
            file_mtime = md_file.stat().st_mtime
            # 5분(300초) 내에 생성된 파일만 선택
            if current_time - file_mtime < 300:
                recent_files.append(md_file)
            else:
                new_files.append(md_file)
        
        # 3단계: 업로드할 파일 결정
        files_to_upload = recent_files if recent_files else new_files
        print(f"📤 업로드할 파일: {len(files_to_upload)}개 (중복 제외: {len(existing_docs)}개)")
        
        print(f"📤 {len(files_to_upload)}개 문서 RAG 업로드 중...")
        
        for md_file in files_to_upload:
            print(f"📤 {md_file.name} RAG 업로드 중...")
            
            with open(md_file, 'r', encoding='utf-8') as f:
                content = f.read()
            
            # 안정적인 문서 ID 생성 (UUID 사용)
            import uuid
            doc_id = str(uuid.uuid4())
            
            # 파일 경로에서 카테고리 및 태그 추출
            file_path_str = str(md_file)
            category = "general"
            tags = []
            
            # 카테고리 결정
            if "analyzers" in file_path_str:
                category = "analyzers"
                tags.extend(["분석", "검증", "오류검출"])
            elif "formatters" in file_path_str:
                category = "formatters"
                tags.extend(["포맷팅", "출력", "템플릿"])
            elif "streaming" in file_path_str:
                category = "streaming"
                tags.extend(["스트리밍", "실시간", "처리"])
            elif "shared" in file_path_str:
                category = "shared"
                tags.extend(["공통", "유틸리티", "설정"])
            
            # 파일명 기반 태그 추가
            if "issue_detector" in md_file.name:
                tags.extend(["오류유형", "7가지", "검출", "duplicate_condition", "type_mismatch"])
            elif "validation" in md_file.name:
                tags.extend(["검증", "유효성", "타입체크", "validation"])
            elif "metrics" in md_file.name:
                tags.extend(["메트릭", "복잡도", "성능", "complexity"])
            elif "condition_analyzer" in md_file.name:
                tags.extend(["조건분석", "condition", "analyzer"])
            
            # RAG에 문서 업로드 (향상된 메타데이터)
            response = requests.post(
                "http://localhost:8003/api/v1/documents",
                json={
                    "id": doc_id,
                    "content": content,
                    "metadata": {
                        "source": str(md_file),
                        "filename": md_file.name,
                        "type": "generated_doc",
                        "category": category,
                        "tags": ", ".join(tags),
                        "created_at": time.strftime("%Y-%m-%d %H:%M:%S"),
                        "file_size": len(content),
                        "word_count": len(content.split())
                    }
                },
                timeout=10
            )
            
            if response.status_code == 200:
                docs_uploaded += 1
                print(f"✅ {md_file.name} 업로드 완료")
            else:
                print(f"❌ {md_file.name} 업로드 실패: {response.status_code}")
    
    except Exception as e:
        print(f"❌ RAG 업로드 중 오류: {e}")
        raise e
    
    return docs_uploaded

@app.get("/api/v1/demo/test-chat")
async def test_chat():
    """RAG 기반 채팅 테스트"""
    try:
        import requests
        
        # 간단한 테스트 질문
        test_queries = [
            "Python 함수는 어떻게 정의하나요?",
            "FastAPI 서비스 구조는 어떻게 되어있나요?",
            "우리 프로젝트의 아키텍처는?"
        ]
        
        results = []
        
        for query in test_queries:
            # RAG 검색
            rag_response = requests.post(
                "http://localhost:8003/api/v1/search",
                json={"query": query, "limit": 3},
                timeout=5
            )
            
            if rag_response.status_code == 200:
                rag_data = rag_response.json()
                results.append({
                    "query": query,
                    "found_docs": rag_data.get("total_found", 0),
                    "status": "success"
                })
            else:
                results.append({
                    "query": query,
                    "found_docs": 0,
                    "status": "failed"
                })
        
        return {
            "status": "success",
            "message": "RAG 검색 테스트 완료",
            "test_results": results
        }
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"채팅 테스트 중 오류 발생: {str(e)}")

@app.get("/api/v1/demo/available-apis")
async def get_available_apis():
    """사용 가능한 API 목록"""
    return {
        "rag_search": {
            "url": "POST http://localhost:8003/api/v1/search",
            "description": "문서 검색 (RAG)",
            "example": {
                "query": "Python 함수 정의",
                "limit": 5
            }
        },
        "llm_chat": {
            "url": "POST http://localhost:8004/api/v1/chat/sessions",
            "description": "LLM 채팅 세션 생성",
            "example": {
                "title": "새 채팅"
            }
        },
        "workflow": {
            "url": "POST http://localhost:8006/workflow",
            "description": "통합 워크플로우 (RAG + LLM)",
            "example": {
                "query": "코드에 대해 설명해주세요",
                "use_rag": True,
                "model": "gpt-3.5-turbo"
            }
        }
    }

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8007)
