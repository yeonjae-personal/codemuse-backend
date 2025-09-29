# 📄 ExcelCellStyleSupportCustom.java

> **파일 경로**: `vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/util/excel/ExcelCellStyleSupportCustom.java`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ExcelCellStyleSupportCustom`](#class-excelcellstylesupportcustom) - 복잡도: 17

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `org.apache.poi.ss.usermodel.CellStyle` • `org.apache.poi.ss.usermodel.Font` • `org.apache.poi.ss.usermodel.Workbook` | ⚡ **총 복잡도**: 17 |
| 📊 **총 토큰 수**: 82 |  |



## 🏗️ 클래스

### <a id="class-excelcellstylesupportcustom"></a>🎯 `ExcelCellStyleSupportCustom`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `ExcelCellStyleSupport` |
| ⚡ 복잡도 | 17 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 17 || 📍 **라인 범위** | 7-7 |
| 🏗️ **상속** | `ExcelCellStyleSupport` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ExcelCellStyleSupportCustom extends ExcelCellStyleSupport {

	public ExcelCellStyleSupportCustom(Workbook workbook) {
		super(workbook);
	}
	
	@Override
	public Font createFont() {
		return super.createFont();
	}
	
	@Override
	public CellStyle createCellStyle() {
		return super.createCellStyle();
	}
	
	@Override
	public CellStyle createTitleCellStyle() {
		return super.createTitleCellStyle();
	}
}...
```

**Chunk 정보**
- 🆔 **ID**: `daef28a5b10f`
- 📍 **라인**: 7-7
- 📊 **토큰**: 37
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **82개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 45 | 54.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 17.0 | 37 | 45.1% |

