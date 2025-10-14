# 📄 UiLabelManagementController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/UiLabelManagementController.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiLabelManagementController`](#class-uilabelmanagementcontroller) - 복잡도: 54 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.io.IOException` • `java.util.List` • `org.springframework.http.HttpStatus` • `org.springframework.http.ResponseEntity` • `org.springframework.validation.annotation.Validated` • `org.springframework.web.bind.annotation.CrossOrigin` 외 19개 | ⚡ **총 복잡도**: 54 |
| 📊 **총 토큰 수**: 475 |  |



## 🏗️ 클래스

### <a id="class-uilabelmanagementcontroller"></a>🎯 `UiLabelManagementController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 54 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 54 || 📍 **라인 범위** | 38-38 |
| 🏷️ **태그** | `class, java, getmapping, postmapping, putmapping, deletemapping, requestparam, requestbody, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiLabelManagementController {

	private final UiLabelService labelService;

	@GetMapping
	@Operation(summary = "show label", description = "show label")
	public PageResult<MultiLangLabelDto> getData(@RequestParam Integer page, @RequestParam Integer size, 
							@RequestParam(required = false) String type, @RequestParam(required = false) String value, HttpServletRequest request) {
		String language = request.getHeader("X-Language");
		LabelSearchPagingDto searchPaging = new LabelSearchPagingDto();
		if (page != null && size != null) {
			searchPaging.setPage(page);
			searchPaging.setSize(size);
		}
		searchPaging.setType(type);
		searchPaging.setValue(value);
		searchPaging.setLangCode(language);
		return labelService.findAll(searchPaging);
	}

	@GetMapping("/i18n")
	@Operati...
```

**Chunk 정보**
- 🆔 **ID**: `9a920212373c`
- 📍 **라인**: 38-38
- 📊 **토큰**: 204
- 🏷️ **태그**: `class, java, getmapping, postmapping, putmapping...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **475개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 271 | 57.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 54.0 | 204 | 42.9% |

