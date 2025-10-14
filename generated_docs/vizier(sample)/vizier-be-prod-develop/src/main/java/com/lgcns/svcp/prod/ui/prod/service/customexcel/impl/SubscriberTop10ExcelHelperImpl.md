# 📄 SubscriberTop10ExcelHelperImpl.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/customexcel/impl/SubscriberTop10ExcelHelperImpl.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`SubscriberTop10ExcelHelperImpl`](#class-subscribertop10excelhelperimpl) - 복잡도: 48 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `java.util.Map` • `org.apache.poi.ss.usermodel.Cell` • `org.apache.poi.ss.usermodel.CellStyle` • `org.apache.poi.ss.usermodel.Font` • `org.apache.poi.ss.usermodel.IndexedColors` 외 14개 | ⚡ **총 복잡도**: 48 |
| 📊 **총 토큰 수**: 369 |  |



## 🏗️ 클래스

### <a id="class-subscribertop10excelhelperimpl"></a>🎯 `SubscriberTop10ExcelHelperImpl`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `ExcelHelperImpl` |
| ⚡ 복잡도 | 48 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 48 || 📍 **라인 범위** | 28-28 |
| 🏗️ **상속** | `ExcelHelperImpl` || 🏷️ **태그** | `class, java, autowired, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class SubscriberTop10ExcelHelperImpl extends ExcelHelperImpl implements SubscriberTop10ExcelHelper {
	
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("unchecked")
	@Override
	public void handleCustomTemplate(Workbook workbook, Object object) {
		ExcelCellStyleSupportCustom excelCell = new ExcelCellStyleSupportCustom(workbook);
		Map<String, Object> maps = (Map<String, Object>) object;
		XSSFSheet sheet = (XSSFSheet) workbook.getSheet(DashboardConstant.SUBSCRIBE_TOP_10_ITEM_NAME);
		createBatchDateStyle(sheet, maps);
		createStyleStatusColumn(sheet, maps, excelCell);
	}

	@SuppressWarnings("unchecked")
	private void createStyleStatusColumn(Sheet sheet, Map<String, Object> maps, ExcelCellStyleSupportCustom excelCellStyle) {
		List<DsbdOfferSubCntEntity> datas = ...
```

**Chunk 정보**
- 🆔 **ID**: `8710dcafd6f2`
- 📍 **라인**: 28-28
- 📊 **토큰**: 163
- 🏷️ **태그**: `class, java, autowired, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **369개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 206 | 55.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 48.0 | 163 | 44.2% |

