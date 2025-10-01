# 📄 CreateEntityReqDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/multiEntity/create/CreateEntityReqDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---


## 🗄️ 데이터베이스 엔티티

### `CreateEntityReqDto`
- **테이블**: `create_entity_req_dto`
- **주요 필드**: 
- **관계**: 
- **라인**: 11


## 💼 비즈니스 로직

### `CreateEntityReqDto`
- **도메인**: user
- **목적**: CreateEntityReqDto 데이터 모델 정의
- **복잡도**: 19
- **관련 파일**: /Users/roseline/projects/codemuse-backend/sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/multiEntity/create/*
- **라인**: 11


## 📑 목차

### 🏗️ 클래스
- [`CreateEntityReqDto`](#class-createentityreqdto) - 복잡도: 19

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.BaseDto` • `com.lgcns.svcp.prod.ui.prod.dto.multiEntity.MultiEntityAdditionalDto` • `lombok.Data` | ⚡ **총 복잡도**: 19 |
| 📊 **총 토큰 수**: 127 |  |



## 🏗️ 클래스

### <a id="class-createentityreqdto"></a>🎯 `CreateEntityReqDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseDto` |
| ⚡ 복잡도 | 19 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 19 || 📍 **라인 범위** | 11-11 |
| 🏗️ **상속** | `BaseDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class CreateEntityReqDto extends BaseDto {
	private String entityCode;
	private String entityName;
	private String entityTypeCode;
	private String validStartDtm;
	private String validEndDtm;
	private String ovwCntn;
	private List<MultiEntityAdditionalDto> additional;

	/* BsnLineDto */
	private String bsnLineTypeCode;

	/* DcTrgtDto */
	private String groupUuid;
	private String offerUuid;
	private String cpntUuid;
	private String rscUuid;
	private String chrgTypeCode;

	/* SaleCpnyDto */
	private String mvnoBsnoYn;

}...
```

**Chunk 정보**
- 🆔 **ID**: `46ca31e0e05a`
- 📍 **라인**: 11-11
- 📊 **토큰**: 58
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **127개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 69 | 54.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 19.0 | 58 | 45.7% |

