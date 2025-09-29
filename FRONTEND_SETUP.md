# 프론트엔드 설정 가이드 (Chunk 기반 아키텍처)

## Vue 3+ 프론트엔드 프로젝트

CodeMuse의 프론트엔드는 **Chunk 기반 RAG + 2단계 LLM 파이프라인**과 연동하는 Vue 3+ 기반 SPA입니다. 
별도 프로젝트로 관리되며, 백엔드 마이크로서비스들과 RESTful API로 통신합니다.

### 프로젝트 생성

```bash
# Vue 3 프로젝트 생성
npm create vue@latest codemuse-frontend

# 또는 Vite로 직접 생성
npm create vite@latest codemuse-frontend -- --template vue-ts
```

### 권장 구조 (Chunk 기반)

```
codemuse-frontend/
├── src/
│   ├── components/              # 재사용 가능한 컴포넌트
│   │   ├── Chat/               # 채팅 관련 컴포넌트
│   │   │   ├── ChatMessage.vue # 채팅 메시지
│   │   │   ├── ChatInput.vue   # 채팅 입력
│   │   │   └── SearchStrategy.vue # 검색 전략 표시
│   │   ├── CodeEditor/         # 코드 에디터
│   │   │   ├── MonacoEditor.vue
│   │   │   └── CodePreview.vue
│   │   ├── DocumentViewer/     # 문서 뷰어 (Chunk 기반)
│   │   │   ├── ChunkList.vue   # Chunk 목록
│   │   │   ├── ChunkDetail.vue # Chunk 상세 정보
│   │   │   ├── ChunkMetadata.vue # 메타데이터 표시
│   │   │   └── MermaidChart.vue # Mermaid 차트 렌더링
│   │   ├── Workflow/           # 워크플로우 상태 (새로 추가)
│   │   │   ├── WorkflowStatus.vue # 파이프라인 단계 표시
│   │   │   ├── LLMSummary.vue    # LLM 요약 표시
│   │   │   └── SearchResults.vue # RAG 검색 결과
│   │   └── common/             # 공통 컴포넌트
│   │       ├── LoadingSpinner.vue
│   │       ├── ErrorMessage.vue
│   │       └── Badge.vue
│   ├── views/                  # 페이지 컴포넌트
│   │   ├── Dashboard.vue       # 대시보드 (Chunk 통계 포함)
│   │   ├── Chat.vue            # 채팅 페이지 (2단계 파이프라인)
│   │   ├── Documents.vue       # 문서 관리 (Chunk 기반)
│   │   └── ChunkExplorer.vue   # Chunk 탐색기 (새로 추가)
│   ├── stores/                 # Pinia 상태 관리
│   │   ├── chat.ts             # 채팅 상태 (파이프라인 단계 포함)
│   │   ├── chunks.ts           # Chunk 상태 (새로 추가)
│   │   ├── workflow.ts         # 워크플로우 상태 (새로 추가)
│   │   ├── documents.ts        # 문서 상태
│   │   └── user.ts             # 사용자 상태
│   ├── services/               # API 서비스
│   │   ├── api.ts              # API 클라이언트
│   │   ├── chat.ts             # 채팅 API
│   │   ├── workflow.ts         # 워크플로우 API (새로 추가)
│   │   ├── chunks.ts           # Chunk API (새로 추가)
│   │   └── documents.ts        # 문서 API
│   ├── types/                  # TypeScript 타입 정의 (새로 추가)
│   │   ├── chunk.ts            # Chunk 관련 타입
│   │   ├── workflow.ts         # 워크플로우 타입
│   │   └── api.ts              # API 응답 타입
│   ├── utils/                  # 유틸리티
│   │   ├── chunkUtils.ts       # Chunk 처리 유틸리티
│   │   └── formatters.ts       # 데이터 포맷팅
│   └── assets/                 # 정적 자원
├── public/                     # 공개 자원
└── package.json
```

### 기술 스택 (Chunk 기반 확장)

#### 핵심 프레임워크
- **Vue 3+**: Composition API + `<script setup>`
- **TypeScript**: 타입 안전성 + Chunk/Workflow 타입 정의
- **Pinia**: 상태 관리 (Chunk, Workflow 스토어 포함)
- **Vite**: 빌드 도구 + HMR

