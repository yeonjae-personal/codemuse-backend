# 📄 ExcelInput.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/util/excel/ExcelInput.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ExcelInput`](#class-excelinput) - 복잡도: 44

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.List` | ⚡ **총 복잡도**: 44 |
| 📊 **총 토큰 수**: 250 |  |



## 🏗️ 클래스

### <a id="class-excelinput"></a>🎯 `ExcelInput`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 44 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 44 || 📍 **라인 범위** | 6-6 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ExcelInput {
	
	private List<?> datas = new ArrayList<>();
	private String extention;
	private String sheetName;
	private String fileName;
	private String formatDate;
	private Object object;

	public String getExtention() {
		return extention;
	}
	public void setExtention(String extention) {
		this.extention = extention;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public List<?> getDatas() {
		return datas;
	}
	public void setDatas(List<?> datas) {
		this.datas = datas;
	}
	public String getFormatDate() {
		return formatDate;
	}
	public void setFormatDate(Str...
```

**Chunk 정보**
- 🆔 **ID**: `fba26e262a73`
- 📍 **라인**: 6-6
- 📊 **토큰**: 122
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **250개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 128 | 51.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 44.0 | 122 | 48.8% |

