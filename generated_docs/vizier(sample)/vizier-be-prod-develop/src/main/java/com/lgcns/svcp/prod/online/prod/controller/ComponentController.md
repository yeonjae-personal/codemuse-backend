# 📄 ComponentController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/controller/ComponentController.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `ComponentController`
- **도메인**: user
- **목적**: ComponentController API 엔드포인트 제공
- **복잡도**: 147
- **관련 파일**: /Users/roseline/projects/codemuse-backend/sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/controller/*
- **라인**: 29


## 📑 목차

### 🏗️ 클래스
- [`ComponentController`](#class-componentcontroller) - 복잡도: 147 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.springframework.beans.factory.annotation.Autowired` • `org.springframework.http.ResponseEntity` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.PathVariable` • `org.springframework.web.bind.annotation.RequestMapping` 외 13개 | ⚡ **총 복잡도**: 147 |
| 📊 **총 토큰 수**: 1,507 |  |



## 🏗️ 클래스

### <a id="class-componentcontroller"></a>🎯 `ComponentController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 147 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 147 || 📍 **라인 범위** | 29-29 |
| 🏷️ **태그** | `class, java, autowired, getmapping, pathvariable, param, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ComponentController {
	@Autowired
	private ComponentService componentService;
	
	@GetMapping(value = "/price/basefees")
	@Operation(summary = "기본료 전체 조회 API", description = "기본료 상세정보 전체 리스트 조회")
	public ResponseEntity<List<?>> retrieveBaseFeeList() {
		List<?> response = componentService.retrieveBaseFeeList();

		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/price/basefees/{bsfcode}")
	@Operation(summary = "기본료 단건조회 API", description = "기본료코드로 기본료 상세정보, 연결된 리소스 조회")
	public ResponseEntity<BaseFeeAndResourceDto> retrieveBaseFeeAndResource(@Parameter(description ="기본료코드", required = true, example = "PRRC000027")
																			@PathVariable("bsfcode") String bsfCode) {
		BaseFeeAndResourceDto response = componentService.retrieveBaseFeeAndResource(bsfCode);
...
```

**Chunk 정보**
- 🆔 **ID**: `48dc2a205c8c`
- 📍 **라인**: 29-29
- 📊 **토큰**: 729
- 🏷️ **태그**: `class, java, autowired, getmapping, pathvariable...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **1,507개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 778 | 51.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 147.0 | 729 | 48.4% |

