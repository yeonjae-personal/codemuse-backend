# 📄 RestTemplateConfig.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/config/RestTemplateConfig.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`RestTemplateConfig`](#class-resttemplateconfig) - 복잡도: 9 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `org.springframework.boot.web.client.RestTemplateBuilder` • `org.springframework.context.annotation.Bean` • `org.springframework.context.annotation.Configuration` • `org.springframework.web.client.RestTemplate` • `java.time.Duration` | ⚡ **총 복잡도**: 9 |
| 📊 **총 토큰 수**: 59 |  |



## 🏗️ 클래스

### <a id="class-resttemplateconfig"></a>🎯 `RestTemplateConfig`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 9 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 9 || 📍 **라인 범위** | 11-11 |
| 🏷️ **태그** | `class, java, bean, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(5)) // 연결 타임아웃
                .setReadTimeout(Duration.ofSeconds(10))   // 응답 타임아웃
                .build();
    };

}...
```

**Chunk 정보**
- 🆔 **ID**: `7468c37db82b`
- 📍 **라인**: 11-11
- 📊 **토큰**: 23
- 🏷️ **태그**: `class, java, bean, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **59개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 36 | 61.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 9.0 | 23 | 39.0% |

