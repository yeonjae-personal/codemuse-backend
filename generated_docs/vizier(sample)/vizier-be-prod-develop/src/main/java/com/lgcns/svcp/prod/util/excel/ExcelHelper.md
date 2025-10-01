# 📄 ExcelHelper.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/util/excel/ExcelHelper.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`ExcelHelper`](#interface-excelhelper)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `org.apache.poi.ss.usermodel.Workbook` • `jakarta.servlet.http.HttpServletResponse` | ⚡ **총 복잡도**: 8 |
| 📊 **총 토큰 수**: 68 |  |




## 🔌 인터페이스

### <a id="interface-excelhelper"></a>🔌 `ExcelHelper`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 7-7 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface ExcelHelper {
	void downloadExcel(ExcelInput input, HttpServletResponse response, boolean isCustomTemplate);

	String[] createTitleHeader(Object... object);

	void createSheet(ExcelWriter excelWriter, ExcelInput excelInput);

	default void handleCustomTemplate(Workbook workbook, Object object) {
	}

	ExcelHelper of(Class<?> tClass);
}...
```

**Chunk 정보**
- 🆔 **ID**: `e42d477ba791`
- 📊 **토큰**: 31

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **68개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 37 | 54.4% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 8.0 | 31 | 45.6% |

