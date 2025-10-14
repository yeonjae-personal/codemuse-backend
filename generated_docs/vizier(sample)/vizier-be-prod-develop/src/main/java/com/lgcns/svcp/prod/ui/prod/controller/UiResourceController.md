# 📄 UiResourceController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/UiResourceController.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiResourceController`](#class-uiresourcecontroller) - 복잡도: 64 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `java.util.Map` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.ModelAttribute` • `org.springframework.web.bind.annotation.PostMapping` • `org.springframework.web.bind.annotation.PutMapping` 외 19개 | ⚡ **총 복잡도**: 64 |
| 📊 **총 토큰 수**: 507 |  |



## 🏗️ 클래스

### <a id="class-uiresourcecontroller"></a>🎯 `UiResourceController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 64 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 64 || 📍 **라인 범위** | 36-36 |
| 🏷️ **태그** | `class, java, getmapping, postmapping, putmapping, requestparam, requestbody, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiResourceController {

	private final UiResourceService uiResourceService;

	@GetMapping(value = "/resource/items")
	@Operation()
	public List<Item> getAllItem(@RequestParam(required = false) String mItemCode,
			@RequestParam(required = false) String lItemCode, @RequestParam(required = false) String itemCode) {
		ItemReq req = new ItemReq();
		req.setItemCode(itemCode);
		req.setMItemCode(mItemCode);
		req.setLItemCode(lItemCode);
		return uiResourceService.getAllItem(req);
	}

	@GetMapping(value = "/resource/create-info")
	@Operation
	public ResourceCreateInfoRes getCreateResourceForm(@RequestParam String itemCode) {
		return uiResourceService.getResourceForm(itemCode);
	}

	@PostMapping(value = "/resource")
	@Operation()
	public Map<String, String> createResource(@RequestB...
```

**Chunk 정보**
- 🆔 **ID**: `2791cfc53893`
- 📍 **라인**: 36-36
- 📊 **토큰**: 222
- 🏷️ **태그**: `class, java, getmapping, postmapping, putmapping...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **507개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 285 | 56.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 64.0 | 222 | 43.8% |

