"""
RAG Engine 서비스 실행 스크립트 (ChromaDB 기반)
"""

import sys
import os
import uuid
from pathlib import Path
from datetime import datetime
# 별도 내부 모듈 의존 없음. 상위 src 추가는 불필요하나, 루트 기준 실행 시 안전하게 유지
_BASE_DIR = os.path.dirname(__file__)
_SRC_DIR = os.path.dirname(_BASE_DIR)
if _SRC_DIR not in sys.path:
    sys.path.append(_SRC_DIR)

from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import List, Optional
import chromadb
from chromadb.config import Settings
from rank_bm25 import BM25Okapi
import re
import openai

# OpenAI API 키 설정 및 로깅
openai.api_key = os.getenv("OPENAI_API_KEY")
print(f"🔑 RAG Engine OpenAI API 키: {openai.api_key[:20]}..." if openai.api_key else "❌ OpenAI API 키가 설정되지 않음")

# 간단한 링크 매핑 (프론트엔드 호환성)
SIMPLE_LINK_MAPPING = {
    # IssueDetector 클래스 관련 링크들
    'class_issuedetector': 'sample_code/rule_analyzer/analyzers/issue_detector.py/<a id="class-issuedetector"></a>🎯 `issuedetector`',
    'class-issuedetector': 'sample_code/rule_analyzer/analyzers/issue_detector.py/<a id="class-issuedetector"></a>🎯 `issuedetector`',
    'issuedetector': 'sample_code/rule_analyzer/analyzers/issue_detector.py/<a id="class-issuedetector"></a>🎯 `issuedetector`',
    'issue_detector': 'sample_code/rule_analyzer/analyzers/issue_detector.py/<a id="class-issuedetector"></a>🎯 `issuedetector`',
    'IssueDetector': 'sample_code/rule_analyzer/analyzers/issue_detector.py/<a id="class-issuedetector"></a>🎯 `issuedetector`',
    
    # 함수 관련 링크들
    'func_detect_type_mismatch_157': 'sample_code/rule_analyzer/analyzers/issue_detector.py/<a id="func-detect-type-mismatch-157"></a>🔧 `detect_type_mismatch`',
    'detect_type_mismatch': 'sample_code/rule_analyzer/analyzers/issue_detector.py/<a id="func-detect-type-mismatch-157"></a>🔧 `detect_type_mismatch`',
    
    # 파일 관련 링크들
    'file_issue_detector': 'sample_code/rule_analyzer/analyzers/issue_detector.py/<a id="class-issuedetector"></a>🎯 `issuedetector`',
    'issue_detector.py': 'sample_code/rule_analyzer/analyzers/issue_detector.py/<a id="class-issuedetector"></a>🎯 `issuedetector`',
}

app = FastAPI(title="RAG Engine", version="1.0.0")

# CORS 설정
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class SearchRequest(BaseModel):
    query: str
    limit: int = 5
    # 선택적 필터들
    chunk_type_filter: Optional[str] = None  # 예: "overview|function|class"
    exclude_chunk_types: Optional[List[str]] = None
    file_filter: Optional[List[str]] = None  # 파일명 또는 경로 일부
    include_metadata: Optional[bool] = True

class SearchResponse(BaseModel):
    results: List[dict]
    query: str
    total_found: int

class DocumentRequest(BaseModel):
    id: Optional[str] = None
    content: str
    metadata: Optional[dict] = None

def korean_tokenize(text: str) -> List[str]:
    """한국어 토크나이징 (간단한 공백 기반)"""
    # 공백으로 분리하고 특수문자 제거
    tokens = re.findall(r'[가-힣a-zA-Z0-9]+', text.lower())
    return tokens

def preprocess_korean_text(text: str) -> str:
    """한국어 텍스트 전처리 (사용자 제시 방안 적용)"""
    # 유니코드 정규화 (NFC)
    import unicodedata
    text = unicodedata.normalize('NFC', text.strip())
    
    # 숫자/단위 정규화
    text = re.sub(r'(\d+)\s*원', r'\1원', text)  # "10 원" -> "10원"
    text = re.sub(r'(\d+)\s*개', r'\1개', text)  # "5 개" -> "5개"
    text = re.sub(r'(\d+)\s*G', r'\1G', text)    # "5 G" -> "5G"
    
    # 불필요한 공백 제거
    text = re.sub(r'\s+', ' ', text)
    
    # 복합명사 분해 (디컴파운드)
    decompositions = {
        '오류유형': '오류 유형',
        '복잡도계산': '복잡도 계산',
        '조건분석': '조건 분석',
        '통신비청구절삭': '통신비 청구 절삭',
        '타입불일치': '타입 불일치',
        '중복조건': '중복 조건',
        '자기모순': '자기 모순'
    }
    
    for compound, decomposed in decompositions.items():
        text = text.replace(compound, decomposed)
    
    return text

def expand_query_terms(query: str) -> str:
    """질의 동의어 확장 및 정규화"""
    base = preprocess_korean_text(query)
    q_lower = base.lower()
    synonyms = {
        '오류': ['에러', '이슈', 'error', 'issue'],
        '유형': ['타입', '종류', 'type', 'category'],
        '검출': ['탐지', '감지', 'detect', 'detection', '분석', 'analysis', 'analyze'],
        '추가': ['확장', '등록', 'extend', 'register', 'add'],
        '복잡도': ['complexity', 'metrics', '점수', '지표'],
        '개요': ['overview', 'summary', '설명'],
        '시스템': ['system', '프로젝트', 'solution', '서비스'],
    }
    expanded_terms = []
    for key, vals in synonyms.items():
        if key in q_lower:
            expanded_terms.extend(vals)
        for v in vals:
            if v in q_lower:
                expanded_terms.append(key)
    # 중복 제거
    expanded_terms = list(dict.fromkeys(expanded_terms))
    if expanded_terms:
        return base + " " + " ".join(expanded_terms)
    return base

def classify_question(query: str) -> str:
    q = query.lower()
    if any(k in q for k in ['유형', '타입', '종류']):
        return 'types'
    if any(k in q for k in ['추가', '확장', '등록', 'extend', 'register', 'add']):
        return 'extend'
    if any(k in q for k in ['복잡도', 'complexity', 'metrics']):
        return 'complexity'
    return 'general'

def extract_enumeration_bonus(text: str) -> float:
    """목록/열거 신호에 대한 보너스 점수"""
    try:
        count = len(re.findall(r'(?m)^\s*(?:\d+\.|[-*])\s+\S+', text or ''))
        if count >= 3:
            return min(0.1, 0.02 * count)
        return 0.0
    except Exception:
        return 0.0

def keywords_bonus(meta: dict, label: str) -> float:
    kw = str((meta or {}).get('keywords', ''))
    if not kw:
        return 0.0
    if label == 'types' and any(w in kw for w in ['유형', '타입', '종류', 'type']):
        return 0.05
    if label == 'extend' and any(w in kw for w in ['추가', '확장', '등록', 'extend', 'register']):
        return 0.05
    if label == 'complexity' and any(w in kw for w in ['복잡도', 'complexity', 'metrics']):
        return 0.05
    return 0.0

