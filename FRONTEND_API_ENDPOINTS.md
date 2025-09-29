# CodeMuse Frontend API 엔드포인트 가이드 (MD Chunk 기반)

## 🎯 프론트엔드용 핵심 엔드포인트 (완성된 2단계 LLM 파이프라인)

**✅ 최종 완성 상태**: 기존 파일 단위 RAG → **MD 섹션 기반 하이브리드 검색 + 완성된 2단계 LLM 파이프라인**

**🔄 주요 업데이트 (최신)**:
- **MD 문서 기반**: Python 소스 대신 구조화된 MD 섹션 검색
- **완전한 RAG 초기화**: 0개 문서에서 시작 (깨끗한 상태)
- **소스 구조 유지**: `sample_code/` → `generated_docs/` 동일 구조
- **템플릿 분리**: Jinja2 기반 외부 템플릿 시스템

### **1. Chunk 기반 RAG 검색** (Chunk 단위 검색)
```
POST http://localhost:8003/api/v1/search
Content-Type: application/json

{
  "query": "사용자 질문",
  "limit": 10,
  "chunk_type_filter": "overview|function|class",  // 옵션: chunk 타입 필터
  "file_filter": ["analyzer.py", "validator.py"],  // 옵션: 파일 필터
  "include_metadata": true
}
```

**응답 예시 (Chunk 기반):**
```json
{
  "query": "오류 분석은 어떻게 하나요?",
  "results": [
    {
      "chunk_id": "chunk_issue_detector_001",
      "content": "def detect_issues(self, code: str) -> List[Issue]:\n    \"\"\"코드 오류를 검출합니다\"\"\"\n    issues = []\n    # 7가지 오류 유형 검출 로직...",
      "score": 0.92,
      "metadata": {
        "filename": "issue_detector.py",
        "chunk_type": "function",
        "name": "detect_issues",
        "line_range": "45-78",
        "complexity": 8,
        "tags": ["error_detection", "analysis", "validation"],
        "folder_priority": 95,
        "is_async": false
      }
    },
    {
      "chunk_id": "chunk_issue_detector_overview",
      "content": "IssueDetector 클래스는 Python 코드에서 7가지 주요 오류 유형을 검출합니다:\n1. 타입 불일치\n2. 중복 코드...",
      "score": 0.88,
      "metadata": {
        "filename": "issue_detector.py", 
        "chunk_type": "overview",
        "name": "IssueDetector",
        "dependencies": ["ast", "typing", "logging"],
        "folder_priority": 95
      }
    }
  ],
  "total_results": 15,
  "search_strategy": {
    "overview_chunks": 3,
    "function_chunks": 7,
    "hybrid_score": true
  },
  "search_time": 0.12
}
```

### **2. LLM 채팅** (대화, 8004)

#### 2.1 채팅 세션 생성
```
POST http://localhost:8004/api/v1/chat/sessions
Content-Type: application/json

{
  "title": "새 채팅"
}
```

**응답 예시:**
```json
{
  "id": "session_123",
  "title": "새 채팅",
  "created_at": "2024-01-01T00:00:00Z",
  "messages": []
}
```

#### 2.2 메시지 전송
```
POST http://localhost:8004/api/v1/chat/sessions/{session_id}/messages
Content-Type: application/json

{
  "message": "사용자 질문",
  "model": "gpt-3.5-turbo",
  "context": {
    "search_results": [검색된 문서들]
  }
}
```

**응답 예시:**
```json
{
  "message": "Python에서 함수는 def 키워드로 정의합니다...",
  "session_id": "session_123",
  "model": "gpt-3.5-turbo",
  "usage": {
    "prompt_tokens": 150,
    "completion_tokens": 200,
    "total_tokens": 350
  },
  "response_time": 2.5
}
```

#### 2.3 메시지 목록 조회
```
GET http://localhost:8004/api/v1/chat/sessions/{session_id}/messages
```

#### 2.4 세션 목록 조회
```
GET http://localhost:8004/api/v1/chat/sessions
```

### **3. 통합 워크플로우** (2단계 LLM 파이프라인, 8006) ⭐ **메인 API**
```
POST http://localhost:8006/workflow
Content-Type: application/json

{
  "query": "사용자 질문",
  "use_rag": true,
  "model": "gpt-4",
  "max_chunks": 5,           // 검색할 최대 chunk 수
  "include_overview": true   // overview chunk 포함 여부
}
```

