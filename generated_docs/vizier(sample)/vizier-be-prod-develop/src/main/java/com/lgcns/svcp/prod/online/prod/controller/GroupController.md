# 📄 GroupController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/controller/GroupController.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`GroupController`](#class-groupcontroller) - 복잡도: 32 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.springframework.beans.factory.annotation.Autowired` • `org.springframework.http.ResponseEntity` • `org.springframework.web.bind.annotation.CrossOrigin` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.PathVariable` 외 10개 | ⚡ **총 복잡도**: 32 |
| 📊 **총 토큰 수**: 373 |  |



## 🏗️ 클래스

### <a id="class-groupcontroller"></a>🎯 `GroupController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 32 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 32 || 📍 **라인 범위** | 27-27 |
| 🏷️ **태그** | `class, java, autowired, getmapping, pathvariable, requestparam, param, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class GroupController {
	@Autowired
	private GroupService groupService;
	
	@GetMapping(value = "/offer-groups")
	@Operation(summary = "빌링요소 전체 조회 API", description = "빌링요소 상세정보 전체 리스트 조회")
	public ResponseEntity<List<?>> retrieveOfferGroupList() {
		List<?> response = groupService.retrieveOfferGroupList();

		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/offer-groups/{offergroupcode}")
	@Operation(summary = "오퍼그룹 단건 조회 API", description = "오퍼그룹코드로 오퍼그룹정보와 연결된 오퍼 조회")
	public ResponseEntity<OfferGroupAndOfferDto> retrieveOfferGroupAndOffer(@Parameter(description ="오퍼그룹코드", required = true, example = "GROG000010")
	@PathVariable("offergroupcode") String offerGroupCode) {
		OfferGroupAndOfferDto response = groupService.retrieveOfferGroupAndOffer(offerGroupCode);
		ret...
```

**Chunk 정보**
- 🆔 **ID**: `bdc2ba4c3901`
- 📍 **라인**: 27-27
- 📊 **토큰**: 164
- 🏷️ **태그**: `class, java, autowired, getmapping, pathvariable...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **373개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 209 | 56.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 32.0 | 164 | 44.0% |

