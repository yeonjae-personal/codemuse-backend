# 📄 UIGroupController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/UIGroupController.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UIGroupController`](#class-uigroupcontroller) - 복잡도: 62 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `java.util.Map` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.ModelAttribute` • `org.springframework.web.bind.annotation.PostMapping` • `org.springframework.web.bind.annotation.PutMapping` 외 17개 | ⚡ **총 복잡도**: 62 |
| 📊 **총 토큰 수**: 571 |  |



## 🏗️ 클래스

### <a id="class-uigroupcontroller"></a>🎯 `UIGroupController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 62 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 62 || 📍 **라인 범위** | 34-34 |
| 🏷️ **태그** | `class, java, getmapping, postmapping, putmapping, requestparam, requestbody, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UIGroupController {

	private final UIGroupService uiGroupService;

	@GetMapping()
	@Operation(summary = "오퍼그룹 조회", description = "오퍼그룹 조회")
	public Object searchOfferGroup(@RequestParam(required = false) String objCode,
			@RequestParam(required = false) String objName, @RequestParam(required = false) String itemCode,
			@RequestParam(required = false) String childOffrUuid, @RequestParam(required = false) boolean onlyValidDtm,
			@RequestParam(defaultValue = "1", required = false) int page,
			@RequestParam(defaultValue = "10", required = false) int size,
			@RequestParam(defaultValue = "true", required = false) boolean isPaged) {

		SearchGroupReqDto reqDto = new SearchGroupReqDto();
		reqDto.setSize(size);
		reqDto.setPage(page);
		reqDto.setOffrGrpCd(objCode);
		reqDto.set...
```

**Chunk 정보**
- 🆔 **ID**: `eaab850af668`
- 📍 **라인**: 34-34
- 📊 **토큰**: 256
- 🏷️ **태그**: `class, java, getmapping, postmapping, putmapping...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **571개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 315 | 55.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 62.0 | 256 | 44.8% |

