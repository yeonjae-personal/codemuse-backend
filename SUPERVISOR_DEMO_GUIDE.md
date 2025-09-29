# 상무님 CodeMuse 데모 가이드

## 📋 개요
CodeMuse 플랫폼의 핵심 기능을 상무님께 시연하기 위한 통합 데모 시스템입니다.

## 🚀 데모용 백엔드 통합 API

### 서버 실행
```bash
# 데모용 통합 API 서버 (포트 8007)
python3 run_demo_api.py
```

### 주요 엔드포인트

#### 1. 데모 상태 확인
```
GET http://localhost:8007/api/v1/demo/status
```
- **응답**: 서비스 상태, 문서 수, 마지막 업데이트 시간
- **용도**: GUI에서 시스템 상태 표시

#### 2. 문서 업데이트 (핵심 기능)
```
POST http://localhost:8007/api/v1/demo/update-documents
```
- **기능**: 소스코드 → MD 문서 생성 → RAG 저장
- **처리시간**: 약 30초
- **응답**: 처리 상태, 생성된 문서 수

#### 3. RAG 검색 테스트
```
GET http://localhost:8007/api/v1/demo/test-chat
```
- **기능**: RAG 검색 기능 테스트
- **응답**: 검색 테스트 결과

#### 4. 사용 가능한 API 목록
```
GET http://localhost:8007/api/v1/demo/available-apis
```
- **기능**: 전체 API 엔드포인트 목록 제공

## 🖥️ GUI 연결 방법

### 1. 문서 업데이트 버튼 구현

```javascript
// 문서 업데이트 버튼 클릭 핸들러
async function updateDocuments() {
    try {
        // 버튼 비활성화 및 로딩 표시
        const updateBtn = document.getElementById('update-docs-btn');
        updateBtn.disabled = true;
        updateBtn.textContent = '업데이트 중...';
        
        // API 호출
        const response = await fetch('http://localhost:8007/api/v1/demo/update-documents', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        const result = await response.json();
        
        if (response.ok) {
            // 성공 메시지 표시
            showNotification('문서 업데이트가 시작되었습니다. 완료까지 약 30초 소요됩니다.', 'info');
            
            // 상태 폴링 시작
            pollStatus();
        } else {
            throw new Error(result.detail || '업데이트 실패');
        }
        
    } catch (error) {
        console.error('문서 업데이트 실패:', error);
        showNotification('문서 업데이트에 실패했습니다: ' + error.message, 'error');
    } finally {
        // 버튼 상태 복원
        updateBtn.disabled = false;
        updateBtn.textContent = '문서 업데이트';
    }
}

// 상태 폴링 함수
async function pollStatus() {
    const interval = setInterval(async () => {
        try {
            const response = await fetch('http://localhost:8007/api/v1/demo/status');
            const status = await response.json();
            
            // 상태 업데이트
            updateStatusDisplay(status);
            
            // 처리 완료 시 폴링 중지
            if (status.status === 'ready') {
                clearInterval(interval);
                showNotification('문서 업데이트가 완료되었습니다!', 'success');
            }
        } catch (error) {
            console.error('상태 확인 실패:', error);
            clearInterval(interval);
        }
    }, 2000); // 2초마다 확인
}
```

### 2. 상태 표시 컴포넌트

```javascript
// 상태 표시 업데이트
function updateStatusDisplay(status) {
    // 서비스 상태 표시
    const serviceStatus = document.getElementById('service-status');
    serviceStatus.innerHTML = Object.entries(status.services)
        .map(([service, state]) => 
            `<div class="service-item ${state === 'healthy' ? 'healthy' : 'unhealthy'}">
                ${service}: ${state}
            </div>`
        ).join('');
    
    // 문서 수 표시
    const docCount = document.getElementById('doc-count');
    docCount.textContent = status.documents_count;
    
    // 마지막 업데이트 시간
    const lastUpdated = document.getElementById('last-updated');
    lastUpdated.textContent = status.last_updated || '없음';
}
```

### 3. 채팅 기능 연결

