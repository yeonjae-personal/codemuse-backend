# 📄 DiscountGeneralDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/offer/list/dto/DiscountGeneralDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`DiscountGeneralDto`](#class-discountgeneraldto) - 복잡도: 8

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.offer.detail.OfferGeneralDto` • `com.lgcns.svcp.prod.ui.prod.dto.offer.list.DcCfgrtDto` • `com.lgcns.svcp.prod.ui.prod.dto.offer.list.DcRateDto` • `lombok.Data` • `lombok.EqualsAndHashCode` | ⚡ **총 복잡도**: 8 |
| 📊 **총 토큰 수**: 68 |  |



## 🏗️ 클래스

### <a id="class-discountgeneraldto"></a>🎯 `DiscountGeneralDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `OfferGeneralDto` |
| ⚡ 복잡도 | 8 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 8 || 📍 **라인 범위** | 14-14 |
| 🏗️ **상속** | `OfferGeneralDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class DiscountGeneralDto extends OfferGeneralDto {
	private String dcTypeCode;
	private String pnltOcrcYn;
	private String dcPriorRank;
	private String dcOvwCntn;
	private List<DcRateDto> dcRateList;
	private List<DcCfgrtDto> dcCfgrtList;
}...
```

**Chunk 정보**
- 🆔 **ID**: `874a3c07368d`
- 📍 **라인**: 14-14
- 📊 **토큰**: 25
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **68개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 43 | 63.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 8.0 | 25 | 36.8% |

