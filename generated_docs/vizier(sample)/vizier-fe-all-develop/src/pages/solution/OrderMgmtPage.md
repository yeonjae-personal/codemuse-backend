# 📄 OrderMgmtPage.vue

> **파일 경로**: `vizier(sample)/vizier-fe-all-develop/src/pages/solution/OrderMgmtPage.vue`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 29개  
> **언어**: Javascript / Vue
---




## 📑 목차

### ⚙️ 함수
- [`formatDateToYYYYMMDD`](#function-formatdatetoyyyymmdd) - 복잡도: 7


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `ag-grid-vue3` • `ag-grid-community/styles/ag-grid.css` • `axios` • `@/pages/solution/OrderEventPage.vue` • `@/components/controls/CfDateRangePicker.vue` • `@/store` 외 4개 | ⚡ **총 복잡도**: 304 |
| 📊 **총 토큰 수**: 2,689 |  |




## ⚙️ 함수

### <a id="function-formatdatetoyyyymmdd"></a>🔧 `formatDateToYYYYMMDD`

![복잡도](https://img.shields.io/badge/복잡도-7-orange)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 7 |
| 📊 토큰 수 | 28 |
| 📍 라인 범위 | 35-42 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function formatDateToYYYYMMDD(dateString: any) {
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");

  return `${year}-${month}-${day}`;
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `46dba0e88b1f`
- 🏷️ **태그**: `function, javascript`

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **29개의 chunk**로 구성되어 있으며, **2,689개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 1,735 | 64.5% |
| template | 1 | 0.0 | 0 | 0.0% |
| ⚙️ 함수 | 1 | 7.0 | 28 | 1.0% |
| arrow_function | 25 | 11.9 | 926 | 34.4% |
| style | 1 | 0.0 | 0 | 0.0% |

