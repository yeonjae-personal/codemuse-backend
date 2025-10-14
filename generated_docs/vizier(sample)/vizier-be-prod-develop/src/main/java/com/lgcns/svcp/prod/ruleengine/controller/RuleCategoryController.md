# 📄 RuleCategoryController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ruleengine/controller/RuleCategoryController.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`RuleCategoryController`](#class-rulecategorycontroller) - 복잡도: 73 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.List` • `java.util.Map` • `java.util.stream.Collectors` • `org.apache.commons.lang3.StringUtils` • `org.springframework.beans.factory.annotation.Autowired` 외 14개 | ⚡ **총 복잡도**: 73 |
| 📊 **총 토큰 수**: 621 |  |



## 🏗️ 클래스

### <a id="class-rulecategorycontroller"></a>🎯 `RuleCategoryController`

> 📝 **클래스 설명**  
> /**

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 73 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 73 || 📍 **라인 범위** | 37-37 |
| 🏷️ **태그** | `class, java, autowired, getmapping, postmapping, deletemapping, pathvariable, requestparam, requestbody, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |
#### 📚 Javadoc 상세

```
/**
```


<details>
<summary>🔍 코드 미리보기</summary>

```java
public class RuleCategoryController {
	
	@Autowired
	private RuleCategoryService ruleCategoryService;
	
	@GetMapping
	@Operation(summary = "룰 카테고리 기본 전체 조회 API", description = "룰 카테고리 기본 정보 전체 조회 API")
	public List<RuleCategoryTreeDto> retrieveRuleCategoryList() {
		List<RuleCategoryTreeDto> response = ruleCategoryService.retrieveRuleCategoryList();
		return response;
	}
	
	@GetMapping(value = "/tree")
	@Operation(summary = "get category tree", description = "get category tree")
	public List<Map<String, Object>> retrieveRuleCatgoryTreeList(@RequestParam(required = false) String searchBy, @RequestParam(required = false) String name) {
		List<RuleCategoryTreeDto> results = new ArrayList<>();
		if (StringUtils.isNotBlank(name) && searchBy.equals("category")) {
			List<RuleCategoryTreeDto> cat...
```

**Chunk 정보**
- 🆔 **ID**: `1a7ba98e26f0`
- 📍 **라인**: 37-37
- 📊 **토큰**: 277
- 🏷️ **태그**: `class, java, autowired, getmapping, postmapping...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **621개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 344 | 55.4% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 73.0 | 277 | 44.6% |

