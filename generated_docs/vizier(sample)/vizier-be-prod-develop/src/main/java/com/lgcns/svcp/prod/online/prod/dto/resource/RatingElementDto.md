# 📄 RatingElementDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/dto/resource/RatingElementDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`RatingElementDto`](#class-ratingelementdto) - 복잡도: 18

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.online.prod.dto.common.AdditionalColumnsDto` • `lombok.Data` | ⚡ **총 복잡도**: 18 |
| 📊 **총 토큰 수**: 115 |  |



## 🏗️ 클래스

### <a id="class-ratingelementdto"></a>🎯 `RatingElementDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 18 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 18 || 📍 **라인 범위** | 10-10 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class RatingElementDto {
	private String objUuid;
	private String objCode;
	private String rtngRscTypeCode;
	private String blngDivCode;
	private String acntSalesCode;
	private String pymntCmsnApplyYn;
	private String vatApplyYn;
	private String rtngRscOvwCntn;
	private String dplcTrgtUuid;
	private String chgDeptName;
	private String chgUser;
	private String rgstUser;
	private String rgstDtm;
	private String updUser;
	private String updDtm;
	private List<AdditionalColumnsDto> additionalColumns;
}...
```

**Chunk 정보**
- 🆔 **ID**: `e66f2a3bf844`
- 📍 **라인**: 10-10
- 📊 **토큰**: 53
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **115개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 62 | 53.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 18.0 | 53 | 46.1% |

