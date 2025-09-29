# CodeMuse 저장소 구조

## 저장소 분리 전략

CodeMuse는 **백엔드**와 **프론트엔드**를 별도 저장소로 분리하여 관리합니다.

## 저장소 구성

### 1. codemuse-backend (현재 저장소)
- **위치**: `/Users/roseline/projects/codemuse-backend`
- **역할**: Python + FastAPI 기반 백엔드 서비스
- **구성**:
  - 5개 마이크로서비스 (Document Generator, RAG Engine, Web Interface, LLM Chat, Workflow Orchestrator)
  - 공통 모듈 (shared)
  - Docker 설정
  - 테스트 코드

### 2. codemuse-frontend (별도 저장소)
- **위치**: `/Users/roseline/projects/codemuse-frontend`
- **역할**: Vue 3+ 기반 프론트엔드 웹 인터페이스
- **구성**:
  - Vue 3 + TypeScript
  - Pinia 상태 관리
  - Monaco Editor
  - Socket.IO 실시간 통신

## 개발 워크플로우

### 백엔드 개발
```bash
cd /Users/roseline/projects/codemuse-backend
pip install -r requirements.txt
docker-compose up -d
```

### 프론트엔드 개발
```bash
cd /Users/roseline/projects/codemuse-frontend
npm install
npm run dev
```

### 통합 테스트
```bash
# 백엔드 서비스 실행
cd codemuse-backend
docker-compose up -d

# 프론트엔드 개발 서버 실행
cd ../codemuse-frontend
npm run dev
```

## API 통신

프론트엔드는 다음 백엔드 서비스들과 통신합니다:

- **Web Interface Service** (포트: 8005): 메인 API
- **Workflow Orchestrator** (포트: 8006): 워크플로우 관리 및 WebSocket
- **LLM Chat Service** (포트: 8004): LLM 대화 (백엔드를 통해)

## 배포 전략

### 개발 환경
- 백엔드: Docker Compose로 로컬 실행
- 프론트엔드: Vite 개발 서버 (프록시 설정)

### 프로덕션 환경
- 백엔드: Kubernetes 또는 Docker Swarm
- 프론트엔드: 정적 파일로 빌드 후 Nginx 서빙

## 모노레포 고려사항

향후 필요시 다음 도구들을 고려할 수 있습니다:

- **Lerna**: 패키지 관리
- **Nx**: 모노레포 빌드 시스템
- **Rush**: 엔터프라이즈 모노레포 도구

현재는 저장소 분리가 더 적합한 구조입니다.
