# 📄 ComponentExcelHelperImpl.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/customexcel/impl/ComponentExcelHelperImpl.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `ComponentExcelHelperImpl`
- **도메인**: user
- **목적**: 비즈니스 로직 처리
- **복잡도**: 226
- **관련 파일**: /Users/roseline/projects/codemuse-backend/sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/customexcel/impl/*
- **라인**: 41


## 📑 목차

### 🏗️ 클래스
- [`ComponentExcelHelperImpl`](#class-componentexcelhelperimpl) - 복잡도: 226 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.io.UnsupportedEncodingException` • `java.lang.reflect.Field` • `java.text.ParseException` • `java.time.ZonedDateTime` • `java.time.format.DateTimeFormatter` • `java.time.format.DateTimeParseException` 외 28개 | ⚡ **총 복잡도**: 226 |
| 📊 **총 토큰 수**: 1,663 |  |



## 🏗️ 클래스

### <a id="class-componentexcelhelperimpl"></a>🎯 `ComponentExcelHelperImpl`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `ExcelHelperImpl` |
| ⚡ 복잡도 | 226 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 226 || 📍 **라인 범위** | 41-41 |
| 🏗️ **상속** | `ExcelHelperImpl` || 🏷️ **태그** | `class, java, autowired, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ComponentExcelHelperImpl extends ExcelHelperImpl implements ComponentExcelHelper {
	
	private final int MAX_COLUMN_WIDTH = 40 * 261;
		
	@Autowired
	private MessageSource messageSource;
	 	
	@Override
	public String[] createTitleHeader(Object... object) {
		ComponentExportDto dataHeader = (ComponentExportDto) object[0];

		List<String> header = new ArrayList<>();
		header.add(getHeaderLabel("index"));

		List<GeneralDetailDto> general = dataHeader.getGeneral();
		List<AdditionalDetailDto> additional = dataHeader.getAdditional();
		general.sort(Comparator.comparing(GeneralDetailDto::getSortNo));
		additional.sort(Comparator.comparing(AdditionalDetailDto::getSortNo));

		for (GeneralDetailDto g : general) {
			if ("HD".equals(g.getFieldTypeCode()) || "item_code".equals(g.getColN...
```

**Chunk 정보**
- 🆔 **ID**: `67ca716d0f88`
- 📍 **라인**: 41-41
- 📊 **토큰**: 796
- 🏷️ **태그**: `class, java, autowired, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **1,663개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 867 | 52.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 226.0 | 796 | 47.9% |

