# 📄 fetch-i18n.ts

> **파일 경로**: `vizier(sample)/vizier-fe-all-develop/src/utils/fetch-i18n.ts`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 4개  
> **언어**: Javascript / Typescript
---

## 📑 목차

### ⚙️ 함수
- [`fetchAndSaveTranslations`](#function-fetchandsavetranslations) - 복잡도: 26
- [`applyTranslations`](#function-applytranslations) - 복잡도: 5
- [`updateLabelI18n`](#function-updatelabeli18n) - 복잡도: 11


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `@/api/prod/labelApi` • `@/i18n` | ⚡ **총 복잡도**: 42 |
| 📊 **총 토큰 수**: 503 |  |




## ⚙️ 함수

### <a id="function-fetchandsavetranslations"></a>🔧 `fetchAndSaveTranslations`

![복잡도](https://img.shields.io/badge/복잡도-26-red)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 26 |
| 📊 토큰 수 | 71 |
| 📍 라인 범위 | 54-84 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
export async function fetchAndSaveTranslations() {
  try {
    const cachedTranslations = localStorage.getItem("translations");
    let translations: MultiLanguageTranslations = {};
    if (cachedTranslations) {
      translations = JSON.parse(cachedTranslations);
      applyTranslations(translations);
    }

    // console.time("fetchAndSaveTranslations");
    const response: any = await getAllLanguages();
    // console.timeEnd("fetchAndSaveTranslations");
    const apiTranslations: MultiLanguageTranslations = convert(
      response.data || []
    );

    if (!cachedTranslations) {
      applyTranslations(apiTranslations);
      localStorage.setItem("translations", JSON.stringify(apiTranslations));
      return;
    }

    if (!areTranslationsEqual(translations, apiTranslations)) {
    ...
```

**Chunk 메타데이터**
- 🆔 **ID**: `fbe9de607e3a`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-applytranslations"></a>🔧 `applyTranslations`

![복잡도](https://img.shields.io/badge/복잡도-5-green)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 5 |
| 📊 토큰 수 | 13 |
| 📍 라인 범위 | 86-90 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function applyTranslations(translations: MultiLanguageTranslations) {
  Object.keys(translations).forEach((langCode) => {
    i18n.global.setLocaleMessage(langCode, translations[langCode as string]);
  });
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `06cfd3936170`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-updatelabeli18n"></a>🔧 `updateLabelI18n`

![복잡도](https://img.shields.io/badge/복잡도-11-red)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 11 |
| 📊 토큰 수 | 36 |
| 📍 라인 범위 | 92-102 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
export function updateLabelI18n(data, labelId = "") {
  const translations: MultiLanguageTranslations = convert(data || []);
  Object.keys(translations).forEach((langCode) => {
    const currentMessage = i18n.global.getLocaleMessage(langCode);
    if (labelId) delete currentMessage[labelId as string];
    i18n.global.setLocaleMessage(langCode, {
      ...currentMessage,
      ...translations[langCode as string],
    });
  });
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `eec6373a2345`
- 🏷️ **태그**: `function, javascript`

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **4개의 chunk**로 구성되어 있으며, **503개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 383 | 76.1% |
| ⚙️ 함수 | 3 | 14.0 | 120 | 23.9% |

