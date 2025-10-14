# 📄 MatrixExcelHelper.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/customexcel/MatrixExcelHelper.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`MatrixExcelHelper`](#interface-matrixexcelhelper)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.MatrixExportReqDto` • `com.lgcns.svcp.prod.util.excel.ExcelHelper` • `jakarta.servlet.http.Part` | ⚡ **총 복잡도**: 5 |
| 📊 **총 토큰 수**: 50 |  |




## 🔌 인터페이스

### <a id="interface-matrixexcelhelper"></a>🔌 `MatrixExcelHelper`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 8-8 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface MatrixExcelHelper extends ExcelHelper {
    String DEFAULT_FONT = "LG스마트체 Regular";
    short DEFAULT_FONT_SIZE = 10;

    MatrixExportReqDto parseMatrixFromExcel(Part filePart, MatrixExportReqDto matrixExportReqDto);
}...
```

**Chunk 정보**
- 🆔 **ID**: `2304c43ceb59`
- 📊 **토큰**: 21

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **50개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 29 | 58.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 5.0 | 21 | 42.0% |