def pair_boost(content: str, label: str) -> float:
    try:
        text = (content or '').lower()
        pairs = []
        if label == 'types':
            pairs = [('오류','유형'), ('이슈','타입'), ('error','type')]
        elif label == 'extend':
            pairs = [('추가','등록'), ('확장','등록'), ('extend','register')]
        elif label == 'complexity':
            pairs = [('복잡도','점수'), ('complexity','metrics')]
        boost = 0.0
        for a,b in pairs:
            if a in text and b in text:
                boost += 0.05
        return min(boost, 0.1)
    except Exception:
        return 0.0

class OpenAIEmbeddingFunction:
    """ChromaDB 호환 OpenAI 임베딩 함수"""
    
    _MODEL_NAME = "text-embedding-3-small"
    _DIMENSION = 1536

    def name(self) -> str:
        """ChromaDB가 임베딩 함수 호환성을 비교할 때 사용하는 이름"""
        return f"openai:{self._MODEL_NAME}"

    def __call__(self, input: List[str]) -> List[List[float]]:
        """ChromaDB 인터페이스에 맞는 임베딩 함수"""
        embeddings = []
        for text in input:
            try:
                # 전처리된 텍스트 사용
                processed_text = preprocess_korean_text(text)
                
                print(f"🔍 임베딩 API 호출: {processed_text[:50]}... (키: {openai.api_key[:20]}...)")
                response = openai.embeddings.create(
                    model=self._MODEL_NAME,
                    input=processed_text
                )
                print(f"✅ 임베딩 API 성공: {len(response.data[0].embedding)}차원")
                emb = response.data[0].embedding
                # 안전 장치: 임베딩 길이 보정
                if len(emb) != self._DIMENSION:
                    if len(emb) > self._DIMENSION:
                        emb = emb[: self._DIMENSION]
                    else:
                        emb = emb + [0.0] * (self._DIMENSION - len(emb))
                embeddings.append(emb)
            except Exception as e:
                print(f"OpenAI 임베딩 오류: {e}")
                # 폴백: 결정적 난수 기반 1536차원 벡터 생성
                import hashlib, random
                seed = int(hashlib.sha256(processed_text.encode()).hexdigest(), 16) % (2**32)
                rnd = random.Random(seed)
                emb = [rnd.random() for _ in range(self._DIMENSION)]
                embeddings.append(emb)
        
        return embeddings

def get_openai_embedding(text: str) -> List[float]:
    """단일 텍스트용 임베딩 함수 (하위 호환성)"""
    embedding_func = OpenAIEmbeddingFunction()
    return embedding_func([text])[0]

# ChromaDB 설정
CHROMA_DIR = Path("rag_storage/chroma_db")
CHROMA_DIR.mkdir(parents=True, exist_ok=True)

# ChromaDB 클라이언트 초기화
client = chromadb.PersistentClient(
    path=str(CHROMA_DIR),
    settings=Settings(
        anonymized_telemetry=False,
        allow_reset=True
    )
)

# BM25 인덱스를 위한 전역 변수
bm25_index = None
document_texts = []
document_metadata = []

# 컬렉션 생성 또는 가져오기 (OpenAI 임베딩 사용)
embedding_function = OpenAIEmbeddingFunction()

# 부팅 시 컬렉션 초기화 여부를 ENV로 제어 (기본 false)
reset_on_start = os.getenv("RAG_RESET_ON_START", "false").lower() == "true"

try:
    if reset_on_start:
        try:
            client.delete_collection("codemuse_documents")
            print("🗑️ 기존 문서 컬렉션 삭제됨")
        except Exception:
            pass
        try:
            client.delete_collection("codemuse_docstrings")
            print("🗑️ 기존 독스트링 컬렉션 삭제됨")
        except Exception:
            pass

    # 문서 컬렉션 확보
    try:
        collection = client.get_collection("codemuse_documents", embedding_function=embedding_function)
        print("✅ 문서 컬렉션 사용 준비 완료 (기존)")
    except Exception:
        try:
            collection = client.create_collection(
                name="codemuse_documents",
                metadata={"description": "CodeMuse 문서 저장소 (OpenAI 임베딩 1536차원)"},
                embedding_function=embedding_function
            )
            print("✅ 메인 컬렉션 'codemuse_documents' 생성됨 (OpenAI text-embedding-3-small, 1536차원)")
        except Exception:
            # 이미 존재 오류 등 어떤 이유로든 create 실패 시 최종 get으로 시도
            collection = client.get_collection("codemuse_documents", embedding_function=embedding_function)
            print("✅ 문서 컬렉션 사용 준비 완료 (get-or-create 폴백)")

    # 독스트링 컬렉션 확보
    try:
        docstring_collection = client.get_collection("codemuse_docstrings", embedding_function=embedding_function)
        print("✅ 독스트링 컬렉션 사용 준비 완료 (기존)")
    except Exception:
        try:
            docstring_collection = client.create_collection(
                name="codemuse_docstrings",
                metadata={"description": "독스트링 전용 검색 인덱스 (OpenAI 임베딩 1536차원)"},
                embedding_function=embedding_function
            )
            print("✅ 독스트링 컬렉션 'codemuse_docstrings' 생성됨 (OpenAI text-embedding-3-small, 1536차원)")
        except Exception:
            docstring_collection = client.get_collection("codemuse_docstrings", embedding_function=embedding_function)
            print("✅ 독스트링 컬렉션 사용 준비 완료 (get-or-create 폴백)")

except Exception as e:
    print(f"❌ 컬렉션 준비 오류: {e}")
    raise

def update_bm25_index():
    """BM25 인덱스 업데이트"""
    global bm25_index, document_texts, document_metadata
    
    # 모든 문서 가져오기
    all_docs = collection.get()
    document_texts = []
    document_metadata = []
    
    for i, content in enumerate(all_docs['documents']):
        if content:
            # 한국어 토크나이징
            tokens = korean_tokenize(content)
            document_texts.append(tokens)
            document_metadata.append({
                'id': all_docs['ids'][i],
                'metadata': all_docs['metadatas'][i] if all_docs['metadatas'] else {}
            })
    
    if document_texts:
        bm25_index = BM25Okapi(document_texts)
        print(f"✅ BM25 인덱스 업데이트 완료: {len(document_texts)}개 문서")
    else:
        bm25_index = None
        print("ℹ️  BM25 인덱스: 문서가 없습니다")

# 초기 BM25 인덱스 생성
update_bm25_index()

