from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from typing import Optional, Dict, Any
import os
import asyncio
import logging
from datetime import datetime

from ..core.template_chunk_generator import generate_template_chunk_documents, TemplateChunkGenerator, upload_generated_documents_to_rag

# 로거 설정
logger = logging.getLogger("chunker_document_generator")

router = APIRouter(prefix="/api/v1/documents", tags=["document-generator"])

@router.get("/health")
async def generator_health():
    return {"status": "ok", "module": "chunker_document_generator"}


class GenerateRequest(BaseModel):
    source_dir: Optional[str] = None
    output_dir: Optional[str] = None
    template_dir: Optional[str] = None
    upload_to_rag: Optional[bool] = True


@router.post("/generate")
async def generate_documents(request: GenerateRequest) -> Dict[str, Any]:
    try:
        logger.info(f"🔍 받은 요청: upload_to_rag={request.upload_to_rag}, source_dir={request.source_dir}")
        logger.info(f"🔍 요청 타입: {type(request)}, 내용: {request}")
        
        base_dir = os.getcwd()
        source_dir = request.source_dir or os.path.join(base_dir, "sample_code")
        output_dir = request.output_dir or os.path.join(base_dir, "generated_docs")
        template_dir = request.template_dir or os.path.join(base_dir, "src", "chunker_document_generator", "core", "templates")

        if not os.path.exists(source_dir):
            logger.error(f"❌ 소스 디렉토리 없음: {source_dir}")
            raise HTTPException(status_code=400, detail=f"source_dir not found: {source_dir}")

        if request.upload_to_rag is False:
            # RAG 업로드만 건너뛰고 로컬 생성
            logger.info("📝 로컬 MD 생성 모드 (RAG 업로드 건너뜀)")
            generator = TemplateChunkGenerator(source_dir, output_dir, template_dir)
            result = await generator.generate_documents(upload_to_rag=False)
        else:
            # 긴 작업을 백그라운드로 실행하고 즉시 응답
            logger.info("📝 로컬 생성 + 백그라운드 RAG 업로드 모드")
            generator = TemplateChunkGenerator(source_dir, output_dir, template_dir)
            # 먼저 로컬 생성만 수행
            result = await generator.generate_documents(upload_to_rag=False)
            
            # RAG 업로드는 백그라운드로 실행 (응답 속도 개선)
            logger.info("🔄 백그라운드 RAG 업로드 시작")
            asyncio.create_task(upload_generated_documents_to_rag(source_dir, output_dir, template_dir))
            
            # 즉시 응답 (RAG 업로드는 백그라운드에서 진행)
            result["rag_upload_status"] = "background_processing"
            result["rag_upload_message"] = "RAG 업로드가 백그라운드에서 진행 중입니다. RAG 헬스체크로 문서 수를 확인하세요."
        
        # 프론트 호환성을 위한 추가 필드
        result["docs_created"] = result.get("total_files", 0)
        result["documents_processed"] = result.get("total_files", 0)
        result["md_files_count"] = len(result.get("generated_files", []))
        
        logger.info(f"✅ 응답 준비 완료: docs_created={result.get('docs_created')}, md_files_count={result.get('md_files_count')}")
        return {"status": "success", "result": result}
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"❌ 오류 발생: {e}")
        import traceback
        logger.error(f"❌ 스택 트레이스: {traceback.format_exc()}")
        raise HTTPException(status_code=500, detail=f"문서 생성 중 오류: {str(e)}")

class UploadOnlyRequest(BaseModel):
    source_dir: Optional[str] = None
    output_dir: Optional[str] = None
    template_dir: Optional[str] = None


@router.post("/upload")
async def upload_only(request: UploadOnlyRequest) -> Dict[str, Any]:
    """이미 생성된 MD를 RAG에만 업로드 (단독 실행)"""
    try:
        base_dir = os.getcwd()
        source_dir = request.source_dir or os.path.join(base_dir, "sample_code")
        output_dir = request.output_dir or os.path.join(base_dir, "generated_docs")
        template_dir = request.template_dir or os.path.join(base_dir, "src", "chunker_document_generator", "core", "templates")

        result = await upload_generated_documents_to_rag(source_dir, output_dir, template_dir)
        return {"status": "success", "result": result}
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"RAG 업로드 중 오류: {str(e)}")

