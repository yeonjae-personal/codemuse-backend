# 📄 AppConfig.java

> **파일 경로**: `vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/config/AppConfig.java`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`AppConfig`](#class-appconfig) - 복잡도: 33 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.Locale` • `org.springframework.boot.web.servlet.FilterRegistrationBean` • `org.springframework.context.annotation.Bean` • `org.springframework.context.annotation.Configuration` • `org.springframework.context.support.ResourceBundleMessageSource` • `org.springframework.web.filter.CommonsRequestLoggingFilter` 외 3개 | ⚡ **총 복잡도**: 33 |
| 📊 **총 토큰 수**: 187 |  |



## 🏗️ 클래스

### <a id="class-appconfig"></a>🎯 `AppConfig`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 33 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 33 || 📍 **라인 범위** | 16-16 |
| 🏷️ **태그** | `class, java, bean, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class AppConfig {
	
	@Bean
    public FilterRegistrationBean<CORSFilter> corsFilterRegistration() {
        FilterRegistrationBean<CORSFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CORSFilter());
        registrationBean.addUrlPatterns("/*"); // CORS 필터를 적용할 URL 패턴
        registrationBean.setOrder(1); // 숫자가 작을수록 먼저 적용됨
        return registrationBean;
    }
	
	@Bean
    public LocaleResolver localeResolver() {
		MyLocaleResolver localeResolver = new MyLocaleResolver();
		localeResolver.setDefaultLocale(Locale.ENGLISH);
		return localeResolver;
	}
	
	@Bean
    public ResourceBundleMessageSource messageSource() {
        var source = new ResourceBundleMessageSource();
        source.setBasenames("messages/message-common");
        ...
```

**Chunk 정보**
- 🆔 **ID**: `52b2200d8d59`
- 📍 **라인**: 16-16
- 📊 **토큰**: 83
- 🏷️ **태그**: `class, java, bean, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **187개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 104 | 55.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 33.0 | 83 | 44.4% |

