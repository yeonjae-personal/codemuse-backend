# 📄 UserImage.vue

> **파일 경로**: `vizier(sample)/vizier-fe-all-develop/src/components/prod/dashboard/UserImage.vue`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 7개  
> **언어**: Javascript / Vue
---


## 🏪 상태 관리 (Pinia)

### `rImages`
- **사용 위치**: 현재 컴포넌트



## 📑 목차

### ⚙️ 함수
- [`goToPrevSlide`](#function-gotoprevslide) - 복잡도: 3
- [`goToNextSlide`](#function-gotonextslide) - 복잡도: 3


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `keen-slider/keen-slider.min.css` • `keen-slider` • `@/api/prod/path` • `@/store/userImagesStore` • `@/utils/http-common` | ⚡ **총 복잡도**: 44 |
| 📊 **총 토큰 수**: 1,220 |  |




## ⚙️ 함수

### <a id="function-gotoprevslide"></a>🔧 `goToPrevSlide`

![복잡도](https://img.shields.io/badge/복잡도-3-green)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 3 |
| 📊 토큰 수 | 7 |
| 📍 라인 범위 | 80-82 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function goToPrevSlide() {
  if (sliderInstance.value) sliderInstance.value.prev();
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `5ca9db9d0891`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-gotonextslide"></a>🔧 `goToNextSlide`

![복잡도](https://img.shields.io/badge/복잡도-3-green)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 3 |
| 📊 토큰 수 | 7 |
| 📍 라인 범위 | 84-86 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function goToNextSlide() {
  if (sliderInstance.value) sliderInstance.value.next();
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `d05e00b3fe74`
- 🏷️ **태그**: `function, javascript`

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **7개의 chunk**로 구성되어 있으며, **1,220개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 1,113 | 91.2% |
| template | 1 | 0.0 | 0 | 0.0% |
| ⚙️ 함수 | 2 | 3.0 | 14 | 1.1% |
| arrow_function | 2 | 19.0 | 93 | 7.6% |
| style | 1 | 0.0 | 0 | 0.0% |

