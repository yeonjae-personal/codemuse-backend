# 📄 BentoGridItem.vue

> **파일 경로**: `vizier(sample)/vizier-fe-all-develop/src/components/prod/layout/BentoGridItem.vue`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 9개  
> **언어**: Javascript / Vue
---




## 📑 목차

### ⚙️ 함수
- [`openEditDialog`](#function-openeditdialog) - 복잡도: 7


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `../dashboard/UploadUserImage.vue` • `../icons/DetailIcon.vue` • `../dashboard/recently-worked/RecentlyWorkedItem.vue` • `../dashboard/UploadUserImageEditMode.vue` • `../icons/UserImageIcon.vue` • `../icons/RecentlyWorkedIcon.vue` 외 10개 | ⚡ **총 복잡도**: 32 |
| 📊 **총 토큰 수**: 902 |  |




## ⚙️ 함수

### <a id="function-openeditdialog"></a>🔧 `openEditDialog`

![복잡도](https://img.shields.io/badge/복잡도-7-orange)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 7 |
| 📊 토큰 수 | 18 |
| 📍 라인 범위 | 63-69 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function openEditDialog() {
  isViewDialogOpen.value = false;
  dialogMode.value = "edit";
  setTimeout(() => {
    isEditDialogOpen.value = true;
  }, 200);
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `f1456f4e629d`
- 🏷️ **태그**: `function, javascript`

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **9개의 chunk**로 구성되어 있으며, **902개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 804 | 89.1% |
| template | 1 | 0.0 | 0 | 0.0% |
| ⚙️ 함수 | 1 | 7.0 | 18 | 2.0% |
| arrow_function | 5 | 5.0 | 80 | 8.9% |
| style | 1 | 0.0 | 0 | 0.0% |

