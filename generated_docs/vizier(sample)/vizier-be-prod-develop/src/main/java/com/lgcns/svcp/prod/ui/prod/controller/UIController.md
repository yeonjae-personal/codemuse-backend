# 📄 UIController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/UIController.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UIController`](#class-uicontroller) - 복잡도: 9 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.springframework.web.bind.annotation.CrossOrigin` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.RequestMapping` • `org.springframework.web.bind.annotation.RequestParam` • `org.springframework.web.bind.annotation.RestController` 외 5개 | ⚡ **총 복잡도**: 9 |
| 📊 **총 토큰 수**: 113 |  |



## 🏗️ 클래스

### <a id="class-uicontroller"></a>🎯 `UIController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 9 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 9 || 📍 **라인 범위** | 23-23 |
| 🏷️ **태그** | `class, java, getmapping, requestparam, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UIController {

	private final UiCommonService uiCommonService;

	@GetMapping(value = "/common/item-structure")
	@Operation(summary = "아이템 구조 조회", description = "아이템 구조 조회")
	public List<ItemStructureDto> retreiveItemStructure(@RequestParam(required = false) String itemCode,
			@RequestParam(required = false) String mctgrItemCode) {
		return uiCommonService.retreiveItemStructure(itemCode, mctgrItemCode);
	}
}...
```

**Chunk 정보**
- 🆔 **ID**: `25cc28775363`
- 📍 **라인**: 23-23
- 📊 **토큰**: 39
- 🏷️ **태그**: `class, java, getmapping, requestparam, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **113개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 74 | 65.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 9.0 | 39 | 34.5% |

