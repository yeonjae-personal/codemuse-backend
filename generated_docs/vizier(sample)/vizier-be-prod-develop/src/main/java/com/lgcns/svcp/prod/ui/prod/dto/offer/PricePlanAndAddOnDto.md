# 📄 PricePlanAndAddOnDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/offer/PricePlanAndAddOnDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`PricePlanAndAddOnDto`](#class-priceplanandaddondto) - 복잡도: 16

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.BaseDto` • `com.lgcns.svcp.prod.ui.prod.dto.common.ColumnMetaDataDto` • `lombok.Data` | ⚡ **총 복잡도**: 16 |
| 📊 **총 토큰 수**: 109 |  |



## 🏗️ 클래스

### <a id="class-priceplanandaddondto"></a>🎯 `PricePlanAndAddOnDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseDto` |
| ⚡ 복잡도 | 16 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 16 || 📍 **라인 범위** | 11-11 |
| 🏗️ **상속** | `BaseDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class PricePlanAndAddOnDto extends BaseDto {
	private String type;
	private String prodUuid;
	private String prodCd;
	private String prodNm;
	private String prodKdCd;
	private String custKdCd;
	private String prodAgeDivsCd;
	private String prodValdStrtDtm;
	private String prodValdEndDtm;
	private String saleValdStrtDtm;
	private String saleValdEndDtm;
	private String prodOvwDesc;
	private String duplTrgtUuid;

	private List<ColumnMetaDataDto> columnMetaData;
}...
```

**Chunk 정보**
- 🆔 **ID**: `038e4be01042`
- 📍 **라인**: 11-11
- 📊 **토큰**: 49
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **109개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 60 | 55.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 16.0 | 49 | 45.0% |

