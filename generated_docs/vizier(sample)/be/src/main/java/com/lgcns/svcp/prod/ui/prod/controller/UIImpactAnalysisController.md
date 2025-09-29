# 📄 UIImpactAnalysisController.java

> **파일 경로**: `vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/UIImpactAnalysisController.java`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UIImpactAnalysisController`](#class-uiimpactanalysiscontroller) - 복잡도: 74 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.springframework.beans.factory.annotation.Autowired` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.RequestMapping` • `org.springframework.web.bind.annotation.RequestParam` • `org.springframework.web.bind.annotation.RestController` 외 9개 | ⚡ **총 복잡도**: 74 |
| 📊 **총 토큰 수**: 675 |  |



## 🏗️ 클래스

### <a id="class-uiimpactanalysiscontroller"></a>🎯 `UIImpactAnalysisController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 74 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 74 || 📍 **라인 범위** | 25-25 |
| 🏷️ **태그** | `class, java, autowired, getmapping, requestparam, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UIImpactAnalysisController {
	@Autowired
	private UiImpactAnalysisService uiImpactAnalysisService;

	@GetMapping(value = "/relation")
	@Operation(summary = "(화면) 부모,형제상품 조회 API", description = "부모,형제상품 uuid, 코드, 명칭 조회")
	public ImpactAnalysisResponseDto getImpactAnalysisResponseDto(@RequestParam String prodUuid) {
		ItemDto request = new ItemDto();
		request.setProdUuid(prodUuid);
		return uiImpactAnalysisService.getImpactAnalysisResponseDto(request);
	}

	@GetMapping(value = "/children")
	@Operation(summary = "(화면) 자식상품 조회 API", description = "자식상품 uuid, 코드, 명칭 조회")
	public List<ItemDto> getSiblings(@RequestParam String prodUuid) {
		ItemDto request = new ItemDto();
		request.setProdUuid(prodUuid);
		return uiImpactAnalysisService.getChildrenProdM(request);
	}

	@GetMapping(v...
```

**Chunk 정보**
- 🆔 **ID**: `8008b91abb6b`
- 📍 **라인**: 25-25
- 📊 **토큰**: 316
- 🏷️ **태그**: `class, java, autowired, getmapping, requestparam...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **675개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 359 | 53.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 74.0 | 316 | 46.8% |

