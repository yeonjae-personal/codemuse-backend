# 📄 MultiEntityAdditionalDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/multiEntity/MultiEntityAdditionalDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---


## 🗄️ 데이터베이스 엔티티

### `MultiEntityAdditionalDto`
- **테이블**: `multi_entity_additional_dto`
- **주요 필드**: 
- **관계**: 
- **라인**: 10


## 💼 비즈니스 로직

### `MultiEntityAdditionalDto`
- **도메인**: product_ui
- **목적**: MultiEntityAdditionalDto 데이터 모델 정의
- **복잡도**: 13
- **관련 파일**: ./sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/multiEntity/*
- **라인**: 10


## 📑 목차

### 🏗️ 클래스
- [`MultiEntityAdditionalDto`](#class-multientityadditionaldto) - 복잡도: 13

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.ui.prod.dto.BaseDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 13 |
| 📊 **총 토큰 수**: 90 |  |



## 🏗️ 클래스

### <a id="class-multientityadditionaldto"></a>🎯 `MultiEntityAdditionalDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseDto` |
| ⚡ 복잡도 | 13 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 13 || 📍 **라인 범위** | 10-10 |
| 🏗️ **상속** | `BaseDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class MultiEntityAdditionalDto extends BaseDto {
	private String attrUuid;
	private String entityCode;
	private String entityTypeCode;
	private String fieldTypeCode;
	private String commGroupCode;
	private String labelId;
	private String requiredYn;
	private String attrMaxLength;
	private String sortNo;
	private String attrVal;
	private String labelDscr;
}...
```

**Chunk 정보**
- 🆔 **ID**: `77e5361ee7b2`
- 📍 **라인**: 10-10
- 📊 **토큰**: 40
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **90개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 50 | 55.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 13.0 | 40 | 44.4% |

