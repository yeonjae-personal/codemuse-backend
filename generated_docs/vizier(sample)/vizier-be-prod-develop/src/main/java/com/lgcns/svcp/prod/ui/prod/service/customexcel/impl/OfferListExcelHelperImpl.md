# 📄 OfferListExcelHelperImpl.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/customexcel/impl/OfferListExcelHelperImpl.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`OfferListExcelHelperImpl`](#class-offerlistexcelhelperimpl) - 복잡도: 188

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.io.UnsupportedEncodingException` • `java.lang.reflect.Field` • `java.text.ParseException` • `java.time.ZonedDateTime` • `java.time.format.DateTimeFormatter` • `java.time.format.DateTimeParseException` 외 25개 | ⚡ **총 복잡도**: 188 |
| 📊 **총 토큰 수**: 1,391 |  |



## 🏗️ 클래스

### <a id="class-offerlistexcelhelperimpl"></a>🎯 `OfferListExcelHelperImpl`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `ExcelHelperImpl` |
| ⚡ 복잡도 | 188 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 188 || 📍 **라인 범위** | 38-38 |
| 🏗️ **상속** | `ExcelHelperImpl` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class OfferListExcelHelperImpl extends ExcelHelperImpl implements OfferListExcelHelper {
	
	private final int MAX_COLUMN_WIDTH = 40 * 261;
	
	@Override
	public String[] createTitleHeader(Object... object) {
		OfferExportDto dataHeader = (OfferExportDto) object[0];

		List<String> header = new ArrayList<>();
		header.add(getHeaderLabel("index"));

		List<GeneralDetailDto> general = dataHeader.getGeneral();
		List<AdditionalDetailDto> additional = dataHeader.getAdditional();
		general.sort(Comparator.comparing(GeneralDetailDto::getSortNo));
		additional.sort(Comparator.comparing(AdditionalDetailDto::getSortNo));

		for (GeneralDetailDto g : general) {
			if ("HD".equals(g.getFieldTypeCode()) || "item_code".equals(g.getColName())) {
				continue;
			}
			header.add(g.getLabelName());
	...
```

**Chunk 정보**
- 🆔 **ID**: `e3c2be367f30`
- 📍 **라인**: 38-38
- 📊 **토큰**: 663
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **1,391개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 728 | 52.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 188.0 | 663 | 47.7% |

