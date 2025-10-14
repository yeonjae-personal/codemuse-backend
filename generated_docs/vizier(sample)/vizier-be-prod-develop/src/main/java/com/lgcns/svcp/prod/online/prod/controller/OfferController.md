# 📄 OfferController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/controller/OfferController.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`OfferController`](#class-offercontroller) - 복잡도: 43 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `java.util.stream.Collectors` • `org.springframework.beans.factory.annotation.Autowired` • `org.springframework.http.ResponseEntity` • `org.springframework.web.bind.annotation.CrossOrigin` • `org.springframework.web.bind.annotation.GetMapping` 외 13개 | ⚡ **총 복잡도**: 43 |
| 📊 **총 토큰 수**: 720 |  |



## 🏗️ 클래스

### <a id="class-offercontroller"></a>🎯 `OfferController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 43 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 43 || 📍 **라인 범위** | 30-30 |
| 🏷️ **태그** | `class, java, autowired, getmapping, pathvariable, requestparam, param, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class OfferController {
	@Autowired
	private OfferService offerService;

//	@GetMapping(value = "/products")
//	@Operation(summary = "요금제, 부가상품 전체 조회 API", description = "요금제와 부가상품 상세정보 전체 리스트 조회")
//	public ResponseEntity<List<ProdMDto>> retrieveProdM(@RequestParam(required = false) String prodNm, @RequestParam(required = false) String prodKdCd) {
//		ProdMDto request = new ProdMDto();
//		request.setProdNm(prodNm);
//		request.setProdKdCd(prodKdCd);
//		List<ProdMDto> response = offerService.retrieveProdMList(request);
//		return ResponseEntity.ok(response);
//	}
	
	@GetMapping(value = "/priceplans")
	@Operation(summary = "요금제전체 조회 API", description = "요금제 상세정보 전체 리스트 조회")
	public ResponseEntity<List<?>> retrievePricePlans() {
		List<?> response = offerService.retrievePricePlanLis...
```

**Chunk 정보**
- 🆔 **ID**: `c507a571b605`
- 📍 **라인**: 30-30
- 📊 **토큰**: 335
- 🏷️ **태그**: `class, java, autowired, getmapping, pathvariable...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **720개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 385 | 53.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 43.0 | 335 | 46.5% |

