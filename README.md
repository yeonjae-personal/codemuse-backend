# CodeMuse Backend

AI 기반 개발 어시스턴트 플랫폼 - 백엔드 서비스

## 프로젝트 개요

CodeMuse는 소스코드 분석부터 바이브코딩까지의 전체 개발 워크플로우를 지원하는 AI 어시스턴트 플랫폼입니다.

### 핵심 기능

- **소스코드 분석**: AST 파싱을 통한 코드 구조 분석
- **자동 문서화**: MD 문서 자동 생성
- **RAG 검색**: 의미적 검색을 통한 코드 컨텍스트 제공
- **LLM 대화**: 컨텍스트 기반 지능적 대화
- **바이브코딩**: 실시간 코드 생성 및 수정 지원

## 아키텍처

### 모듈 구조

```
src/
├── document_generator/     # 문서 생성 서비스
├── rag_engine/            # RAG 검색 엔진
├── web_interface/         # 웹 인터페이스
├── llm_chat/              # LLM 채팅 서비스
├── workflow_orchestrator/  # 워크플로우 조율기
└── shared/                # 공통 모듈
    ├── models/            # 데이터 모델
    ├── utils/             # 유틸리티
    └── config/            # 설정
```

### 서비스 구성

- **Document Generator Service** (포트: 8002)
- **RAG Engine Service** (포트: 8003)
- **LLM Chat Service** (포트: 8004)
- **Web Interface Service** (포트: 8005)
- **Workflow Orchestrator Service** (포트: 8006)

## 저장소 구조

이 저장소는 CodeMuse의 **백엔드 서비스**를 포함합니다.

- **프론트엔드**: [codemuse-frontend](https://github.com/codemuse/codemuse-frontend) (Vue 3+)
- **백엔드**: 현재 저장소 (Python + FastAPI)

## 설치 및 실행

### 사전 요구사항

- Python 3.11+
- Docker & Docker Compose
- PostgreSQL
- Redis
- ChromaDB

### 환경 설정

1. 환경 변수 파일 복사:
```bash
cp env.example .env
```

2. 환경 변수 설정:
```bash
# .env 파일에서 필요한 값들 설정
DATABASE_URL=postgresql://username:password@localhost:5432/codemuse
SECRET_KEY=your-secret-key-here
OPENAI_API_KEY=your-openai-api-key
ANTHROPIC_API_KEY=your-anthropic-api-key
```

### Docker Compose로 실행

```bash
# 모든 서비스 시작
docker-compose up -d

# 로그 확인
docker-compose logs -f

# 서비스 중지
docker-compose down
```

### 로컬 개발 환경

```bash
# 의존성 설치
pip install -r requirements.txt

# 데이터베이스 마이그레이션
alembic upgrade head

# 개발 서버 실행
uvicorn src.main:app --reload --host 0.0.0.0 --port 8000
```

## API 문서

서비스 실행 후 다음 URL에서 API 문서를 확인할 수 있습니다:

- **Document Generator**: http://localhost:8002/docs
- **RAG Engine**: http://localhost:8003/docs
- **LLM Chat**: http://localhost:8004/docs
- **Web Interface**: http://localhost:8005/docs
- **Workflow Orchestrator**: http://localhost:8006/docs

## 개발 가이드

### 모듈별 개발

각 모듈은 독립적으로 개발할 수 있으며, 공통 인터페이스를 통해 통신합니다.

### 코드 스타일

- **Python**: Black, Flake8, MyPy 사용
- **타입 힌트**: 모든 함수에 타입 힌트 필수
- **문서화**: Docstring 필수

### 테스트

```bash
# 단위 테스트
pytest tests/unit/

# 통합 테스트
pytest tests/integration/

# E2E 테스트
pytest tests/e2e/
```

## 배포

### 프로덕션 환경

```bash
# 프로덕션 빌드
docker-compose -f docker-compose.prod.yml up -d

# 모니터링 확인
docker-compose logs -f
```

### 모니터링

- **Prometheus**: 메트릭 수집
- **Grafana**: 대시보드
- **ELK Stack**: 로그 분석

## 라이선스

MIT License

## 기여하기

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## MVP 구현 상태 ✅

### Phase 1: MVP 완료
- ✅ **Document Generator**: AST 파싱 및 MD 문서 자동 생성
- ✅ **RAG Engine**: 벡터화 및 의미적 검색
- ✅ **LLM Chat Service**: OpenAI/Anthropic API 연동
- ✅ **Web Interface**: FastAPI 기반 채팅 API
- ✅ **Workflow Orchestrator**: 서비스 간 통신 조율
- ✅ **Docker 설정**: 모든 서비스 컨테이너화 완료

### 빠른 시작
```bash
# 서비스 시작
./scripts/setup/start_services.sh

# 서비스 테스트
python scripts/setup/test_services.py

# 서비스 중지
./scripts/setup/stop_services.sh
```

## 문의

프로젝트 관련 문의사항은 이슈를 통해 제출해 주세요.
