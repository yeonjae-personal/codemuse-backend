# 📄 PatternUtil.java

> **파일 경로**: `vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/util/PatternUtil.java`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`PatternUtil`](#class-patternutil) - 복잡도: 5

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.regex.Pattern` | ⚡ **총 복잡도**: 5 |
| 📊 **총 토큰 수**: 36 |  |



## 🏗️ 클래스

### <a id="class-patternutil"></a>🎯 `PatternUtil`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 5 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 5 || 📍 **라인 범위** | 5-5 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class PatternUtil {
	
	public static boolean matches(Pattern pattern, String string) {
        return pattern.matcher(string).matches();
    }
}...
```

**Chunk 정보**
- 🆔 **ID**: `1229da9345d0`
- 📍 **라인**: 5-5
- 📊 **토큰**: 16
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **36개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 20 | 55.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 5.0 | 16 | 44.4% |