@router.post("/upload-direct")
async def upload_direct_to_rag(request: UploadOnlyRequest) -> Dict[str, Any]:
    """프론트엔드에서 직접 호출하는 RAG 업로드 엔드포인트"""
    try:
        base_dir = os.getcwd()
        source_dir = request.source_dir or os.path.join(base_dir, "sample_code")
        output_dir = request.output_dir or os.path.join(base_dir, "generated_docs")
        template_dir = request.template_dir or os.path.join(base_dir, "src", "chunker_document_generator", "core", "templates")

        print(f"🔄 프론트엔드에서 RAG 업로드 요청: {source_dir} -> {output_dir}")
        
        # RAG 업로드 실행
        result = await upload_generated_documents_to_rag(source_dir, output_dir, template_dir)
        
        # 프론트엔드 호환 응답 형식
        total_sections = result.get("total_sections", 0)
        successful_files = result.get("successful_files", 0)
        uploaded_files = result.get("uploaded_files", 0)
        
        print(f"📊 RAG 업로드 결과: {successful_files}/{uploaded_files}개 파일, {total_sections}개 섹션")
        
        return {
            "status": "success",
            "message": f"RAG 업로드가 완료되었습니다. {successful_files}개 파일, {total_sections}개 섹션 저장",
            "uploaded_files": uploaded_files,
            "successful_files": successful_files,
            "total_sections": total_sections,
            "details": result.get("details", []),
            "success_count": successful_files
        }
    except HTTPException:
        raise
    except Exception as e:
        print(f"❌ RAG 업로드 오류: {str(e)}")
        raise HTTPException(status_code=500, detail=f"RAG 업로드 중 오류: {str(e)}")


@router.delete("/generated")
async def delete_generated_md_files() -> Dict[str, Any]:
    """생성된 MD 문서 전체 삭제"""
    try:
        import shutil
        base_dir = os.getcwd()
        output_dir = os.path.join(base_dir, "generated_docs")
        
        deleted_count = 0
        if os.path.exists(output_dir):
            # MD 파일 개수 세기
            for root, dirs, files in os.walk(output_dir):
                for file in files:
                    if file.endswith('.md'):
                        deleted_count += 1
            
            # generated_docs 디렉토리 전체 삭제
            shutil.rmtree(output_dir)
            print(f"🗑️ generated_docs 디렉토리 삭제 완료: {deleted_count}개 MD 파일")
        
        return {
            "status": "success", 
            "message": f"{deleted_count}개 MD 파일 삭제 완료",
            "deleted_files": deleted_count
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"MD 파일 삭제 중 오류: {str(e)}")


@router.get("/status")
async def get_comprehensive_status() -> Dict[str, Any]:
    """MD/샘플코드/RAG 통합 헬스체크"""
    try:
        import httpx
        base_dir = os.getcwd()
        
        # 1. MD 파일 개수
        output_dir = os.path.join(base_dir, "generated_docs")
        md_count = 0
        if os.path.exists(output_dir):
            for root, dirs, files in os.walk(output_dir):
                for file in files:
                    if file.endswith('.md'):
                        md_count += 1
        
        # 2. 샘플 코드 파일 개수 (MD 생성 시 지원하는 모든 파일)
        source_dir = os.path.join(base_dir, "sample_code")
        source_count = 0
        if os.path.exists(source_dir):
            supported_extensions = ['.py', '.java', '.js', '.jsx', '.vue', '.ts', '.tsx']
            for root, dirs, files in os.walk(source_dir):
                for file in files:
                    if any(file.endswith(ext) for ext in supported_extensions):
                        source_count += 1
        
        # 3. RAG 문서 개수
        rag_count = 0
        rag_status = "unknown"
        try:
            rag_service_url = os.getenv("RAG_SERVICE_URL", "http://localhost:8003")
            async with httpx.AsyncClient() as client:
                response = await client.get(f"{rag_service_url}/health", timeout=3.0)
                if response.status_code == 200:
                    rag_data = response.json()
                    rag_count = rag_data.get("documents_count", 0)
                    rag_status = rag_data.get("status", "unknown")
        except Exception as e:
            rag_status = f"error: {str(e)}"
        
        return {
            "status": "ok",
            "timestamp": datetime.now().isoformat(),
            "counts": {
                "md_documents": md_count,
                "source_files": source_count, 
                "rag_documents": rag_count
            },
            "services": {
                "document_generator": "running",
                "rag_engine": rag_status
            },
            "paths": {
                "source_dir": source_dir,
                "output_dir": output_dir
            }
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"상태 확인 중 오류: {str(e)}")


