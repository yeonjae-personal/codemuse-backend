# 📄 ItemDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/analysis/ItemDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ItemDto`](#class-itemdto) - 복잡도: 19

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.fasterxml.jackson.annotation.JsonInclude` • `com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto` • `com.lgcns.svcp.prod.util.paging.BasePaginationDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 19 |
| 📊 **총 토큰 수**: 128 |  |



## 🏗️ 클래스

### <a id="class-itemdto"></a>🎯 `ItemDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BasePaginationDto` |
| ⚡ 복잡도 | 19 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 19 || 📍 **라인 범위** | 14-14 |
| 🏗️ **상속** | `BasePaginationDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ItemDto extends BasePaginationDto {
	private String prodUuid;
	private String type;
	private String detlType;
	private String subType;
	private String prodItemCd;
	private String prodItemNm;
	private String objCode;
	private String objName;
	private String objUuid;
	private String itemCode;
	private String validStartDtm;
	private String validEndDtm;
	private int trgtProdItemCount;
	private int baseProdItemCount;
	private boolean onlyValidDtm;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<AdditionalDetailDto> additional;
}...
```

**Chunk 정보**
- 🆔 **ID**: `5407f9eca520`
- 📍 **라인**: 14-14
- 📊 **토큰**: 56
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **128개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 72 | 56.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 19.0 | 56 | 43.8% |

