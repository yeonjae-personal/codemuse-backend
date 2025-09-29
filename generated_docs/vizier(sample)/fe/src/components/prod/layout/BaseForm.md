# 📄 BaseForm.vue

> **파일 경로**: `vizier(sample)/fe/src/components/prod/layout/BaseForm.vue`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 9개  
> **언어**: Javascript / Vue
---




## 📑 목차

### ⚙️ 함수
- [`updateMenuItem`](#function-updatemenuitem) - 복잡도: 12
- [`removeMenuItem`](#function-removemenuitem) - 복잡도: 5
- [`addMenuItem`](#function-addmenuitem) - 복잡도: 10
- [`resetSelectedMenuItem`](#function-resetselectedmenuitem) - 복잡도: 3
- [`constructUpdateMenuItemPayload`](#function-constructupdatemenuitempayload) - 복잡도: 19
- [`constructAddMenuItemPayload`](#function-constructaddmenuitempayload) - 복잡도: 19


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `@vuelidate/validators` • `@vuelidate/core` • `@/store` • `uuid` | ⚡ **총 복잡도**: 68 |
| 📊 **총 토큰 수**: 852 |  |




## ⚙️ 함수

### <a id="function-updatemenuitem"></a>🔧 `updateMenuItem`

![복잡도](https://img.shields.io/badge/복잡도-12-red)

> 📖 **함수 설명**  
> // Methods

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 12 |
| 📊 토큰 수 | 21 |
| 📍 라인 범위 | 153-164 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function updateMenuItem() {
  v$.value.$validate();
  if (isFormInvalid.value) {
    return;
  }
  let payload = constructUpdateMenuItemPayload();
  menuStore.updateMenuItem(payload);
  if (isMenuItemLevelChanged.value) {
    menuStore.removeMenuItem(initialSelectedMenuItem.value);
  }
  resetSelectedMenuItem();
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `4081cd0abfaf`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-removemenuitem"></a>🔧 `removeMenuItem`

![복잡도](https://img.shields.io/badge/복잡도-5-green)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 5 |
| 📊 토큰 수 | 10 |
| 📍 라인 범위 | 166-170 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function removeMenuItem() {
  let payload = constructUpdateMenuItemPayload();
  menuStore.removeMenuItem(payload.menuId);
  resetSelectedMenuItem();
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `95bab8342ab3`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-addmenuitem"></a>🔧 `addMenuItem`

![복잡도](https://img.shields.io/badge/복잡도-10-orange)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 10 |
| 📊 토큰 수 | 18 |
| 📍 라인 범위 | 172-181 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function addMenuItem() {
  v$.value.$validate();
  if (isFormInvalid.value) {
    return;
  }
  let payload = constructAddMenuItemPayload();
  menuStore.addMenuItem(payload);
  resetSelectedMenuItem();
  emit("setDialogStatus", false);
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `bc2ead1978fb`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-resetselectedmenuitem"></a>🔧 `resetSelectedMenuItem`

![복잡도](https://img.shields.io/badge/복잡도-3-green)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 3 |
| 📊 토큰 수 | 5 |
| 📍 라인 범위 | 183-185 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function resetSelectedMenuItem() {
  menuStore.setSelectedMenuItem(null);
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `6d680c70bb20`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-constructupdatemenuitempayload"></a>🔧 `constructUpdateMenuItemPayload`

![복잡도](https://img.shields.io/badge/복잡도-19-red)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 19 |
| 📊 토큰 수 | 41 |
| 📍 라인 범위 | 187-205 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function constructUpdateMenuItemPayload() {
  let payload = {
    menuId: selectedMenuItem.value.menuId,
    menuNm: titleFieldValue.value,
    menuLv: menuLevelFieldValue.value,
    grandParentId:
      menuLevelFieldValue.value === 3
        ? selectedFirstLevelMenuValue.value
        : null,
    parentId:
      menuLevelFieldValue.value === 2
        ? selectedFirstLevelMenuValue.value
        : menuLevelFieldValue.value === 3
          ? selectedSecondLevelMenuValue.value
          : null,
    children: initialSelectedMenuItem.value.children,
  };
  return payload;
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `2f94dccdd0ba`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-constructaddmenuitempayload"></a>🔧 `constructAddMenuItemPayload`

![복잡도](https://img.shields.io/badge/복잡도-19-red)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 19 |
| 📊 토큰 수 | 41 |
| 📍 라인 범위 | 207-225 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function constructAddMenuItemPayload() {
  let payload = {
    menuId: uuidv4(),
    menuNm: titleFieldValue.value,
    menuLv: menuLevelFieldValue.value,
    grandParentId:
      menuLevelFieldValue.value === 3
        ? selectedFirstLevelMenuValue.value
        : null,
    parentId:
      menuLevelFieldValue.value === 2
        ? selectedFirstLevelMenuValue.value
        : menuLevelFieldValue.value === 3
          ? selectedSecondLevelMenuValue.value
          : null,
    children: null,
  };
  return payload;
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `e8b5e9896009`
- 🏷️ **태그**: `function, javascript`

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **9개의 chunk**로 구성되어 있으며, **852개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 716 | 84.0% |
| template | 1 | 0.0 | 0 | 0.0% |
| ⚙️ 함수 | 6 | 11.3 | 136 | 16.0% |
| style | 1 | 0.0 | 0 | 0.0% |

