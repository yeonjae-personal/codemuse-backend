# 📄 MatrixExcelHelperImpl.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/customexcel/impl/MatrixExcelHelperImpl.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`MatrixExcelHelperImpl`](#class-matrixexcelhelperimpl) - 복잡도: 304

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.awt.Color` • `java.math.BigDecimal` • `java.util.ArrayList` • `java.util.HashMap` • `java.util.List` • `java.util.Map` 외 30개 | ⚡ **총 복잡도**: 304 |
| 📊 **총 토큰 수**: 3,117 |  |



## 🏗️ 클래스

### <a id="class-matrixexcelhelperimpl"></a>🎯 `MatrixExcelHelperImpl`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `ExcelHelperImpl` |
| ⚡ 복잡도 | 304 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 304 || 📍 **라인 범위** | 44-44 |
| 🏗️ **상속** | `ExcelHelperImpl` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class MatrixExcelHelperImpl extends ExcelHelperImpl implements MatrixExcelHelper {

	@SuppressWarnings("unchecked")
	@Override
	public String[] createTitleHeader(Object... object) {
		List<BuilderFactorDto> builderDtos = (List<BuilderFactorDto>) object[0];
		List<String> headers = builderDtos.stream().map(BuilderFactorDto::getFactorName).collect(Collectors.toList());
		headers.add("Value");
		return headers.toArray(new String[0]);
	}

	@Override
	public void createSheet(ExcelWriter excelWriter, ExcelInput excelInput) {
		MatrixExportReqDto exportDto = (MatrixExportReqDto) excelInput.getObject();
		List<MatrixMeasureMDto> measures = exportDto.getMeasureMDtos();
		Workbook workbook = excelWriter.getWorkbook();

		// Set up title style
		excelWriter.getStyle().setTitleCellStyle(createT...
```

**Chunk 정보**
- 🆔 **ID**: `3be48faa2b7a`
- 📍 **라인**: 44-44
- 📊 **토큰**: 1521
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **3,117개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 1,596 | 51.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 304.0 | 1,521 | 48.8% |

