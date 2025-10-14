# 📄 UIRelationController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/UIRelationController.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UIRelationController`](#class-uirelationcontroller) - 복잡도: 24 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.ModelAttribute` • `org.springframework.web.bind.annotation.PostMapping` • `org.springframework.web.bind.annotation.PutMapping` • `org.springframework.web.bind.annotation.RequestBody` • `org.springframework.web.bind.annotation.RequestMapping` 외 9개 | ⚡ **총 복잡도**: 24 |
| 📊 **총 토큰 수**: 231 |  |



## 🏗️ 클래스

### <a id="class-uirelationcontroller"></a>🎯 `UIRelationController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 24 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 24 || 📍 **라인 범위** | 25-25 |
| 🏷️ **태그** | `class, java, getmapping, postmapping, putmapping, requestparam, requestbody, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UIRelationController {
	private final UIRelationService uiRelationService;

	@GetMapping(value = "/search/advanced")
	@Operation(summary = "Retrieve advanced list of relations", description = "Fetches a filtered list of relations based on advanced search criteria.")
	public PageResult<?> retrieveRelationsAdvanced(@ModelAttribute SearchAdvancedReq searchAdvancedReq) {
		return uiRelationService.retrieveRelationsAdvanced(searchAdvancedReq);
	}

	@GetMapping("/create-info")
	@Operation(summary = "Get full realtion creation info")
	public ItemMappingDetailDto retrieveRelationCreateInfo(@RequestParam String itemCode,
			@RequestParam(required = false, defaultValue = "en") String language) {
		return uiRelationService.retrieveRelationCreateInfo(itemCode, language);
	}

	@PostMapping...
```

**Chunk 정보**
- 🆔 **ID**: `da858c9d9680`
- 📍 **라인**: 25-25
- 📊 **토큰**: 94
- 🏷️ **태그**: `class, java, getmapping, postmapping, putmapping...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **231개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 137 | 59.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 24.0 | 94 | 40.7% |

