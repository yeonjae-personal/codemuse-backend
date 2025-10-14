# 📄 UiAttributeManagementController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/UiAttributeManagementController.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiAttributeManagementController`](#class-uiattributemanagementcontroller) - 복잡도: 23 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.springframework.validation.annotation.Validated` • `org.springframework.web.bind.annotation.CrossOrigin` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.PostMapping` • `org.springframework.web.bind.annotation.RequestBody` 외 12개 | ⚡ **총 복잡도**: 23 |
| 📊 **총 토큰 수**: 224 |  |



## 🏗️ 클래스

### <a id="class-uiattributemanagementcontroller"></a>🎯 `UiAttributeManagementController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 23 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 23 || 📍 **라인 범위** | 32-32 |
| 🏷️ **태그** | `class, java, getmapping, postmapping, requestparam, requestbody, valid, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiAttributeManagementController {
	
	private final UiAttributeManagementService attributeService;

	@GetMapping
	@Operation(summary = "show list attribute", description = "show list attribute")
	public List<AttributeViewDto> getData() {
		return attributeService.getData();
	}
	
	@GetMapping("/detail")
	@Operation(summary = "show attribute detail", description = "show attribute detail")
	public AttributeDetailDto getAttributeDetail(@RequestParam String code) {
		return attributeService.getDetail(code);
	}
	
	@GetMapping("/item")
	@Operation(summary = "get upper/lower items", description = "get upper/lower items")
	public AttributeUpperLowerItemsDto getItems(@RequestParam String largeItem) {
		return attributeService.getUpperOrLowerItems(largeItem);
	}
	
	@PostMapping
	@Operatio...
```

**Chunk 정보**
- 🆔 **ID**: `1d646d444110`
- 📍 **라인**: 32-32
- 📊 **토큰**: 85
- 🏷️ **태그**: `class, java, getmapping, postmapping, requestparam...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **224개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 139 | 62.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 23.0 | 85 | 37.9% |

