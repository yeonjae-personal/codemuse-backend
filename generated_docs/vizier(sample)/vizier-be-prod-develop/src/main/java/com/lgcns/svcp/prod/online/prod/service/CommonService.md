# 📄 CommonService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/service/CommonService.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`CommonService`](#class-commonservice) - 복잡도: 20 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.springframework.beans.factory.annotation.Autowired` • `org.springframework.stereotype.Component` • `com.lgcns.svcp.prod.online.prod.dto.ProdItemMapgMDto` • `com.lgcns.svcp.prod.online.prod.dto.common.ItemCodeRequestDto` • `com.lgcns.svcp.prod.online.prod.dto.common.OfferInfoDto` 외 2개 | ⚡ **총 복잡도**: 20 |
| 📊 **총 토큰 수**: 169 |  |



## 🏗️ 클래스

### <a id="class-commonservice"></a>🎯 `CommonService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 20 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 20 || 📍 **라인 범위** | 16-16 |
| 🏷️ **태그** | `class, java, autowired, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class CommonService {
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private OfferService offerService;
	@Autowired
	private ResourceService resourceService;
	
	public List<ProdItemMapgMDto> retrieveProdItemMapgMList(ProdItemMapgMDto ProdItemMapgMDto) {
		List<ProdItemMapgMDto> resultList = commonDao.selectList("Common.retrieveProdItemMapgMList", ProdItemMapgMDto);
		return resultList;
	}
	
	public String getItemCodeByUuid(String uuid) {
		ItemCodeRequestDto request = new ItemCodeRequestDto();
		
		return commonDao.select("Common.getItemCodeByUuid", request);
	}
	
	public OfferInfoDto retrieveOfferInfo(PricePlanDto pricePlanDto) {
		OfferInfoDto result = new OfferInfoDto();
		
//		String bsfCd = commonDao.select("Common.getBaseFeeCodeByPricePlanCode", pricePlanDto);
//		Stri...
```

**Chunk 정보**
- 🆔 **ID**: `e46193639d89`
- 📍 **라인**: 16-16
- 📊 **토큰**: 75
- 🏷️ **태그**: `class, java, autowired, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **169개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 94 | 55.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 20.0 | 75 | 44.4% |