**응답 예시 (2단계 파이프라인):**
```json
{
  "query": "어떤 오류 유형들을 분석하고 있나요?",
  "use_rag": true,
  "search_strategy": {
    "overview_results": 2,
    "function_chunks": 3,
    "filtered_files": ["issue_detector.py", "validator.py"],
    "search_keywords": ["분석", "error", "detection", "issue_detector"],
    "total_chunks_found": 5
  },
  "rag_results": [
    {
      "chunk_id": "chunk_issue_detector_overview",
      "content": "IssueDetector는 7가지 오류 유형을 검출합니다:\n1. 타입 불일치\n2. 중복 코드\n3. 사용되지 않는 변수...",
      "metadata": {
        "filename": "issue_detector.py",
        "chunk_type": "overview",
        "folder_priority": 95
      }
    }
  ],
  "llm_summary": null,
  "llm_response": "CodeMuse는 현재 7가지 주요 오류 유형을 분석하고 있습니다:\n\n1. **타입 불일치**: 변수나 함수의 타입이 맞지 않는 경우\n2. **중복 코드**: 동일하거나 유사한 코드가 반복되는 경우\n3. **사용되지 않는 변수**: 정의했지만 사용하지 않는 변수들\n4. **순환 참조**: 모듈 간 순환 의존성 문제\n5. **복잡도 초과**: 함수나 클래스의 복잡도가 너무 높은 경우\n6. **네이밍 규칙 위반**: Python PEP8 네이밍 컨벤션 위반\n7. **보안 취약점**: 잠재적인 보안 문제가 있는 코드 패턴\n\n이러한 분석은 IssueDetector 클래스를 통해 자동화되어 있으며, AST 파싱을 기반으로 정확한 검출을 수행합니다.",
  "workflow_time": 2.8,
  "services_used": [
    "LLM Keyword Extraction",
    "RAG Engine", 
    "LLM Summary",
    "LLM Chat"
  ]
}
```

### **4. 문서 생성 및 업데이트** (Document Generator, 8001) ⭐ **프론트엔드 핵심**
```
POST http://localhost:8001/api/v1/documents/generate
Content-Type: application/json
```

