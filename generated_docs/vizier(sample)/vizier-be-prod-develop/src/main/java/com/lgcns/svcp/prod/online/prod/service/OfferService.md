# 📄 OfferService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/service/OfferService.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`OfferService`](#class-offerservice) - 복잡도: 111 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.HashMap` • `java.util.List` • `java.util.Map` • `java.util.stream.Collectors` • `org.springframework.beans.factory.annotation.Autowired` • `org.springframework.stereotype.Component` 외 10개 | ⚡ **총 복잡도**: 111 |
| 📊 **총 토큰 수**: 1,125 |  |



## 🏗️ 클래스

### <a id="class-offerservice"></a>🎯 `OfferService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 111 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 111 || 📍 **라인 범위** | 24-24 |
| 🏷️ **태그** | `class, java, autowired, param, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class OfferService {
	@Autowired
	private CommonDao commonDao;

	//	public List<ProdMDto> retrieveProdMList(ProdMDto prodMDTO) {
	//		List<ProdMDto> resultList = commonDao.selectList("Offer.retrieveProdMList", prodMDTO);
	//		return resultList;
	//	}

	public int getTotalCounts() {
		return commonDao.select("Offer.retriveCounts");
	}
	
	public List<?> retrievePricePlanList() {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("PP");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);
		PricePlanDto pricePlanDto = new PricePlanDto();
		pricePlanDto.setAdditionalColumns(additionalColumns);
		
		List<Map<String, Object>> resultList = commonDao.selectList("Offer.selectPricePlanList", pricePlan...
```

**Chunk 정보**
- 🆔 **ID**: `1b1c30b29f53`
- 📍 **라인**: 24-24
- 📊 **토큰**: 545
- 🏷️ **태그**: `class, java, autowired, param, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **1,125개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 580 | 51.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 111.0 | 545 | 48.4% |

