# 📄 historyTab.store.ts

> **파일 경로**: `vizier(sample)/vizier-fe-all-develop/src/store/catalog/historyTab.store.ts`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 4개  
> **언어**: Javascript / Typescript
---

## 📑 목차

### ⚙️ 함수
- [`fetchHistory`](#function-fetchhistory) - 복잡도: 9


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `@/constants/generalField` • `@/api/prod/productApi` | ⚡ **총 복잡도**: 15 |
| 📊 **총 토큰 수**: 220 |  |




## ⚙️ 함수

### <a id="function-fetchhistory"></a>🔧 `fetchHistory`

![복잡도](https://img.shields.io/badge/복잡도-9-orange)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 9 |
| 📊 토큰 수 | 26 |
| 📍 라인 범위 | 29-37 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
  async function fetchHistory(payload: ParamUIHistoryTab) {
    try {
      const response = await getUiHistoryTab(payload);
      history.value = updateCommGroupCode(response.data);
    } catch (error) {
      history.value = null;
      throw error;
    }
  }...
```

**Chunk 메타데이터**
- 🆔 **ID**: `5814e73df2cc`
- 🏷️ **태그**: `function, javascript`

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **4개의 chunk**로 구성되어 있으며, **220개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 173 | 78.6% |
| ⚙️ 함수 | 1 | 9.0 | 26 | 11.8% |
| arrow_function | 2 | 3.0 | 21 | 9.5% |

