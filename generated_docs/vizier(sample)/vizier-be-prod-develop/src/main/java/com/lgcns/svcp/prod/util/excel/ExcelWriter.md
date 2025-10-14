# 📄 ExcelWriter.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/util/excel/ExcelWriter.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ExcelWriter`](#class-excelwriter) - 복잡도: 151

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.io.UnsupportedEncodingException` • `java.lang.reflect.Field` • `java.time.LocalDate` • `java.time.LocalDateTime` • `java.util.ArrayList` • `java.util.Calendar` 외 11개 | ⚡ **총 복잡도**: 151 |
| 📊 **총 토큰 수**: 1,218 |  |



## 🏗️ 클래스

### <a id="class-excelwriter"></a>🎯 `ExcelWriter`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 151 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 151 || 📍 **라인 범위** | 23-23 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ExcelWriter {
	
	protected Workbook workbook;
	protected String excelType;
	private ExcelCellStyleSupport style;
	protected String[] titles = null;
	protected boolean isAutoCellSize = false;

	public static final String XLS = "xls";

	public static final String XLSX = "xlsx";

	private final int MAX_COLUMN_WIDTH = 40 * 261;

	public ExcelWriter(String excelType) {
		if (XLS.equalsIgnoreCase(excelType)) {
			workbook = new HSSFWorkbook();
		} else if (XLSX.equalsIgnoreCase(excelType)) {
			workbook = new XSSFWorkbook();
		}
		this.excelType = excelType;
		initStyle();
	}

	protected void initStyle() {
		style = createCellStyleSupport(workbook);
	}

	public ExcelCellStyleSupport createCellStyleSupport(Workbook workbook) {
		return new ExcelCellStyleSupport(workbook);
	}

	public...
```

**Chunk 정보**
- 🆔 **ID**: `0cd3b08af476`
- 📍 **라인**: 23-23
- 📊 **토큰**: 591
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **1,218개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 627 | 51.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 151.0 | 591 | 48.5% |

