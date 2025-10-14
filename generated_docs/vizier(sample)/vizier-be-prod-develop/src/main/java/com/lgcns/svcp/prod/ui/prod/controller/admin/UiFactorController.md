# 📄 UiFactorController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/admin/UiFactorController.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiFactorController`](#class-uifactorcontroller) - 복잡도: 59 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.PutMapping` • `org.springframework.web.bind.annotation.RequestBody` • `org.springframework.web.bind.annotation.RequestMapping` • `org.springframework.web.bind.annotation.RequestParam` 외 9개 | ⚡ **총 복잡도**: 59 |
| 📊 **총 토큰 수**: 509 |  |



## 🏗️ 클래스

### <a id="class-uifactorcontroller"></a>🎯 `UiFactorController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 59 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 59 || 📍 **라인 범위** | 26-26 |
| 🏷️ **태그** | `class, java, getmapping, putmapping, requestparam, requestbody, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiFactorController {

	private final UiFactorService uiFactorService;

	@GetMapping("/search-factor-type")
	@Operation(summary = "Factor-Type 조회", description = "Factor-Type 조회")
	public PageResult<?> searchFactorType(@RequestParam(required = false) String factorTypeCode,
			@RequestParam(required = false) String factorTypeName,
			@RequestParam(defaultValue = "1", required = false) int page,
			@RequestParam(defaultValue = "10", required = false) int size) {

		SearchFactorTypeReqDto reqDto = new SearchFactorTypeReqDto();
		reqDto.setSize(size);
		reqDto.setPage(page);
		reqDto.setFactorTypeCode(factorTypeCode);
		reqDto.setFactorTypeName(factorTypeName);

		return uiFactorService.searchFactorType(reqDto);
	}

	@GetMapping("/search-factor-info")
	@Operation(summary = "Factor ...
```

**Chunk 정보**
- 🆔 **ID**: `666ec4fe8039`
- 📍 **라인**: 26-26
- 📊 **토큰**: 233
- 🏷️ **태그**: `class, java, getmapping, putmapping, requestparam...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **509개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 276 | 54.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 59.0 | 233 | 45.8% |