def hybrid_search(query: str, limit: int = 10) -> List[dict]:
    """하이브리드 검색 (BM25 + 벡터)"""
    global bm25_index, document_metadata
    
    if not bm25_index or not document_metadata:
        return []
    
    # 1. BM25 검색
    query_tokens = korean_tokenize(query)
    bm25_scores = bm25_index.get_scores(query_tokens)
    
    # 2. 벡터 검색
    vector_results = collection.query(
        query_texts=[query],
        n_results=min(limit * 2, 50),
        include=['documents', 'metadatas', 'distances']
    )
    
    # 3. 결과 통합 (Reciprocal Rank Fusion)
    doc_scores = {}
    
    # BM25 점수 정규화 및 저장
    if len(bm25_scores) > 0:
        max_bm25 = max(bm25_scores)
        min_bm25 = min(bm25_scores)
        if max_bm25 > min_bm25:
            bm25_scores = [(score - min_bm25) / (max_bm25 - min_bm25) for score in bm25_scores]
        
        for i, score in enumerate(bm25_scores):
            if i < len(document_metadata):
                doc_id = document_metadata[i]['id']
                doc_scores[doc_id] = {'bm25': score, 'vector': 0, 'metadata': document_metadata[i]['metadata']}
    
    # 벡터 점수 저장
    if vector_results['documents'] and vector_results['documents'][0]:
        for i, doc_id in enumerate(vector_results['ids'][0]):
            distance = vector_results['distances'][0][i]
            vector_score = 1.0 / (1.0 + distance)  # 거리를 점수로 변환
            
            if doc_id in doc_scores:
                doc_scores[doc_id]['vector'] = vector_score
            else:
                doc_scores[doc_id] = {
                    'bm25': 0,
                    'vector': vector_score,
                    'metadata': vector_results['metadatas'][0][i] if vector_results['metadatas'] and vector_results['metadatas'][0] else {}
                }
    
    # 4. RRF 점수 계산 (Reciprocal Rank Fusion)
    final_scores = []
    for doc_id, scores in doc_scores.items():
        # RRF 공식: 1 / (k + rank)
        # 여기서는 점수를 rank로 사용 (낮은 점수 = 높은 순위)
        k = 60  # RRF 상수
        
        # 벡터와 BM25 점수를 rank로 변환 (1 - score)
        vector_rank = 1.0 - scores['vector']
        bm25_rank = 1.0 - scores['bm25']
        
        # RRF 점수 계산
        rrf_score = (1.0 / (k + vector_rank)) + (1.0 / (k + bm25_rank))
        
        final_scores.append({
            'id': doc_id,
            'score': rrf_score,
            'bm25_score': scores['bm25'],
            'vector_score': scores['vector'],
            'metadata': scores['metadata']
        })
    
    # 5. 점수순 정렬 및 상위 결과 반환
    final_scores.sort(key=lambda x: x['score'], reverse=True)
    
    # 문서 내용 가져오기
    results = []
    for doc in final_scores[:limit]:
        doc_content = collection.get(ids=[doc['id']], include=['documents'])
        if doc_content['documents'] and doc_content['documents'][0]:
            results.append({
                'id': doc['id'],
                'content': doc_content['documents'][0],
                'metadata': doc['metadata'],
                'score': doc['score'],
                'bm25_score': doc['bm25_score'],
                'vector_score': doc['vector_score'],
                'similarity_score': doc['vector_score'],
                'folder_priority': _get_folder_priority(doc['metadata'])
            })
    
    return results

@app.get("/")
async def root():
    return {"service": "RAG Engine", "status": "running", "port": 8003, "storage": "ChromaDB"}

@app.get("/health")
async def health():
    try:
        # 컬렉션 상태 확인
        count = collection.count()
        return {
            "status": "healthy", 
            "service": "RAG Engine",
            "storage": "ChromaDB",
            "documents_count": count
        }
    except Exception as e:
        return {"status": "unhealthy", "error": str(e)}

def _get_folder_priority(metadata):
    """폴더별 우선순위 점수 계산"""
    source = metadata.get('source', '')
    
    # 폴더별 우선순위 (높을수록 우선)
    priority_map = {
        'analyzers': 100,      # 핵심 분석기 (가장 중요)
        'shared': 80,          # 공유 모듈
        'streaming': 60,       # 스트리밍 모듈
        'formatters': 40,      # 포매터 모듈
        'templates': 20,       # 템플릿 (낮은 우선순위)
        'options': 20,         # 옵션 (낮은 우선순위)
        'utils': 30,           # 유틸리티
        'config': 50,          # 설정
        'protocols': 40,       # 프로토콜
    }
    
    # 파일 경로에서 폴더 추출
    for folder, priority in priority_map.items():
        if f'/{folder}/' in source or f'\\{folder}\\' in source:
            return priority
    
    # 기본 우선순위
    return 10

def _calculate_enhanced_score(semantic_score, metadata):
    """의미적 점수 + 폴더 우선순위로 최종 점수 계산"""
    folder_priority = _get_folder_priority(metadata)
    
    # 의미적 점수 (0-1) + 폴더 우선순위 (0-100)을 결합
    # 폴더 우선순위를 0.1 스케일로 조정하여 의미적 점수에 더함
    enhanced_score = semantic_score + (folder_priority * 0.01)
    
    return min(enhanced_score, 1.0)  # 최대 1.0으로 제한

@app.post("/api/v1/search", response_model=SearchResponse)
async def search_documents(request: SearchRequest):
    """문서 검색 API (하이브리드 검색: BM25 + 벡터)"""
    try:
        print(f"🔍 하이브리드 검색 요청: '{request.query}' (limit: {request.limit})")
        question_label = classify_question(request.query)
        expanded = expand_query_terms(request.query)

        # 하이브리드 검색 수행 (여유분으로 더 많이)
        raw_results = hybrid_search(expanded, max(request.limit * 3, request.limit))

        # 결과를 표준 형식으로 변환
        formatted_results = []
        for result in raw_results:
            formatted_results.append({
                "id": result['id'],
                "content": result['content'],
                "metadata": result['metadata'],
                "score": result['score'],
                "similarity_score": result['vector_score'],
                "semantic_score": result['vector_score'],
                "bm25_score": result['bm25_score'],
                "folder_priority": result['folder_priority']
            })

        # 1) 필터 적용
        def apply_filters(items: List[dict]) -> List[dict]:
            filtered = items
            # chunk_type_filter (파이프 구분자 지원)
            if request.chunk_type_filter:
                allow_types = [t.strip().lower() for t in str(request.chunk_type_filter).split('|') if t.strip()]
                filtered = [x for x in filtered if str(x.get('metadata', {}).get('chunk_type', '')).lower() in allow_types]
            # exclude_chunk_types
            if request.exclude_chunk_types:
                exclude_set = set([t.strip().lower() for t in request.exclude_chunk_types if isinstance(t, str)])
                filtered = [x for x in filtered if str(x.get('metadata', {}).get('chunk_type', '')).lower() not in exclude_set]
            # file_filter (부분 문자열 매칭)
            if request.file_filter:
                def match_file(meta: dict) -> bool:
                    filename = (meta.get('filename') or '')
                    source_file = (meta.get('source_file') or '')
                    source = (meta.get('source') or '')
                    target = f"{filename} {source_file} {source}".lower()
                    for f in request.file_filter:
                        if isinstance(f, str) and f.strip().lower() in target:
                            return True
                    return False
                filtered = [x for x in filtered if match_file(x.get('metadata', {}))]
            return filtered

        filtered_results = apply_filters(formatted_results)

        # 재랭킹: 열거 보너스 + 키워드 보너스 + 의미쌍 보너스(일반 규칙)
        for r in filtered_results:
            enum_boost = extract_enumeration_bonus(r.get('content', ''))
            kw_boost = keywords_bonus(r.get('metadata', {}), question_label)
            pair = pair_boost(r.get('content',''), question_label)
            r['score'] = r.get('score', 0) + enum_boost + kw_boost + pair

            # 추가 가중치: 'types' 의도 시 class/function 우선, overview 미세 감점
            ct = str(r.get('metadata', {}).get('chunk_type', '')).lower()
            if question_label == 'types':
                if ct in ('class', 'function'):
                    r['score'] += 0.05
                elif ct == 'overview':
                    r['score'] -= 0.02

        # 2) 폴더 우선순위 정렬 (analyzers 우선), 폴더 내 점수순
        filtered_results.sort(key=lambda x: (x['folder_priority'], x['score']), reverse=True)

        # 3) 상위 limit 추출
        final_results = filtered_results[:request.limit]

        print(f"📊 하이브리드 검색 결과 분석 (필터 적용 후 {len(final_results)}/{len(filtered_results)}):")
        for i, result in enumerate(final_results):
            folder = result['metadata'].get('source', '').split('/')[-2] if '/' in result['metadata'].get('source', '') else 'unknown'
            print(f"  {i+1}. {result['metadata'].get('filename', 'N/A')} (폴더: {folder}, 점수: {result['score']:.3f})")

        print(f"✅ 검색 완료: {len(final_results)}개 결과 반환")

        return SearchResponse(
            results=final_results,
            query=request.query,
            total_found=len(final_results)
        )
        
    except Exception as e:
        print(f"❌ 검색 오류: {str(e)}")
        raise HTTPException(status_code=500, detail=f"검색 중 오류 발생: {str(e)}")

