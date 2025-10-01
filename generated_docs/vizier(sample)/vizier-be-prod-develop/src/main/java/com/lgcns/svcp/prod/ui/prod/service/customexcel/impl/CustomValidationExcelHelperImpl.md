# 📄 CustomValidationExcelHelperImpl.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/customexcel/impl/CustomValidationExcelHelperImpl.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`CustomValidationExcelHelperImpl`](#class-customvalidationexcelhelperimpl) - 복잡도: 302

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.awt.Color` • `java.time.LocalDateTime` • `java.time.format.DateTimeFormatter` • `java.time.format.DateTimeParseException` • `java.util.ArrayList` • `java.util.List` 외 19개 | ⚡ **총 복잡도**: 302 |
| 📊 **총 토큰 수**: 2,623 |  |



## 🏗️ 클래스

### <a id="class-customvalidationexcelhelperimpl"></a>🎯 `CustomValidationExcelHelperImpl`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `ExcelHelperImpl` |
| ⚡ 복잡도 | 302 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 302 || 📍 **라인 범위** | 32-32 |
| 🏗️ **상속** | `ExcelHelperImpl` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class CustomValidationExcelHelperImpl extends ExcelHelperImpl implements CustomValidationExcelHelper {
	
	private final int MAX_COLUMN_WIDTH = 40 * 261;
		
	@Override
	@SuppressWarnings("unchecked")
	public void createSheet(ExcelWriter excelWriter, ExcelInput excelInput) {
		Workbook workbook = excelWriter.getWorkbook();
		Sheet sheet = workbook.createSheet(excelInput.getSheetName()); 
		ExcelCellStyleSupportCustom excelCellStyle = new ExcelCellStyleSupportCustom(excelWriter.getWorkbook());
		CellStyle createCellStyle = excelCellStyle.createCellStyle();
		createTitleValidation(sheet, excelWriter.getWorkbook());
		Map<Integer, List<CustomValidationExcelDto>> maps = (Map<Integer, List<CustomValidationExcelDto>>) excelInput.getObject();
		Integer index = 4;

		// Array to store the max...
```

**Chunk 정보**
- 🆔 **ID**: `55348e39a9d0`
- 📍 **라인**: 32-32
- 📊 **토큰**: 1285
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **2,623개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 1,338 | 51.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 302.0 | 1,285 | 49.0% |

