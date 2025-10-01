# 📄 SpinnerPage.vue

> **파일 경로**: `vizier(sample)/vizier-fe-all-develop/src/pages/controls/SpinnerPage.vue`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Javascript / Vue
---

## 📑 목차

### ⚙️ 함수
- [`startProgressLoop`](#function-startprogressloop) - 복잡도: 5


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `@/components/controls/CfSpinner.vue` • `vue-prism-component` • `@/components/controls/examples/CfSpinnerExample` | ⚡ **총 복잡도**: 5 |
| 📊 **총 토큰 수**: 212 |  |




## ⚙️ 함수

### <a id="function-startprogressloop"></a>🔧 `startProgressLoop`

![복잡도](https://img.shields.io/badge/복잡도-5-green)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 5 |
| 📊 토큰 수 | 18 |
| 📍 라인 범위 | 9-13 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function startProgressLoop() {
  interval.value = setInterval(() => {
    value.value = (value.value + 10) % 110;
  }, 1000);
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `1f101895a8b9`
- 🏷️ **태그**: `function, javascript`

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **212개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 194 | 91.5% |
| template | 1 | 0.0 | 0 | 0.0% |
| ⚙️ 함수 | 1 | 5.0 | 18 | 8.5% |

