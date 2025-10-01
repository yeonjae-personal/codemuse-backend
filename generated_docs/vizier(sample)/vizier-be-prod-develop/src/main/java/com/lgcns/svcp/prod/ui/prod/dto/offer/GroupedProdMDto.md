# 📄 GroupedProdMDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/offer/GroupedProdMDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`GroupedProdMDto`](#class-groupedprodmdto) - 복잡도: 49

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `java.util.Map` • `com.lgcns.svcp.prod.ui.prod.dto.common.ColumnMetaDataDto` • `lombok.Data` | ⚡ **총 복잡도**: 49 |
| 📊 **총 토큰 수**: 317 |  |



## 🏗️ 클래스

### <a id="class-groupedprodmdto"></a>🎯 `GroupedProdMDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 49 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 49 || 📍 **라인 범위** | 11-11 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class GroupedProdMDto {
	public GroupedProdMDto(ProdMDto prodMDto) {
		this.generalDetails = new GeneralDetailFields(prodMDto);
//		this.salesPeriod = new SalesPeriodFields(prodMDto);
		this.additionalParams = new AdditionalParamFields(prodMDto);
		this.overView = prodMDto.getOverView();
		this.comment = prodMDto.getComment();
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String prodCd;
		private String prodNm;
		private String prodKdCd;
//		private Map<String, String> prodKdCd;
		private String custKdCd;
		private String prodAgeDivsCd;
		private String saleValdStrtDtm;
		private String saleValdEndDtm;

		public GeneralDetailFields (ProdMDto prodMDto) {
			this.type = prodMDto.getType();
			this.pr...
```

**Chunk 정보**
- 🆔 **ID**: `3bf8fa2cf628`
- 📍 **라인**: 11-11
- 📊 **토큰**: 153
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **317개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 164 | 51.7% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 49.0 | 153 | 48.3% |

