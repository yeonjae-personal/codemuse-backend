# 📄 MultiEntityDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/multiEntity/MultiEntityDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---


## 🗄️ 데이터베이스 엔티티

### `MultiEntityDto`
- **테이블**: `multi_entity_dto`
- **주요 필드**: 
- **관계**: 
- **라인**: 13


## 💼 비즈니스 로직

### `MultiEntityDto`
- **도메인**: user
- **목적**: MultiEntityDto 데이터 모델 정의
- **복잡도**: 8
- **관련 파일**: /Users/roseline/projects/codemuse-backend/sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/multiEntity/*
- **라인**: 13


## 📑 목차

### 🏗️ 클래스
- [`MultiEntityDto`](#class-multientitydto) - 복잡도: 8

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.fasterxml.jackson.annotation.JsonInclude` • `com.lgcns.svcp.prod.ui.prod.dto.ItemMappingDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 8 |
| 📊 **총 토큰 수**: 60 |  |



## 🏗️ 클래스

### <a id="class-multientitydto"></a>🎯 `MultiEntityDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `ItemMappingDto` |
| ⚡ 복잡도 | 8 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 8 || 📍 **라인 범위** | 13-13 |
| 🏗️ **상속** | `ItemMappingDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class MultiEntityDto extends ItemMappingDto {
	private String entityCode;
	private String entityName;
	private String entityTypeCode;
    private String entityScope;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<MultiEntityAdditionalDto> additional;
}...
```

**Chunk 정보**
- 🆔 **ID**: `cb744de12f5b`
- 📍 **라인**: 13-13
- 📊 **토큰**: 23
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **60개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 37 | 61.7% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 8.0 | 23 | 38.3% |

