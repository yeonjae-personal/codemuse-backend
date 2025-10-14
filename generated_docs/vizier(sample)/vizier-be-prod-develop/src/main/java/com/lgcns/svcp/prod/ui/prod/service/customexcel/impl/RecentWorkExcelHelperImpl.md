# 📄 RecentWorkExcelHelperImpl.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/customexcel/impl/RecentWorkExcelHelperImpl.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`RecentWorkExcelHelperImpl`](#class-recentworkexcelhelperimpl) - 복잡도: 27 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.Map` • `org.apache.poi.ss.usermodel.Cell` • `org.apache.poi.ss.usermodel.Row` • `org.apache.poi.ss.usermodel.Sheet` • `org.apache.poi.ss.usermodel.Workbook` • `org.apache.poi.ss.util.CellRangeAddress` 외 7개 | ⚡ **총 복잡도**: 27 |
| 📊 **총 토큰 수**: 225 |  |



## 🏗️ 클래스

### <a id="class-recentworkexcelhelperimpl"></a>🎯 `RecentWorkExcelHelperImpl`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `ExcelHelperImpl` |
| ⚡ 복잡도 | 27 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 27 || 📍 **라인 범위** | 20-20 |
| 🏗️ **상속** | `ExcelHelperImpl` || 🏷️ **태그** | `class, java, autowired, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class RecentWorkExcelHelperImpl extends ExcelHelperImpl implements RecentWorkExcelHelper {
	
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("unchecked")
	@Override
	public void handleCustomTemplate(Workbook workbook, Object object) {
		Map<String, Object> maps = (Map<String, Object>) object;
		XSSFSheet sheet = (XSSFSheet) workbook.getSheet("recentlywork");
		createBatchDateStyle(sheet, maps);
	}
	
	private void createBatchDateStyle(Sheet sheet, Map<String, Object> maps) {
		int rowNumer = 3;
		sheet.shiftRows(0, sheet.getLastRowNum(), rowNumer);
		for (int i = 0; i < rowNumer; i++) {
			sheet.createRow(i);
		}
		int firstRow = 1;
		int lastRow = 1;
		int firstCol = 5;
		int lastCol = 7;
		sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol,...
```

**Chunk 정보**
- 🆔 **ID**: `fa33b72efc0b`
- 📍 **라인**: 20-20
- 📊 **토큰**: 98
- 🏷️ **태그**: `class, java, autowired, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **225개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 127 | 56.4% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 27.0 | 98 | 43.6% |

