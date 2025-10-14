# 📄 CORSFilter.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/filters/CORSFilter.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`CORSFilter`](#class-corsfilter) - 복잡도: 26

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.io.IOException` • `java.util.Arrays` • `java.util.List` • `org.springframework.stereotype.Component` • `jakarta.servlet.Filter` • `jakarta.servlet.FilterChain` 외 6개 | ⚡ **총 복잡도**: 26 |
| 📊 **총 토큰 수**: 239 |  |



## 🏗️ 클래스

### <a id="class-corsfilter"></a>🎯 `CORSFilter`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 26 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 26 || 📍 **라인 범위** | 20-20 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class CORSFilter implements Filter{
	public static final List<String> ALLOWED_ORIGINS = Arrays.asList(
			"http://localhost:5173", //로컬,
			"http://localhost:5174", //로컬,
			"dev-service-billing-797985966.ap-northeast-2.elb.amazonaws.com", // SBUI 도메인
			"http://10.63.166.299:5173" // 제 3자 테스트를 위한 도메인
			);

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		// CORS 설정
		String origin = request.getHeader("Origin");
        if (ALLOWED_ORIGINS.contains(origin)) {
			response.setHeader("Access-Control-Allow-Origin", origin);
			response.setHeader("Access-Control-Allow-Methods", "GET, POS...
```

**Chunk 정보**
- 🆔 **ID**: `1434743a356b`
- 📍 **라인**: 20-20
- 📊 **토큰**: 106
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **239개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 133 | 55.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 26.0 | 106 | 44.4% |

