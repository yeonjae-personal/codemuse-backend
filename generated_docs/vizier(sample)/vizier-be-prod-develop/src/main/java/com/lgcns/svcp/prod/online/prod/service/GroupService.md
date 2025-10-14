# 📄 GroupService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/service/GroupService.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`GroupService`](#class-groupservice) - 복잡도: 43 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `java.util.Map` • `java.util.stream.Collectors` • `org.springframework.beans.factory.annotation.Autowired` • `org.springframework.stereotype.Component` • `com.lgcns.svcp.prod.online.prod.dto.common.AdditionalColumnsDto` 외 7개 | ⚡ **총 복잡도**: 43 |
| 📊 **총 토큰 수**: 317 |  |



## 🏗️ 클래스

### <a id="class-groupservice"></a>🎯 `GroupService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 43 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 43 || 📍 **라인 범위** | 21-21 |
| 🏷️ **태그** | `class, java, autowired, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class GroupService {
	@Autowired
	private CommonDao commonDao;
	//오퍼그룹 전체리스트, 상품코드로 연결되어있는 오퍼그룹만 조회도 가능
	public List<OfferGroupMDto> retrieveOfferGroupMList() {
		List<OfferGroupMDto> resultList = commonDao.selectList("Group.retrieveOfferGroupMList");
		return resultList;
	}
	
	// 오퍼그룹코드-상품코드-시작-종료 새로운 dto, 상품코드로 조회 가능
	public List<OfferGroupWithOfferDto> retrieveOfferGroupWithOfferList(OfferGroupWithOfferDto offerGroupWithOfferDto) {
		List<OfferGroupWithOfferDto> resultList = commonDao.selectList("Group.retrieveOfferGroupWithOfferList", offerGroupWithOfferDto);
		return resultList;
	}
	
	
	public List<?> retrieveOfferGroupList() {
		AdditionalColumnsDto request = new AdditionalColumnsDto();
		request.setItemCode("OG");
		List<AdditionalColumnsDto> additionalColumns = commonDao.sel...
```

**Chunk 정보**
- 🆔 **ID**: `536947f2ca63`
- 📍 **라인**: 21-21
- 📊 **토큰**: 144
- 🏷️ **태그**: `class, java, autowired, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **317개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 173 | 54.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 43.0 | 144 | 45.4% |

