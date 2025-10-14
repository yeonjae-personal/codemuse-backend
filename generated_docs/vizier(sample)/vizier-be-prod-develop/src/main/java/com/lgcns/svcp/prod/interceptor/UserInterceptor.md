# 📄 UserInterceptor.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/interceptor/UserInterceptor.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UserInterceptor`](#class-userinterceptor) - 복잡도: 14

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `org.springframework.stereotype.Component` • `org.springframework.web.servlet.HandlerInterceptor` • `com.lgcns.svcp.prod.context.UserContext` • `jakarta.servlet.http.HttpServletRequest` • `jakarta.servlet.http.HttpServletResponse` | ⚡ **총 복잡도**: 14 |
| 📊 **총 토큰 수**: 191 |  |



## 🏗️ 클래스

### <a id="class-userinterceptor"></a>🎯 `UserInterceptor`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 14 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 14 || 📍 **라인 범위** | 15-15 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // Retrieve the username from the header (temporary solution)
        String user = request.getHeader("X-User-Id");

        // TODO: Replace this with token-based extraction logic
        // Example:
        // String token = request.getHeader("Authorization");
        // String user = TokenUtil.extractUsername(token);

        UserContext.setCurrentUser(user); // Store user in ThreadLocal

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Except...
```

**Chunk 정보**
- 🆔 **ID**: `def9fbab7437`
- 📍 **라인**: 15-15
- 📊 **토큰**: 83
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **191개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 108 | 56.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 14.0 | 83 | 43.5% |

