# 📄 install-pinia.ts

> **파일 경로**: `vizier(sample)/fe/tests/setup/install-pinia.ts`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 2개  
> **언어**: Javascript / Typescript
---

## 📑 목차

### ⚙️ 함수
- [`installPinia`](#function-installpinia) - 복잡도: 11


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `vue` • `@pinia/testing` • `lodash-es/cloneDeep` • `vitest` • `@vue/test-utils` | ⚡ **총 복잡도**: 11 |
| 📊 **총 토큰 수**: 88 |  |




## ⚙️ 함수

### <a id="function-installpinia"></a>🔧 `installPinia`

![복잡도](https://img.shields.io/badge/복잡도-11-red)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 11 |
| 📊 토큰 수 | 28 |
| 📍 라인 범위 | 7-19 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
export function installPinia(options?: Partial<TestingOptions>) {
  const globalConfigBackup = cloneDeep(config.global);

  beforeAll(() => {
    config.global.plugins.unshift(
      createTestingPinia(options) as unknown as Plugin
    );
  });

  afterAll(() => {
    config.global = globalConfigBackup;
  });
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `8fe9b8753ef4`
- 🏷️ **태그**: `function, javascript`

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **2개의 chunk**로 구성되어 있으며, **88개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 60 | 68.2% |
| ⚙️ 함수 | 1 | 11.0 | 28 | 31.8% |

