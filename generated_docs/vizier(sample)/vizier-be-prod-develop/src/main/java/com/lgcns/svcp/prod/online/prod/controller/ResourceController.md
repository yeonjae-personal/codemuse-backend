# 📄 ResourceController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/controller/ResourceController.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ResourceController`](#class-resourcecontroller) - 복잡도: 43 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.springframework.beans.factory.annotation.Autowired` • `org.springframework.http.ResponseEntity` • `org.springframework.web.bind.annotation.CrossOrigin` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.PathVariable` 외 6개 | ⚡ **총 복잡도**: 43 |
| 📊 **총 토큰 수**: 433 |  |



## 🏗️ 클래스

### <a id="class-resourcecontroller"></a>🎯 `ResourceController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 43 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 43 || 📍 **라인 범위** | 23-23 |
| 🏷️ **태그** | `class, java, autowired, getmapping, pathvariable, param, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ResourceController {
	@Autowired
	private ResourceService resourceService;

	@GetMapping(value = "/billing-elements")
	@Operation(summary = "빌링요소 전체 조회 API", description = "빌링요소 상세정보 전체 리스트 조회")
	public ResponseEntity<List<?>> retrieveBillingElementList() {
		List<?> response = resourceService.retrieveBillingElementList();

		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/billing-elements/{blngrsccode}")
	@Operation(summary = "빌링요소 단건 조회 API", description = "빌링요소코드로 빌링요소 상세정보 조회")
	public ResponseEntity<List<?>> retrieveBillingElement(@Parameter(description ="빌링요소코드", required = true, example = "RSBE000030")
	@PathVariable("blngrsccode") String blngRscCode) {
		List<?> response = resourceService.retrieveBillingElement(blngRscCode);
		return ResponseEntity.ok(r...
```

**Chunk 정보**
- 🆔 **ID**: `954636620e69`
- 📍 **라인**: 23-23
- 📊 **토큰**: 198
- 🏷️ **태그**: `class, java, autowired, getmapping, pathvariable...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **433개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 235 | 54.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 43.0 | 198 | 45.7% |