@app.get("/documents")
async def list_documents():
    """저장된 문서 목록 조회"""
    try:
        # 모든 문서 가져오기
        results = collection.get()
        
        documents = []
        if results['documents']:
            for i, doc in enumerate(results['documents']):
                documents.append({
                    "id": results['ids'][i],
                    "content": doc,
                    "metadata": results['metadatas'][i] if results['metadatas'] else {}
                })
        
        return {"documents": documents, "count": len(documents)}
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"문서 목록 조회 중 오류 발생: {str(e)}")

@app.get("/api/v1/documents/folders")
async def get_folder_structure():
    """폴더 구조 기반 문서 목록 조회 (개선된 구조)"""
    try:
        results = collection.get()
        
        # 프로젝트별 폴더 구조 생성
        project_structure = {}
        
        if results['documents']:
            for i, doc in enumerate(results['documents']):
                doc_id = results['ids'][i]
                metadata = results['metadatas'][i] if results['metadatas'] else {}
                source = metadata.get('source', '')
                
                # 경로에서 프로젝트와 폴더 구조 추출
                if '/' in source:
                    path_parts = source.split('/')
                    
                    # generated_docs/raas-rule-analyzer/src/raas_rule_analyzer/analyzers/issue_detector.md
                    # -> 프로젝트: raas-rule-analyzer, 폴더: analyzers, 파일: issue_detector.md
                    
                    if 'generated_docs' in path_parts:
                        # generated_docs/프로젝트명/... 형태
                        if len(path_parts) > 2:
                            # generated_docs/rule_analyzer/analyzers/file.md
                            project_name = path_parts[1]
                            remaining_parts = path_parts[2:]  # generated_docs/프로젝트명 제거
                        else:
                            # generated_docs/project_summary.md (루트 레벨 파일)
                            project_name = 'sample_code'  # 분석 대상 프로젝트명으로 분류
                            remaining_parts = [path_parts[1]]  # 파일명만
                    else:
                        # 직접적인 경로
                        project_name = 'sample_code'
                        remaining_parts = path_parts
                    
                    if remaining_parts:
                        filename = remaining_parts[-1]
                        folder_parts = remaining_parts[:-1]
                        
                        # 폴더 경로 생성 (프로젝트 내부 구조만)
                        if folder_parts:
                            # src/raas_rule_analyzer/analyzers -> analyzers
                            # src/raas_rule_analyzer/formatters/options -> formatters/options
                            if 'src' in folder_parts and len(folder_parts) > 2:
                                # src/raas_rule_analyzer 제거하고 나머지 경로 유지
                                src_index = folder_parts.index('src')
                                if src_index + 2 < len(folder_parts):
                                    folder_path = '/'.join(folder_parts[src_index + 2:])
                                else:
                                    folder_path = '루트'
                            else:
                                folder_path = '/'.join(folder_parts)
                        else:
                            folder_path = '루트'
                    else:
                        folder_path = '루트'
                        filename = source
                else:
                    project_name = '기타'
                    folder_path = '루트'
                    filename = source
                
                # 프로젝트별 구조 생성
                if project_name not in project_structure:
                    project_structure[project_name] = {}
                
                if folder_path not in project_structure[project_name]:
                    project_structure[project_name][folder_path] = []
                
                project_structure[project_name][folder_path].append({
                    "id": doc_id,
                    "filename": filename,
                    "source": source,
                    "created_at": metadata.get('created_at', ''),
                    "type": metadata.get('type', 'document'),
                    "category": metadata.get('category', 'general'),
                    "preview": doc[:150] + "..." if len(doc) > 150 else doc
                })
        
        # 폴더별로 정렬
        for project in project_structure:
            for folder in project_structure[project]:
                project_structure[project][folder].sort(key=lambda x: x['filename'])
        
        return {
            "projects": project_structure,
            "total_documents": len(results['ids']) if results['ids'] else 0,
            "project_count": len(project_structure)
        }
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"폴더 구조 조회 중 오류 발생: {str(e)}")

@app.get("/api/v1/documents/{document_id}")
async def get_document_detail(document_id: str):
    """특정 문서 상세 조회 (간단한 링크 매핑 지원)"""
    try:
        # 간단한 링크 매핑에서 실제 문서 ID 찾기
        real_doc_id = SIMPLE_LINK_MAPPING.get(document_id, document_id)
        print(f"🔍 문서 상세 조회 - 링크 매핑: '{document_id}' → '{real_doc_id}'")
        
        # 실제 문서 ID로 조회
        results = collection.get(ids=[real_doc_id])
        
        if not results['ids']:
            raise HTTPException(status_code=404, detail="문서를 찾을 수 없습니다.")
        
        doc_id = results['ids'][0]
        content = results['documents'][0]
        metadata = results['metadatas'][0] if results['metadatas'] else {}
        
        return {
            "id": doc_id,
            "content": content,
            "metadata": metadata,
            "word_count": len(content.split()),
            "char_count": len(content),
            "match_type": "link_mapping" if document_id != real_doc_id else "direct_id"
        }
        
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"문서 상세 조회 중 오류 발생: {str(e)}")

