# 📄 FieldController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ruleengine/controller/FieldController.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`FieldController`](#class-fieldcontroller) - 복잡도: 19 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `org.springframework.http.ResponseEntity` • `org.springframework.web.bind.annotation.CrossOrigin` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.PostMapping` • `org.springframework.web.bind.annotation.RequestBody` • `org.springframework.web.bind.annotation.RequestMapping` 외 10개 | ⚡ **총 복잡도**: 19 |
| 📊 **총 토큰 수**: 202 |  |



## 🏗️ 클래스

### <a id="class-fieldcontroller"></a>🎯 `FieldController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 19 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 19 || 📍 **라인 범위** | 27-27 |
| 🏷️ **태그** | `class, java, getmapping, postmapping, requestparam, requestbody, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class FieldController {
	
	private final FieldService fieldService;
	
	@GetMapping
	@Operation(summary = "search field", description = "search field")
	public PageResult<FieldDto> searchField(@RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size, 
				@RequestParam(name = "type", required = false) String type, @RequestParam(name = "value", required = false) String value, HttpServletRequest request) {
		
		FieldSearchPagingDto searchPaging = new FieldSearchPagingDto();
		searchPaging.setPage(page);
		searchPaging.setSize(size);
		searchPaging.setType(type);
		searchPaging.setValue(value);
	    return fieldService.retrieveAllFields(searchPaging);
	}
	
	@PostMapping
	@Operation(summary = "save field", description = "save field")
	public void saveField(@Re...
```

**Chunk 정보**
- 🆔 **ID**: `b89c8fb805cc`
- 📍 **라인**: 27-27
- 📊 **토큰**: 78
- 🏷️ **태그**: `class, java, getmapping, postmapping, requestparam...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **202개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 124 | 61.4% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 19.0 | 78 | 38.6% |

