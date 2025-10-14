# 📄 UiCustomValidationController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/UiCustomValidationController.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiCustomValidationController`](#class-uicustomvalidationcontroller) - 복잡도: 50 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.io.IOException` • `java.util.List` • `org.apache.commons.lang3.StringUtils` • `org.springframework.validation.annotation.Validated` • `org.springframework.web.bind.annotation.CrossOrigin` • `org.springframework.web.bind.annotation.GetMapping` 외 18개 | ⚡ **총 복잡도**: 50 |
| 📊 **총 토큰 수**: 638 |  |



## 🏗️ 클래스

### <a id="class-uicustomvalidationcontroller"></a>🎯 `UiCustomValidationController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 50 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 50 || 📍 **라인 범위** | 37-37 |
| 🏷️ **태그** | `class, java, getmapping, postmapping, requestparam, requestbody, valid, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiCustomValidationController {
	
	private final UiCustomValidationService customValidationService;
	
	@GetMapping
	@Operation(summary = "get list custom validation in main", description ="get list custom validation in main")
	public List<CustomValidationMainRespone> showListMain(@RequestParam String item, @RequestParam String type,
																		@RequestParam(required = false) String subType,
																		@RequestParam(required = false) String attrUuid) {
		return customValidationService.getListMain(item, type, subType, attrUuid);
	}
	
	@GetMapping("/admin")
	@Operation(summary = "load data custom validation in admin", description ="load data custom validation in admin")
	public Object showListAdmin(@RequestParam String view, @RequestParam(required = false) String sea...
```

**Chunk 정보**
- 🆔 **ID**: `8630acb9cab3`
- 📍 **라인**: 37-37
- 📊 **토큰**: 287
- 🏷️ **태그**: `class, java, getmapping, postmapping, requestparam...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **638개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 351 | 55.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 50.0 | 287 | 45.0% |

