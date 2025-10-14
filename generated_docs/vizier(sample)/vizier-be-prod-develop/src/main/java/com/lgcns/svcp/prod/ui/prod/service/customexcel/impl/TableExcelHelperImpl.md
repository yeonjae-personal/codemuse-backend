# 📄 TableExcelHelperImpl.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/customexcel/impl/TableExcelHelperImpl.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`TableExcelHelperImpl`](#class-tableexcelhelperimpl) - 복잡도: 86

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.io.UnsupportedEncodingException` • `java.util.ArrayList` • `java.util.Arrays` • `java.util.LinkedHashMap` • `java.util.List` • `java.util.Map.Entry` 외 14개 | ⚡ **총 복잡도**: 86 |
| 📊 **총 토큰 수**: 701 |  |



## 🏗️ 클래스

### <a id="class-tableexcelhelperimpl"></a>🎯 `TableExcelHelperImpl`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `ExcelHelperImpl` |
| ⚡ 복잡도 | 86 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 86 || 📍 **라인 범위** | 27-27 |
| 🏗️ **상속** | `ExcelHelperImpl` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class TableExcelHelperImpl extends ExcelHelperImpl implements TableExcelHelper {
	
	private final int MAX_COLUMN_WIDTH = 40 * 261;
	
	@Override
	public String[] createTitleHeader(Object... object) {
		List<TableColumnDto> dataHeader = (List<TableColumnDto>) object[0];

		List<String> header = new ArrayList<>();
		header.add(getHeaderLabel("index"));

		for (TableColumnDto columnDto : dataHeader) {
			header.add(columnDto.getColumnComment());
		}
		
		return header.toArray(new String[0]);
	}

	@Override
	public void createSheet(ExcelWriter excelWriter, ExcelInput excelInput) {
		Workbook workbook = excelWriter.getWorkbook();

		// Create sheet
		excelWriter.getStyle().setTitleCellStyle(createTitleCellStyleCustom(new ExcelCellStyleSupportCustom(workbook)));

		Sheet sheet = workbook.c...
```

**Chunk 정보**
- 🆔 **ID**: `83094686c315`
- 📍 **라인**: 27-27
- 📊 **토큰**: 329
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **701개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 372 | 53.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 86.0 | 329 | 46.9% |

