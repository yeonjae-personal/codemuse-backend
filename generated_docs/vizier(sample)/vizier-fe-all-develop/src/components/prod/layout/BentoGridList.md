# 📄 BentoGridList.vue

> **파일 경로**: `vizier(sample)/vizier-fe-all-develop/src/components/prod/layout/BentoGridList.vue`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 6개  
> **언어**: Javascript / Vue
---




## 📑 목차

### ⚙️ 함수
- [`syncMousePosition`](#function-syncmouseposition) - 복잡도: 5
- [`dragEnd`](#function-dragend) - 복잡도: 7


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `vue-i18n` • `@/composables/useDragUserPocket` • `lodash-es/throttle` • `./WidgetItem.vue` • `vue-draggable-plus` | ⚡ **총 복잡도**: 24 |
| 📊 **총 토큰 수**: 555 |  |




## ⚙️ 함수

### <a id="function-syncmouseposition"></a>🔧 `syncMousePosition`

![복잡도](https://img.shields.io/badge/복잡도-5-green)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 5 |
| 📊 토큰 수 | 11 |
| 📍 라인 범위 | 60-64 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function syncMousePosition(event) {
  event.preventDefault();
  mouseAt.x = event.clientX;
  mouseAt.y = event.clientY;
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `4c430e9e516f`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-dragend"></a>🔧 `dragEnd`

![복잡도](https://img.shields.io/badge/복잡도-7-orange)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 7 |
| 📊 토큰 수 | 22 |
| 📍 라인 범위 | 95-101 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function dragEnd() {
  if (dragItem.value.disabled) return;
  const dragEndEvent = new CustomEvent("drag-ended", {
    detail: { mouseAt, dragItem: dragItem.value, dropId },
  });
  document.dispatchEvent(dragEndEvent);
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `6bcc850dea1f`
- 🏷️ **태그**: `function, javascript`

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **6개의 chunk**로 구성되어 있으며, **555개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 484 | 87.2% |
| template | 1 | 0.0 | 0 | 0.0% |
| ⚙️ 함수 | 2 | 6.0 | 33 | 5.9% |
| arrow_function | 1 | 12.0 | 38 | 6.8% |
| style | 1 | 0.0 | 0 | 0.0% |

