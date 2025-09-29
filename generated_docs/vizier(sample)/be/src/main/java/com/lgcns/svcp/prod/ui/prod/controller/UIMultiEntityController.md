# 📄 UIMultiEntityController.java

> **파일 경로**: `vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/UIMultiEntityController.java`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 🌐 API 엔드포인트

### `GET /item-relation`
- **설명**: unknown 메서드
- **파라미터**: 
- **응답**: void
- **인증**: False
- **라인**: 61


## 🗄️ 데이터베이스 엔티티

### `UIMultiEntityController`
- **테이블**: `ui_multi_entity_controller`
- **주요 필드**: 
- **관계**: 
- **라인**: 33


## 💼 비즈니스 로직

### `UIMultiEntityController`
- **도메인**: product_ui
- **목적**: UIMultiEntityController API 엔드포인트 제공
- **복잡도**: 60
- **관련 파일**: ./sample_code/vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/*
- **라인**: 33


## 📑 목차

### 🏗️ 클래스
- [`UIMultiEntityController`](#class-uimultientitycontroller) - 복잡도: 60 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `java.util.Map` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.PostMapping` • `org.springframework.web.bind.annotation.PutMapping` • `org.springframework.web.bind.annotation.RequestBody` 외 16개 | ⚡ **총 복잡도**: 60 |
| 📊 **총 토큰 수**: 570 |  |



## 🏗️ 클래스

### <a id="class-uimultientitycontroller"></a>🎯 `UIMultiEntityController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 60 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 60 || 📍 **라인 범위** | 33-33 |
| 🏷️ **태그** | `class, java, getmapping, postmapping, putmapping, requestparam, requestbody, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UIMultiEntityController {

	private final UIMultiEntityService uiMultiEntityService;

	@GetMapping(value = "/search-info")
	@Operation(summary = "Multi Entity 검색을 위한 정보 조회", description = "Multi Entity 검색을 위한 정보 조회")
	public List<SelectOptionDto> retrieveSearchInfo() {
		return uiMultiEntityService.retrieveSearchInfo();
	}

	@GetMapping()
	@Operation(summary = "Multi Entity 목록 조회", description = "Multi Entity 목록 조회")
	public PageResult<?> retrieveMultiEntityList(@RequestParam String itemCode,
			@RequestParam(required = false) String entityTypeCode,
			@RequestParam(required = false) String multiEntityCode,
			@RequestParam(required = false) String multiEntityName,
			@RequestParam(required = false) boolean onlyValidDtm,
			@RequestParam(defaultValue = "1", required = false) i...
```

**Chunk 정보**
- 🆔 **ID**: `86b4122c987d`
- 📍 **라인**: 33-33
- 📊 **토큰**: 256
- 🏷️ **태그**: `class, java, getmapping, postmapping, putmapping...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **570개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 314 | 55.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 60.0 | 256 | 44.9% |

