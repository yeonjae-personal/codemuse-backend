# 📄 userImagesStore.ts

> **파일 경로**: `vizier(sample)/fe/src/store/userImagesStore.ts`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 14개  
> **언어**: Javascript / Typescript
---

## 📑 목차

### ⚙️ 함수
- [`setUploadedImagesExtend`](#function-setuploadedimagesextend) - 복잡도: 14
- [`deleteImage`](#function-deleteimage) - 복잡도: 10


## 📋 파일 개요

| | |
|--|--|
| ⚡ **총 복잡도**: 57 | 📊 **총 토큰 수**: 517 |




## ⚙️ 함수

### <a id="function-setuploadedimagesextend"></a>🔧 `setUploadedImagesExtend`

![복잡도](https://img.shields.io/badge/복잡도-14-red)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 14 |
| 📊 토큰 수 | 41 |
| 📍 라인 범위 | 19-32 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
  function setUploadedImagesExtend(apiImages) {
    uploadedImagesExtend.value.requests.forEach((image) => {
      const apiImage = apiImages.find((img) => img.imageSeq === image.imageSeq);
      if (apiImage) {
        image.imageBase64 = "";
        image.imageName = apiImage.imageName;
        image.imagePath = apiImage.imagePath;
      } else {
        image.imageBase64 = "";
        image.imageName = "";
        image.imagePath = "";
      }
    });
  }...
```

**Chunk 메타데이터**
- 🆔 **ID**: `c945496bba18`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-deleteimage"></a>🔧 `deleteImage`

![복잡도](https://img.shields.io/badge/복잡도-10-orange)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 10 |
| 📊 토큰 수 | 27 |
| 📍 라인 범위 | 77-86 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
  function deleteImage(imageSeq) {
    const imageToDelete = uploadedImagesExtend.value.requests.find(
      (image) => image.imageSeq === imageSeq
    );
    if (imageToDelete) {
      imageToDelete.imageBase64 = "";
      imageToDelete.imageName = "";
      imageToDelete.imagePath = "";
    }
  }...
```

**Chunk 메타데이터**
- 🆔 **ID**: `52c99ae8e23f`
- 🏷️ **태그**: `function, javascript`

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **14개의 chunk**로 구성되어 있으며, **517개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 324 | 62.7% |
| ⚙️ 함수 | 2 | 12.0 | 68 | 13.2% |
| arrow_function | 11 | 3.0 | 125 | 24.2% |

