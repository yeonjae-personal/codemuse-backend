# 📄 webfontloader.js

> **파일 경로**: `vizier(sample)/fe/src/plugins/webfontloader.js`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 2개  
> **언어**: Javascript / Javascript
---

## 📑 목차

### ⚙️ 함수
- [`loadFonts`](#function-loadfonts) - 복잡도: 8


## 📋 파일 개요

| | |
|--|--|
| ⚡ **총 복잡도**: 8 | 📊 **총 토큰 수**: 57 |




## ⚙️ 함수

### <a id="function-loadfonts"></a>🔧 `loadFonts`

![복잡도](https://img.shields.io/badge/복잡도-8-orange)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 8 |
| 📊 토큰 수 | 24 |
| 📍 라인 범위 | 7-15 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
export async function loadFonts () {
  const webFontLoader = await import(/* webpackChunkName: "webfontloader" */'webfontloader')

  webFontLoader.load({
    google: {
      families: ['Noto Sans KR:400,700']
    },
  })
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `919072d69379`
- 🏷️ **태그**: `function, javascript`

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **2개의 chunk**로 구성되어 있으며, **57개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 33 | 57.9% |
| ⚙️ 함수 | 1 | 8.0 | 24 | 42.1% |

