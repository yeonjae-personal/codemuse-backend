# 📄 MatrixMeasureMDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/admin/matrix/MatrixMeasureMDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`MatrixMeasureMDto`](#class-matrixmeasuremdto) - 복잡도: 7

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.math.BigDecimal` • `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.BaseDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 7 |
| 📊 **총 토큰 수**: 58 |  |



## 🏗️ 클래스

### <a id="class-matrixmeasuremdto"></a>🎯 `MatrixMeasureMDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseDto` |
| ⚡ 복잡도 | 7 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 7 || 📍 **라인 범위** | 13-13 |
| 🏗️ **상속** | `BaseDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class MatrixMeasureMDto extends BaseDto {
    private String matrixCode;
    private long rowId;
    private BigDecimal matrixNumValue;
    private List<MatrixMeasureDDto> measureDDtos;
    private boolean isChanged;
}...
```

**Chunk 정보**
- 🆔 **ID**: `43f006d12ffc`
- 📍 **라인**: 13-13
- 📊 **토큰**: 22
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **58개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 36 | 62.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 7.0 | 22 | 37.9% |

