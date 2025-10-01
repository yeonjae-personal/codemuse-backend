# 📄 extend-utils.ts

> **파일 경로**: `vizier(sample)/vizier-fe-all-develop/src/utils/extend-utils.ts`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 9개  
> **언어**: Javascript / Typescript
---

## 📑 목차

### ⚙️ 함수
- [`DFSDetectCycle`](#function-dfsdetectcycle) - 복잡도: 24
- [`dfs`](#function-dfs) - 복잡도: 10
- [`checkDuplicateEdges`](#function-checkduplicateedges) - 복잡도: 11
- [`checkNumberIsInteger`](#function-checknumberisinteger) - 복잡도: 3


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `@/constants/impactAnalysis` | ⚡ **총 복잡도**: 94 |
| 📊 **총 토큰 수**: 1,061 |  |




## ⚙️ 함수

### <a id="function-dfsdetectcycle"></a>🔧 `DFSDetectCycle`

![복잡도](https://img.shields.io/badge/복잡도-24-red)

> 📖 **함수 설명**  
> // DFS - Deep First Search Cycle Detection

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 24 |
| 📊 토큰 수 | 80 |
| 📍 라인 범위 | 113-144 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
export function DFSDetectCycle(edges) {
  const graph = new Map();

  for (const [u, v] of edges) {
    if (!graph.has(u)) graph.set(u, []);
    graph.get(u).push(v);
  }

  const visited = new Map();

  function dfs(node) {
    if (visited.get(node) === 1) return true;
    if (visited.get(node) === 2) return false;

    visited.set(node, 1);

    for (const neighbor of graph.get(node) || []) {
      if (dfs(neighbor)) return true;
    }

    visited.set(node, 2);
    return false;
  }

  for (const node of graph.keys()) {
    if (!visited.has(node)) {
      if (dfs(node)) return true;
    }
  }

  return false;
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `895a9c3e6845`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-dfs"></a>🔧 `dfs`

![복잡도](https://img.shields.io/badge/복잡도-10-orange)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 10 |
| 📊 토큰 수 | 35 |
| 📍 라인 범위 | 123-135 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
  function dfs(node) {
    if (visited.get(node) === 1) return true;
    if (visited.get(node) === 2) return false;

    visited.set(node, 1);

    for (const neighbor of graph.get(node) || []) {
      if (dfs(neighbor)) return true;
    }

    visited.set(node, 2);
    return false;
  }...
```

**Chunk 메타데이터**
- 🆔 **ID**: `f7864e6bbc76`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-checkduplicateedges"></a>🔧 `checkDuplicateEdges`

![복잡도](https://img.shields.io/badge/복잡도-11-red)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 11 |
| 📊 토큰 수 | 31 |
| 📍 라인 범위 | 146-158 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
export function checkDuplicateEdges(edges) {
  const edgeSet = new Set();

  for (const [u, v] of edges) {
    const edge = `${u},${v}`;
    if (edgeSet.has(edge)) {
      return true;
    }
    edgeSet.add(edge);
  }

  return false;
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `4ff3108287cd`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-checknumberisinteger"></a>🔧 `checkNumberIsInteger`

![복잡도](https://img.shields.io/badge/복잡도-3-green)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 3 |
| 📊 토큰 수 | 7 |
| 📍 라인 범위 | 160-162 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
export function checkNumberIsInteger(number) {
  return Number.isInteger(Number(number));
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `90dba602a4fe`
- 🏷️ **태그**: `function, javascript`

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **9개의 chunk**로 구성되어 있으며, **1,061개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 742 | 69.9% |
| ⚙️ 함수 | 4 | 12.0 | 153 | 14.4% |
| arrow_function | 4 | 11.5 | 166 | 15.6% |

