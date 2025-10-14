# 📄 UploadUserImageEditMode.vue

> **파일 경로**: `vizier(sample)/vizier-fe-all-develop/src/components/prod/dashboard/UploadUserImageEditMode.vue`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 11개  
> **언어**: Javascript / Vue
---


## 🏪 상태 관리 (Pinia)

### `rImages`
- **사용 위치**: 현재 컴포넌트



## 📑 목차

### ⚙️ 함수
- [`handleDeleteImage`](#function-handledeleteimage) - 복잡도: 7
- [`handleImageUpload`](#function-handleimageupload) - 복잡도: 26
- [`closeEditDialog`](#function-closeeditdialog) - 복잡도: 3
- [`cancelEditDialog`](#function-canceleditdialog) - 복잡도: 3
- [`goToPrevSlide`](#function-gotoprevslide) - 복잡도: 3
- [`goToNextSlide`](#function-gotonextslide) - 복잡도: 3


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `@/store` • `vue-i18n` • `keen-slider/keen-slider.min.css` • `@/utils/http-common` • `vue-upload-drop-images` • `keen-slider` 외 3개 | ⚡ **총 복잡도**: 62 |
| 📊 **총 토큰 수**: 2,494 |  |




## ⚙️ 함수

### <a id="function-handledeleteimage"></a>🔧 `handleDeleteImage`

![복잡도](https://img.shields.io/badge/복잡도-7-orange)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 7 |
| 📊 토큰 수 | 18 |
| 📍 라인 범위 | 54-60 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function handleDeleteImage(index) {
  flagDel.value = true;
  const image = imageStore.uploadedImagesExtend.requests[index];
  deleteIndex.value = index;
  imageSeqToDelete.value = image.imageSeq;
  deleteImage(imageSeqToDelete.value);
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `8fbadfb16047`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-handleimageupload"></a>🔧 `handleImageUpload`

![복잡도](https://img.shields.io/badge/복잡도-26-red)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 26 |
| 📊 토큰 수 | 74 |
| 📍 라인 범위 | 62-87 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function handleImageUpload(event, index) {
  isDeleted.value = false;
  isUpload.value = true;
  const latestFile = event[event.length - 1];
  if (latestFile) {
    const reader = new FileReader();
    reader.onload = () => {
      const existingImage = tempImages.value.find(
        (img) => img.imageSeq === index
      );
      if (existingImage) {
        existingImage.imageBase64 = reader.result;
        existingImage.imageName = latestFile.name;
      } else {
        tempImages.value.push({
          imageSeq: index,
          imageBase64: reader.result,
          imageName: latestFile.name,
        });
      }
    };
    reader.readAsDataURL(latestFile);
  } else {
    tempImages.value = tempImages.value.filter((img) => img.imageSeq !== index);
  }
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `63a7a10a8385`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-closeeditdialog"></a>🔧 `closeEditDialog`

![복잡도](https://img.shields.io/badge/복잡도-3-green)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 3 |
| 📊 토큰 수 | 5 |
| 📍 라인 범위 | 121-123 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function closeEditDialog() {
  emit("close-edit-dialog");
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `43424778288f`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-canceleditdialog"></a>🔧 `cancelEditDialog`

![복잡도](https://img.shields.io/badge/복잡도-3-green)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 3 |
| 📊 토큰 수 | 5 |
| 📍 라인 범위 | 125-127 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function cancelEditDialog() {
  emit("cancel-edit-dialog");
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `44f9ff3735d6`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-gotoprevslide"></a>🔧 `goToPrevSlide`

![복잡도](https://img.shields.io/badge/복잡도-3-green)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 3 |
| 📊 토큰 수 | 7 |
| 📍 라인 범위 | 180-182 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function goToPrevSlide() {
  if (sliderInstance.value) sliderInstance.value.prev();
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `705a9c1013eb`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-gotonextslide"></a>🔧 `goToNextSlide`

![복잡도](https://img.shields.io/badge/복잡도-3-green)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 3 |
| 📊 토큰 수 | 7 |
| 📍 라인 범위 | 184-186 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function goToNextSlide() {
  if (sliderInstance.value) sliderInstance.value.next();
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `aec156ba653a`
- 🏷️ **태그**: `function, javascript`

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **11개의 chunk**로 구성되어 있으며, **2,494개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 2,323 | 93.1% |
| template | 1 | 0.0 | 0 | 0.0% |
| ⚙️ 함수 | 6 | 7.5 | 116 | 4.7% |
| arrow_function | 2 | 8.5 | 55 | 2.2% |
| style | 1 | 0.0 | 0 | 0.0% |

