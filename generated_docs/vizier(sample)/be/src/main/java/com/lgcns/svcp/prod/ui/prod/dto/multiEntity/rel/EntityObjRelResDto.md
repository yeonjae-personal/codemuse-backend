# 📄 EntityObjRelResDto.java

> **파일 경로**: `vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/multiEntity/rel/EntityObjRelResDto.java`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 3개  
> **언어**: Java
---


## 🗄️ 데이터베이스 엔티티

### `EntityObjRelResDto`
- **테이블**: `entity_obj_rel_res_dto`
- **주요 필드**: 
- **관계**: 
- **라인**: 6


## 💼 비즈니스 로직

### `EntityObjRelResDto`
- **도메인**: product_ui
- **목적**: EntityObjRelResDto 데이터 모델 정의
- **복잡도**: 10
- **관련 파일**: ./sample_code/vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/multiEntity/rel/*
- **라인**: 6


## 📑 목차

### 🏗️ 클래스
- [`EntityObjRelResDto`](#class-entityobjrelresdto) - 복잡도: 10

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `lombok.Data` | ⚡ **총 복잡도**: 10 |
| 📊 **총 토큰 수**: 63 |  |



## 🏗️ 클래스

### <a id="class-entityobjrelresdto"></a>🎯 `EntityObjRelResDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 10 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 10 || 📍 **라인 범위** | 6-6 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class EntityObjRelResDto {
	private String objUuid;
	private String entityCode;
	private String validStartDtm;
	private String validEndDtm;
	private String entityName;
	private String entityTypeCode;
    private String itemValidStartDtm;
    private String itemValidEndDtm;
}...
```

**Chunk 정보**
- 🆔 **ID**: `9832d38fe5c8`
- 📍 **라인**: 6-6
- 📊 **토큰**: 29
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **63개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 34 | 54.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 10.0 | 29 | 46.0% |

