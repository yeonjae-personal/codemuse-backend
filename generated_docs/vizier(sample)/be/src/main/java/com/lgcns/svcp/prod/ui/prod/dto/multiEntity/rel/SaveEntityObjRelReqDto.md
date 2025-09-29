# 📄 SaveEntityObjRelReqDto.java

> **파일 경로**: `vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/multiEntity/rel/SaveEntityObjRelReqDto.java`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 3개  
> **언어**: Java
---


## 🗄️ 데이터베이스 엔티티

### `SaveEntityObjRelReqDto`
- **테이블**: `save_entity_obj_rel_req_dto`
- **주요 필드**: 
- **관계**: 
- **라인**: 9


## 💼 비즈니스 로직

### `SaveEntityObjRelReqDto`
- **도메인**: product_ui
- **목적**: SaveEntityObjRelReqDto 데이터 모델 정의
- **복잡도**: 9
- **관련 파일**: ./sample_code/vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/multiEntity/rel/*
- **라인**: 9


## 📑 목차

### 🏗️ 클래스
- [`SaveEntityObjRelReqDto`](#class-saveentityobjrelreqdto) - 복잡도: 9

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.fasterxml.jackson.annotation.JsonIgnore` • `com.lgcns.svcp.prod.ui.prod.dto.BaseDto` • `lombok.Data` | ⚡ **총 복잡도**: 9 |
| 📊 **총 토큰 수**: 57 |  |



## 🏗️ 클래스

### <a id="class-saveentityobjrelreqdto"></a>🎯 `SaveEntityObjRelReqDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseDto` |
| ⚡ 복잡도 | 9 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 9 || 📍 **라인 범위** | 9-9 |
| 🏗️ **상속** | `BaseDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class SaveEntityObjRelReqDto extends BaseDto {
	@JsonIgnore
	private String objUuid;
	@JsonIgnore
	private String oldEntityCode;
	private String entityCode;
	private String validStartDtm;
	private String validEndDtm;
}...
```

**Chunk 정보**
- 🆔 **ID**: `39263de41548`
- 📍 **라인**: 9-9
- 📊 **토큰**: 24
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **57개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 33 | 57.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 9.0 | 24 | 42.1% |

