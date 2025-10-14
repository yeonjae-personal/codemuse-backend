# 📄 UiMatrixController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/admin/UiMatrixController.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiMatrixController`](#class-uimatrixcontroller) - 복잡도: 40 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.PathVariable` • `org.springframework.web.bind.annotation.PostMapping` • `org.springframework.web.bind.annotation.PutMapping` • `org.springframework.web.bind.annotation.RequestBody` 외 15개 | ⚡ **총 복잡도**: 40 |
| 📊 **총 토큰 수**: 361 |  |



## 🏗️ 클래스

### <a id="class-uimatrixcontroller"></a>🎯 `UiMatrixController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 40 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 40 || 📍 **라인 범위** | 32-32 |
| 🏷️ **태그** | `class, java, getmapping, postmapping, putmapping, pathvariable, requestparam, requestbody, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiMatrixController {
	private final UiMatrixService uiMatrixService;

	@GetMapping()
	@Operation(summary = "Matrix 조회", description = "Matrix 조회")
	public PageResult<?> searchMatrix(@RequestParam(required = false) String matrixCodeName,
			@RequestParam(defaultValue = "1", required = false) int page,
			@RequestParam(defaultValue = "10", required = false) int size) {

		SearchMatrixReqDto reqDto = new SearchMatrixReqDto();
		reqDto.setSize(size);
		reqDto.setPage(page);
		reqDto.setMatrixCodeName(matrixCodeName);

		return uiMatrixService.searchMatrix(reqDto);
	}

	@GetMapping("/builder")
	@Operation(summary = "Matrix Builder 상세조회", description = "Matrix Builder 상세조회")
	public List<BuilderFactorDto> retrieveMatrixBuilder(String matrixCode) {
		return uiMatrixService.retrieveMa...
```

**Chunk 정보**
- 🆔 **ID**: `9988c9e83b6c`
- 📍 **라인**: 32-32
- 📊 **토큰**: 153
- 🏷️ **태그**: `class, java, getmapping, postmapping, putmapping...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **361개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 208 | 57.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 40.0 | 153 | 42.4% |

