# 📄 SecurityHeaderFilter.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/filters/SecurityHeaderFilter.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`SecurityHeaderFilter`](#class-securityheaderfilter) - 복잡도: 10

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `jakarta.servlet.FilterChain` • `jakarta.servlet.ServletException` • `jakarta.servlet.http.HttpServletRequest` • `jakarta.servlet.http.HttpServletResponse` • `org.springframework.stereotype.Component` • `org.springframework.web.filter.OncePerRequestFilter` 외 1개 | ⚡ **총 복잡도**: 10 |
| 📊 **총 토큰 수**: 83 |  |



## 🏗️ 클래스

### <a id="class-securityheaderfilter"></a>🎯 `SecurityHeaderFilter`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `OncePerRequestFilter` |
| ⚡ 복잡도 | 10 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 10 || 📍 **라인 범위** | 13-13 |
| 🏗️ **상속** | `OncePerRequestFilter` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class SecurityHeaderFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    response.setHeader("X-Content-Type-Options", "nosniff");
    response.setHeader("X-Frame-Options", "DENY");
    response.setHeader("X-XSS-Protection", "1; mode=block");
    response.setHeader("Content-Security-Policy", "default-src 'self'");

    filterChain.doFilter(request, response);
  }
}...
```

**Chunk 정보**
- 🆔 **ID**: `2226eff7e7eb`
- 📍 **라인**: 13-13
- 📊 **토큰**: 33
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **83개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 50 | 60.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 10.0 | 33 | 39.8% |

