# 📄 ExcelHelperImpl.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/util/excel/ExcelHelperImpl.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ExcelHelperImpl`](#class-excelhelperimpl) - 복잡도: 87 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.awt.Color` • `java.io.IOException` • `java.lang.reflect.Field` • `java.text.DateFormat` • `java.text.SimpleDateFormat` • `java.util.ArrayList` 외 18개 | ⚡ **총 복잡도**: 87 |
| 📊 **총 토큰 수**: 592 |  |



## 🏗️ 클래스

### <a id="class-excelhelperimpl"></a>🎯 `ExcelHelperImpl`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 87 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 87 || 📍 **라인 범위** | 33-33 |
| 🏷️ **태그** | `class, java, autowired, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ExcelHelperImpl implements ExcelHelper {

	private Class<?> tClass;

	@Autowired
	public MessageSource messageSource;

	@Override
	public void downloadExcel(ExcelInput input, HttpServletResponse response, boolean isCustomTemplate) {
		if (System.getProperty("org.apache.poi.ss.ignoreMissingFontSystem") == null) {
			System.setProperty("org.apache.poi.ss.ignoreMissingFontSystem", "true");
		}
		ExcelWriter excelWriter = new ExcelWriter(input.getExtention());
		try (ServletOutputStream outputStream = response.getOutputStream();
				Workbook workbook = excelWriter.getWorkbook();) {
			response.setContentType("application/octet-stream");
			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename="
					+ createExcelFileName(input.getFileName(), input...
```

**Chunk 정보**
- 🆔 **ID**: `aa22ebac8827`
- 📍 **라인**: 33-33
- 📊 **토큰**: 270
- 🏷️ **태그**: `class, java, autowired, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **592개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 322 | 54.4% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 87.0 | 270 | 45.6% |