@app.get("/api/v1/documents/{document_id}/content")
async def get_document_content(document_id: str):
    """특정 문서 내용만 조회 (간단한 링크 매핑 지원)"""
    try:
        # 간단한 링크 매핑에서 실제 문서 ID 찾기
        real_doc_id = SIMPLE_LINK_MAPPING.get(document_id, document_id)
        print(f"🔍 링크 매핑: '{document_id}' → '{real_doc_id}'")
        
        # 실제 문서 ID로 조회
        results = collection.get(ids=[real_doc_id])
        
        if results['ids']:
            doc_id = results['ids'][0]
            content = results['documents'][0]
            metadata = results['metadatas'][0] if results['metadatas'] else {}
            
            return {
                "id": doc_id,
                "content": content,
                "metadata": metadata
            }
        else:
            raise HTTPException(status_code=404, detail="문서를 찾을 수 없습니다.")
            
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"문서 내용 조회 중 오류 발생: {str(e)}")

@app.get("/api/v1/documents/{path:path}")
async def get_document_by_path(path: str):
    """경로 기반 문서 조회 (간단한 링크 매핑 지원)"""
    try:
        # URL 디코딩 및 경로 정규화
        import urllib.parse
        decoded_path = urllib.parse.unquote(path)
        print(f"🔍 경로 기반 문서 조회: {decoded_path}")
        
        # 간단한 링크 매핑에서 실제 문서 ID 찾기
        real_doc_id = SIMPLE_LINK_MAPPING.get(decoded_path, decoded_path)
        print(f"🔍 링크 매핑: '{decoded_path}' → '{real_doc_id}'")
        
        # 실제 문서 ID로 조회
        results = collection.get(ids=[real_doc_id])
        
        if results['ids']:
            doc_id = results['ids'][0]
            content = results['documents'][0]
            metadata = results['metadatas'][0] if results['metadatas'] else {}
            
            return {
                "id": doc_id,
                "content": content,
                "metadata": metadata,
                "word_count": len(content.split()),
                "char_count": len(content),
                "match_type": "link_mapping",
                "all_matches": 1
            }
        
        # 문서를 찾을 수 없는 경우
        raise HTTPException(status_code=404, detail="문서를 찾을 수 없습니다.")
        
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"문서 조회 중 오류 발생: {str(e)}")
        for i, doc_id in enumerate(all_docs['ids']):
            metadata = all_docs['metadatas'][i] if all_docs['metadatas'] else {}
            source = metadata.get('source', '')
            filename = metadata.get('filename', '')
            chunk_type = metadata.get('chunk_type', '')
            
            print(f"  📄 문서 {i}: ID='{doc_id}', filename='{filename}', chunk_type='{chunk_type}', source='{source}'")
            
            # 경로 매칭 시도
            # 1. 정확한 ID 매칭 (최우선)
            if doc_id == decoded_path:
                print(f"    ✅ 정확한 ID 매칭!")
                matched_docs.append({
                    'id': doc_id,
                    'content': all_docs['documents'][i],
                    'metadata': metadata,
                    'match_type': 'exact_id'
                })
            # 2. 전체 경로 매칭
            elif decoded_path in source or source in decoded_path:
                print(f"    ✅ source_path 매칭!")
                matched_docs.append({
                    'id': doc_id,
                    'content': all_docs['documents'][i],
                    'metadata': metadata,
                    'match_type': 'source_path'
                })
            # 3. 파일명 + 청크 타입 매칭
            elif filename and chunk_type:
                # sample_code/rule_analyzer/exceptions.py/file overview 형태로 매칭
                # .md 파일명을 .py로 변환하여 매칭
                py_filename = filename.replace('.md', '.py')
                expected_pattern = f"{py_filename}/{chunk_type}"
                if expected_pattern.lower() in decoded_path.lower():
                    print(f"    ✅ filename_chunk 매칭! 패턴: '{expected_pattern}'")
                    matched_docs.append({
                        'id': doc_id,
                        'content': all_docs['documents'][i],
                        'metadata': metadata,
                        'match_type': 'filename_chunk'
                    })
            # 4. 파일명만 매칭 (청크 타입이 'overview'인 경우)
            elif filename and 'overview' in decoded_path.lower():
                if filename.lower() in decoded_path.lower():
                    print(f"    ✅ filename_overview 매칭!")
                    matched_docs.append({
                        'id': doc_id,
                        'content': all_docs['documents'][i],
                        'metadata': metadata,
                        'match_type': 'filename_overview'
                    })
            # 5. 새로운 링크 형식 매칭 (class-issuedetector, class_IssueDetector)
            # class-issuedetector 형식 매칭 (프론트엔드 호환)
            elif decoded_path.startswith('class-'):
                class_name = decoded_path.replace('class-', '').lower()
                # 클래스명이 content에 포함되어 있는지 확인 (조건 완화)
                content_lower = all_docs['documents'][i].lower()
                if class_name in content_lower:
                    print(f"    ✅ class_dash_link 매칭! 클래스: '{class_name}', chunk_type: '{chunk_type}'")
                    matched_docs.append({
                        'id': doc_id,
                        'content': all_docs['documents'][i],
                        'metadata': metadata,
                        'match_type': 'class_dash_link'
                    })
            # class_IssueDetector 형식 매칭
            elif decoded_path.startswith('class_'):
                class_name = decoded_path.replace('class_', '').lower()
                # 클래스명이 content에 포함되어 있는지 확인
                content_lower = all_docs['documents'][i].lower()
                if class_name in content_lower and chunk_type == 'class':
                    print(f"    ✅ class_underscore_link 매칭! 클래스: '{class_name}'")
                    matched_docs.append({
                        'id': doc_id,
                        'content': all_docs['documents'][i],
                        'metadata': metadata,
                        'match_type': 'class_underscore_link'
                    })
            # func_detect_type_mismatch_157 형식 매칭
            elif decoded_path.startswith('func_'):
                func_parts = decoded_path.replace('func_', '').split('_')
                if len(func_parts) >= 2:
                    func_name = '_'.join(func_parts[:-1])  # 마지막 부분(줄번호) 제외
                    line_number = func_parts[-1]  # 마지막 부분이 줄번호
                    content_lower = all_docs['documents'][i].lower()
                    if func_name in content_lower and chunk_type == 'function':
                        print(f"    ✅ func_link 매칭! 함수: '{func_name}', 줄: '{line_number}'")
                        matched_docs.append({
                            'id': doc_id,
                            'content': all_docs['documents'][i],
                            'metadata': metadata,
                            'match_type': 'func_link'
                        })
            # file_issue_detector.py 형식 매칭
            elif decoded_path.startswith('file_'):
                file_name = decoded_path.replace('file_', '')
                if filename and file_name.lower() in filename.lower():
                    print(f"    ✅ file_link 매칭! 파일: '{file_name}'")
                    matched_docs.append({
                        'id': doc_id,
                        'content': all_docs['documents'][i],
                        'metadata': metadata,
                        'match_type': 'file_link'
                    })
        
        if not matched_docs:
            # 더 유연한 매칭 시도
            path_parts = decoded_path.lower().split('/')
            for i, doc_id in enumerate(all_docs['ids']):
                metadata = all_docs['metadatas'][i] if all_docs['metadatas'] else {}
                source = metadata.get('source', '').lower()
                filename = metadata.get('filename', '').lower()
                chunk_type = metadata.get('chunk_type', '').lower()
                content_lower = all_docs['documents'][i].lower()
                
                # class-issuedetector 형식 특별 처리
                if decoded_path == 'class-issuedetector':
                    if 'issuedetector' in content_lower and chunk_type == 'class':
                        print(f"    ✅ class-issuedetector 특별 매칭!")
                        matched_docs.append({
                            'id': doc_id,
                            'content': all_docs['documents'][i],
                            'metadata': metadata,
                            'match_type': 'class_special_match'
                        })
                        break  # 첫 번째 매칭만 사용
                
                # 경로의 마지막 부분이 파일명과 매칭되는지 확인
                elif path_parts and filename:
                    last_part = path_parts[-1]
                    if (filename.replace('.py', '').replace('.md', '') in last_part or 
                        last_part in filename.replace('.py', '').replace('.md', '')):
                        matched_docs.append({
                            'id': doc_id,
                            'content': all_docs['documents'][i],
                            'metadata': metadata,
                            'match_type': 'flexible_match'
                        })
        
        if not matched_docs:
            raise HTTPException(status_code=404, detail=f"경로 '{decoded_path}'에 해당하는 문서를 찾을 수 없습니다.")
        
        # 가장 적합한 매칭 결과 선택 (우선순위: exact_id > source_path > filename_chunk > filename_overview > flexible_match)
        priority_order = ['exact_id', 'source_path', 'filename_chunk', 'filename_overview', 'flexible_match']
        best_match = None
        for priority in priority_order:
            for doc in matched_docs:
                if doc['match_type'] == priority:
                    best_match = doc
                    break
            if best_match:
                break
        
        # 기본적으로 첫 번째 매칭 결과 사용
        if not best_match:
            best_match = matched_docs[0]
        
        content = best_match['content']
        metadata = best_match['metadata']
        
        print(f"✅ 문서 찾음: {metadata.get('filename', 'N/A')} (타입: {metadata.get('chunk_type', 'N/A')})")
        
        return {
            "id": best_match['id'],
            "content": content,
            "metadata": metadata,
            "word_count": len(content.split()),
            "char_count": len(content),
            "match_type": best_match['match_type'],
            "all_matches": len(matched_docs)
        }
        
    except HTTPException:
        raise
    except Exception as e:
        print(f"❌ 경로 기반 문서 조회 오류: {str(e)}")
        raise HTTPException(status_code=500, detail=f"문서 조회 중 오류 발생: {str(e)}")

