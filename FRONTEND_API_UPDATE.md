# 프론트엔드 API 엔드포인트 수정 가이드

## 🔴 현재 문제

프론트엔드에서 "RAG 저장" 버튼이 잘못된 엔드포인트를 호출하고 있습니다.

### 현재 (잘못된 설정)
```javascript
// RAG 저장 버튼
POST https://rag.codemuse.beauty/api/v1/documents/upload
```

**문제점**:
- `rag.codemuse.beauty`는 RAG 엔진 (8003 포트)
- `/api/v1/documents/upload`는 단일 문서 업로드 API
- 이 API는 문서 1개씩 개별 업로드 (느림)
- **배치 최적화가 적용되지 않음**

---

## ✅ 올바른 엔드포인트 설정

### 1. MD 변환 버튼 (문서 생성만, RAG 업로드 안 함)
```javascript
// 요청
POST https://doc.codemuse.beauty/api/v1/documents/generate

// 요청 본문
{
  "upload_to_rag": false  // RAG 업로드 건너뜀
}

// 응답
{
  "status": "success",
  "result": {
    "total_files": 1583,
    "docs_created": 1583,
    "md_files_count": 1583
  }
}
```

### 2. RAG 저장 버튼 (이미 생성된 MD를 RAG에만 업로드)
```javascript
// ✅ 올바른 엔드포인트
POST https://doc.codemuse.beauty/api/v1/documents/upload

// 요청 본문 (선택 사항)
{
  "source_dir": "/path/to/sample_code",  // 옵션 (기본값 사용 가능)
  "output_dir": "/path/to/generated_docs" // 옵션 (기본값 사용 가능)
}

// 응답
{
  "status": "success",
  "result": {
    "success": true,
    "total_sections": 7505,
    "uploaded_sections": 7505,
    "elapsed_time": 120.5  // 초
  }
}
```

---

## 🚀 성능 차이

### 잘못된 방식 (현재)
```
RAG 엔진 개별 업로드 API 사용
→ 7,505개 섹션 × 개별 호출
→ 예상 시간: ~30분
```

### 올바른 방식 (최적화)
```
문서 생성기 배치 업로드 API 사용
→ 76개 배치 (100개씩)
→ 예상 시간: ~2-3분 (90% 단축)
```

---

## 📝 프론트엔드 코드 수정 예시

### Before (잘못됨)
```typescript
// RAG 저장 버튼 핸들러
async function handleRagUpload() {
  const response = await fetch('https://rag.codemuse.beauty/api/v1/documents/upload', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      id: documentId,
      content: content,
      metadata: metadata
    })
  });
}
```

### After (올바름)
```typescript
// RAG 저장 버튼 핸들러 (최적화 적용)
async function handleRagUpload() {
  const response = await fetch('https://doc.codemuse.beauty/api/v1/documents/upload', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({})  // 기본 경로 사용
  });
  
  const result = await response.json();
  
  if (result.status === 'success') {
    console.log(`✅ ${result.result.uploaded_sections}개 섹션 업로드 완료`);
    console.log(`⏱️ 소요 시간: ${result.result.elapsed_time}초`);
  }
}
```

---

## 🔍 두 방식의 차이

| 항목 | 잘못된 방식 | 올바른 방식 |
|------|-------------|-------------|
| 엔드포인트 | `rag.codemuse.beauty` (RAG 엔진) | `doc.codemuse.beauty` (문서 생성기) |
| API 경로 | `/api/v1/documents/upload` | `/api/v1/documents/upload` |
| 처리 방식 | 단일 문서 개별 업로드 | 배치 업로드 (100개씩) |
| 임베딩 | 1개씩 API 호출 | 100개씩 배치 호출 |
| BM25 인덱스 | 매번 전체 재구성 | 마지막에 1회만 |
| 속도 | ~30분 | ~2-3분 |

---

## 🎯 권장 설정

### Option 1: 자동 RAG 업로드 (권장)
```javascript
// MD 변환 버튼 → 변환 + 자동 RAG 업로드
POST https://doc.codemuse.beauty/api/v1/documents/generate
{
  "upload_to_rag": true  // 자동 업로드 (백그라운드)
}
```

→ 사용자 클릭 1번으로 모든 작업 완료

### Option 2: 수동 분리 (선택적)
```javascript
// 1단계: MD만 변환
POST https://doc.codemuse.beauty/api/v1/documents/generate
{
  "upload_to_rag": false
}

// 2단계: RAG에만 업로드 (나중에)
POST https://doc.codemuse.beauty/api/v1/documents/upload
{}
```

→ 사용자가 시점을 선택할 수 있음

---

## 🧪 테스트 방법

### 1. RAG 데이터 초기화
```bash
curl -X DELETE https://rag.codemuse.beauty/api/v1/documents
```

### 2. MD만 생성 (RAG 업로드 안 함)
```bash
curl -X POST https://doc.codemuse.beauty/api/v1/documents/generate \
  -H "Content-Type: application/json" \
  -d '{"upload_to_rag": false}'
```

### 3. RAG에만 업로드 (최적화 적용)
```bash
curl -X POST https://doc.codemuse.beauty/api/v1/documents/upload \
  -H "Content-Type: application/json" \
  -d '{}'
```

### 4. 진행 상황 모니터링
```bash
curl https://rag.codemuse.beauty/health | jq '.documents_count, .embedding_cache'
```

---

## 📊 현재 진행 중인 업로드 상태

```
총 섹션: 7,505개
배치 수: 76개
배치 크기: 100개
진행률: 1/76 (배치 진행 중)
```

예상 소요 시간: **약 2-3분**

---

생성일: 2025-10-01  
목적: 프론트엔드 RAG 저장 버튼 수정

