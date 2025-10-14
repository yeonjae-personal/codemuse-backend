# 📄 UiTableController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/admin/UiTableController.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiTableController`](#class-uitablecontroller) - 복잡도: 59 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.PathVariable` • `org.springframework.web.bind.annotation.PostMapping` • `org.springframework.web.bind.annotation.PutMapping` • `org.springframework.web.bind.annotation.RequestBody` 외 15개 | ⚡ **총 복잡도**: 59 |
| 📊 **총 토큰 수**: 552 |  |



## 🏗️ 클래스

### <a id="class-uitablecontroller"></a>🎯 `UiTableController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 59 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 59 || 📍 **라인 범위** | 32-32 |
| 🏷️ **태그** | `class, java, getmapping, postmapping, putmapping, pathvariable, requestparam, requestbody, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiTableController {
	private final UiTableService uiTableService;

	@GetMapping()
	@Operation(summary = "테이블 유형 조회", description = "테이블 유형 조회")
	public PageResult<?> searchTableType(@RequestParam(required = false) String tableTypeName,
			@RequestParam(required = false) String tableTypeCode,
			@RequestParam(required = false) String useYn, @RequestParam(defaultValue = "1", required = false) int page,
			@RequestParam(defaultValue = "10", required = false) int size) {

		SearchTableTypeReqDto reqDto = new SearchTableTypeReqDto();
		reqDto.setSize(size);
		reqDto.setPage(page);
		reqDto.setTableTypeName(tableTypeName);
		reqDto.setTableTypeCode(tableTypeCode);
		reqDto.setUseYn(useYn);

		return uiTableService.searchTableType(reqDto);
	}

	@GetMapping("/{tableTypeCode}")
	@Opera...
```

**Chunk 정보**
- 🆔 **ID**: `735f836751ab`
- 📍 **라인**: 32-32
- 📊 **토큰**: 248
- 🏷️ **태그**: `class, java, getmapping, postmapping, putmapping...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **552개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 304 | 55.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 59.0 | 248 | 44.9% |

