# 📄 CategoryController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/controller/CategoryController.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`CategoryController`](#class-categorycontroller) - 복잡도: 62 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.LinkedHashMap` • `java.util.List` • `java.util.Map` • `java.util.stream.Collectors` • `org.springframework.beans.factory.annotation.Autowired` 외 11개 | ⚡ **총 복잡도**: 62 |
| 📊 **총 토큰 수**: 619 |  |



## 🏗️ 클래스

### <a id="class-categorycontroller"></a>🎯 `CategoryController`

> 📝 **클래스 설명**  
> /**

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 62 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 62 || 📍 **라인 범위** | 33-33 |
| 🏷️ **태그** | `class, java, autowired, getmapping, pathvariable, param, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |
#### 📚 Javadoc 상세

```
/**
```


<details>
<summary>🔍 코드 미리보기</summary>

```java
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	@GetMapping(value = "")
	@Operation(summary = "카테고리 기본 전체 조회 API", description = "카테고리와 연결된 상위카테고리 정보 전체 리스트 조회")
	public ResponseEntity<List<CtgrNodeMDto>> retrieveCtgrNodeMList() {
		List<CtgrNodeMDto> response = categoryService.retrieveCtgrNodeMList();

		return ResponseEntity.ok(response);	
	}

	@GetMapping(value = "/{catgUuid}")
	@Operation(summary = "특정 카테고리UUID로 상세 정보조회 API", description = "카테고리 UUID로 Category Node 조회")
	public ResponseEntity<CtgrNodeMDto> retrieveCatgMByCatgUuid(@Parameter(description ="카테고리 UUID", required = true, example = "0230027b-b136-4011-b0fa-cedd9e2437df") @PathVariable("catgUuid") String catgUuid) {
		CtgrNodeMDto request = new CtgrNodeMDto();
		request.setCtgrNodeUui...
```

**Chunk 정보**
- 🆔 **ID**: `858fcdbd24b5`
- 📍 **라인**: 33-33
- 📊 **토큰**: 280
- 🏷️ **태그**: `class, java, autowired, getmapping, pathvariable...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **619개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 339 | 54.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 62.0 | 280 | 45.2% |

