# 📄 UIExtendsController.java

> **파일 경로**: `vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/UIExtendsController.java`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UIExtendsController`](#class-uiextendscontroller) - 복잡도: 75 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.PostMapping` • `org.springframework.web.bind.annotation.PutMapping` • `org.springframework.web.bind.annotation.RequestBody` • `org.springframework.web.bind.annotation.RequestMapping` 외 16개 | ⚡ **총 복잡도**: 75 |
| 📊 **총 토큰 수**: 703 |  |



## 🏗️ 클래스

### <a id="class-uiextendscontroller"></a>🎯 `UIExtendsController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 75 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 75 || 📍 **라인 범위** | 33-33 |
| 🏷️ **태그** | `class, java, getmapping, postmapping, putmapping, requestparam, requestbody, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UIExtendsController {

	private final UIExtendsService uiExtendsService;

	@GetMapping(value = "/target")
	@Operation(summary = "리더/폴로어의 그룹 조회", description = "리더/폴로어의 그룹 조회")
	public TargetResDto getTarget(@RequestParam String offerUuid,
			@RequestParam(required = false) boolean onlyValidDtm,
			@RequestParam(required = false) boolean includeGroup) {
		TargetReqDto req = new TargetReqDto();
		req.setOfferUuid(offerUuid);
		req.setOnlyValidDtm(onlyValidDtm);
		req.setIncludeGroup(includeGroup);

		return uiExtendsService.getTarget(req);
	}

	@GetMapping(value = "/leader")
	@Operation(summary = "리더 조회", description = "리더 조회")
	public List<RelationViewResDto> getLeader(@RequestParam String targetUuid,
			@RequestParam(required = false) boolean onlyValidDtm,
			@RequestParam(req...
```

**Chunk 정보**
- 🆔 **ID**: `4f67ce454e40`
- 📍 **라인**: 33-33
- 📊 **토큰**: 323
- 🏷️ **태그**: `class, java, getmapping, postmapping, putmapping...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **703개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 380 | 54.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 75.0 | 323 | 45.9% |