@router.get("/source-structure")
async def get_source_code_structure() -> Dict[str, Any]:
    """샘플 코드 디렉토리 구조 조회 (카테고리별)"""
    try:
        base_dir = os.getcwd()
        source_dir = os.path.join(base_dir, "sample_code")
        
        if not os.path.exists(source_dir):
            return {
                "structure": {},
                "total_files": 0,
                "total_folders": 0,
                "message": "sample_code 디렉토리가 존재하지 않습니다."
            }
        
        def build_directory_tree():
            """계층적 디렉토리 구조 생성"""
            tree = {}
            total_files = 0
            
            for root, dirs, filenames in os.walk(source_dir):
                # .DS_Store 파일 제외
                filenames = [f for f in filenames if not f.startswith('.DS_Store')]
                
                for filename in filenames:
                    # MD 생성 시 지원하는 파일 확장자만 처리
                    supported_extensions = ['.py', '.java', '.js', '.jsx', '.vue', '.ts', '.tsx']
                    if any(filename.endswith(ext) for ext in supported_extensions):
                        file_path = os.path.join(root, filename)
                        relative_path = os.path.relpath(file_path, source_dir)
                        
                        # 파일 정보
                        stat = os.stat(file_path)
                        file_size = stat.st_size
                        
                        # 파일 확장자에 따른 언어 정보
                        file_ext = os.path.splitext(filename)[1].lower()
                        if file_ext == '.py':
                            language = 'python'
                        elif file_ext == '.java':
                            language = 'java'
                        elif file_ext in ['.js', '.jsx', '.vue', '.ts', '.tsx']:
                            language = 'javascript'
                        else:
                            language = 'unknown'
                        
                        # 파일 내용 미리보기 (첫 3줄)
                        try:
                            with open(file_path, 'r', encoding='utf-8') as f:
                                lines = f.readlines()
                                preview = ''.join(lines[:3]).strip()
                        except:
                            preview = "파일을 읽을 수 없습니다."
                        
                        file_info = {
                            "type": "file",
                            "name": filename,
                            "path": relative_path,
                            "size": file_size,
                            "size_readable": f"{file_size / 1024:.1f} KB" if file_size > 1024 else f"{file_size} bytes",
                            "lines": len(lines) if 'lines' in locals() else 0,
                            "preview": preview[:200] + "..." if len(preview) > 200 else preview,
                            "created_at": stat.st_ctime,
                            "modified_at": stat.st_mtime,
                            "language": language,
                            "extension": file_ext
                        }
                        
                        # 경로를 분할하여 트리에 삽입
                        path_parts = relative_path.split(os.sep)
                        current_node = tree
                        
                        # 폴더 경로 생성
                        for i, part in enumerate(path_parts[:-1]):  # 마지막 파일명 제외
                            if part not in current_node:
                                current_node[part] = {
                                    "type": "folder",
                                    "name": part,
                                    "path": "/".join(path_parts[:i+1]),
                                    "children": {},
                                    "file_count": 0,
                                    "description": _get_folder_description(part)
                                }
                            if part in current_node and "children" in current_node[part]:
                                current_node = current_node[part]["children"]
                            else:
                                current_node = {}
                        
                        # 파일 추가 (확장자 제거)
                        file_key = os.path.splitext(path_parts[-1])[0]
                        current_node[file_key] = file_info
                        total_files += 1
                        
                        # 상위 폴더들의 파일 카운트 증가
                        temp_node = tree
                        for part in path_parts[:-1]:
                            if part in temp_node and "file_count" in temp_node[part]:
                                temp_node[part]["file_count"] += 1
                            if part in temp_node and "children" in temp_node[part]:
                                temp_node = temp_node[part]["children"]
                            else:
                                temp_node = {}
            
            return tree, total_files
        
        def count_folders(node):
            """폴더 개수 계산"""
            count = 0
            for key, value in node.items():
                if isinstance(value, dict) and value.get("type") == "folder":
                    count += 1
                    children = value.get("children", {})
                    if children:
                        count += count_folders(children)
            return count
        
        def _get_folder_description(folder_name: str) -> str:
            """폴더별 설명"""
            descriptions = {
                "rule_analyzer": "📋 규칙 분석 엔진 - 조건 파싱, 이슈 검출, 메트릭 생성",
                "prompt_manager": "🎯 프롬프트 관리 시스템 - 템플릿 관리, 검증, 오케스트레이션",
                "analyzers": "🔍 분석기 모듈 - 조건/이슈/복잡도/고급 분석",
                "formatters": "📝 포매터 모듈 - 텍스트/스트리밍 출력 처리",
                "streaming": "🌊 스트리밍 모듈 - 실시간 분석 결과 전송",
                "shared": "🔧 공통 모듈 - 설정/로깅/유틸리티",
                "service": "⚙️ 서비스 레이어 - 비즈니스 로직 구현",
                "templates": "📄 템플릿 모듈 - 다국어/커스텀 템플릿",
                "options": "⚙️ 옵션 모듈 - 포매팅 옵션 관리",
                "protocols": "🌐 프로토콜 모듈 - HTTP/SSE 스트리밍",
                "utils": "🛠️ 유틸리티 - 헬퍼 함수/공통 기능",
                "config": "⚙️ 설정 모듈 - 환경설정 관리",
                "vizier(sample)": "🎯 Vizier 샘플 프로젝트 - Java Spring Boot + Vue.js 풀스택",
                "be": "☕ 백엔드 - Java Spring Boot 애플리케이션",
                "fe": "🎨 프론트엔드 - Vue.js TypeScript 애플리케이션",
                "src": "📁 소스 코드 디렉터리",
                "main": "🏗️ 메인 소스 코드",
                "java": "☕ Java 소스 코드",
                "resources": "📄 리소스 파일들",
                "components": "🧩 Vue 컴포넌트들",
                "pages": "📄 Vue 페이지들",
                "views": "👁️ Vue 뷰들",
                "api": "🔌 API 관련 코드",
                "store": "🗄️ Vuex/Pinia 스토어",
                "utils": "🛠️ 유틸리티 함수들",
                "types": "📝 TypeScript 타입 정의",
                "constants": "📋 상수 정의",
                "enums": "🔢 열거형 정의",
                "interfaces": "🔗 인터페이스 정의",
                "composables": "🎣 Vue Composables",
                "layouts": "🏗️ 레이아웃 컴포넌트들",
                "mocks": "🎭 Mock 데이터",
                "tests": "🧪 테스트 코드",
                "styles": "🎨 스타일 파일들"
            }
            return descriptions.get(folder_name, f"📁 {folder_name} 모듈")
        
        try:
            tree, total_files = build_directory_tree()
            total_folders = count_folders(tree)
            
            return {
                "structure": tree,
                "total_files": total_files,
                "total_folders": total_folders,
                "source_path": source_dir,
                "summary": {
                    "root_files": len([k for k, v in tree.items() if v.get("type") == "file"]),
                    "root_folders": len([k for k, v in tree.items() if v.get("type") == "folder"]),
                    "main_modules": list(tree.keys())
                }
            }
        except Exception as e:
            import traceback
            print(f"디버깅 에러: {str(e)}")
            print(f"에러 상세: {traceback.format_exc()}")
            raise e
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"소스 구조 조회 중 오류: {str(e)}")


