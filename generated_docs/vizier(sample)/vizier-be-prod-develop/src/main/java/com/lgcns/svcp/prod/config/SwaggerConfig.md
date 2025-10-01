# 📄 SwaggerConfig.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/config/SwaggerConfig.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`SwaggerConfig`](#class-swaggerconfig) - 복잡도: 9 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `org.springframework.context.annotation.Bean` • `org.springframework.context.annotation.Configuration` • `io.swagger.v3.oas.models.OpenAPI` • `io.swagger.v3.oas.models.info.Info` | ⚡ **총 복잡도**: 9 |
| 📊 **총 토큰 수**: 59 |  |



## 🏗️ 클래스

### <a id="class-swaggerconfig"></a>🎯 `SwaggerConfig`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 9 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 9 || 📍 **라인 범위** | 10-10 |
| 🏷️ **태그** | `class, java, bean, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class SwaggerConfig {
	
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("PROD Service API 명세서")
								.description("상품 서비스 API 명세서")
								.version("v2.0.0"));
	}
}...
```

**Chunk 정보**
- 🆔 **ID**: `6e0a62f72cf7`
- 📍 **라인**: 10-10
- 📊 **토큰**: 24
- 🏷️ **태그**: `class, java, bean, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **59개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 35 | 59.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 9.0 | 24 | 40.7% |

