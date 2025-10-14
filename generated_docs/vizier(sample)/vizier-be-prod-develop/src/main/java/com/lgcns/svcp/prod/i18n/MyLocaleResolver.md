# 📄 MyLocaleResolver.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/i18n/MyLocaleResolver.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`MyLocaleResolver`](#class-mylocaleresolver) - 복잡도: 29

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.Locale` • `org.springframework.web.servlet.i18n.AbstractLocaleResolver` • `jakarta.servlet.http.HttpServletRequest` • `jakarta.servlet.http.HttpServletResponse` | ⚡ **총 복잡도**: 29 |
| 📊 **총 토큰 수**: 228 |  |



## 🏗️ 클래스

### <a id="class-mylocaleresolver"></a>🎯 `MyLocaleResolver`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `AbstractLocaleResolver` |
| ⚡ 복잡도 | 29 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 29 || 📍 **라인 범위** | 10-10 |
| 🏗️ **상속** | `AbstractLocaleResolver` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class MyLocaleResolver extends AbstractLocaleResolver {
	
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		Locale defaultLocale = getDefaultLocale();
		if (defaultLocale != null && request.getHeader("X-Language") == null && request.getParameter("language") == null) {
			return defaultLocale;
		}
		String language = request.getHeader("X-Language");
		String param = request.getParameter("language");
		Locale locale = getLocale(language != null ? language : param);
		if (locale == null) {
			return defaultLocale;
		}
		return locale;
	}

	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		throw new UnsupportedOperationException(
				"Cannot change HTTP X-Language header - use a different locale resolution...
```

**Chunk 정보**
- 🆔 **ID**: `f22bb887e465`
- 📍 **라인**: 10-10
- 📊 **토큰**: 109
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **228개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 119 | 52.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 29.0 | 109 | 47.8% |

