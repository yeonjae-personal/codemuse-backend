# 📄 ApiLoggingInterceptor.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/interceptor/ApiLoggingInterceptor.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ApiLoggingInterceptor`](#class-apilogginginterceptor) - 복잡도: 59 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.context.RequestContextHolder` • `com.lgcns.svcp.prod.context.UserContext` • `com.lgcns.svcp.prod.dataaccess.CommonDao` • `com.lgcns.svcp.prod.entity.ApiLog` • `com.lgcns.svcp.prod.entity.ChngDataListL` • `jakarta.servlet.http.HttpServletRequest` 외 11개 | ⚡ **총 복잡도**: 59 |
| 📊 **총 토큰 수**: 409 |  |



## 🏗️ 클래스

### <a id="class-apilogginginterceptor"></a>🎯 `ApiLoggingInterceptor`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 59 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 59 || 📍 **라인 범위** | 24-24 |
| 🏷️ **태그** | `class, java, autowired, value, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ApiLoggingInterceptor implements HandlerInterceptor {

    @Value("${external.api.comm}")
    private String commUrl;

    @Autowired
    private CommonDao commonDao;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String url    = request.getRequestURI();
        String method = request.getMethod();
        String query  = request.getQueryString();
        String body   = "";
        String userId = UserContext.getCurrentUser();
        String orgNm  = "";

        if (request instanceof ContentCachingRequestWrapper wrapper) {
            body = new String(wrapper.getContentAsByteArray(), StandardCharse...
```

**Chunk 정보**
- 🆔 **ID**: `9233b43f375c`
- 📍 **라인**: 24-24
- 📊 **토큰**: 186
- 🏷️ **태그**: `class, java, autowired, value, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **409개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 223 | 54.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 59.0 | 186 | 45.5% |