@app.get("/api/v1/code/search")
async def search_source_code(query: str, limit: int = 10):
    """소스코드에서 특정 로직의 위치를 찾는 API"""
    try:
        import os
        import re
        from pathlib import Path
        
        print(f"🔍 소스코드 검색: '{query}'")
        
        # 검색할 디렉토리 설정 (sample_code만 검색)
        search_dirs = [
            "sample_code"
        ]
        
        matches = []
        
        for search_dir in search_dirs:
            if not os.path.exists(search_dir):
                continue
                
            # Python 파일 검색
            for root, dirs, files in os.walk(search_dir):
                for file in files:
                    if file.endswith('.py'):
                        file_path = os.path.join(root, file)
                        try:
                            with open(file_path, 'r', encoding='utf-8') as f:
                                lines = f.readlines()
                                
                            # 파일 내용에서 검색
                            for line_num, line in enumerate(lines, 1):
                                if re.search(query.lower(), line.lower(), re.IGNORECASE):
                                    # 함수나 클래스 정의 찾기
                                    context_start = max(0, line_num - 5)
                                    context_end = min(len(lines), line_num + 5)
                                    context = ''.join(lines[context_start:context_end])
                                    
                                    matches.append({
                                        "file": file_path,
                                        "line": line_num,
                                        "content": line.strip(),
                                        "context": context,
                                        "relative_path": os.path.relpath(file_path)
                                    })
                                    
                                    if len(matches) >= limit:
                                        break
                                        
                        except Exception as e:
                            print(f"❌ 파일 읽기 오류 {file_path}: {e}")
                            continue
                            
                if len(matches) >= limit:
                    break
                    
            if len(matches) >= limit:
                break
        
        print(f"✅ {len(matches)}개 매칭 결과 발견")
        
        return {
            "query": query,
            "matches": matches[:limit],
            "total_found": len(matches)
        }
        
    except Exception as e:
        print(f"❌ 소스코드 검색 오류: {str(e)}")
        raise HTTPException(status_code=500, detail=f"소스코드 검색 중 오류 발생: {str(e)}")

@app.get("/api/v1/files/list")
async def list_md_files():
    """MD 파일 리스트 조회 (사람용 브라우징) - 간단한 구조"""
    try:
        generated_docs_path = "generated_docs"
        if not os.path.exists(generated_docs_path):
            return {
                "files": [],
                "total_files": 0,
                "folders": []
            }
        
        files = []
        folders = set()
        
        for root, dirs, filenames in os.walk(generated_docs_path):
            for filename in filenames:
                if filename.endswith('.md'):
                    file_path = os.path.join(root, filename)
                    relative_path = os.path.relpath(file_path, generated_docs_path)
                    
                    # 파일 정보
                    stat = os.stat(file_path)
                    file_size = stat.st_size
                    
                    # 폴더 경로 추출
                    path_parts = relative_path.split(os.sep)
                    folder_path = "/".join(path_parts[:-1]) if len(path_parts) > 1 else "루트"
                    folders.add(folder_path)
                    
                    file_info = {
                        "id": filename.replace('.md', ''),
                        "name": filename,
                        "path": relative_path,
                        "folder": folder_path,
                        "size": file_size,
                        "size_readable": f"{file_size / 1024:.1f} KB" if file_size > 1024 else f"{file_size} bytes",
                        "created_at": stat.st_ctime,
                        "modified_at": stat.st_mtime
                    }
                    
                    files.append(file_info)
        
        return {
            "files": files,
            "total_files": len(files),
            "folders": sorted(list(folders))
        }
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"MD 파일 목록 조회 중 오류 발생: {str(e)}")

@app.get("/api/v1/files/{file_id}")
async def get_md_file_content(file_id: str):
    """MD 파일 내용 조회 (사람용 브라우징)"""
    try:
        generated_docs_path = "generated_docs"
        
        # 파일 찾기
        md_file_path = None
        for root, dirs, filenames in os.walk(generated_docs_path):
            for filename in filenames:
                if filename.endswith('.md') and filename.replace('.md', '') == file_id:
                    md_file_path = os.path.join(root, filename)
                    break
            if md_file_path:
                break
        
        if not md_file_path or not os.path.exists(md_file_path):
            raise HTTPException(status_code=404, detail="MD 파일을 찾을 수 없습니다.")
        
        # 파일 내용 읽기
        with open(md_file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # 파일 정보
        stat = os.stat(md_file_path)
        relative_path = os.path.relpath(md_file_path, generated_docs_path)
        
        return {
            "id": file_id,
            "filename": os.path.basename(md_file_path),
            "path": relative_path,
            "content": content,
            "size": stat.st_size,
            "created_at": stat.st_ctime,
            "modified_at": stat.st_mtime,
            "word_count": len(content.split()),
            "char_count": len(content),
            "line_count": len(content.splitlines())
        }
        
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"MD 파일 내용 조회 중 오류 발생: {str(e)}")

