# 📄 UIOfferController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/UIOfferController.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UIOfferController`](#class-uioffercontroller) - 복잡도: 82 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.text.ParseException` • `java.util.List` • `java.util.Map` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.ModelAttribute` • `org.springframework.web.bind.annotation.PostMapping` 외 21개 | ⚡ **총 복잡도**: 82 |
| 📊 **총 토큰 수**: 791 |  |



## 🏗️ 클래스

### <a id="class-uioffercontroller"></a>🎯 `UIOfferController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 82 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 82 || 📍 **라인 범위** | 38-38 |
| 🏷️ **태그** | `class, java, getmapping, postmapping, putmapping, requestparam, requestbody, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UIOfferController {

	private final UiOfferService uiOfferService;

	@GetMapping(value = "/structure")
	@Operation(summary = "(화면) 상품구조 base 또는 trgt으로 조회 API", description = "상품구조 리스트 조회")
	public List<ProdStruDWithItemsMDto> retrieveProdStruDWithItemsMByBaseAndTrgt(
			@RequestParam(required = false) String baseUuid, @RequestParam(required = false) String trgtUuid) {

		ProdStruDWithItemsMDto request = new ProdStruDWithItemsMDto();
		request.setBaseUuid(baseUuid);
		request.setTrgtUuid(trgtUuid);
		return uiOfferService.retrieveProdStruDWithItemsMListWithPartiotion(request);
	}

	@GetMapping(value = "/offers")
	@Operation(summary = "(화면) 요금제, 부가상품 전체 조회 API", description = "요금제와 부가상품 상세정보 전체 리스트 조회")
	public PageResult<?> getOffers(@RequestParam(value = "page", defaultValue =...
```

**Chunk 정보**
- 🆔 **ID**: `b93a1165ca3a`
- 📍 **라인**: 38-38
- 📊 **토큰**: 362
- 🏷️ **태그**: `class, java, getmapping, postmapping, putmapping...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **791개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 429 | 54.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 82.0 | 362 | 45.8% |

