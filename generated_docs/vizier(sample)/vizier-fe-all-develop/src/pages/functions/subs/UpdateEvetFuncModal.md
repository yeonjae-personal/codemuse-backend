# 📄 UpdateEvetFuncModal.vue

> **파일 경로**: `vizier(sample)/vizier-fe-all-develop/src/pages/functions/subs/UpdateEvetFuncModal.vue`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 16개  
> **언어**: Javascript / Vue
---




## 📑 목차

### ⚙️ 함수
- [`handleCheckboxChange`](#function-handlecheckboxchange) - 복잡도: 4


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `@/store` • `@/utils/common-ordr` • `ag-grid-vue3` • `ag-grid-community/styles/ag-theme-alpine.css` • `ag-grid-community/styles/ag-grid.css` • `ag-grid-community` 외 1개 | ⚡ **총 복잡도**: 187 |
| 📊 **총 토큰 수**: 2,902 |  |




## ⚙️ 함수

### <a id="function-handlecheckboxchange"></a>🔧 `handleCheckboxChange`

![복잡도](https://img.shields.io/badge/복잡도-4-green)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 4 |
| 📊 토큰 수 | 19 |
| 📍 라인 범위 | 706-710 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function handleCheckboxChange(event) {
  const selectedRow = event.data;
  //selectedRow.status = event.target.checked ? 'Y' : 'N';
  gridApi_evetFunc.value.updateRowData({ update: [selectedRow] });
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `604f5be2ff0f`
- 🏷️ **태그**: `function, javascript`

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **16개의 chunk**로 구성되어 있으며, **2,902개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 2,319 | 79.9% |
| template | 1 | 0.0 | 0 | 0.0% |
| ⚙️ 함수 | 1 | 4.0 | 19 | 0.7% |
| arrow_function | 12 | 15.2 | 564 | 19.4% |
| style | 1 | 0.0 | 0 | 0.0% |