@app.post("/api/v1/documents")
async def add_document(request: DocumentRequest):
    """새 문서 추가"""
    try:
        # 문서 ID 생성
        doc_id = request.id or str(uuid.uuid4())
        
        # 메타데이터 준비
        metadata = request.metadata or {}
        
        # tags가 리스트인 경우 문자열로 변환 (ChromaDB 호환성)
        if "tags" in metadata and isinstance(metadata["tags"], list):
            metadata["tags"] = ", ".join(metadata["tags"])
            
        metadata.update({
            "created_at": datetime.now().isoformat(),
            "source": metadata.get("source", "unknown")
        })
        
        # ChromaDB에 문서 upsert (안정적 ID로 최신 상태 유지)
        try:
            collection.upsert(
                documents=[request.content],
                metadatas=[metadata],
                ids=[doc_id]
            )
        except Exception:
            # 구버전 호환: 존재 시 update, 없으면 add
            try:
                existing = collection.get(ids=[doc_id])
                if existing and existing.get('ids'):
                    collection.update(ids=[doc_id], metadatas=[metadata], documents=[request.content])
                else:
                    collection.add(ids=[doc_id], metadatas=[metadata], documents=[request.content])
            except Exception as inner:
                raise HTTPException(status_code=500, detail=f"문서 upsert 실패: {inner}")
        
        # 독스트링이 있는 경우 독스트링 컬렉션에도 upsert
        docstring = metadata.get("docstring")
        if docstring and docstring.strip():
            docstring_id = f"docstring_{doc_id}"
            docstring_metadata = {
                "parent_id": doc_id,
                "chunk_type": metadata.get("chunk_type", "unknown"),
                "name": metadata.get("name", "unknown"),
                "filename": metadata.get("filename", "unknown"),
                "project": metadata.get("project", "unknown"),
                "source": metadata.get("source", "unknown"),
                "created_at": datetime.now().isoformat()
            }
            
            try:
                docstring_collection.upsert(
                    documents=[docstring],
                    metadatas=[docstring_metadata],
                    ids=[docstring_id]
                )
            except Exception:
                try:
                    existing = docstring_collection.get(ids=[docstring_id])
                    if existing and existing.get('ids'):
                        docstring_collection.update(ids=[docstring_id], metadatas=[docstring_metadata], documents=[docstring])
                    else:
                        docstring_collection.add(ids=[docstring_id], metadatas=[docstring_metadata], documents=[docstring])
                except Exception as inner:
                    raise HTTPException(status_code=500, detail=f"독스트링 upsert 실패: {inner}")
        
        # BM25 인덱스 업데이트
        update_bm25_index()
        
        return {
            "status": "success",
            "message": f"문서 '{doc_id}' 저장 완료",
            "document_id": doc_id,
            "total_documents": collection.count()
        }
        
    except Exception as e:
        print(f"❌ 문서 추가 오류: {str(e)}")
        raise HTTPException(status_code=500, detail=f"문서 추가 중 오류 발생: {str(e)}")

@app.post("/api/v1/documents/upload")
async def upload_documents(request: DocumentRequest):
    """문서 업로드 (프론트엔드 호환용)"""
    try:
        result = await add_document(request)
        # 프론트엔드 호환 응답 형식으로 변환
        return {
            "status": "success",
            "message": result.get("message", "문서가 성공적으로 업로드되었습니다"),
            "document_id": result.get("document_id"),
            "total_documents": result.get("total_documents", 0)
        }
    except Exception as e:
        print(f"❌ 문서 업로드 오류: {str(e)}")
        raise HTTPException(status_code=500, detail=f"문서 업로드 중 오류: {str(e)}")

@app.delete("/api/v1/documents/{doc_id}")
async def delete_document(doc_id: str):
    """문서 삭제"""
    try:
        # 문서 존재 확인
        existing = collection.get(ids=[doc_id])
        if not existing['ids']:
            raise HTTPException(status_code=404, detail=f"문서 '{doc_id}'를 찾을 수 없습니다")
        
        # 문서 삭제
        collection.delete(ids=[doc_id])
        
        return {
            "status": "success",
            "message": f"문서 '{doc_id}' 삭제 완료",
            "total_documents": collection.count()
        }
            
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"문서 삭제 중 오류 발생: {str(e)}")

@app.delete("/api/v1/documents")
async def clear_all_documents():
    """모든 문서 삭제"""
    try:
        # 삭제 전 문서 수 확인
        initial_count = collection.count()
        print(f"🗑️ 삭제 전 문서 수: {initial_count}")
        
        # 모든 문서 ID 가져오기
        all_docs = collection.get()
        deleted_count = 0
        if all_docs['ids']:
            print(f"🗑️ 삭제할 문서 ID 수: {len(all_docs['ids'])}")
            collection.delete(ids=all_docs['ids'])
            deleted_count = len(all_docs['ids'])
        
        # 독스트링 컬렉션도 모두 삭제
        all_docstrings = docstring_collection.get()
        docstring_count = 0
        if all_docstrings['ids']:
            print(f"🗑️ 삭제할 독스트링 ID 수: {len(all_docstrings['ids'])}")
            docstring_collection.delete(ids=all_docstrings['ids'])
            docstring_count = len(all_docstrings['ids'])
        
        # 독스트링 컬렉션도 재생성 (완전 초기화)
        try:
            client.delete_collection("codemuse_docstrings")
            docstring_collection = client.create_collection(
                name="codemuse_docstrings",
                metadata={"description": "CodeMuse 독스트링 저장소 (OpenAI 임베딩 1536차원)"},
                embedding_function=embedding_function
            )
            print("✅ 독스트링 컬렉션 재생성 완료")
            # 전역 변수 업데이트
            globals()['docstring_collection'] = docstring_collection
        except Exception as docstring_error:
            print(f"⚠️ 독스트링 컬렉션 재생성 실패: {docstring_error}")
        
        # BM25 인덱스 완전 초기화
        global bm25_index, document_texts, document_metadata
        bm25_index = None
        document_texts = []
        document_metadata = []
        print("✅ BM25 인덱스 완전 초기화 완료")
        
        # 삭제 후 실제 문서 수 확인
        final_count = collection.count()
        print(f"🗑️ 삭제 후 문서 수: {final_count}")
        
        if final_count > 0:
            print(f"⚠️ 일부 문서가 남아있음: {final_count}개")
            # 남은 문서가 있다면 강제로 다시 삭제 시도
            remaining_docs = collection.get()
            if remaining_docs['ids']:
                print(f"🔄 남은 문서 재삭제 시도: {len(remaining_docs['ids'])}개")
                collection.delete(ids=remaining_docs['ids'])
                final_count = collection.count()
                print(f"🗑️ 재삭제 후 문서 수: {final_count}")
                
                # 여전히 문서가 남아있다면 컬렉션 재생성
                if final_count > 0:
                    print(f"🔄 컬렉션 재생성으로 강제 초기화")
                    try:
                        # 기존 컬렉션 삭제
                        client.delete_collection("codemuse_documents")
                        # 새 컬렉션 생성
                        collection = client.create_collection(
                            name="codemuse_documents",
                            metadata={"description": "CodeMuse 문서 저장소 (OpenAI 임베딩 1536차원)"},
                            embedding_function=embedding_function
                        )
                        print("✅ 컬렉션 재생성 완료")
                        final_count = 0
                    except Exception as recreate_error:
                        print(f"❌ 컬렉션 재생성 실패: {recreate_error}")
                        # 전역 변수 업데이트
                        globals()['collection'] = collection
        
        return {
            "status": "success",
            "message": f"모든 문서 삭제 완료 (삭제된 문서: {deleted_count}개, 독스트링: {docstring_count}개)",
            "total_documents": final_count,
            "deleted_documents": deleted_count,
            "deleted_docstrings": docstring_count
        }
    except Exception as e:
        print(f"❌ 문서 전체 삭제 오류: {str(e)}")
        raise HTTPException(status_code=500, detail=f"문서 전체 삭제 중 오류 발생: {str(e)}")

