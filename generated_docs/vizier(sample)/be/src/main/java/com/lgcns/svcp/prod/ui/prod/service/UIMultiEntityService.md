# 📄 UIMultiEntityService.java

> **파일 경로**: `vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/ui/prod/service/UIMultiEntityService.java`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 3개  
> **언어**: Java
---


## 🗄️ 데이터베이스 엔티티

### `UIMultiEntityService`
- **테이블**: `ui_multi_entity_service`
- **주요 필드**: 
- **관계**: 
- **라인**: 35


## 💼 비즈니스 로직

### `UIMultiEntityService`
- **도메인**: product_ui
- **목적**: UIMultiEntityService 비즈니스 로직 처리
- **복잡도**: 101
- **관련 파일**: ./sample_code/vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/ui/prod/service/*
- **라인**: 35


## 📑 목차

### 🏗️ 클래스
- [`UIMultiEntityService`](#class-uimultientityservice) - 복잡도: 101 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.Collections` • `java.util.HashMap` • `java.util.List` • `java.util.Map` • `java.util.stream.Collectors` 외 20개 | ⚡ **총 복잡도**: 101 |
| 📊 **총 토큰 수**: 914 |  |



## 🏗️ 클래스

### <a id="class-uimultientityservice"></a>🎯 `UIMultiEntityService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 101 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 101 || 📍 **라인 범위** | 35-35 |
| 🏷️ **태그** | `class, java, transactional, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UIMultiEntityService {

	private final CommonDao commonDao;

	private static final int MAXIMUM_ENTITY_OF_SINGLE_RELATION = 1;

	public List<SelectOptionDto> retrieveSearchInfo() {
		return commonDao.selectList("Ui-multiEntity.retrieveMultiEntitySearchInfo");
	}

	public PageResult<?> retrieveMultiEntityList(SearchMultiEntityReqDto reqDto) {
		return commonDao.selectPagedList("Ui-multiEntity.retrieveMultiEntityList", reqDto);
	}

	public MultiEntityDto retrieveMultiEntityDetail(String entityCode, String entityTypeCode, String langCode) {
		EntityTypeCode typeCode = EntityTypeCode.getEnumFromCode(entityTypeCode);
		Map<String, Object> params = new HashMap<>();
		params.put("entityCode", entityCode);
		params.put("langCode", langCode);
		return switch (typeCode) {
		case EBL -> c...
```

**Chunk 정보**
- 🆔 **ID**: `e86505d2d1a7`
- 📍 **라인**: 35-35
- 📊 **토큰**: 386
- 🏷️ **태그**: `class, java, transactional, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **914개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 528 | 57.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 101.0 | 386 | 42.2% |

