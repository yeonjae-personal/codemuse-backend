# 📄 BentoGrid.vue

> **파일 경로**: `vizier(sample)/vizier-fe-all-develop/src/components/prod/layout/BentoGrid.vue`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 11개  
> **언어**: Javascript / Vue
---




## 📑 목차

### ⚙️ 함수
- [`filterLoading`](#function-filterloading) - 복잡도: 9
- [`handleDragMoving`](#function-handledragmoving) - 복잡도: 11
- [`getGridCell`](#function-getgridcell) - 복잡도: 5
- [`handleDragEnded`](#function-handledragended) - 복잡도: 33
- [`moveEvent`](#function-moveevent) - 복잡도: 14
- [`removeItem`](#function-removeitem) - 복잡도: 11


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `grid-layout-plus` • `@/store` • `vue` | ⚡ **총 복잡도**: 108 |
| 📊 **총 토큰 수**: 1,137 |  |




## ⚙️ 함수

### <a id="function-filterloading"></a>🔧 `filterLoading`

![복잡도](https://img.shields.io/badge/복잡도-9-orange)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 9 |
| 📊 토큰 수 | 42 |
| 📍 라인 범위 | 71-80 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function filterLoading(x, y) {
  const itemIndex = layout.value.findIndex(
    (item) => item.name === "" && item.x === x && item.y === y
  );

  layout.value = layout.value.map((item, index) => {
    return { ...item, loading: index === itemIndex };
  });
  return itemIndex + 1;
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `f772f18afa7b`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-handledragmoving"></a>🔧 `handleDragMoving`

![복잡도](https://img.shields.io/badge/복잡도-11-red)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 11 |
| 📊 토큰 수 | 55 |
| 📍 라인 범위 | 82-94 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function handleDragMoving(event) {
  const { mouseAt, dragItem } = event.detail;
  const parentRect = wrapper.value?.getBoundingClientRect();

  if (!parentRect || !gridLayoutRef.value) return;

  const cellWidth = parentRect.width / 3;
  const cellHeight = parentRect.height / 2;
  const mouseX = mouseAt.x - parentRect.left;
  const mouseY = mouseAt.y - parentRect.top;
  const gridCell = getGridCell(mouseX, mouseY, cellWidth, cellHeight);
  dragItem.i = filterLoading(gridCell.col, gridCell.row);
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `b6b47ea1541f`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-getgridcell"></a>🔧 `getGridCell`

![복잡도](https://img.shields.io/badge/복잡도-5-green)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 5 |
| 📊 토큰 수 | 24 |
| 📍 라인 범위 | 96-100 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function getGridCell(mouseX, mouseY, cellWidth, cellHeight) {
  const col = Math.floor(mouseX / cellWidth);
  const row = Math.floor(mouseY / cellHeight);
  return { col, row };
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `46caff3238c0`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-handledragended"></a>🔧 `handleDragEnded`

![복잡도](https://img.shields.io/badge/복잡도-33-red)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 33 |
| 📊 토큰 수 | 128 |
| 📍 라인 범위 | 102-134 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
async function handleDragEnded(event) {
  const { mouseAt, dragItem } = event.detail;
  const parentRect = wrapper.value?.getBoundingClientRect();
  if (!parentRect) return;
  const cellWidth = parentRect.width / 3;
  const cellHeight = parentRect.height / 2;
  const mouseX = mouseAt.x - parentRect.left;
  const mouseY = mouseAt.y - parentRect.top;
  const gridCell = getGridCell(mouseX, mouseY, cellWidth, cellHeight);
  setTimeout(() => {
    layout.value = layout.value.map((item) => {
      return { ...item, loading: false };
    });
  }, 300);
  const itemIndex = layout.value.findIndex(
    (item) => item.name === dragItem.name
  );
  const currentItem = layout.value.find(
    (item) => item.x === gridCell.col && item.y === gridCell.row
  );
  if (currentItem.name) {
    showSnackbar("It...
```

**Chunk 메타데이터**
- 🆔 **ID**: `4c9f3f60ef30`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-moveevent"></a>🔧 `moveEvent`

![복잡도](https://img.shields.io/badge/복잡도-14-red)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 14 |
| 📊 토큰 수 | 54 |
| 📍 라인 범위 | 173-186 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function moveEvent(i, newX, newY) {
  const currentItem = layout.value.find((item) => item.i === i);
  const swappedItem = layout.value.find(
    (item) => item.x === newX && item.y === newY
  );
  if (currentItem && swappedItem) {
    const tempX = swappedItem.x;
    const tempY = swappedItem.y;
    swappedItem.x = currentItem.x;
    swappedItem.y = currentItem.y;
    currentItem.x = tempX;
    currentItem.y = tempY;
  }
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `114bd979424e`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-removeitem"></a>🔧 `removeItem`

![복잡도](https://img.shields.io/badge/복잡도-11-red)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 11 |
| 📊 토큰 수 | 35 |
| 📍 라인 범위 | 188-198 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function removeItem(id) {
  const currentItem = layout.value.find((item) => item.i === id);
  if (currentItem) {
    currentItem.code = "";
    currentItem.name = "";
    currentItem.desc = "";
    currentItem.id = "";
  } else {
    showSnackbar("Cannot delete item", "error");
  }
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `dee94121915d`
- 🏷️ **태그**: `function, javascript`

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **11개의 chunk**로 구성되어 있으며, **1,137개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 722 | 63.5% |
| template | 1 | 0.0 | 0 | 0.0% |
| ⚙️ 함수 | 6 | 13.8 | 338 | 29.7% |
| arrow_function | 2 | 12.5 | 77 | 6.8% |
| style | 1 | 0.0 | 0 | 0.0% |

