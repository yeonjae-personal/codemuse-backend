# 📄 SearchMultiEntityReqDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/multiEntity/search/SearchMultiEntityReqDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---


## 🗄️ 데이터베이스 엔티티

### `SearchMultiEntityReqDto`
- **테이블**: `search_multi_entity_req_dto`
- **주요 필드**: 
- **관계**: 
- **라인**: 10


## 💼 비즈니스 로직

### `SearchMultiEntityReqDto`
- **도메인**: product_ui
- **목적**: SearchMultiEntityReqDto 데이터 모델 정의
- **복잡도**: 7
- **관련 파일**: ./sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/multiEntity/search/*
- **라인**: 10


## 📑 목차

### 🏗️ 클래스
- [`SearchMultiEntityReqDto`](#class-searchmultientityreqdto) - 복잡도: 7

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.util.paging.BasePaginationDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 7 |
| 📊 **총 토큰 수**: 54 |  |



## 🏗️ 클래스

### <a id="class-searchmultientityreqdto"></a>🎯 `SearchMultiEntityReqDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BasePaginationDto` |
| ⚡ 복잡도 | 7 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 7 || 📍 **라인 범위** | 10-10 |
| 🏗️ **상속** | `BasePaginationDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class SearchMultiEntityReqDto extends BasePaginationDto {
	private String itemCode;
	private String entityTypeCode;
	private String multiEntityCode;
	private String multiEntityName;
	private boolean onlyValidDtm;
}...
```

**Chunk 정보**
- 🆔 **ID**: `64fb28ead193`
- 📍 **라인**: 10-10
- 📊 **토큰**: 22
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **54개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 32 | 59.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 7.0 | 22 | 40.7% |

