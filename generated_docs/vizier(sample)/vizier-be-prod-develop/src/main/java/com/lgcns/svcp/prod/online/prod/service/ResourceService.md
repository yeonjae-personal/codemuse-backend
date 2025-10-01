# 📄 ResourceService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/service/ResourceService.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ResourceService`](#class-resourceservice) - 복잡도: 96 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `java.util.Map` • `java.util.stream.Collectors` • `org.springframework.beans.factory.annotation.Autowired` • `org.springframework.stereotype.Component` • `com.lgcns.svcp.prod.online.prod.dto.common.AdditionalColumnsDto` 외 6개 | ⚡ **총 복잡도**: 96 |
| 📊 **총 토큰 수**: 621 |  |



## 🏗️ 클래스

### <a id="class-resourceservice"></a>🎯 `ResourceService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 96 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 96 || 📍 **라인 범위** | 20-20 |
| 🏷️ **태그** | `class, java, autowired, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ResourceService {
	@Autowired
	private CommonDao commonDao;

	public List<?> retrieveSalesList() {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("SI");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		SalesDto salesDto = new SalesDto();
		salesDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Characteristic.selectSalesList", salesDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}

	public List<?> retrieveSales(String inputCode) {

		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCod...
```

**Chunk 정보**
- 🆔 **ID**: `28ccb52b1c51`
- 📍 **라인**: 20-20
- 📊 **토큰**: 297
- 🏷️ **태그**: `class, java, autowired, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **621개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 324 | 52.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 96.0 | 297 | 47.8% |

