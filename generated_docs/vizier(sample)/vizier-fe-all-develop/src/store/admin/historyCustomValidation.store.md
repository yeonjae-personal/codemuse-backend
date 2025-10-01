# 📄 historyCustomValidation.store.ts

> **파일 경로**: `vizier(sample)/vizier-fe-all-develop/src/store/admin/historyCustomValidation.store.ts`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 4개  
> **언어**: Javascript / Typescript
---

## 📑 목차

### ⚙️ 함수
- [`fetchHistory`](#function-fetchhistory) - 복잡도: 9


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `@/constants/generalField` • `@/api/prod/customValidationApi` | ⚡ **총 복잡도**: 15 |
| 📊 **총 토큰 수**: 224 |  |




## ⚙️ 함수

### <a id="function-fetchhistory"></a>🔧 `fetchHistory`

![복잡도](https://img.shields.io/badge/복잡도-9-orange)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 9 |
| 📊 토큰 수 | 26 |
| 📍 라인 범위 | 31-39 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
    async function fetchHistory(payload: ParamHistoryCustomValidation) {
      try {
        const response = await getCustomValidationHistory(payload);
        history.value = updateCommGroupCode(response.data);
      } catch (error) {
        history.value = null;
        throw error;
      }
    }...
```

**Chunk 메타데이터**
- 🆔 **ID**: `f5712dd69e76`
- 🏷️ **태그**: `function, javascript`

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **4개의 chunk**로 구성되어 있으며, **224개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 177 | 79.0% |
| ⚙️ 함수 | 1 | 9.0 | 26 | 11.6% |
| arrow_function | 2 | 3.0 | 21 | 9.4% |

