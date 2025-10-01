# 📄 ExcelCellStyleSupport.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/util/excel/ExcelCellStyleSupport.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ExcelCellStyleSupport`](#class-excelcellstylesupport) - 복잡도: 190

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.sql.Timestamp` • `java.time.LocalDate` • `java.time.LocalDateTime` • `java.util.Calendar` • `java.util.Date` • `org.apache.poi.ss.usermodel.BorderStyle` 외 6개 | ⚡ **총 복잡도**: 190 |
| 📊 **총 토큰 수**: 1,144 |  |



## 🏗️ 클래스

### <a id="class-excelcellstylesupport"></a>🎯 `ExcelCellStyleSupport`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 190 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 190 || 📍 **라인 범위** | 17-17 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ExcelCellStyleSupport {
	public static final String DEFAULT_DATE_FORMAT = "yyyy-mm-dd";

	public static final String DEFAULT_TIME_FORMAT = "yyyy-mm-dd h:mm:ss";

	public static final short DEFAULT_TITLE_FONT_HEIGHT = 10;

	public static final short DEFAULT_FONT_HEIGHT = 10;

	private String calendarFormat = DEFAULT_DATE_FORMAT;

	private String dateFormat = DEFAULT_DATE_FORMAT;

	private String timeFormat = DEFAULT_TIME_FORMAT;

	private String fontName = "LG스마트체 Regular";

	public short titleFontHeight = DEFAULT_TITLE_FONT_HEIGHT;

	public short fontHeight = DEFAULT_FONT_HEIGHT;

	private Font font = null;

	private Workbook workbook;

	/**
	 * 타이틀 CellStyle
	 */
	private CellStyle titleCellStyle;

	/**
	 * 일반 데이터 CellStyle
	 */
	private CellStyle cellStyle;

	/**
	 * Date Ce...
```

**Chunk 정보**
- 🆔 **ID**: `9a787e2e3e22`
- 📍 **라인**: 17-17
- 📊 **토큰**: 559
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **1,144개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 585 | 51.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 190.0 | 559 | 48.9% |