@app.delete("/api/v1/documents/force-reset")
async def force_reset_collections():
    """강제로 모든 컬렉션 초기화 (완전 삭제)"""
    try:
        print("🔄 강제 컬렉션 초기화 시작")
        
        # 모든 컬렉션 삭제
        try:
            client.delete_collection("codemuse_documents")
            print("✅ 문서 컬렉션 삭제됨")
        except Exception as e:
            print(f"⚠️ 문서 컬렉션 삭제 실패: {e}")
        
        try:
            client.delete_collection("codemuse_docstrings")
            print("✅ 독스트링 컬렉션 삭제됨")
        except Exception as e:
            print(f"⚠️ 독스트링 컬렉션 삭제 실패: {e}")
        
        # 새 컬렉션 생성
        global collection, docstring_collection, bm25_index, document_texts, document_metadata
        
        collection = client.create_collection(
            name="codemuse_documents",
            metadata={"description": "CodeMuse 문서 저장소 (OpenAI 임베딩 1536차원)"},
            embedding_function=embedding_function
        )
        print("✅ 새 문서 컬렉션 생성됨")
        
        docstring_collection = client.create_collection(
            name="codemuse_docstrings",
            metadata={"description": "CodeMuse 독스트링 저장소 (OpenAI 임베딩 1536차원)"},
            embedding_function=embedding_function
        )
        print("✅ 새 독스트링 컬렉션 생성됨")
        
        # BM25 인덱스 초기화
        bm25_index = None
        document_texts = []
        document_metadata = []
        print("✅ BM25 인덱스 초기화됨")
        
        return {
            "status": "success",
            "message": "모든 컬렉션이 강제 초기화되었습니다",
            "total_documents": 0
        }
        
    except Exception as e:
        print(f"❌ 강제 초기화 오류: {str(e)}")
        raise HTTPException(status_code=500, detail=f"강제 초기화 중 오류 발생: {str(e)}")

@app.post("/api/v1/search/docstrings")
async def search_docstrings(request: SearchRequest):
    """독스트링 전용 검색 (1차 검색)"""
    try:
        # 독스트링 컬렉션에서 검색
        results = docstring_collection.query(
            query_texts=[request.query],
            n_results=min(request.limit, 20),
            include=['documents', 'metadatas', 'distances']
        )
        
        # 결과 포매팅
        formatted_results = []
        if results['documents'] and results['documents'][0]:
            for i, doc in enumerate(results['documents'][0]):
                metadata = results['metadatas'][0][i]
                distance = results['distances'][0][i]
                
                formatted_results.append({
                    "id": results['ids'][0][i],
                    "content": doc,
                    "metadata": metadata,
                    "score": 1.0 / (1.0 + distance),  # 거리를 점수로 변환
                    "parent_id": metadata.get("parent_id")
                })
        
        return {
            "query": request.query,
            "results": formatted_results,
            "total_results": len(formatted_results),
            "search_type": "docstring_only"
        }
        
    except Exception as e:
        print(f"❌ 독스트링 검색 오류: {str(e)}")
        raise HTTPException(status_code=500, detail=f"독스트링 검색 중 오류 발생: {str(e)}")

@app.post("/api/v1/search/two-stage")
async def two_stage_search(request: SearchRequest):
    """2단계 검색: 1차 독스트링 → 2차 상세 문서"""
    try:
        # 1단계: 독스트링 검색으로 후보 좁히기
        docstring_results = docstring_collection.query(
            query_texts=[request.query],
            n_results=min(request.limit * 2, 20),  # 더 많은 후보 확보
            include=['documents', 'metadatas', 'distances']
        )
        
        # 후보 문서 ID 추출
        candidate_ids = []
        if docstring_results['metadatas'] and docstring_results['metadatas'][0]:
            for metadata in docstring_results['metadatas'][0]:
                parent_id = metadata.get("parent_id")
                if parent_id:
                    candidate_ids.append(parent_id)
        
        # 2단계: 후보 문서에서 상세 검색 (최대 limit개)
        if candidate_ids:
            # 후보 문서들 직접 조회
            detailed_docs = collection.get(ids=candidate_ids[:request.limit])
            formatted_results = []
            
            if detailed_docs['documents']:
                for i, doc in enumerate(detailed_docs['documents']):
                    metadata = detailed_docs['metadatas'][i]
                    doc_id = detailed_docs['ids'][i]
                    
                    # 기본 점수 (독스트링 검색 기반)
                    score = 0.8  # 독스트링 매칭을 통해 찾은 문서는 높은 기본 점수
                    
                    formatted_results.append({
                        "id": doc_id,
                        "content": doc,
                        "metadata": metadata,
                        "score": score,
                        "enhanced_score": _calculate_enhanced_score(score, metadata)
                    })
        else:
            # 후보가 없으면 일반 검색
            detailed_results = collection.query(
                query_texts=[request.query],
                n_results=request.limit,
                include=['documents', 'metadatas', 'distances']
            )
            
            formatted_results = []
            if detailed_results['documents'] and detailed_results['documents'][0]:
                for i, doc in enumerate(detailed_results['documents'][0]):
                    metadata = detailed_results['metadatas'][0][i]
                    distance = detailed_results['distances'][0][i]
                    
                    formatted_results.append({
                        "id": detailed_results['ids'][0][i],
                        "content": doc,
                        "metadata": metadata,
                        "score": 1.0 / (1.0 + distance),
                        "enhanced_score": _calculate_enhanced_score(1.0 / (1.0 + distance), metadata)
                    })
        
        # 점수순 정렬
        formatted_results.sort(key=lambda x: x['enhanced_score'], reverse=True)
        
        return {
            "query": request.query,
            "results": formatted_results,
            "total_results": len(formatted_results),
            "search_type": "two_stage",
            "candidate_count": len(candidate_ids)
        }
        
    except Exception as e:
        print(f"❌ 2단계 검색 오류: {str(e)}")
        raise HTTPException(status_code=500, detail=f"2단계 검색 중 오류 발생: {str(e)}")

if __name__ == "__main__":
    import uvicorn
    print("🚀 ChromaDB 기반 RAG Engine 시작 중...")
    print(f"📁 저장소 위치: {CHROMA_DIR}")
    uvicorn.run(app, host="0.0.0.0", port=8003)