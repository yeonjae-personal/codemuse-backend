# 📄 UiDashboardController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/UiDashboardController.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiDashboardController`](#class-uidashboardcontroller) - 복잡도: 104 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `java.util.Map` • `org.springframework.validation.annotation.Validated` • `org.springframework.web.bind.annotation.CrossOrigin` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.PathVariable` 외 29개 | ⚡ **총 복잡도**: 104 |
| 📊 **총 토큰 수**: 1,028 |  |



## 🏗️ 클래스

### <a id="class-uidashboardcontroller"></a>🎯 `UiDashboardController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 104 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 104 || 📍 **라인 범위** | 48-48 |
| 🏷️ **태그** | `class, java, getmapping, postmapping, pathvariable, requestparam, requestbody, valid, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiDashboardController {

	private final UiDashboardService dashboardService;
	private final UiUserImageService userImageService;
	private final UiCalendarService calendarService;
	private final UiSubscriberTop10Service subscriberService;
	private final UiRecentlyWorkService recentlyWorkService;
	
	@GetMapping
	@Operation(summary = "init data", description = "init data")
	public Map<String, Object> initData() {
		String userId = UserContext.getCurrentUser();
		return dashboardService.initData(userId);
	}

	@GetMapping(value = "/view/{dsbdviewuuid}")
	@Operation(summary = "find view detail", description = "find view detail")
	public DsbdViewDto findDsbdViewById(@PathVariable("dsbdviewuuid") String id) {
		return dashboardService.findViewByUuid(id);
	}
	
	@PostMapping
	@Operation...
```

**Chunk 정보**
- 🆔 **ID**: `f9cd97f860d2`
- 📍 **라인**: 48-48
- 📊 **토큰**: 472
- 🏷️ **태그**: `class, java, getmapping, postmapping, pathvariable...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **1,028개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 556 | 54.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 104.0 | 472 | 45.9% |

