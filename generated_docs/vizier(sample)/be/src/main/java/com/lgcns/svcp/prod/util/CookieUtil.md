# 📄 CookieUtil.java

> **파일 경로**: `vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/util/CookieUtil.java`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`CookieUtil`](#class-cookieutil) - 복잡도: 10

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `jakarta.servlet.http.Cookie` • `jakarta.servlet.http.HttpServletRequest` • `java.util.Arrays` | ⚡ **총 복잡도**: 10 |
| 📊 **총 토큰 수**: 64 |  |



## 🏗️ 클래스

### <a id="class-cookieutil"></a>🎯 `CookieUtil`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 10 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 10 || 📍 **라인 범위** | 8-8 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class CookieUtil {

    public static   String getCookieValue(HttpServletRequest req, String name) {
        if (req.getCookies() == null) return null;
        return Arrays.stream(req.getCookies())
                .filter(c -> name.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}...
```

**Chunk 정보**
- 🆔 **ID**: `9f2f92a4a705`
- 📍 **라인**: 8-8
- 📊 **토큰**: 28
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **64개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 36 | 56.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 10.0 | 28 | 43.8% |

