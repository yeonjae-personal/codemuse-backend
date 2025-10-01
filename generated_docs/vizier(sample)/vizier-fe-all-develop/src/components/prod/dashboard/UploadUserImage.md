# 📄 UploadUserImage.vue

> **파일 경로**: `vizier(sample)/vizier-fe-all-develop/src/components/prod/dashboard/UploadUserImage.vue`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 8개  
> **언어**: Javascript / Vue
---


## 🏪 상태 관리 (Pinia)

### `rImages`
- **사용 위치**: 현재 컴포넌트



## 📑 목차

### ⚙️ 함수
- [`closeDialog`](#function-closedialog) - 복잡도: 3
- [`editImage`](#function-editimage) - 복잡도: 3
- [`goToPrevSlide`](#function-gotoprevslide) - 복잡도: 3
- [`goToNextSlide`](#function-gotonextslide) - 복잡도: 3


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `keen-slider/keen-slider.min.css` • `keen-slider` • `../icons/UserImageIconDetail.vue` • `vuetify/components` • `@/api/prod/path` • `../icons/UpdateIcon.vue` 외 3개 | ⚡ **총 복잡도**: 25 |
| 📊 **총 토큰 수**: 1,536 |  |




## ⚙️ 함수

### <a id="function-closedialog"></a>🔧 `closeDialog`

![복잡도](https://img.shields.io/badge/복잡도-3-green)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 3 |
| 📊 토큰 수 | 5 |
| 📍 라인 범위 | 35-37 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function closeDialog() {
  emit("close-dialog");
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `f59ae595d4c1`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-editimage"></a>🔧 `editImage`

![복잡도](https://img.shields.io/badge/복잡도-3-green)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 3 |
| 📊 토큰 수 | 5 |
| 📍 라인 범위 | 38-40 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function editImage() {
  emit("edit");
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `5fe6da7259e2`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-gotoprevslide"></a>🔧 `goToPrevSlide`

![복잡도](https://img.shields.io/badge/복잡도-3-green)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 3 |
| 📊 토큰 수 | 7 |
| 📍 라인 범위 | 76-78 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function goToPrevSlide() {
  if (sliderInstance.value) sliderInstance.value.prev();
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `35d3e1b5bebc`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-gotonextslide"></a>🔧 `goToNextSlide`

![복잡도](https://img.shields.io/badge/복잡도-3-green)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 3 |
| 📊 토큰 수 | 7 |
| 📍 라인 범위 | 80-82 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function goToNextSlide() {
  if (sliderInstance.value) sliderInstance.value.next();
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `1c09790b40dd`
- 🏷️ **태그**: `function, javascript`

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **8개의 chunk**로 구성되어 있으며, **1,536개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 1,476 | 96.1% |
| template | 1 | 0.0 | 0 | 0.0% |
| ⚙️ 함수 | 4 | 3.0 | 24 | 1.6% |
| arrow_function | 1 | 13.0 | 36 | 2.3% |
| style | 1 | 0.0 | 0 | 0.0% |

