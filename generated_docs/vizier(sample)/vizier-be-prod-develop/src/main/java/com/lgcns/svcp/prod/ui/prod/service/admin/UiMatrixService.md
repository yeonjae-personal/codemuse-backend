# 📄 UiMatrixService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/admin/UiMatrixService.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiMatrixService`](#class-uimatrixservice) - 복잡도: 195 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.io.BufferedReader` • `java.io.IOException` • `java.io.InputStreamReader` • `java.math.BigDecimal` • `java.util.ArrayList` • `java.util.Collections` 외 30개 | ⚡ **총 복잡도**: 195 |
| 📊 **총 토큰 수**: 1,594 |  |



## 🏗️ 클래스

### <a id="class-uimatrixservice"></a>🎯 `UiMatrixService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 195 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 195 || 📍 **라인 범위** | 45-45 |
| 🏷️ **태그** | `class, java, transactional, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiMatrixService {
	private final CommonDao commonDao;
	private final MatrixExcelHelper excelHelper;
	private final MessageSource messageSource;

	public PageResult<?> searchMatrix(SearchMatrixReqDto reqDto) {
		return commonDao.selectPagedList("Ui-matrix.searchMatrix", reqDto);
	}

	public List<BuilderFactorDto> retrieveMatrixBuilder(String matrixCode) {
		return commonDao.selectList("Ui-matrix.retrieveMatrixBuilder", matrixCode);
	}

	public List<MatrixMeasureMDto> retrieveMatrix(String matrixCode, BuilderReqDto builderReqDto) {
		if (builderReqDto == null || builderReqDto.getBuilderDtos() == null) {
			return commonDao.selectList("Ui-matrix.retrieveMatrix", matrixCode);
		}
		return generateCartesianMatrix(matrixCode, builderReqDto.getBuilderDtos());
	}

	private List<Matrix...
```

**Chunk 정보**
- 🆔 **ID**: `084e95e591b4`
- 📍 **라인**: 45-45
- 📊 **토큰**: 759
- 🏷️ **태그**: `class, java, transactional, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **1,594개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 835 | 52.4% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 195.0 | 759 | 47.6% |

