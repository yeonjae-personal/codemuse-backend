# 📄 RequestGuardFilter.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/filters/RequestGuardFilter.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`RequestGuardFilter`](#class-requestguardfilter) - 복잡도: 44

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `jakarta.servlet.FilterChain` • `jakarta.servlet.ServletException` • `jakarta.servlet.http.HttpServletRequest` • `jakarta.servlet.http.HttpServletResponse` • `org.springframework.stereotype.Component` • `org.springframework.web.filter.OncePerRequestFilter` 외 4개 | ⚡ **총 복잡도**: 44 |
| 📊 **총 토큰 수**: 397 |  |



## 🏗️ 클래스

### <a id="class-requestguardfilter"></a>🎯 `RequestGuardFilter`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `OncePerRequestFilter` |
| ⚡ 복잡도 | 44 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 44 || 📍 **라인 범위** | 16-16 |
| 🏗️ **상속** | `OncePerRequestFilter` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class RequestGuardFilter extends OncePerRequestFilter {

    private static final Pattern PATH_ALLOW =
            Pattern.compile("^[A-Za-z0-9._~!$&'()*+,;=:@/\\-`\\[\\]{}|^<>\\\\]*$");

    // TODO xss 임시
    private static final Pattern FORBIDDEN =
            Pattern.compile("(?i)(<script|javascript:|data:text/html|onerror=|onload=)");

    // 과도한 URI로 인한 DoS 방지
    private static final int MAX_URI_LENGTH = 4096;
    private static final int MAX_QS_LENGTH  = 4096;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String rawUri = req.getRequestURI();
        String rawQs  = req.getQueryString();

        if ((rawUri != null && rawUri.length() > MAX_URI_...
```

**Chunk 정보**
- 🆔 **ID**: `4e4a2b37f436`
- 📍 **라인**: 16-16
- 📊 **토큰**: 187
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **397개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 210 | 52.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 44.0 | 187 | 47.1% |

