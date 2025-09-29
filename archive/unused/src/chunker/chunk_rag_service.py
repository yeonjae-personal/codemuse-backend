"""
Chunk 기반 RAG 서비스

기존 파일 단위 RAG 대신 chunk 단위로 저장하고 검색하는 시스템
"""

import os
import json
import requests
from typing import List, Dict, Any, Optional
from dataclasses import asdict

try:
    from .ast_chunker import CodeChunk
except ImportError:
    from ast_chunker import CodeChunk


class ChunkRAGService:
    """Chunk 기반 RAG 서비스"""
    
    def __init__(self, rag_base_url: str = "http://localhost:8003"):
        """
        Args:
            rag_base_url: RAG 서비스 기본 URL
        """
        self.rag_base_url = rag_base_url
    
    def upload_chunks(self, chunks: List[CodeChunk], collection_name: str = "codemuse_chunks") -> Dict[str, Any]:
        """
        Chunk 리스트를 RAG에 업로드
        
        Args:
            chunks: 업로드할 CodeChunk 리스트
            collection_name: ChromaDB 컬렉션 이름
            
        Returns:
            업로드 결과 정보
        """
        print(f"🚀 Chunk RAG 업로드 시작...")
        print(f"📦 총 chunk 수: {len(chunks)}")
        print(f"🗄️ 컬렉션: {collection_name}")
        
        # 1. 기존 컬렉션 삭제 (새로운 chunk 시스템을 위해)
        self._clear_collection(collection_name)
        
        # 2. chunk별로 RAG에 업로드
        upload_results = []
        failed_uploads = []
        
        for i, chunk in enumerate(chunks):
            try:
                print(f"📤 업로드 중... {i+1}/{len(chunks)} - {chunk.chunk_type}:{chunk.name}")
                
                # chunk를 RAG 형식으로 변환
                rag_document = self._chunk_to_rag_document(chunk)
                
                # RAG 서비스에 업로드
                response = self._upload_single_document(rag_document)
                
                if response.get('success'):
                    upload_results.append({
                        'chunk_id': chunk.chunk_id,
                        'status': 'success'
                    })
                else:
                    failed_uploads.append({
                        'chunk_id': chunk.chunk_id,
                        'error': response.get('error', 'Unknown error')
                    })
                    
            except Exception as e:
                print(f"❌ Chunk {chunk.chunk_id} 업로드 실패: {e}")
                failed_uploads.append({
                    'chunk_id': chunk.chunk_id,
                    'error': str(e)
                })
        
        # 3. 결과 요약
        result = {
            'collection_name': collection_name,
            'total_chunks': len(chunks),
            'successful_uploads': len(upload_results),
            'failed_uploads': len(failed_uploads),
            'success_rate': len(upload_results) / len(chunks) * 100 if chunks else 0,
            'upload_results': upload_results,
            'failed_uploads': failed_uploads
        }
        
        print(f"✅ 업로드 완료!")
        print(f"   성공: {len(upload_results)}개")
        print(f"   실패: {len(failed_uploads)}개")
        print(f"   성공률: {result['success_rate']:.1f}%")
        
        return result
    
    def _clear_collection(self, collection_name: str) -> bool:
        """기존 컬렉션 삭제"""
        try:
            print(f"🗑️ 기존 컬렉션 '{collection_name}' 삭제 중...")
            
            # 모든 문서 삭제 API 호출
            response = requests.delete(f"{self.rag_base_url}/api/v1/documents")
            
            if response.status_code == 200:
                print(f"   ✅ 컬렉션 삭제 성공")
                return True
            else:
                print(f"   ⚠️ 컬렉션 삭제 응답: {response.status_code}")
                return False
                
        except Exception as e:
            print(f"   ❌ 컬렉션 삭제 오류: {e}")
            return False
    
    def _chunk_to_rag_document(self, chunk: CodeChunk) -> Dict[str, Any]:
        """CodeChunk를 RAG 문서 형식으로 변환"""
        
        # 메타데이터 구성
        metadata = {
            "chunk_id": chunk.chunk_id,
            "file_path": chunk.file_path,
            "filename": os.path.basename(chunk.file_path),
            "chunk_type": chunk.chunk_type,
            "name": chunk.name,
            "line_range": chunk.line_range,
            "tags": ",".join(chunk.tags),  # ChromaDB는 문자열만 지원
            "token_count": chunk.token_count or 0,
            "complexity": chunk.complexity or 0,
            "is_async": chunk.is_async,
            "is_generator": chunk.is_generator
        }
        
        # 선택적 메타데이터 추가
        if chunk.parent:
            metadata["parent"] = chunk.parent
        
        if chunk.base_classes:
            metadata["base_classes"] = ",".join(chunk.base_classes)
        
        if chunk.decorators:
            metadata["decorators"] = ",".join(chunk.decorators)
        
        if chunk.dependencies:
            metadata["dependencies"] = ",".join(chunk.dependencies)
        
        # 기본 폴더 우선순위 (질문별로 동적 조정됨)
        metadata["folder_priority"] = 50  # 기본값
        
        # 향상된 콘텐츠 구성
        content = self._generate_enhanced_content(chunk)
        
        return {
            "content": content,
            "metadata": metadata
        }
    
    def get_dynamic_folder_priority(self, file_path: str, query: str, keywords: List[str]) -> int:
        """질문 내용과 키워드에 기반한 동적 폴더 우선순위 계산"""
        
        path_lower = file_path.lower()
        query_lower = query.lower()
        keywords_lower = [k.lower() for k in keywords]
        
        # 기본 우선순위
        base_priority = 50
        
        # 질문 패턴별 폴더 매핑
        folder_patterns = {
            # 분석 관련
            "analyzers": {
                "keywords": ["분석", "analysis", "detect", "issue", "error", "오류", "검출", "문제", "detector"],
                "boost": 50
            },
            # 검증 관련  
            "validator": {
                "keywords": ["검증", "validation", "validate", "확인", "체크", "check"],
                "boost": 45
            },
            # 처리 관련
            "processor": {
                "keywords": ["처리", "process", "생성", "create", "변환", "convert", "파싱", "parse"],
                "boost": 40
            },
            # 서비스 관련
            "service": {
                "keywords": ["서비스", "service", "관리", "manage", "orchestrator", "조율", "prompt"],
                "boost": 35
            },
            # 포매팅 관련
            "formatter": {
                "keywords": ["포맷", "format", "템플릿", "template", "출력", "output", "표시", "text"],
                "boost": 30
            },
            # 스트리밍 관련
            "streaming": {
                "keywords": ["스트리밍", "streaming", "실시간", "real-time", "chunk", "진행", "stream"],
                "boost": 30
            },
            # 설정 관련
            "config": {
                "keywords": ["설정", "config", "configuration", "옵션", "option"],
                "boost": 25
            },
            # 유틸리티 관련
            "utils": {
                "keywords": ["유틸", "util", "도구", "helper", "공통", "shared", "상수", "constants", "language"],
                "boost": 20
            }
        }
        
        # 파일 경로에서 폴더 타입 추출
        for folder_type, pattern_info in folder_patterns.items():
            if folder_type in path_lower:
                # 해당 폴더 타입에 대한 키워드 매칭 점수 계산
                keyword_score = 0
                
                # 질문에서 직접 매칭
                for pattern_keyword in pattern_info["keywords"]:
                    if pattern_keyword in query_lower:
                        keyword_score += 3
                
                # 추출된 키워드에서 매칭
                for extracted_keyword in keywords_lower:
                    for pattern_keyword in pattern_info["keywords"]:
                        if pattern_keyword in extracted_keyword or extracted_keyword in pattern_keyword:
                            keyword_score += 2
                
                # 최종 우선순위 = 기본값 + 폴더 부스트 + 키워드 점수
                final_priority = base_priority + pattern_info["boost"] + (keyword_score * 5)
                
                return min(final_priority, 150)  # 최대 150으로 제한
        
        return base_priority
    
    def _generate_enhanced_content(self, chunk: CodeChunk) -> str:
        """검색 품질 향상을 위한 콘텐츠 생성"""
        
        content_parts = []
        
        # 1. 기본 정보
        content_parts.append(f"# {chunk.chunk_type}: {chunk.name}")
        
        # 2. 파일 정보
        file_name = os.path.basename(chunk.file_path)
        content_parts.append(f"파일: {file_name}")
        
        # 3. 부모 정보 (메서드인 경우)
        if chunk.parent:
            content_parts.append(f"소속: {chunk.parent}")
        
        # 4. 특성 정보
        characteristics = []
        if chunk.is_async:
            characteristics.append("비동기")
        if chunk.is_generator:
            characteristics.append("제너레이터")
        if chunk.decorators:
            characteristics.extend(chunk.decorators)
        
        if characteristics:
            content_parts.append(f"특성: {', '.join(characteristics)}")
        
        # 5. docstring (있는 경우)
        if chunk.docstring:
            content_parts.append(f"설명: {chunk.docstring}")
        
        # 6. 태그 정보 (검색 키워드로 활용)
        if chunk.tags:
            content_parts.append(f"태그: {', '.join(chunk.tags)}")
        
        # 7. 실제 코드 내용
        content_parts.append("코드:")
        content_parts.append(chunk.content)
        
        return "\n\n".join(content_parts)
    
    def _upload_single_document(self, document: Dict[str, Any]) -> Dict[str, Any]:
        """단일 문서를 RAG에 업로드"""
        
        try:
            response = requests.post(
                f"{self.rag_base_url}/api/v1/documents",
                json=document,
                headers={"Content-Type": "application/json"},
                timeout=30
            )
            
            if response.status_code == 200:
                return {"success": True, "response": response.json()}
            else:
                return {
                    "success": False,
                    "error": f"HTTP {response.status_code}: {response.text}"
                }
                
        except requests.exceptions.Timeout:
            return {"success": False, "error": "Request timeout"}
        except Exception as e:
            return {"success": False, "error": str(e)}
    
    def verify_upload(self) -> Dict[str, Any]:
        """업로드된 chunk 확인"""
        
        try:
            # RAG 서비스 상태 확인
            response = requests.get(f"{self.rag_base_url}/health")
            
            if response.status_code != 200:
                return {"success": False, "error": "RAG service not available"}
            
            # 문서 수 확인
            response = requests.get(f"{self.rag_base_url}/api/v1/documents")
            
            if response.status_code == 200:
                data = response.json()
                return {
                    "success": True,
                    "total_documents": data.get("total_documents", 0),
                    "collection_info": data
                }
            else:
                return {
                    "success": False,
                    "error": f"Cannot get document count: {response.status_code}"
                }
                
        except Exception as e:
            return {"success": False, "error": str(e)}
    
    def search_chunks(self, query: str, chunk_types: Optional[List[str]] = None, 
                     file_filter: Optional[List[str]] = None, 
                     top_k: int = 10) -> Dict[str, Any]:
        """
        Chunk 검색 (테스트용)
        
        Args:
            query: 검색 쿼리
            chunk_types: 검색할 chunk 타입 필터 (예: ["function", "method"])
            file_filter: 검색할 파일 필터 (예: ["issue_detector.py"])
            top_k: 반환할 최대 결과 수
            
        Returns:
            검색 결과
        """
        
        try:
            search_request = {
                "query": query,
                "top_k": top_k
            }
            
            # 필터 추가 (구현 예정)
            if chunk_types:
                search_request["chunk_types"] = chunk_types
            if file_filter:
                search_request["file_filter"] = file_filter
            
            response = requests.post(
                f"{self.rag_base_url}/api/v1/search",
                json=search_request,
                headers={"Content-Type": "application/json"}
            )
            
            if response.status_code == 200:
                return {"success": True, "results": response.json()}
            else:
                return {
                    "success": False,
                    "error": f"Search failed: {response.status_code}"
                }
                
        except Exception as e:
            return {"success": False, "error": str(e)}


