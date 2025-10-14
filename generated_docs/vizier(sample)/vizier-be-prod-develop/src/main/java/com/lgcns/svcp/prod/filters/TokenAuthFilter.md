# 📄 TokenAuthFilter.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/filters/TokenAuthFilter.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`TokenAuthFilter`](#class-tokenauthfilter) - 복잡도: 20

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.auth.service.AuthService` • `com.lgcns.svcp.prod.properties.AuthProperties` • `com.lgcns.svcp.prod.util.CookieUtil` • `com.lgcns.svcp.prod.util.StringUtilCustom` • `jakarta.servlet.*` • `jakarta.servlet.http.HttpServletRequest` 외 5개 | ⚡ **총 복잡도**: 20 |
| 📊 **총 토큰 수**: 152 |  |



## 🏗️ 클래스

### <a id="class-tokenauthfilter"></a>🎯 `TokenAuthFilter`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `OncePerRequestFilter` |
| ⚡ 복잡도 | 20 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 20 || 📍 **라인 범위** | 18-18 |
| 🏗️ **상속** | `OncePerRequestFilter` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class TokenAuthFilter extends OncePerRequestFilter {

    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (StringUtilCustom.isIncludes(uri, AuthProperties.ALLOW_URIS) || StringUtilCustom.isContains(uri, AuthProperties.ALLOW_INCLUDED_URIS)|| StringUtilCustom.isStartWith(uri, AuthProperties.WEBSOCKET_URIS)) {
            filterChain.doFilter(request, response);
        }else {
            String aCookieToken = CookieUtil.getCookieValue(request, "aToken");
            String aToken = request.getHeader("A-TOKEN");
            if(!aToken.equals(aCookieToken)){
          ...
```

**Chunk 정보**
- 🆔 **ID**: `d78e0197045e`
- 📍 **라인**: 18-18
- 📊 **토큰**: 63
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **152개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 89 | 58.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 20.0 | 63 | 41.4% |

