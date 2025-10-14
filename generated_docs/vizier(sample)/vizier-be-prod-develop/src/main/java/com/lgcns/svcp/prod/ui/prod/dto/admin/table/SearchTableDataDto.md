# 📄 SearchTableDataDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/admin/table/SearchTableDataDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`SearchTableDataDto`](#class-searchtabledatadto) - 복잡도: 6

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.common.search.DynamicFieldReq` • `com.lgcns.svcp.prod.util.paging.BasePaginationDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 6 |
| 📊 **총 토큰 수**: 52 |  |



## 🏗️ 클래스

### <a id="class-searchtabledatadto"></a>🎯 `SearchTableDataDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BasePaginationDto` |
| ⚡ 복잡도 | 6 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 6 || 📍 **라인 범위** | 13-13 |
| 🏗️ **상속** | `BasePaginationDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class SearchTableDataDto extends BasePaginationDto {
	private String tableName;
	private List<String> selectColumns;
	private List<DynamicFieldReq> fieldSearchs;
	private String orderByClause;
}...
```

**Chunk 정보**
- 🆔 **ID**: `5b795055beb5`
- 📍 **라인**: 13-13
- 📊 **토큰**: 19
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **52개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 33 | 63.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 6.0 | 19 | 36.5% |

