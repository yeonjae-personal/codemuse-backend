# 📄 EntityItemRelResDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/multiEntity/rel/EntityItemRelResDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---


## 🗄️ 데이터베이스 엔티티

### `EntityItemRelResDto`
- **테이블**: `entity_item_rel_res_dto`
- **주요 필드**: 
- **관계**: 
- **라인**: 13


## 💼 비즈니스 로직

### `EntityItemRelResDto`
- **도메인**: user
- **목적**: EntityItemRelResDto 데이터 모델 정의
- **복잡도**: 9
- **관련 파일**: /Users/roseline/projects/codemuse-backend/sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/multiEntity/rel/*
- **라인**: 13


## 📑 목차

### 🏗️ 클래스
- [`EntityItemRelResDto`](#class-entityitemrelresdto) - 복잡도: 9

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.export.MultiEntityExportDto` • `com.lgcns.svcp.prod.ui.prod.enums.entity.EntityScope` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 9 |
| 📊 **총 토큰 수**: 66 |  |



## 🏗️ 클래스

### <a id="class-entityitemrelresdto"></a>🎯 `EntityItemRelResDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 9 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 9 || 📍 **라인 범위** | 13-13 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class EntityItemRelResDto {
	private String itemCode;
	private String entityTypeCode;
	private String entityTypeName;
	private EntityScope entityScope;
	private int sortNo;
	private List<EntityObjRelResDto> objRel;
	private List<MultiEntityExportDto> multiEntityExportDtos;
}...
```

**Chunk 정보**
- 🆔 **ID**: `30a2aeb095f0`
- 📍 **라인**: 13-13
- 📊 **토큰**: 26
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **66개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 40 | 60.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 9.0 | 26 | 39.4% |

