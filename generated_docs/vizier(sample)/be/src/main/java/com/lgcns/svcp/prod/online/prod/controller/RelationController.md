# 📄 RelationController.java

> **파일 경로**: `vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/online/prod/controller/RelationController.java`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`RelationController`](#class-relationcontroller) - 복잡도: 14 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.springframework.beans.factory.annotation.Autowired` • `org.springframework.http.ResponseEntity` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.ModelAttribute` • `org.springframework.web.bind.annotation.PathVariable` 외 10개 | ⚡ **총 복잡도**: 14 |
| 📊 **총 토큰 수**: 326 |  |



## 🏗️ 클래스

### <a id="class-relationcontroller"></a>🎯 `RelationController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 14 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 14 || 📍 **라인 범위** | 27-27 |
| 🏷️ **태그** | `class, java, autowired, getmapping, param, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class RelationController {
	@Autowired
	private RelationService relationService;
	
	@GetMapping(value = "/offer-relations")
	@Operation(summary = "종속관계 전체 조회 API", description = "상품종속관계 상세정보 전체 리스트 조회")
	public ResponseEntity<List<ProdDpndRelDDto>> retrieveAllProdDpndRelDList(@ModelAttribute ProdDpndRelDDto prodDpndRelDDto) {
		ProdDpndRelDDto request = new ProdDpndRelDDto();
		request.setBaseProdItemCd(prodDpndRelDDto.getBaseProdItemCd());
		request.setTrgtProdItemCd(prodDpndRelDDto.getTrgtProdItemCd());
		request.setDpndRelDivsCd(prodDpndRelDDto.getDpndRelDivsCd());
		List<ProdDpndRelDDto> response = relationService.retrieveAllProdDpndRelDList(request);

		return ResponseEntity.ok(response);	
	}
	
//	@GetMapping(value = "/offercostruct-relations")
//	@Operation(summary = "구성관계 전체 ...
```

**Chunk 정보**
- 🆔 **ID**: `ba717c954621`
- 📍 **라인**: 27-27
- 📊 **토큰**: 141
- 🏷️ **태그**: `class, java, autowired, getmapping, param...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **326개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 185 | 56.7% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 14.0 | 141 | 43.3% |