#### UI/UX 라이브러리
- **Material-UI**: UI 컴포넌트 라이브러리
- **Monaco Editor**: 코드 에디터 (Chunk 코드 표시용)
- **Mermaid**: 차트 렌더링 (플로우차트, 시퀀스 다이어그램)
- **Highlight.js**: 코드 신택스 하이라이팅

#### 데이터 처리
- **Axios**: HTTP 클라이언트 (워크플로우 API 호출)
- **Socket.io**: 실시간 채팅 (WebSocket)
- **Lodash**: 유틸리티 함수 (Chunk 데이터 처리)

#### 개발 도구
- **ESLint**: 코드 린팅
- **Prettier**: 코드 포맷팅
- **Vitest**: 단위 테스트

### 백엔드 연동 (Chunk 기반 아키텍처)

프론트엔드는 다음 백엔드 마이크로서비스들과 RESTful API로 통신합니다:

#### 주요 서비스 연동
- **Workflow Orchestrator** (포트: 8006): 
  - **POST** `/workflow` - Chunk 기반 2단계 LLM 파이프라인
  - **GET** `/services/status` - 서비스 헬스체크
- **RAG Engine Service** (포트: 8003):
  - **POST** `/api/v1/search` - Chunk 하이브리드 검색
  - **POST** `/api/v1/chunks/upload` - Chunk 업로드
  - **GET** `/api/v1/chunks/metadata` - Chunk 메타데이터 조회
- **LLM Chat Service** (포트: 8004):
  - **POST** `/api/v1/chat/sessions` - 채팅 세션 관리
  - **POST** `/api/v1/chat/sessions/{id}/messages` - 메시지 전송
- **Document Generator** (포트: 8001):
  - **POST** `/api/v1/documents/generate` - Chunk 기반 문서 생성
  - **GET** `/api/v1/documents` - 생성된 문서 조회

#### 실시간 통신
- **WebSocket**: 실시간 채팅 스트리밍
- **Server-Sent Events**: 워크플로우 진행 상태 실시간 업데이트

#### API 응답 포맷 (업데이트됨)
워크플로우 API는 다음과 같은 구조화된 응답을 제공합니다:
```typescript
interface WorkflowResponse {
  query: string;
  use_rag: boolean;
  search_strategy: {
    overview_results: number;
    function_chunks: number;
    filtered_files: string[];
    search_keywords: string[];
    total_chunks_found: number;
  };
  rag_results: ChunkResult[];
  llm_summary?: string;  // 다수 chunk인 경우 요약
  llm_response: string;
  workflow_time: number;
  services_used: string[];
}
```

### 개발 환경 설정 (Chunk 기반)

#### 필수 패키지 설치
```bash
# 프로젝트 생성 후 추가 패키지 설치
npm install axios pinia @pinia/nuxt
npm install mermaid monaco-editor highlight.js
npm install lodash @types/lodash
npm install socket.io-client
npm install @vueuse/core  # Vue Composition API 유틸리티

# 개발 도구
npm install -D @types/node
npm install -D vitest @vitejs/plugin-vue
npm install -D eslint prettier
```

#### 환경 변수 설정
`.env.development` 파일 생성:
```env
# 백엔드 서비스 URL
VITE_WORKFLOW_API_URL=http://localhost:8006
VITE_RAG_API_URL=http://localhost:8003
VITE_LLM_API_URL=http://localhost:8004
VITE_DOC_GENERATOR_API_URL=http://localhost:8001

# WebSocket URL
VITE_WEBSOCKET_URL=ws://localhost:8005

# 개발 모드 설정
VITE_API_TIMEOUT=30000
VITE_CHUNK_PAGE_SIZE=20
```

#### 스크립트 실행
```bash
# 의존성 설치
npm install

# 개발 서버 실행 (HMR 포함)
npm run dev

# 타입 체크
npm run type-check

# 린팅
npm run lint

# 테스트
npm run test

# 빌드
npm run build

# 빌드 미리보기
npm run preview
```

### 배포 (Chunk 기반 고려사항)

#### 정적 파일 배포
프론트엔드는 정적 파일로 빌드되어 Nginx를 통해 서빙됩니다.

