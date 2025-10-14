# 🏗️ CodeMuse 아키텍처팀 전달 문서

> **작성일**: 2025-10-13  
> **대상**: 아키텍처팀  
> **목적**: CodeMuse 백엔드 시스템의 아키텍처 현황 및 기술 스택 공유

---

## 📋 목차

1. [시스템 아키텍처 개요](#1-시스템-아키텍처-개요)
2. [기술 스택 및 의존성](#2-기술-스택-및-의존성)
3. [서비스별 상세 스펙](#3-서비스별-상세-스펙)
4. [인프라 및 배포](#4-인프라-및-배포)
5. [보안 고려사항](#5-보안-고려사항)
6. [성능 및 확장성](#6-성능-및-확장성)
7. [모니터링 및 운영](#7-모니터링-및-운영)
8. [아키텍처 결정 사항(ADR)](#8-아키텍처-결정-사항adr)
9. [마이그레이션/배포 전략](#9-마이그레이션배포-전략)
10. [개선 권고사항](#10-개선-권고사항-우선순위별)
11. [기술 부채 및 제약사항](#11-기술-부채-및-제약사항)
12. [참고 문서](#12-참고-문서)

---

## 1. 시스템 아키텍처 개요

### 1.1 전체 구조

- **아키텍처 패턴**: 마이크로서비스 기반 모듈형 아키텍처
- **서비스 구성**: 4개의 독립 서비스 + 공통 모듈
- **통신 방식**: REST API (포트 기반 서비스 격리)
- **데이터 흐름**: Document Generator → RAG Engine → LLM Chat Service ← Workflow Orchestrator

### 1.2 서비스 다이어그램

```
┌─────────────────────────────────────────────────────────────┐
│                    Workflow Orchestrator                     │
│                    (포트: 8006)                              │
│        - 서비스 간 조율 및 워크플로우 관리                    │
│        - SSE 스트리밍 조율                                    │
└──────────────┬────────────┬─────────────┬───────────────────┘
               │            │             │
    ┌──────────▼──────┐  ┌──▼────────┐  ┌▼─────────────────┐
    │ Document        │  │ RAG       │  │ LLM Chat         │
    │ Generator       │  │ Engine    │  │ Service          │
    │ (포트: 8001)    │  │(포트:8003)│  │ (포트: 8004)     │
    │                 │  │           │  │                  │
    │ - AST 파싱      │  │ - 벡터 DB │  │ - OpenAI 연동    │
    │ - MD 생성       │  │ - 하이브  │  │ - 세션 관리      │
    │ - 청크 분할     │  │   리드    │  │ - 컨텍스트       │
    │                 │  │   검색    │  │   기반 응답      │
    └─────────────────┘  └───────────┘  └──────────────────┘
```

### 1.3 핵심 서비스별 책임

| 서비스 | 포트 | 핵심 책임 | 기술 스택 |
|--------|------|-----------|----------|
| Document Generator | 8001 | AST 파싱, MD 문서 생성 | Python, Jinja2, AST |
| RAG Engine | 8003 | 벡터 검색, 하이브리드 검색 | ChromaDB, BM25, OpenAI |
| LLM Chat Service | 8004 | LLM 연동, 세션 관리 | OpenAI API, Redis |
| Workflow Orchestrator | 8006 | 서비스 조율, 워크플로우 | AsyncIO, SSE |

---

## 2. 기술 스택 및 의존성

### 2.1 백엔드 프레임워크

- **언어**: Python 3.11+
- **웹 프레임워크**: FastAPI (비동기 처리 지원)
- **비동기 처리**: asyncio, aiohttp

### 2.2 데이터 저장소

```yaml
PostgreSQL:
  용도: 세션, 메타데이터
  포트: 5432
  버전: 15
  
Redis:
  용도: 캐싱, 세션 스토어
  포트: 6379
  버전: 7-alpine
  
ChromaDB:
  용도: 벡터 임베딩 저장
  모드: 로컬 퍼시스턴트
  위치: rag_storage/chroma_db
  임베딩 모델: text-embedding-3-small
  차원: 1536
```

### 2.3 외부 API

| API | 용도 | 모델 |
|-----|------|------|
| OpenAI API | LLM 응답 생성 | GPT-3.5-turbo, GPT-4 |
| OpenAI Embeddings | 벡터 임베딩 생성 | text-embedding-3-small |
| Anthropic API | LLM 응답 생성 (옵션) | Claude |

### 2.4 주요 라이브러리

```python
# 웹 프레임워크
fastapi==0.104.1
uvicorn[standard]==0.24.0
pydantic==2.5.0

# 데이터베이스
sqlalchemy==2.0.23
alembic==1.13.1
psycopg2-binary==2.9.9
redis==5.0.1

# LLM 및 AI
openai==1.3.7
anthropic==0.7.8
langchain==0.0.350
chromadb==0.4.18

# 벡터 검색
numpy==1.24.3
scikit-learn==1.3.2
rank-bm25  # 하이브리드 검색

# 템플릿 엔진
jinja2==3.1.2

# 비동기 처리
celery==5.3.4
asyncio==3.4.3
httpx==0.25.2
aiofiles==23.2.1

# 개발 도구
pytest==7.4.3
black==23.11.0
flake8==6.1.0
mypy==1.7.1
```

---

## 3. 서비스별 상세 스펙

### 3.1 Document Generator (포트: 8001)

#### 핵심 책임
- Python 소스코드 AST 파싱
- Semantic chunk 생성 (평균 462개/프로젝트)
- Markdown 문서 자동 생성
- RAG Engine으로 자동 업로드

#### 기술적 특징
- **템플릿 시스템**: Jinja2 기반 외부 템플릿
  - 위치: `src/chunker_document_generator/core/templates/`
  - 언어별 템플릿: Python, JavaScript, Vue, Java 등
- **복잡도 분석**: McCabe Complexity 자동 계산
- **메타데이터 추출**: 의존성, 태그, 라인 범위, 토큰 수
- **폴백 시스템**: 템플릿 실패 시 기본 방식으로 자동 전환

#### 주요 API 엔드포인트

```http
POST /api/v1/documents/generate
  - 소스코드 분석 및 문서 생성

GET /api/v1/documents/{document_id}
  - 생성된 문서 조회

GET /api/v1/documents
  - 전체 문서 목록

GET /health
  - 서비스 상태 확인
```

#### 생성되는 문서 구조

```
generated_docs/
├── project_overview.md          # 프로젝트 전체 요약
├── project_summary.md           # 고수준 요약
└── [프로젝트명]/
    ├── analyzers/
    │   ├── issue_detector.md    # 파일별 문서
    │   └── rule_analyzer.md
    ├── formatters/
    └── streaming/
```

---

### 3.2 RAG Engine (포트: 8003)

#### 핵심 책임
- MD 문서 섹션 벡터화
- 하이브리드 검색 (BM25 + 벡터 유사도)
- RRF(Reciprocal Rank Fusion) 기반 재랭킹
- 임베딩 캐싱 최적화

#### 검색 전략

```python
# 1. 질의 전처리
- 한국어 정규화 (NFC)
- 복합명사 분해
- 동의어 확장 (오류 ↔ 에러, 이슈 / 유형 ↔ 타입, 종류)

# 2. 하이브리드 검색
- BM25 키워드 검색 (한국어 토크나이징)
- 벡터 유사도 검색 (OpenAI 임베딩)
- RRF로 결과 통합

# 3. 재랭킹
- 폴더 우선순위 적용 (analyzers > formatters > streaming)
- 열거 패턴 보너스
- 키워드 매칭 보너스
- 의미쌍 보너스
```

#### 폴더 우선순위

| 폴더 | 우선순위 | 설명 |
|------|---------|------|
| analyzers | 100 | 핵심 분석기 (최우선) |
| shared | 80 | 공유 모듈 |
| streaming | 60 | 스트리밍 모듈 |
| formatters | 40 | 포매터 모듈 |
| config | 50 | 설정 |
| utils | 30 | 유틸리티 |
| templates | 20 | 템플릿 (낮은 우선순위) |

#### 주요 API 엔드포인트

```http
POST /api/v1/search
  - 하이브리드 검색
  - 파라미터: query, limit, chunk_type_filter, file_filter

POST /api/v1/documents
  - 단일 문서 업로드

POST /api/v1/documents/batch
  - 배치 문서 업로드 (최적화)

DELETE /api/v1/documents
  - 모든 문서 삭제

GET /api/v1/documents/folders
  - 폴더 구조 조회

GET /api/v1/cache/stats
  - 캐시 통계 조회

POST /api/v1/cache/clear
  - 캐시 초기화

GET /health
  - 서비스 상태 확인
```

#### 성능 최적화

```python
# 임베딩 캐싱
- 메모리 기반 해시 캐시
- 히트율 추적
- 배치 처리 (최대 100개/배치)
- 캐시 통계 실시간 모니터링

# BM25 인덱스
- 메모리 기반 인덱스
- 한국어 토크나이징 지원
- 자동 업데이트 (문서 추가/삭제 시)
```

---

### 3.3 LLM Chat Service (포트: 8004)

#### 핵심 책임
- OpenAI API 연동 및 호출
- 세션 기반 대화 관리
- 컨텍스트 기반 프롬프트 생성
- 재시도 로직 및 에러 핸들링

#### 프롬프트 전략

```markdown
시스템 메시지:
- 역할: "CodeMuse" – 레거시 코드베이스 분석 어시스턴트
- 목표: 컨텍스트 기반 실용적 답변 생성

답변 구조 (3단 구성):
1. 핵심 요지 (2~4줄 요약)
2. 근거 섹션 요약 (관련 함수/클래스/모듈)
3. 조치/확장 방법 (단계적 설명)

필수 규칙:
- 숫자 보존 필수 (7가지, 80번 줄, 157번 줄 등)
- 줄 번호 명시 필수
- 파일 경로 정확히 제시
- 문단 구분 명확히
- 근거 부족 시 명시
```

#### 에러 핸들링

```python
# Exponential Backoff Retry
max_retries = 3
base_delay = 1.0

retry_delays:
  - 1차 실패: 1초 대기
  - 2차 실패: 2초 대기
  - 3차 실패: 4초 대기

# 재시도 조건
- 429 오류 (Rate Limit)만 재시도
- 기타 오류는 즉시 실패 반환
```

#### API 호출 통계

```python
api_call_stats = {
    "total_calls": 0,           # 총 호출 수
    "successful_calls": 0,      # 성공 수
    "failed_calls": 0,          # 실패 수
    "calls_per_minute": 0,      # 분당 호출 수
    "last_call_time": None,     # 마지막 호출 시각
    "last_minute_calls": []     # 최근 1분 호출 목록
}
```

#### 주요 API 엔드포인트

```http
POST /api/v1/chat/sessions
  - 새 채팅 세션 생성

GET /api/v1/chat/sessions
  - 세션 목록 조회

GET /api/v1/chat/sessions/{session_id}/messages
  - 세션 메시지 조회

POST /api/v1/chat/sessions/{session_id}/messages
  - 메시지 전송 (동기)

POST /api/v1/chat/stream
  - 메시지 전송 (SSE 스트리밍)

GET /api/v1/stats
  - API 호출 통계 조회

GET /api/v1/debug
  - 디버그 정보 (API 키 상태 등)

GET /health
  - 서비스 상태 확인
```

---

### 3.4 Workflow Orchestrator (포트: 8006)

#### 핵심 책임
- 서비스 간 통신 조율
- 워크플로우 상태 관리
- SSE 기반 진행 상황 스트리밍
- 품질 검토 및 재검색

#### 워크플로우 5단계

```
1단계: 질문 표준화
  - 사용자 질문 정규화
  - 의도 파악

2단계: LLM 키워드 추출
  - GPT를 이용한 검색 키워드 생성
  - 폴백: Rule-based 키워드 추출

3단계: RAG 검색
  - Overview MD 섹션 검색
  - Function MD 섹션 검색
  - 최대 5개 chunk 선별

4단계: 품질 검토
  - 검색 결과 품질 평가
  - 필요 시 재검색

5단계: 최종 응답 생성
  - 컨텍스트 + 질문 → LLM
  - SSE로 토큰 스트리밍
```

#### SSE 이벤트 타입

```javascript
event: status      // 진행 상황 메시지
event: step        // 현재 단계 번호
event: standardized_question  // 표준화된 질문
event: started     // 응답 생성 시작
event: model       // 사용 모델명
event: token       // 응답 토큰 (실시간)
event: metrics     // 최종 메트릭스
event: done        // 완료 (ok/error)
```

#### 주요 API 엔드포인트

```http
POST /workflow/stream
  - SSE 스트리밍 워크플로우 실행

GET /services/status
  - 연결된 서비스 상태 확인

GET /health
  - 워크플로우 서비스 상태
```

#### 서비스 간 통신

```python
# 환경 변수 기반 서비스 URL
LLM_SERVICE_URL = os.getenv("LLM_SERVICE_URL", "http://localhost:8004")
RAG_SERVICE_URL = os.getenv("RAG_SERVICE_URL", "http://localhost:8003")

# Docker Compose 네트워크에서는
LLM_SERVICE_URL = "http://llm-chat:8004"
RAG_SERVICE_URL = "http://rag-engine:8003"
```

---

## 4. 인프라 및 배포

### 4.1 Docker Compose 구성

```yaml
# docker-compose.yml
services:
  postgres:
    image: postgres:15
    ports: ["5432:5432"]
    
  redis:
    image: redis:7-alpine
    ports: ["6379:6379"]
    
  document-generator:
    build: ./docker/services/DocumentGenerator.Dockerfile
    ports: ["8001:8001"]
    depends_on: [postgres, redis]
    
  rag-engine:
    build: ./docker/services/RAGEngine.Dockerfile
    ports: ["8003:8003"]
    depends_on: [postgres, redis]
    
  llm-chat:
    build: ./docker/services/LLMChat.Dockerfile
    ports: ["8004:8004"]
    depends_on: [postgres, redis]
    
  workflow-orchestrator:
    build: ./docker/services/WorkflowOrchestrator.Dockerfile
    ports: ["8006:8006"]
    depends_on: [document-generator, rag-engine, llm-chat]
    
  nginx:
    image: nginx:alpine
    ports: ["80:80", "443:443"]
    depends_on: [document-generator, rag-engine, llm-chat, workflow-orchestrator]
```

### 4.2 볼륨 관리

```yaml
volumes:
  postgres_data:
    # PostgreSQL 데이터 영구 저장
    
  redis_data:
    # Redis 데이터 영구 저장
    
  ./src:/app/src:
    # 개발 시 소스코드 실시간 반영
    
  ./sample_code:/app/sample_code:
    # 분석 대상 코드
    
  ./rag_storage:/app/rag_storage:
    # ChromaDB 퍼시스턴트 저장소
```

### 4.3 헬스체크 설정

```yaml
healthcheck:
  test: ["CMD", "curl", "-f", "http://localhost:8001/health"]
  interval: 30s      # 30초마다 체크
  timeout: 10s       # 10초 타임아웃
  retries: 3         # 3회 실패 시 unhealthy
```

### 4.4 네트워크 구성

```
외부 → Nginx (80/443) → 내부 서비스들
                         ├─ Document Generator:8001
                         ├─ RAG Engine:8003
                         ├─ LLM Chat:8004
                         └─ Workflow:8006
```

---

## 5. 보안 고려사항

### 5.1 API 키 관리

#### 환경 변수 필수

```bash
# .env 파일
OPENAI_API_KEY=sk-proj-...
ANTHROPIC_API_KEY=sk-ant-...
DATABASE_URL=postgresql://user:pass@localhost:5432/codemuse
SECRET_KEY=your-secret-key-here
```

#### ⚠️ 보안 이슈 발견

**위치**: `src/llm_chat_service/app.py`

```python
# 43번 줄, 399번 줄
correct_api_key = "sk-proj-rtIOr0IHt3Z4CUHy..."  # ❌ 하드코딩
```

**권고사항**:
- ✅ 즉시 환경 변수로 이관
- ✅ Git 히스토리에서 제거
- ✅ OpenAI API 키 재발급 권장

### 5.2 CORS 설정

#### 현재 (개발 환경)

```python
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # ⚠️ 모든 origin 허용
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)
```

#### 프로덕션 권장

```python
app.add_middleware(
    CORSMiddleware,
    allow_origins=[
        "https://codemuse.yourdomain.com",
        "https://app.yourdomain.com"
    ],
    allow_credentials=True,
    allow_methods=["GET", "POST", "PUT", "DELETE"],
    allow_headers=["Content-Type", "Authorization"],
)
```

### 5.3 Rate Limiting

#### 현재 상태
- OpenAI API 레벨에서만 관리
- 사용자별/IP별 제한 없음

#### 권고사항

```python
from slowapi import Limiter
from slowapi.util import get_remote_address

limiter = Limiter(key_func=get_remote_address)
app.state.limiter = limiter

@app.post("/api/v1/chat/sessions/{session_id}/messages")
@limiter.limit("10/minute")  # 분당 10회 제한
async def send_message(...):
    ...
```

### 5.4 데이터 암호화

#### 전송 중
- HTTPS/TLS 1.3 사용 (Nginx 설정 필요)
- API 키는 환경 변수로 관리

#### 저장 시
- PostgreSQL: 민감 정보 AES-256 암호화
- Redis: 세션 데이터 암호화 고려
- ChromaDB: 벡터는 평문 (민감 정보 아님)

---

## 6. 성능 및 확장성

### 6.1 현재 성능 특성

| 작업 | 소요 시간 | 처리량 |
|------|----------|--------|
| 문서 생성 (Python 프로젝트) | ~5분 | ~462개 chunk/프로젝트 |
| RAG 하이브리드 검색 | <1초 | ~10K 문서 |
| LLM 호출 (재시도 포함) | 2-5초 | 분당 ~10회 (OpenAI 제한) |
| 임베딩 생성 (배치) | ~3초/100개 | 캐시 히트율 70%+ |

### 6.2 확장성 설계

#### 모듈 단위 분리 (마이크로서비스 전환 준비)

```
현재: 모놀리스 (단일 레포, 독립 서비스)
     ↓
향후: 마이크로서비스
     ├─ document-generator-service
     ├─ rag-engine-service
     ├─ llm-chat-service
     └─ workflow-orchestrator-service
```

#### 수평 확장 가능 서비스

| 서비스 | 수평 확장 | 조건 |
|--------|----------|------|
| Document Generator | ✅ 가능 | 상태 비저장 |
| RAG Engine | ✅ 가능 | 읽기 전용 (벡터 DB 공유) |
| LLM Chat Service | ⚠️ 제한적 | 세션 Redis 이관 필요 |
| Workflow Orchestrator | ✅ 가능 | 상태 비저장 |

#### 병목 지점 및 해결 방안

```
🔴 병목: ChromaDB 단일 인스턴스
   해결: Pinecone/Weaviate Cloud 이관

🔴 병목: OpenAI API 호출량 제한
   해결: 캐싱, 배치 처리, 멀티 키 로테이션

🟡 병목: 세션 메모리 저장
   해결: Redis 기반 세션 스토어

🟢 병목: PostgreSQL (현재 없음)
   향후: 읽기 복제본, 샤딩
```

### 6.3 캐싱 전략

```
L1 (메모리): 임베딩 캐시
  - 위치: RAG Engine 내부
  - 크기: 무제한 (메모리 의존)
  - 히트율: ~70%

L2 (Redis): 검색 결과 캐시
  - 위치: Redis
  - TTL: 1시간
  - 히트율: 미구현 (향후)

L3 (CDN): 정적 문서 캐시
  - 위치: CDN (프론트엔드)
  - 대상: generated_docs/*.md
  - 상태: 미구현
```

### 6.4 데이터베이스 최적화

#### PostgreSQL

```sql
-- 인덱스 전략 (향후 구현)
CREATE INDEX idx_sessions_user_id ON chat_sessions(user_id);
CREATE INDEX idx_messages_session_id ON chat_messages(session_id);
CREATE INDEX idx_documents_created_at ON documents(created_at);
```

#### ChromaDB

```python
# 현재: 로컬 퍼시스턴트 (rag_storage/chroma_db)
# 향후: 클라우드 벡터 DB
#   - Pinecone: 고성능, 관리형
#   - Weaviate: 오픈소스, 자체 호스팅
#   - Qdrant: 고속, 필터링 강력
```

---

## 7. 모니터링 및 운영

### 7.1 로깅 시스템

#### 로그 파일 위치

```
logs/
├── docgen.log              # Document Generator
├── rag.log                 # RAG Engine
├── llm.log                 # LLM Chat Service
├── workflow.log            # Workflow Orchestrator
└── workflow_detailed.log   # Workflow 상세 로그
```

#### 로그 포맷

```
%(asctime)s - %(name)s - %(levelname)s - %(message)s

예시:
2025-10-13 10:30:45 - workflow - INFO - 🔍 [a1b2c3] OpenAI API 호출 시작
```

#### 로그 레벨

```python
DEBUG   # 상세 디버깅 정보
INFO    # 일반 정보 (기본)
WARNING # 경고
ERROR   # 오류
CRITICAL # 치명적 오류
```

### 7.2 메트릭스

#### LLM Chat Service

```python
GET /api/v1/stats

응답:
{
  "api_call_stats": {
    "total_calls": 150,
    "successful_calls": 145,
    "failed_calls": 5,
    "calls_per_minute": 3,
    "last_call_time": 1697184645.123
  }
}
```

#### RAG Engine

```python
GET /api/v1/cache/stats

응답:
{
  "cache_stats": {
    "cache_size": 1024,
    "cache_hits": 750,
    "cache_misses": 250,
    "hit_rate_percent": 75.0
  }
}
```

#### 헬스체크

```python
GET /health

응답:
{
  "status": "healthy",
  "service": "RAG Engine",
  "documents_count": 462,
  "embedding_cache": {
    "cache_size": 1024,
    "hit_rate_percent": 75.0
  }
}
```

### 7.3 운영 API

| 엔드포인트 | 메서드 | 용도 |
|-----------|--------|------|
| `/health` | GET | 서비스 상태 확인 |
| `/api/v1/stats` | GET | API 호출 통계 (LLM) |
| `/api/v1/cache/stats` | GET | 캐시 통계 (RAG) |
| `/api/v1/cache/clear` | POST | 캐시 초기화 (RAG) |
| `/api/v1/documents` | DELETE | 전체 문서 삭제 (RAG) |
| `/api/v1/debug` | GET | 디버그 정보 (LLM) |

### 7.4 알림 시스템 (향후 구현)

```python
# Slack 알림 예시
- OpenAI API 429 오류 발생 시
- 서비스 다운 감지 시
- 캐시 히트율 저하 시
- 디스크 사용량 80% 초과 시
```

---

## 8. 아키텍처 결정 사항(ADR)

### ADR-001: 마이크로서비스 기반 설계

**상태**: ✅ 승인됨  
**날짜**: 2024-09  
**결정자**: 아키텍처팀

#### 컨텍스트
- 초기 모놀리스 구조에서 시작
- 향후 확장성 및 독립 배포 필요
- 팀 간 개발 병렬화

#### 결정
- 모듈 단위로 분리된 서비스 구조 채택
- 각 서비스는 독립 포트로 실행
- REST API 기반 통신

#### 근거
- **장점**:
  - 향후 마이크로서비스 전환 용이
  - 서비스별 독립 배포 가능
  - 장애 격리
  - 기술 스택 독립성
- **단점**:
  - 서비스 간 통신 오버헤드
  - 배포 복잡도 증가
  - 분산 트랜잭션 처리 어려움

#### 영향
- 개발 복잡도 증가
- 운영 복잡도 증가
- 확장성 확보

---

### ADR-002: ChromaDB 로컬 퍼시스턴트 모드

**상태**: ✅ 승인됨  
**날짜**: 2024-09

#### 컨텍스트
- 벡터 DB 선택 필요
- 초기에는 소규모 데이터 (~10K 문서)
- 개발 환경 단순화 필요

#### 결정
- ChromaDB 임베디드 모드 사용
- 별도 컨테이너 불필요
- 로컬 디렉토리에 퍼시스턴트 저장

#### 근거
- **장점**:
  - 운영 복잡도 감소
  - 개발 환경 단순화
  - 충분한 성능 (10K 문서 이하)
  - 무료
- **단점**:
  - 수평 확장 불가
  - 대규모 데이터 처리 제한
  - 고급 기능 부족

#### 영향
- 초기 개발 속도 향상
- 향후 Pinecone/Weaviate 이관 계획

---

### ADR-003: RAG 하이브리드 검색

**상태**: ✅ 승인됨  
**날짜**: 2024-10

#### 컨텍스트
- 순수 벡터 검색만으로는 키워드 매칭 약함
- 한국어 검색 품질 향상 필요
- 특정 파일/함수 찾기 어려움

#### 결정
- BM25 + 벡터 유사도 + RRF 결합
- 폴더 우선순위 추가
- 재랭킹 시스템 도입

#### 근거
- **장점**:
  - 키워드 검색과 의미 검색 장점 결합
  - 한국어 검색 품질 대폭 향상
  - 정확도 개선 (측정 필요)
- **단점**:
  - 복잡도 증가
  - 메모리 사용량 증가 (BM25 인덱스)

#### 영향
- 검색 품질 향상
- 메모리 사용량 증가 (~10MB)

---

### ADR-004: Jinja2 템플릿 기반 문서 생성

**상태**: ✅ 승인됨  
**날짜**: 2024-10

#### 컨텍스트
- 기존 코드 기반 문서 생성 유지보수 어려움
- 다양한 언어/프로젝트 지원 필요
- 템플릿 수정 시 코드 변경 불필요

#### 결정
- Jinja2 기반 외부 템플릿 파일 사용
- 언어별 템플릿 분리
- 폴백 시스템 구현

#### 근거
- **장점**:
  - 템플릿 수정 시 코드 변경 불필요
  - 다양한 언어/프로젝트 지원 확장 용이
  - 디자이너/기획자도 템플릿 수정 가능
  - 폴백 시스템으로 안정성 확보
- **단점**:
  - 템플릿 학습 곡선
  - 디버깅 어려움

#### 영향
- 유지보수성 향상
- 확장성 확보

---

## 9. 마이그레이션/배포 전략

### 9.1 환경별 설정

```
config/
├── development/    # 로컬 개발 환경
│   ├── .env
│   └── docker-compose.dev.yml
│
├── staging/        # 스테이징 환경
│   ├── .env
│   └── docker-compose.staging.yml
│
└── production/     # 프로덕션 환경
    ├── .env
    └── docker-compose.prod.yml
```

### 9.2 배포 단계

```bash
# 1. 환경 변수 설정
cp env.example .env
# .env 파일 수정

# 2. Docker Compose 빌드
docker-compose build

# 3. 데이터베이스 마이그레이션
docker-compose run --rm document-generator alembic upgrade head

# 4. 서비스 시작
docker-compose up -d

# 5. 헬스체크 확인
./scripts/setup/test_services.py

# 6. Nginx 설정 확인
curl http://localhost/health
```

### 9.3 롤백 전략

```bash
# 서비스별 롤백
docker-compose up -d llm-chat:1.0.0  # 이전 버전으로

# 데이터베이스 롤백
docker-compose run --rm document-generator alembic downgrade -1

# 전체 롤백
docker-compose down
docker-compose -f docker-compose.backup.yml up -d
```

### 9.4 블루-그린 배포 (향후)

```
블루(현재 버전)          그린(새 버전)
     ↓                      ↓
   서비스들              서비스들
     ↓                      ↓
   로드밸런서 ← 트래픽 전환
```

### 9.5 CI/CD 파이프라인 (향후 구현)

```yaml
# .github/workflows/deploy.yml

name: Deploy
on:
  push:
    branches: [main]

jobs:
  test:
    - pytest
    - black --check
    - flake8
    - mypy
    
  build:
    - docker build
    - docker push
    
  deploy:
    - ssh production
    - docker-compose pull
    - docker-compose up -d
    - health check
```

---

## 10. 개선 권고사항 (우선순위별)

### 🔴 High Priority (즉시 조치 필요)

#### 1. API 키 하드코딩 제거
**위치**: `src/llm_chat_service/app.py:43, 399`

```python
# ❌ 현재
correct_api_key = "sk-proj-rtIOr0IHt3Z4CUHy..."

# ✅ 개선
import os
openai.api_key = os.getenv("OPENAI_API_KEY")
```

**작업량**: 1시간  
**리스크**: 높음 (보안)

---

#### 2. CORS 설정 강화
**위치**: 모든 서비스 `app.py`

```python
# ❌ 현재
allow_origins=["*"]

# ✅ 개선
allow_origins=os.getenv("ALLOWED_ORIGINS", "").split(",")
```

**작업량**: 2시간  
**리스크**: 중간

---

#### 3. Rate Limiting 추가
**대상**: 모든 외부 API 엔드포인트

```python
from slowapi import Limiter

limiter = Limiter(key_func=get_remote_address)

@app.post("/api/v1/chat/sessions/{session_id}/messages")
@limiter.limit("10/minute")
async def send_message(...):
    ...
```

**작업량**: 4시간  
**리스크**: 중간

---

#### 4. 데이터베이스 마이그레이션 스크립트
**현재 상태**: Alembic 설정만 있음, 실제 마이그레이션 없음

```bash
# 필요 작업
alembic init migrations
alembic revision --autogenerate -m "initial"
alembic upgrade head
```

**작업량**: 8시간  
**리스크**: 높음

---

### 🟡 Medium Priority (1개월 내)

#### 1. 모니터링 시스템 구축
**도구**: Prometheus + Grafana

```yaml
# docker-compose에 추가
prometheus:
  image: prom/prometheus
  
grafana:
  image: grafana/grafana
```

**작업량**: 3일  
**리스크**: 낮음

---

#### 2. CI/CD 파이프라인
**도구**: GitHub Actions

```yaml
# .github/workflows/ci.yml
- 테스트 자동화
- 린트 검사
- Docker 빌드
- 자동 배포
```

**작업량**: 5일  
**리스크**: 중간

---

#### 3. E2E 테스트 작성
**현재 상태**: 테스트 디렉토리 비어있음

```
tests/
├── unit/
│   ├── test_document_generator.py
│   ├── test_rag_engine.py
│   └── test_llm_chat.py
├── integration/
│   └── test_workflow.py
└── e2e/
    └── test_full_workflow.py
```

**작업량**: 2주  
**리스크**: 낮음

---

#### 4. 세션 스토어 Redis 이관
**현재**: 메모리 기반 (재시작 시 손실)

```python
# 현재
sessions = {}  # 메모리

# 개선
import redis
redis_client = redis.Redis(...)
```

**작업량**: 1주  
**리스크**: 중간

---

### 🟢 Low Priority (3개월 내)

#### 1. 클라우드 벡터 DB 이관
**현재**: ChromaDB 로컬  
**목표**: Pinecone/Weaviate

**작업량**: 2주  
**리스크**: 높음

---

#### 2. 프론트엔드 통합
**현재**: 백엔드만 구현  
**목표**: Vue 3 기반 웹 UI

**작업량**: 1개월  
**리스크**: 중간

---

#### 3. 다국어 지원
**현재**: 한국어 중심  
**목표**: 영어, 일본어 등

**작업량**: 2주  
**리스크**: 낮음

---

#### 4. 플러그인 시스템
**목표**: 커스텀 분석기 추가 가능

**작업량**: 1개월  
**리스크**: 중간

---

## 11. 기술 부채 및 제약사항

### 11.1 현재 알려진 제약

#### 세션 저장소
```
문제: 메모리 기반 세션 저장
영향: 서비스 재시작 시 모든 세션 손실
해결: Redis 기반 세션 스토어 이관
우선순위: 🟡 Medium
```

#### 벡터 DB
```
문제: ChromaDB 단일 인스턴스
영향: 수평 확장 불가, 10K 문서 제한
해결: Pinecone/Weaviate 이관
우선순위: 🟢 Low (현재 문제 없음)
```

#### LLM 의존성
```
문제: OpenAI API 다운 시 전체 서비스 영향
영향: 서비스 불가
해결: 멀티 LLM 제공업체 지원 (Anthropic)
우선순위: 🟡 Medium
```

#### 테스트 커버리지
```
문제: 단위/통합 테스트 미구현
영향: 리팩토링 시 회귀 버그 위험
해결: pytest 기반 테스트 작성
우선순위: 🟡 Medium
```

---

### 11.2 기술 부채 목록

| 부채 | 위치 | 영향도 | 우선순위 |
|------|------|--------|---------|
| API 키 하드코딩 | llm_chat_service/app.py | 🔴 High | 🔴 즉시 |
| CORS 설정 미완 | 모든 서비스 | 🟡 Medium | 🔴 즉시 |
| 에러 처리 표준화 부족 | 모든 서비스 | 🟡 Medium | 🟡 1개월 |
| 로깅 포맷 일관성 부족 | 모든 서비스 | 🟢 Low | 🟢 3개월 |
| 메모리 세션 저장 | llm_chat_service | 🟡 Medium | 🟡 1개월 |
| 테스트 미구현 | tests/ | 🟡 Medium | 🟡 1개월 |
| 문서화 부족 | docs/ | 🟢 Low | 🟢 3개월 |
| 성능 프로파일링 미실시 | 전체 | 🟢 Low | 🟢 3개월 |

---

### 11.3 제약사항 및 전제조건

#### OpenAI API 제한
```
Rate Limit: 분당 60회 (Tier 1)
RPD (Requests Per Day): 200회
TPM (Tokens Per Minute): 90K

대응:
- 캐싱으로 호출 수 감소
- 배치 처리
- 재시도 로직
- 향후: 멀티 키 로테이션
```

#### 임베딩 차원
```
모델: text-embedding-3-small
차원: 1536
크기: ~6KB per embedding

영향:
- ChromaDB 저장 용량
- 검색 속도
- 메모리 사용량

10K 문서 기준:
- 임베딩 크기: ~60MB
- 메모리: ~100MB
- 디스크: ~200MB
```

#### PostgreSQL 연결 수
```
기본: 100 동시 연결
권장: 20-30 연결 (서비스당)

모니터링 필요:
SELECT count(*) FROM pg_stat_activity;
```

---

## 12. 참고 문서

### 12.1 내부 문서

| 문서 | 위치 | 설명 |
|------|------|------|
| 설계 문서 | `CodeMuse_Design_Document.md` | 전체 시스템 설계 |
| 저장소 구조 | `REPOSITORY_STRUCTURE.md` | 레포 구조 설명 |
| README | `README.md` | 빠른 시작 가이드 |
| API 엔드포인트 | `FRONTEND_API_ENDPOINTS.md` | 프론트엔드 연동 가이드 |
| 성능 최적화 | `RAG_PERFORMANCE_OPTIMIZATION.md` | RAG 최적화 가이드 |

### 12.2 외부 문서

| 문서 | URL | 설명 |
|------|-----|------|
| FastAPI | https://fastapi.tiangolo.com | 웹 프레임워크 |
| ChromaDB | https://docs.trychroma.com | 벡터 DB |
| OpenAI API | https://platform.openai.com/docs | LLM API |
| Docker Compose | https://docs.docker.com/compose | 컨테이너 오케스트레이션 |
| Jinja2 | https://jinja.palletsprojects.com | 템플릿 엔진 |

### 12.3 API 문서 (Swagger UI)

서비스 실행 후 아래 URL에서 확인 가능:

```
Document Generator:
http://localhost:8001/docs

RAG Engine:
http://localhost:8003/docs

LLM Chat Service:
http://localhost:8004/docs

Workflow Orchestrator:
http://localhost:8006/docs
```

---

## 13. 연락처 및 지원

### 13.1 담당자

| 역할 | 담당자 | 이메일 |
|------|-------|--------|
| 프로젝트 리드 | - | - |
| 백엔드 개발 | - | - |
| 인프라/DevOps | - | - |
| 프론트엔드 개발 | - | - |

### 13.2 이슈 리포팅

```
버그 리포트: GitHub Issues
기능 제안: GitHub Discussions
보안 이슈: security@yourdomain.com
```

---

## 부록 A: 용어 정의

| 용어 | 정의 |
|------|------|
| AST | Abstract Syntax Tree, 추상 구문 트리 |
| RAG | Retrieval-Augmented Generation, 검색 증강 생성 |
| BM25 | Best Matching 25, 키워드 검색 알고리즘 |
| RRF | Reciprocal Rank Fusion, 순위 융합 알고리즘 |
| SSE | Server-Sent Events, 서버 푸시 이벤트 |
| LLM | Large Language Model, 대규모 언어 모델 |
| Chunk | 의미적으로 분할된 코드 단위 |
| Embedding | 텍스트를 벡터로 변환한 것 |
| Hybrid Search | 키워드 + 벡터 검색 결합 |

---

## 부록 B: 포트 매핑

| 서비스 | 포트 | 프로토콜 | 용도 |
|--------|------|---------|------|
| Document Generator | 8001 | HTTP | 문서 생성 API |
| RAG Engine | 8003 | HTTP | 검색 API |
| LLM Chat Service | 8004 | HTTP/SSE | 채팅 API |
| Workflow Orchestrator | 8006 | HTTP/SSE | 워크플로우 API |
| PostgreSQL | 5432 | TCP | 데이터베이스 |
| Redis | 6379 | TCP | 캐시 |
| Nginx | 80/443 | HTTP/HTTPS | 리버스 프록시 |

---

## 부록 C: 환경 변수 전체 목록

```bash
# 데이터베이스
DATABASE_URL=postgresql://user:pass@host:5432/db
REDIS_URL=redis://host:6379

# LLM API
OPENAI_API_KEY=sk-proj-...
ANTHROPIC_API_KEY=sk-ant-...

# 서비스 URL (Docker Compose 내부)
LLM_SERVICE_URL=http://llm-chat:8004
RAG_SERVICE_URL=http://rag-engine:8003

# 보안
SECRET_KEY=your-secret-key
ALLOWED_ORIGINS=https://yourdomain.com

# RAG 설정
RAG_RESET_ON_START=false
EMBEDDING_CACHE_ENABLED=true
CHUNK_CONTEXT_CHAR_LIMIT=800
LLM_CONTEXT_MAX_CHARS=3000

# 로깅
LOG_LEVEL=INFO
```

---

**문서 버전**: 1.0  
**최종 업데이트**: 2025-10-13  
**작성자**: CodeMuse 개발팀  
**검토자**: 아키텍처팀