def upload_chunks_from_metadata(metadata_file: str, rag_base_url: str = "http://localhost:8003") -> Dict[str, Any]:
    """
    chunk_metadata.json 파일에서 chunk들을 읽어서 RAG에 업로드
    
    Args:
        metadata_file: chunk_metadata.json 파일 경로
        rag_base_url: RAG 서비스 URL
        
    Returns:
        업로드 결과
    """
    
    print(f"📂 메타데이터 파일 읽기: {metadata_file}")
    
    try:
        with open(metadata_file, 'r', encoding='utf-8') as f:
            metadata = json.load(f)
        
        chunks_data = metadata.get('chunks', [])
        print(f"📦 로드된 chunk 수: {len(chunks_data)}")
        
        # JSON 데이터를 CodeChunk 객체로 변환
        chunks = []
        for chunk_data in chunks_data:
            # None 값들을 기본값으로 변환
            for key, default in [
                ('base_classes', []),
                ('decorators', []),
                ('tags', []),
                ('dependencies', []),
                ('parent', None),
                ('complexity', None),
                ('docstring', None),
                ('is_async', False),
                ('is_generator', False),
                ('token_count', None)
            ]:
                if chunk_data.get(key) is None:
                    chunk_data[key] = default
            
            chunk = CodeChunk(**chunk_data)
            chunks.append(chunk)
        
        # RAG 서비스에 업로드
        rag_service = ChunkRAGService(rag_base_url)
        result = rag_service.upload_chunks(chunks)
        
        return result
        
    except FileNotFoundError:
        return {"success": False, "error": f"Metadata file not found: {metadata_file}"}
    except json.JSONDecodeError as e:
        return {"success": False, "error": f"Invalid JSON format: {e}"}
    except Exception as e:
        return {"success": False, "error": f"Upload failed: {e}"}


if __name__ == "__main__":
    # 테스트용
    metadata_file = "/Users/roseline/projects/codemuse-backend/generated_docs_chunk/chunk_metadata.json"
    
    print("🚀 Chunk RAG 업로드 테스트 시작...")
    result = upload_chunks_from_metadata(metadata_file)
    
    if result.get('success', False):
        print(f"✅ 업로드 성공!")
        print(f"   성공률: {result['success_rate']:.1f}%")
    else:
        print(f"❌ 업로드 실패: {result.get('error', 'Unknown error')}")
    
    # 업로드 확인
    rag_service = ChunkRAGService()
    verify_result = rag_service.verify_upload()
    print(f"📊 업로드 확인: {verify_result}")
