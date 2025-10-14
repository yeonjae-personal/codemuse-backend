# 📄 UIPublishController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/UIPublishController.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UIPublishController`](#class-uipublishcontroller) - 복잡도: 95 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.PathVariable` • `org.springframework.web.bind.annotation.PostMapping` • `org.springframework.web.bind.annotation.PutMapping` • `org.springframework.web.bind.annotation.RequestBody` • `org.springframework.web.bind.annotation.RequestMapping` 외 13개 | ⚡ **총 복잡도**: 95 |
| 📊 **총 토큰 수**: 806 |  |



## 🏗️ 클래스

### <a id="class-uipublishcontroller"></a>🎯 `UIPublishController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 95 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 95 || 📍 **라인 범위** | 29-29 |
| 🏷️ **태그** | `class, java, getmapping, postmapping, putmapping, pathvariable, requestparam, requestbody, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UIPublishController {
	private final UIPublishService uiPublishService;

	@GetMapping(value = "/packages")
	@Operation(summary = "Package Search", description = "Package Search")
	public PageResult<?> searchPackages(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(required = false) String pubRqstTaskCode,
			@RequestParam(required = false) String pubRqstTaskCodeName,
			@RequestParam(required = false) String pubRqstTaskPubr,
			@RequestParam(required = false) String pubRqstStusCode) {

		PubRqstTaskMSearchReqDto reqDto = new PubRqstTaskMSearchReqDto();
		reqDto.setPage(page);
		reqDto.setSize(size);
		reqDto.setPubRqstTaskCode(pubRqstTaskCode);
		reqDto.setPubRqstTaskCodeName(pubRqstTas...
```

**Chunk 정보**
- 🆔 **ID**: `cbb7a165c998`
- 📍 **라인**: 29-29
- 📊 **토큰**: 377
- 🏷️ **태그**: `class, java, getmapping, postmapping, putmapping...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **806개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 429 | 53.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 95.0 | 377 | 46.8% |