**주요 기능**:
- 소스코드 → MD 문서 생성 (32개 파일)
- MD 섹션별 RAG 자동 업로드 (### 기준 분할)
- 백그라운드 처리로 즉시 응답

**응답 예시**:
```json
{
  "status": "started",
  "message": "문서 업데이트가 시작되었습니다. 완료까지 약 30초 소요됩니다.",
  "docs_created": 0,
  "docs_uploaded": 0, 
  "processing_time": 0.0
}
```

비고: 별도 상태 폴링 API는 제공하지 않습니다. 생성 호출 후 `GET http://localhost:8003/health` 의 `documents_count` 증가로 확인하세요.

### **5. 헬스 체크 및 서비스 상태**
```
GET http://localhost:8003/health  # RAG Engine (MD Chunk 기반)
GET http://localhost:8004/health  # LLM Chat 
GET http://localhost:8006/health  # Workflow Orchestrator (2단계 파이프라인)
GET http://localhost:8001/health  # Document Generator Service
GET http://localhost:8006/services/status  # 통합 서비스 상태 확인
```

**서비스 상태 응답 예시:**
```json
{
  "rag_service": "healthy",
  "llm_service": "healthy", 
  "workflow_status": "operational",
  "chunk_system": "active",
  "pipeline_mode": "2-stage",
  "last_updated": "2024-01-01T00:00:00Z"
}
```

## 🚀 프론트엔드 개발 시나리오 (MD Chunk 기반)

### **시나리오 0: 문서 생성 및 RAG 업로드** ⭐ **필수 첫 단계**
```javascript
// 문서 생성 + RAG 업로드 (프론트엔드에서 버튼 클릭)
const generateAndUploadDocuments = async () => {
  // 1. 문서 생성 시작
  const response = await fetch('http://localhost:8001/api/v1/documents/generate', {
    method: 'POST',
    headers: {'Content-Type': 'application/json'}
  });
  
  const result = await response.json();
  console.log('문서 생성 결과:', result.status);
  
  // 2. 생성 여부 확인 (RAG 문서 수 확인)
  const healthResp = await fetch('http://localhost:8003/health');
  const health = await healthResp.json();
  console.log('RAG documents_count:', health.documents_count);
};

// Vue 3 예시
const { ref } = Vue;
export default {
  setup() {
    const isGenerating = ref(false);
    const documentsCount = ref(0);
    const generationStatus = ref('idle'); // idle, processing, ready
    
    const handleGenerateDocuments = async () => {
      isGenerating.value = true;
      generationStatus.value = 'processing';
      
      try {
        await generateAndUploadDocuments();
        generationStatus.value = 'ready';
      } catch (error) {
        console.error('문서 생성 실패:', error);
        generationStatus.value = 'error';
      } finally {
        isGenerating.value = false;
      }
    };
    
    return {
      isGenerating,
      documentsCount,
      generationStatus,
      handleGenerateDocuments
    };
  }
};
```

### **시나리오 1: MD Chunk 기반 검색** 
```javascript
// Chunk 메타데이터와 함께 검색
const searchChunks = async (query) => {
  const response = await fetch('http://localhost:8003/api/v1/search', {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify({
      query: query,
      top_k: 10,
      chunk_type_filter: "overview|function",  // overview와 function chunk만
      include_metadata: true
    })
  });
  
  const results = await response.json();
  
  // Chunk 메타데이터 활용
  results.results.forEach(chunk => {
    console.log(`[${chunk.metadata.chunk_type}] ${chunk.metadata.name}`);
    console.log(`복잡도: ${chunk.metadata.complexity || 'N/A'}`);
    console.log(`태그: ${chunk.metadata.tags?.join(', ')}`);
  });
  
  return results;
};
```

### **시나리오 2: 2단계 LLM 파이프라인** ⭐ **권장**
```javascript
// 가장 간단하고 강력한 방법
const askQuestion = async (userQuery) => {
  const response = await fetch('http://localhost:8006/workflow', {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify({
      query: userQuery,
      use_rag: true,
      model: 'gpt-4',
      max_chunks: 5,
      include_overview: true
    })
  });
  
  const result = await response.json();
  
  // 워크플로우 단계별 정보 활용
  console.log('🔍 검색 전략:', result.search_strategy);
  console.log('📝 LLM 요약:', result.llm_summary);
  console.log('🤖 최종 답변:', result.llm_response);
  console.log('⏱️ 처리 시간:', result.workflow_time);
  
  return result;
};

// 사용 예시
askQuestion("어떤 오류들을 분석하고 있나요?")
  .then(result => {
    // UI에 결과 표시
    displaySearchStrategy(result.search_strategy);
    displayLLMSummary(result.llm_summary);  
    displayFinalAnswer(result.llm_response);
  });
```

### **시나리오 3: 실시간 워크플로우 상태 추적**
```javascript
// Vue 3 Composition API 예시
import { ref, reactive } from 'vue'

export const useWorkflowStatus = () => {
  const isProcessing = ref(false)
  const currentStep = ref('idle')  // idle, keywords, search, summary, response
  const searchStrategy = reactive({})
  const llmSummary = ref('')
  const finalResponse = ref('')
  
  const executeWorkflow = async (query) => {
    isProcessing.value = true
    currentStep.value = 'keywords'
    
    try {
      const response = await fetch('http://localhost:8006/workflow', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
          query,
          use_rag: true,
          model: 'gpt-4'
        })
      })
      
      const result = await response.json()
      
      // 단계별 상태 업데이트
      currentStep.value = 'search'
      Object.assign(searchStrategy, result.search_strategy)
      
      currentStep.value = 'summary'
      llmSummary.value = result.llm_summary || ''
      
      currentStep.value = 'response'
      finalResponse.value = result.llm_response
      
      currentStep.value = 'completed'
      
    } catch (error) {
      console.error('워크플로우 실행 실패:', error)
      currentStep.value = 'error'
    } finally {
      isProcessing.value = false
    }
  }
  
  return {
    isProcessing,
    currentStep,
    searchStrategy,
    llmSummary,
    finalResponse,
    executeWorkflow
  }
}
```

### **시나리오 4: Chunk 탐색기 구현**
```javascript
// Chunk 메타데이터 기반 필터링 및 정렬
const ChunkExplorer = {
  async getChunksByType(chunkType) {
    const response = await fetch('http://localhost:8003/api/v1/search', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({
        query: '*',  // 전체 검색
        chunk_type_filter: chunkType,
        top_k: 50
      })
    });
    return await response.json();
  },
  
  async getChunksByComplexity(minComplexity = 5) {
    // 복잡도가 높은 함수들 찾기
    const chunks = await this.getChunksByType('function');
    return chunks.results.filter(chunk => 
      chunk.metadata.complexity >= minComplexity
    ).sort((a, b) => b.metadata.complexity - a.metadata.complexity);
  },
  
  async getChunksByTags(tags) {
    // 특정 태그를 가진 chunk들 찾기
    const response = await fetch('http://localhost:8003/api/v1/search', {
      method: 'POST', 
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({
        query: tags.join(' '),  // 태그들을 검색어로 사용
        top_k: 30
      })
    });
    return await response.json();
  }
};
```

## 📋 필요한 환경 변수
```bash
# .env 파일에 설정
OPENAI_API_KEY=your-openai-key
ANTHROPIC_API_KEY=your-anthropic-key
```

## 🔧 CORS 설정
모든 서비스에서 CORS가 허용되어 있어 프론트엔드에서 직접 호출 가능합니다.

## 📝 에러 처리
```javascript
try {
  const response = await fetch('http://localhost:8004/api/v1/chat/sessions', {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify({title: 'My Chat'})
  });
  
  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
  }
  
  const data = await response.json();
  console.log(data);
} catch (error) {
  console.error('API 호출 실패:', error);
}
```

## 🎯 MVP 프론트엔드 플로우 (MD Chunk 기반)

### **완전한 플로우** (권장 - 필수 2단계)
```
STEP 1: 문서 생성 (필수 - 최초 1회)
    ↓
POST /api/v1/demo/update-documents (문서 생성 + RAG 업로드)
    ├─ 소스코드 → 32개 MD 파일 생성
    ├─ MD 파일 → ### 기준 섹션 분할
    ├─ 섹션별 메타데이터 생성
    └─ RAG 벡터화 저장 (462개 chunk)
    ↓
폴링으로 완료 대기 (documents_count > 0)
    ↓
STEP 2: 대화 시작 (반복 가능)
    ↓
POST /workflow (완성된 2단계 LLM 파이프라인)
    ├─ 1단계: LLM 키워드 추출 ("무슨 시스템?" → ["system", "project"])
    ├─ 2단계: MD Chunk 검색 (project_summary → 관련 MD 섹션)
    ├─ 3단계: LLM 요약 (다수 chunk인 경우)
    └─ 4단계: 최종 LLM 응답 ("Rule Analyzer 시스템입니다")
    ↓
결과 표시 (search_strategy + llm_summary + llm_response)
```

### **고급 플로우** (Chunk 탐색 포함)
```
사용자 질문 입력
    ↓
POST /workflow (2단계 파이프라인)
    ↓
search_strategy 분석
    ├─ overview_chunks: X개
    ├─ function_chunks: Y개  
    └─ filtered_files: [파일목록]
    ↓
Chunk 상세 정보 표시
    ├─ 메타데이터 (복잡도, 태그, 라인범위)
    ├─ Mermaid 차트 렌더링
    └─ 코드 신택스 하이라이팅
    ↓
LLM 응답 + 관련 Chunk 표시
```

### **필수 API (MVP)**
1. ⭐ **POST /api/v1/demo/update-documents** - 문서 생성 + RAG 업로드 (최초 필수)
2. **GET /api/v1/demo/status** - 문서 생성 상태 확인 (폴링용)
3. ⭐ **POST /workflow** - 완성된 2단계 LLM 파이프라인 (메인 대화)
4. **GET /services/status** - 서비스 상태 확인

### **선택적 API (확장)**
5. **POST /api/v1/search** - 직접 MD Chunk 검색 (탐색기용)
6. **POST /api/v1/chat/sessions** - 채팅 세션 관리
7. **GET /health** (각 서비스) - 개별 서비스 상태

**단 4개 핵심 API로 완전한 MD 기반 프론트엔드 완성!** 🚀

**🎯 개발 순서**:
1. 문서 생성 버튼 → POST `/api/v1/demo/update-documents`
2. 상태 폴링 → GET `/api/v1/demo/status`
3. 대화 인터페이스 → POST `/workflow`
4. 응답 표시 (search_strategy + llm_summary + llm_response)

---

## 🔧 추가 Chunk API 엔드포인트

### **Chunk 메타데이터 조회**
```
GET http://localhost:8003/api/v1/chunks/metadata
```

### **특정 Chunk 상세 조회**  
```
GET http://localhost:8003/api/v1/chunks/{chunk_id}
```

### **Chunk 업로드 (문서 생성 후)**
```
POST http://localhost:8003/api/v1/chunks/upload
Content-Type: application/json

{
  "chunks": [chunk데이터배열],
  "collection_name": "codemuse_chunks"
}
```