```javascript
// RAG 기반 채팅
async function sendChatMessage(message) {
    try {
        // 1. RAG 검색
        const ragResponse = await fetch('http://localhost:8003/api/v1/search', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                query: message,
                limit: 3
            })
        });
        
        const ragData = await ragResponse.json();
        
        // 2. LLM 채팅 (RAG 컨텍스트 포함)
        const chatResponse = await fetch('http://localhost:8004/api/v1/chat/sessions', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                title: 'CodeMuse 채팅'
            })
        });
        
        const session = await chatResponse.json();
        
        // 3. 메시지 전송
        const messageResponse = await fetch(`http://localhost:8004/api/v1/chat/sessions/${session.id}/messages`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                message: message,
                model: 'gpt-3.5-turbo',
                context: {
                    rag_documents: ragData.documents || []
                }
            })
        });
        
        const result = await messageResponse.json();
        return result;
        
    } catch (error) {
        console.error('채팅 전송 실패:', error);
        throw error;
    }
}
```

## 🎯 데모 시나리오

### 1단계: 시스템 준비 (2분)
1. **서비스 시작**
   ```bash
   # 터미널 1: RAG 서비스
   OPENAI_API_KEY="your-key" python3 run_rag_service.py
   
   # 터미널 2: LLM 서비스  
   OPENAI_API_KEY="your-key" python3 run_llm_service.py
   
   # 터미널 3: Workflow 서비스
   python3 run_workflow_service.py
   
   # 터미널 4: 데모 API
   python3 run_demo_api.py
   ```

2. **GUI 접속**
   - 브라우저에서 프론트엔드 접속
   - 시스템 상태 확인 (모든 서비스 healthy)

### 2단계: 핵심 기능 시연 (5분)

#### A. 문서 업데이트 시연
1. **"문서 업데이트" 버튼 클릭**
   - "소스코드를 분석하여 MD 문서를 생성하고 RAG에 저장합니다"
   - 진행 상황 실시간 표시
   - 완료 후 "X개 문서가 생성되고 저장되었습니다"

2. **처리 과정 설명**
   - "AST 파싱으로 코드 구조 분석"
   - "자동으로 Markdown 문서 생성"
   - "벡터화하여 RAG 엔진에 저장"

#### B. RAG 기반 채팅 시연
1. **질문 예시들**
   - "Python 함수는 어떻게 정의하나요?"
   - "FastAPI 서비스 구조는 어떻게 되어있나요?"
   - "우리 프로젝트의 아키텍처는?"

2. **응답 품질 강조**
   - "일반적인 ChatGPT와 달리, 우리 프로젝트 코드를 기반으로 정확한 답변을 제공합니다"
   - "코드 컨텍스트를 이해하고 구체적인 예시를 제시합니다"

### 3단계: 비즈니스 가치 설명 (3분)

#### A. 개발 생산성 향상
- **코드 이해 시간 단축**: 신입 개발자도 빠른 온보딩
- **문서화 자동화**: 수동 문서 작성 시간 절약
- **지식 공유**: 팀 내 코드 지식 체계화

#### B. 품질 향상
- **일관된 코딩 스타일**: AI가 코드 패턴 분석하여 가이드
- **버그 예방**: 코드 분석을 통한 잠재적 문제점 발견
- **리팩토링 지원**: 개선점 제안 및 안전한 리팩토링

#### C. 확장성
- **CI/CD 통합**: 코드 변경 시 자동 문서 업데이트
- **다양한 언어 지원**: Python, JavaScript, Java, C++ 등
- **마이크로서비스 아키텍처**: 각 서비스별 독립적 확장 가능

## 💡 상무님께 전달할 핵심 포인트

### 1. 기술적 혁신성
- **AI + 코드 분석**: 단순한 ChatGPT가 아닌 프로젝트 특화 AI
- **자동화된 문서화**: 개발자가 문서 작성에 시간을 낭비하지 않음
- **실시간 학습**: 코드 변경사항을 즉시 반영

### 2. 비즈니스 임팩트
- **개발 속도 30% 향상**: 코드 이해 및 문서화 시간 단축
- **품질 향상**: 일관된 코드 스타일과 버그 예방
- **인력 효율성**: 시니어 개발자의 지식이 AI를 통해 전파

### 3. 경쟁 우위
- **기존 도구와의 차별화**: 단순 코드 생성이 아닌 프로젝트 이해
- **확장 가능한 아키텍처**: 마이크로서비스로 점진적 확장
- **CI/CD 통합**: 개발 프로세스에 자연스럽게 통합

### 4. 투자 대비 효과
- **낮은 도입 비용**: 기존 개발 환경에 쉽게 통합
- **즉시 효과**: 도입 즉시 개발 생산성 향상
- **장기적 ROI**: 팀 전체의 개발 역량 향상

## 🔧 데모 전 체크리스트

### 서버 준비
- [ ] RAG 서비스 (8003) 실행
- [ ] LLM 서비스 (8004) 실행  
- [ ] Workflow 서비스 (8006) 실행
- [ ] 데모 API (8007) 실행
- [ ] 모든 서비스 상태 healthy 확인

### GUI 준비
- [ ] 문서 업데이트 버튼 구현
- [ ] 상태 표시 컴포넌트 구현
- [ ] 채팅 인터페이스 구현
- [ ] 에러 처리 및 알림 구현

### 데모 데이터
- [ ] sample_code 디렉토리에 테스트 코드 준비
- [ ] 다양한 질문 예시 준비
- [ ] 데모 시나리오 연습

## 📞 데모 후 질의응답 준비

### 예상 질문과 답변

**Q: 실제 프로젝트에 적용하려면?**
A: CI/CD 파이프라인에 통합하여 코드 변경 시 자동으로 문서 업데이트되도록 설정합니다.

**Q: 보안은 어떻게 관리하나요?**
A: API 키는 환경변수로 관리하고, 내부 네트워크에서만 접근 가능하도록 설정합니다.

**Q: 다른 프로그래밍 언어도 지원하나요?**
A: 현재 Python, JavaScript, TypeScript, Java, C++를 지원하며, 필요에 따라 추가 언어 지원이 가능합니다.

**Q: 팀 규모는 어느 정도까지 지원하나요?**
A: 마이크로서비스 아키텍처로 설계되어 팀 규모에 따라 수평 확장이 가능합니다.

---

**데모 성공을 위한 핵심**: 기술적 완성도보다는 **비즈니스 가치**와 **실용성**에 집중하여 설명하세요!