```nginx
# nginx.conf 예시
server {
    listen 80;
    server_name codemuse.example.com;
    
    root /var/www/codemuse-frontend/dist;
    index index.html;
    
    # SPA 라우팅 지원
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    # API 프록시 (백엔드 서비스로 전달)
    location /api/ {
        proxy_pass http://backend-services;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
    
    # WebSocket 지원
    location /socket.io/ {
        proxy_pass http://websocket-service;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }
    
    # 정적 자원 캐싱
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

#### 환경별 설정

**Production 환경변수** (`.env.production`):
```env
VITE_WORKFLOW_API_URL=https://api.codemuse.com
VITE_RAG_API_URL=https://rag.codemuse.com
VITE_LLM_API_URL=https://llm.codemuse.com
VITE_DOC_GENERATOR_API_URL=https://docs.codemuse.com
VITE_WEBSOCKET_URL=wss://ws.codemuse.com
VITE_API_TIMEOUT=60000
VITE_CHUNK_PAGE_SIZE=50
```

#### 성능 최적화
- **코드 스플리팅**: 라우트별 번들 분리
- **Chunk 지연 로딩**: 큰 Chunk 데이터는 필요시 로딩
- **Mermaid 차트**: 동적 임포트로 번들 크기 최적화
- **Monaco Editor**: Web Worker 활용

#### 모니터링
- **에러 추적**: Sentry 연동
- **성능 모니터링**: Web Vitals 수집
- **사용자 분석**: Google Analytics

---

## 🚀 Chunk 기반 개발 가이드

### 핵심 컴포넌트 개발 우선순위

1. **워크플로우 상태 표시** (`WorkflowStatus.vue`)
   - 2단계 LLM 파이프라인 진행 상태
   - 키워드 추출 → RAG 검색 → 요약 → 최종 답변

2. **Chunk 탐색기** (`ChunkExplorer.vue`)
   - Chunk 메타데이터 기반 필터링
   - 복잡도, 타입, 태그별 정렬

3. **검색 전략 표시** (`SearchStrategy.vue`)
   - overview/function chunks 비율
   - 동적 폴더 우선순위 결과

4. **Mermaid 차트 렌더링** (`MermaidChart.vue`)
   - MD 문서의 플로우차트/시퀀스 다이어그램 표시
   - 동적 로딩 최적화

### TypeScript 타입 정의 예시

```typescript
// types/chunk.ts
export interface CodeChunk {
  chunk_id: string;
  file_path: string;
  chunk_type: 'overview' | 'class' | 'function' | 'async_function' | 'method';
  name: string;
  content: string;
  line_range: string;
  token_count: number;
  complexity?: number;
  docstring?: string;
  tags: string[];
  dependencies: string[];
  parent?: string;
  base_classes: string[];
  decorators: string[];
  is_async: boolean;
  is_generator: boolean;
}

// types/workflow.ts
export interface SearchStrategy {
  overview_results: number;
  function_chunks: number;
  filtered_files: string[];
  search_keywords: string[];
  total_chunks_found: number;
}

export interface WorkflowResponse {
  query: string;
  use_rag: boolean;
  search_strategy: SearchStrategy;
  rag_results: CodeChunk[];
  llm_summary?: string;
  llm_response: string;
  workflow_time: number;
  services_used: string[];
}
```

### Pinia 스토어 구조 예시

```typescript
// stores/workflow.ts
export const useWorkflowStore = defineStore('workflow', () => {
  const currentQuery = ref<string>('')
  const isProcessing = ref<boolean>(false)
  const currentStep = ref<'idle' | 'keywords' | 'search' | 'summary' | 'response'>('idle')
  const searchStrategy = ref<SearchStrategy | null>(null)
  const llmSummary = ref<string | null>(null)
  
  const executeWorkflow = async (query: string) => {
    // 워크플로우 실행 로직
  }
  
  return {
    currentQuery,
    isProcessing,
    currentStep,
    searchStrategy,
    llmSummary,
    executeWorkflow
  }
})
```

### 다음 단계 개발 항목

1. **Phase 2 프론트엔드 개발** 🚧
   - [ ] 기본 Vue 3 프로젝트 설정
   - [ ] Chunk 기반 컴포넌트 개발
   - [ ] 워크플로우 API 연동
   - [ ] 실시간 채팅 스트리밍
   - [ ] Mermaid 차트 렌더링
