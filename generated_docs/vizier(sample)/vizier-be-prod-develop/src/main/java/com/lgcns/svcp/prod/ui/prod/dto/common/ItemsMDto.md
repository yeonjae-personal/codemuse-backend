# 📄 ItemsMDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/common/ItemsMDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ItemsMDto`](#class-itemsmdto) - 복잡도: 16

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.fasterxml.jackson.annotation.JsonIgnore` • `com.lgcns.svcp.prod.ui.prod.enums.ComponentType` • `com.lgcns.svcp.prod.ui.prod.enums.OfferType` • `com.lgcns.svcp.prod.util.paging.BasePaginationDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 16 |
| 📊 **총 토큰 수**: 106 |  |



## 🏗️ 클래스

### <a id="class-itemsmdto"></a>🎯 `ItemsMDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BasePaginationDto` |
| ⚡ 복잡도 | 16 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 16 || 📍 **라인 범위** | 13-13 |
| 🏗️ **상속** | `BasePaginationDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ItemsMDto extends BasePaginationDto {
	private String prodUuid;
	private String prodItemCd;
	private String prodItemNm;
	private ComponentType componentType;
	private String itemTypeNm;
	private String itemDetlTypeCd;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
	@JsonIgnore
	private OfferType offerType;
	@JsonIgnore
	private String baseUuid;
}...
```

**Chunk 정보**
- 🆔 **ID**: `9c27d49a5c13`
- 📍 **라인**: 13-13
- 📊 **토큰**: 45
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **106개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 61 | 57.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 16.0 | 45 | 42.5% |

