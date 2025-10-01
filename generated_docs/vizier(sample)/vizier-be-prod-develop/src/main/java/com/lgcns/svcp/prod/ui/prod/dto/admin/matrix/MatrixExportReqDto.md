# 📄 MatrixExportReqDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/admin/matrix/MatrixExportReqDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`MatrixExportReqDto`](#class-matrixexportreqdto) - 복잡도: 6

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.builder.BuilderFactorDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 6 |
| 📊 **총 토큰 수**: 46 |  |



## 🏗️ 클래스

### <a id="class-matrixexportreqdto"></a>🎯 `MatrixExportReqDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 6 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 6 || 📍 **라인 범위** | 12-12 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class MatrixExportReqDto {
    private String matrixCode;
    private String matrixCodeName;

    private List<MatrixMeasureMDto> measureMDtos;
    private List<BuilderFactorDto> builderDtos;
}...
```

**Chunk 정보**
- 🆔 **ID**: `78c24ab6c5d0`
- 📍 **라인**: 12-12
- 📊 **토큰**: 17
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **46개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 29 | 63.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 6.0 | 17 | 37.0% |

