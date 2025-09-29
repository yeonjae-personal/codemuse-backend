# 📄 MenuManager.vue

> **파일 경로**: `vizier(sample)/fe/src/pages/prod/functions/MenuManager.vue`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 6개  
> **언어**: Javascript / Vue
---




## 📑 목차

### ⚙️ 함수
- [`setSelectedMenuItem`](#function-setselectedmenuitem) - 복잡도: 5
- [`addMenuListItem`](#function-addmenulistitem) - 복잡도: 3
- [`setDialogStatus`](#function-setdialogstatus) - 복잡도: 3


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `@/components/prod/layout/BaseForm.vue` • `vuetify/labs/VTreeview` • `@/store` | ⚡ **총 복잡도**: 11 |
| 📊 **총 토큰 수**: 180 |  |




## ⚙️ 함수

### <a id="function-setselectedmenuitem"></a>🔧 `setSelectedMenuItem`

![복잡도](https://img.shields.io/badge/복잡도-5-green)

> 📖 **함수 설명**  
> // method

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 5 |
| 📊 토큰 수 | 9 |
| 📍 라인 범위 | 13-17 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
async function setSelectedMenuItem(elem) {
  menuStore.setIsShowDetailLayout(true);
  await nextTick();
  menuStore.setSelectedMenuItem(elem);
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `ff08b0ec9cd1`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-addmenulistitem"></a>🔧 `addMenuListItem`

![복잡도](https://img.shields.io/badge/복잡도-3-green)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 3 |
| 📊 토큰 수 | 5 |
| 📍 라인 범위 | 19-21 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function addMenuListItem() {
  baseFormRef.value.addMenuItem();
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `8b1a90d0f0fd`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-setdialogstatus"></a>🔧 `setDialogStatus`

![복잡도](https://img.shields.io/badge/복잡도-3-green)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 3 |
| 📊 토큰 수 | 7 |
| 📍 라인 범위 | 23-25 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function setDialogStatus(dialogStatus) {
  dialog.value = dialogStatus;
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `72c8a4d58dee`
- 🏷️ **태그**: `function, javascript`

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **6개의 chunk**로 구성되어 있으며, **180개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 159 | 88.3% |
| template | 1 | 0.0 | 0 | 0.0% |
| ⚙️ 함수 | 3 | 3.7 | 21 | 11.7% |
| style | 1 | 0.0 | 0 | 0.0% |

