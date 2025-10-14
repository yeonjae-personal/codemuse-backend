# 📄 WebConfig.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/config/WebConfig.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`WebConfig`](#class-webconfig) - 복잡도: 13 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.interceptor.ApiLoggingInterceptor` • `org.springframework.beans.factory.annotation.Autowired` • `org.springframework.context.annotation.Configuration` • `org.springframework.web.servlet.config.annotation.InterceptorRegistry` • `org.springframework.web.servlet.config.annotation.WebMvcConfigurer` • `com.lgcns.svcp.prod.interceptor.UserInterceptor` | ⚡ **총 복잡도**: 13 |
| 📊 **총 토큰 수**: 71 |  |



## 🏗️ 클래스

### <a id="class-webconfig"></a>🎯 `WebConfig`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 13 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 13 || 📍 **라인 범위** | 12-12 |
| 🏷️ **태그** | `class, java, autowired, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private UserInterceptor userInterceptor;

    @Autowired
    private ApiLoggingInterceptor apiLoggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(userInterceptor);
        registry.addInterceptor(apiLoggingInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/**/*swagger*/**", "/**/*swagger*", "/**/*api-docs*/**");
    }
    
}...
```

**Chunk 정보**
- 🆔 **ID**: `3cb511c1ec28`
- 📍 **라인**: 12-12
- 📊 **토큰**: 28
- 🏷️ **태그**: `class, java, autowired, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **71개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 43 | 60.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 13.0 | 28 | 39.4% |

