# 📄 SidebarMenu.vue

> **파일 경로**: `vizier(sample)/vizier-fe-all-develop/src/components/prod/layout/SidebarMenu.vue`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 14개  
> **언어**: Javascript / Vue
---




## 📑 목차

### ⚙️ 함수
- [`isActive`](#function-isactive) - 복잡도: 3
- [`handleHover`](#function-handlehover) - 복잡도: 29
- [`handleLeave`](#function-handleleave) - 복잡도: 5
- [`handleIn`](#function-handlein) - 복잡도: 8
- [`handleClick`](#function-handleclick) - 복잡도: 17
- [`handleHideSubmenu`](#function-handlehidesubmenu) - 복잡도: 3
- [`checkMenu`](#function-checkmenu) - 복잡도: 17
- [`initMenu`](#function-initmenu) - 복잡도: 17


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `@vueuse/core` • `@/interfaces/prod/menu` • `@/store` • `@/utils/config-path` • `@/enums/redirect` • `vue-router` | ⚡ **총 복잡도**: 119 |
| 📊 **총 토큰 수**: 1,263 |  |




## ⚙️ 함수

### <a id="function-isactive"></a>🔧 `isActive`

![복잡도](https://img.shields.io/badge/복잡도-3-green)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 3 |
| 📊 토큰 수 | 9 |
| 📍 라인 범위 | 54-56 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function isActive(item: any) {
  return item.menuId === activeItem.value;
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `ea7cae5601b0`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-handlehover"></a>🔧 `handleHover`

![복잡도](https://img.shields.io/badge/복잡도-29-red)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 29 |
| 📊 토큰 수 | 81 |
| 📍 라인 범위 | 58-86 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function handleHover(item: any, id: any) {
  setTimeout(() => {
    menuTrue.value =
      item.children && item.children.length > 0 ? item.children : [];
  }, 300);
  if (activeItem.value != item.menuId) {
    handleLeave();
    activeItem.value = item.menuId;
    if (item.children && item.children.length > 0) {
      setTimeout(() => {
        idParentMenu.value = item.menuId;
        nameParentMenu.value = item.menuNm;
      }, 300);
      handleIn();
    } else {
      menuTrue.value = [];
      handleLeave();
    }
    setTimeout(() => {
      if (id) {
        const subBar = document.getElementById(id);
        if (subBar) {
          hoverItemOffset.value = subBar.offsetTop;
          handleChangePosition();
        }
      }
    }, 350);
  }
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `1b55aa001420`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-handleleave"></a>🔧 `handleLeave`

![복잡도](https://img.shields.io/badge/복잡도-5-green)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 5 |
| 📊 토큰 수 | 13 |
| 📍 라인 범위 | 88-92 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function handleLeave() {
  activeItem.value = null;
  subMenuOpacity.value = "0";
  translateX.value = "-300px";
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `4e3ebcb5e5d0`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-handlein"></a>🔧 `handleIn`

![복잡도](https://img.shields.io/badge/복잡도-8-orange)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 8 |
| 📊 토큰 수 | 19 |
| 📍 라인 범위 | 94-101 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function handleIn() {
  if (leaveOtherMenu.value) {
    setTimeout(() => {
      subMenuOpacity.value = "1";
      translateX.value = "0px";
    }, 300);
  }
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `2fd64bcd4e3b`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-handleclick"></a>🔧 `handleClick`

![복잡도](https://img.shields.io/badge/복잡도-17-red)

> 📖 **함수 설명**  
> // methods

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 17 |
| 📊 토큰 수 | 35 |
| 📍 라인 범위 | 104-120 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function handleClick(item: any) {
  if (item.menuNm === "Dashboard") {
    if (addTab) {
      addTab({
        ...item,
        rawName: item.menuNm,
      });
    }
    item.path = "/functions/product-platform";
  }
  idParentMenu.value = item.menuId;
  nameParentMenu.value = item.menuNm;
  if (!item.children) {
    router.push(item.path);
    menuStore.updateSelectedMenuDetail(item);
  }
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `67a716e99238`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-handlehidesubmenu"></a>🔧 `handleHideSubmenu`

![복잡도](https://img.shields.io/badge/복잡도-3-green)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 3 |
| 📊 토큰 수 | 8 |
| 📍 라인 범위 | 122-124 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function handleHideSubmenu(idParent: any) {
  activeItem.value = idParent;
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `b98b44537602`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-checkmenu"></a>🔧 `checkMenu`

![복잡도](https://img.shields.io/badge/복잡도-17-red)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 17 |
| 📊 토큰 수 | 61 |
| 📍 라인 범위 | 126-144 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function checkMenu(menu: any, path: any, root: any, parent?: any) {
  if (menu.children?.length) {
    for (const item of menu.children) {
      if (!checkMenu(item, path, root, menu)) {
        return false;
      }
    }
  } else if (path === configPath(menu)) {
    menuStore.setActiveMenu(menu);
    // activeItem.value = root.menuId;
    // menuTrue.value = root.children;
    idParentMenu.value = root.menuId;
    menuStore.setOpenId(parent ? [parent.menuId] : [root.menuId]);
    menuStore.setActiveMenuTree(root.children);
    menuStore.setParentId(root.menuId);
    return false;
  }
  return true;
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `9c0d87e2099a`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-initmenu"></a>🔧 `initMenu`

![복잡도](https://img.shields.io/badge/복잡도-17-red)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 17 |
| 📊 토큰 수 | 50 |
| 📍 라인 범위 | 146-162 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function initMenu() {
  if (menuTree.value.length > 0) {
    const path = router.currentRoute.value.fullPath;
    for (const item of menuTree.value) {
      if (configPath(item) === path) {
        activeItem.value = item.menuId;
        break;
      } else if (item.children?.length) {
        for (const child of item.children) {
          if (!checkMenu(child, path, item)) {
            break;
          }
        }
      }
    }
  }
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `a9bb4dcb31e1`
- 🏷️ **태그**: `function, javascript`

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **14개의 chunk**로 구성되어 있으며, **1,263개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 904 | 71.6% |
| template | 1 | 0.0 | 0 | 0.0% |
| ⚙️ 함수 | 8 | 12.4 | 276 | 21.9% |
| arrow_function | 3 | 6.7 | 83 | 6.6% |
| style | 1 | 0.0 | 0 | 0.0% |

