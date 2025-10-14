# 📄 ComponentService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/service/ComponentService.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `ComponentService`
- **도메인**: product_online
- **목적**: ComponentService 비즈니스 로직 처리
- **복잡도**: 313
- **관련 파일**: ./sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/service/*
- **라인**: 35


## 📑 목차

### 🏗️ 클래스
- [`ComponentService`](#class-componentservice) - 복잡도: 313 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `java.util.Map` • `java.util.stream.Collectors` • `org.springframework.beans.factory.annotation.Autowired` • `org.springframework.stereotype.Component` • `com.lgcns.svcp.prod.online.prod.dto.common.AdditionalColumnsDto` 외 21개 | ⚡ **총 복잡도**: 313 |
| 📊 **총 토큰 수**: 2,057 |  |



## 🏗️ 클래스

### <a id="class-componentservice"></a>🎯 `ComponentService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 313 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 313 || 📍 **라인 범위** | 35-35 |
| 🏷️ **태그** | `class, java, autowired, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ComponentService {
	@Autowired
	private CommonDao commonDao;

	public List<?> retrieveBaseFeeList() {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("RC");
		List<AdditionalColumnsDto> additionalColumns = commonDao.selectList("Common.selectAdditionalColumns", request);

		BaseFeeDto baseFeeDto = new BaseFeeDto();
		baseFeeDto.setAdditionalColumns(additionalColumns);

		List<Map<String, Object>> resultList = commonDao.selectList("Price.selectBaseFeeList", baseFeeDto);

		return resultList.stream()
				.map(result -> MapUtil.mapToFlatMapWithNullHandling(result, additionalColumns))
				.collect(Collectors.toList());
	}

	public BaseFeeAndResourceDto retrieveBaseFeeAndResource(String inputCode) {

		AdditionalColumnsDto request = new AdditionalC...
```

**Chunk 정보**
- 🆔 **ID**: `bac574c2fabf`
- 📍 **라인**: 35-35
- 📊 **토큰**: 1000
- 🏷️ **태그**: `class, java, autowired, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **2,057개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 1,057 | 51.4% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 313.0 | 1,000 | 48.6% |

