# 📄 UiMetadataController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/UiMetadataController.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiMetadataController`](#class-uimetadatacontroller) - 복잡도: 13 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.springframework.http.HttpStatus` • `org.springframework.http.ResponseEntity` • `org.springframework.web.bind.annotation.CrossOrigin` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.RequestMapping` 외 8개 | ⚡ **총 복잡도**: 13 |
| 📊 **총 토큰 수**: 143 |  |



## 🏗️ 클래스

### <a id="class-uimetadatacontroller"></a>🎯 `UiMetadataController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 13 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 13 || 📍 **라인 범위** | 26-26 |
| 🏷️ **태그** | `class, java, getmapping, requestparam, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiMetadataController {
	
	private final MetadataService metadataService;
	
	@GetMapping("/middle-item")
	@Operation(summary = "load middle category item", description = "load middle category item")
	public List<MiddleItemDto> getMiddleItem(@RequestParam(required = false) String largeItemCode) {
		return metadataService.getMiddleItem(largeItemCode);
	}
	
	@GetMapping("/large-item")
	@Operation(summary = "load large item", description = "load large item")
	public List<LargeItemDto> getLargeItem() {
		return metadataService.getLargeItem();
	}
}...
```

**Chunk 정보**
- 🆔 **ID**: `4d0f225b53da`
- 📍 **라인**: 26-26
- 📊 **토큰**: 51
- 🏷️ **태그**: `class, java, getmapping, requestparam, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **143개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 92 | 64.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 13.0 | 51 | 35.7% |

