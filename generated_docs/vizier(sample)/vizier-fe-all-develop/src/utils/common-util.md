# 📄 common-util.ts

> **파일 경로**: `vizier(sample)/vizier-fe-all-develop/src/utils/common-util.ts`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Javascript / Typescript
---

## 📑 목차

### 🏗️ 클래스
- [`CommonUtil`](#class-commonutil) - 복잡도: 7


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `@/i18n` | ⚡ **총 복잡도**: 12 |
| 📊 **총 토큰 수**: 125 |  |



## 🏗️ 클래스

### <a id="class-commonutil"></a>🎯 `CommonUtil`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 7 |



<details>
<summary>🔍 코드 미리보기</summary>

```javascript
export class CommonUtil {
  public static readonly STRING_EMPTY = "";

  public static useTranslatedMessage = () => {
    const { translateMessage, ...rest } = useI18n();
    return { translateMessage, ...rest };
  };
}...
```

**Chunk 정보**
- 🆔 **ID**: `e03acaf03ee5`
- 📍 **라인**: 3-10
- 📊 **토큰**: 31
- 🏷️ **태그**: `class, javascript`

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **125개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 68 | 54.4% |
| arrow_function | 1 | 5.0 | 26 | 20.8% |
| 🏗️ 클래스 | 1 | 7.0 | 31 | 24.8% |

