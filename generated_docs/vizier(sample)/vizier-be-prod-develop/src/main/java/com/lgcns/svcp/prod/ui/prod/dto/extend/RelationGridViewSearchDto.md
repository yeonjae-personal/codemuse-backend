# 📄 RelationGridViewSearchDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/extend/RelationGridViewSearchDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`RelationGridViewSearchDto`](#class-relationgridviewsearchdto) - 복잡도: 11

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.util.paging.BasePaginationDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 11 |
| 📊 **총 토큰 수**: 80 |  |



## 🏗️ 클래스

### <a id="class-relationgridviewsearchdto"></a>🎯 `RelationGridViewSearchDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BasePaginationDto` |
| ⚡ 복잡도 | 11 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 11 || 📍 **라인 범위** | 12-12 |
| 🏗️ **상속** | `BasePaginationDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class RelationGridViewSearchDto extends BasePaginationDto {
	
	private List<String> objUuids;
	private String offerCode;
	private String offerName;
	private String groupCode;
	private String groupName;
	private String relationCode;
	private String relationName;
	private String searchByCode;
	private String searchByName;
}...
```

**Chunk 정보**
- 🆔 **ID**: `85cb7437e1e2`
- 📍 **라인**: 12-12
- 📊 **토큰**: 34
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **80개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 46 | 57.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 11.0 | 34 | 42.5% |

