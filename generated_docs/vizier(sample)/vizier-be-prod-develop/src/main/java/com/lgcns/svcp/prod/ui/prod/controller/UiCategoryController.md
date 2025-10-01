# 📄 UiCategoryController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/UiCategoryController.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiCategoryController`](#class-uicategorycontroller) - 복잡도: 90 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.springframework.web.bind.annotation.CrossOrigin` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.PostMapping` • `org.springframework.web.bind.annotation.RequestBody` • `org.springframework.web.bind.annotation.RequestMapping` 외 17개 | ⚡ **총 복잡도**: 90 |
| 📊 **총 토큰 수**: 874 |  |



## 🏗️ 클래스

### <a id="class-uicategorycontroller"></a>🎯 `UiCategoryController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 90 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 90 || 📍 **라인 범위** | 35-35 |
| 🏷️ **태그** | `class, java, getmapping, postmapping, requestparam, requestbody, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiCategoryController {

	private final UiCategoryService uiCategoryService;

	@GetMapping(value = "/products")
	@Operation(summary = "(화면) 리프노드 카테고리 상품 조회", description = "리프노드 카테고리에 연결된 상품정보 리스트 조회")
	public List<OfferOfLeafNodeDto> retrieveProdMList(@RequestParam String ctgrNodeUuid,
			@RequestParam String ctgrTabUuid) {

		CatgMDto request = new CatgMDto();
		request.setCtgrNodeUuid(ctgrNodeUuid);
		request.setCtgrTabUuid(ctgrTabUuid);
		return uiCategoryService.retrieveProdMList(request);
	}

	@GetMapping(value = "/tree")
	@Operation(summary = "(화면) 카테고리 트리 전체 조회 API", description = "카테고리 트리와 카테고리에 포함된 상품 개수 조회 API")
	public List<CategoryTreeDto> retrieveCategoryTreeList(@RequestParam String ctgrTabUuid) {
		return uiCategoryService.retrieveCategoryTreeWithOfferCounts(ctg...
```

**Chunk 정보**
- 🆔 **ID**: `1c185092aeb1`
- 📍 **라인**: 35-35
- 📊 **토큰**: 407
- 🏷️ **태그**: `class, java, getmapping, postmapping, requestparam...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **874개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 467 | 53.4% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 90.0 | 407 | 46.6% |

