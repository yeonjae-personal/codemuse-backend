# 📄 CategoryPathDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/category/CategoryPathDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`CategoryPathDto`](#class-categorypathdto) - 복잡도: 20

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.fasterxml.jackson.annotation.JsonIgnore` • `com.lgcns.svcp.prod.util.paging.BasePaginationDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 20 |
| 📊 **총 토큰 수**: 120 |  |



## 🏗️ 클래스

### <a id="class-categorypathdto"></a>🎯 `CategoryPathDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BasePaginationDto` |
| ⚡ 복잡도 | 20 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 20 || 📍 **라인 범위** | 11-11 |
| 🏗️ **상속** | `BasePaginationDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class CategoryPathDto extends BasePaginationDto{
	@JsonIgnore
	private String catgUuid;
	private String level1;
	private String level2;
	private String level3;
	private String level4;
	private String level5;
	private String offerCd;
	private String offerNm;
	private String level;
	private String rgstUser;
	private String rgstDtm;
	private String updUser;
	private String updDtm;
	@JsonIgnore
    private String ctgrNodeName;
    @JsonIgnore
    private String ctgrTabUuid;
}...
```

**Chunk 정보**
- 🆔 **ID**: `c7b6b9b4035e`
- 📍 **라인**: 11-11
- 📊 **토큰**: 54
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **120개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 66 | 55.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 20.0 | 54 | 45.0% |

