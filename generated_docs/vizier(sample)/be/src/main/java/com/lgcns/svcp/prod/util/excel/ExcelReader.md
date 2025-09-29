# 📄 ExcelReader.java

> **파일 경로**: `vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/util/excel/ExcelReader.java`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ExcelReader`](#class-excelreader) - 복잡도: 205

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.io.IOException` • `java.io.InputStream` • `java.lang.reflect.Field` • `java.lang.reflect.InvocationTargetException` • `java.sql.Timestamp` • `java.time.LocalDate` 외 22개 | ⚡ **총 복잡도**: 205 |
| 📊 **총 토큰 수**: 1,590 |  |



## 🏗️ 클래스

### <a id="class-excelreader"></a>🎯 `ExcelReader`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 205 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 205 || 📍 **라인 범위** | 35-35 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ExcelReader {

	private Workbook workBook;

	public static final String XLS = "xls";
	public static final String XLSX = "xlsx";

	public ExcelReader() {
	}

	public ExcelReader(Part filePart) {
		try {
			String type = FileUtil.getExtension(filePart.getSubmittedFileName());
			if (XLS.equalsIgnoreCase(type)) {
				this.workBook = new HSSFWorkbook(filePart.getInputStream());
			} else if (XLSX.equalsIgnoreCase(type)) {
				this.workBook = new XSSFWorkbook(filePart.getInputStream());
			}
		} catch (IOException e) {
			throw new BusinessException("Read file fail!");
		}
	}

	public ExcelReader(InputStream fileInputStream, String type) {
		try {
			if (XLS.equalsIgnoreCase(type)) {
				this.workBook = new HSSFWorkbook(fileInputStream);
			} else if (XLSX.equalsIgnoreCase(type)) {...
```

**Chunk 정보**
- 🆔 **ID**: `2cbd928d3c86`
- 📍 **라인**: 35-35
- 📊 **토큰**: 766
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **1,590개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 824 | 51.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 205.0 | 766 | 48.2% |

