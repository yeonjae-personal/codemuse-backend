# 📄 UIHistoryController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/UIHistoryController.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UIHistoryController`](#class-uihistorycontroller) - 복잡도: 8 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.RequestMapping` • `org.springframework.web.bind.annotation.RequestParam` • `org.springframework.web.bind.annotation.RestController` • `com.lgcns.svcp.prod.ui.prod.dto.history.detail.HistoryDetailResDto` • `com.lgcns.svcp.prod.ui.prod.service.UIHistoryService` 외 4개 | ⚡ **총 복잡도**: 8 |
| 📊 **총 토큰 수**: 100 |  |



## 🏗️ 클래스

### <a id="class-uihistorycontroller"></a>🎯 `UIHistoryController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 8 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 8 || 📍 **라인 범위** | 21-21 |
| 🏷️ **태그** | `class, java, getmapping, requestparam, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UIHistoryController {
	private final UIHistoryService uiHistoryService;

	@GetMapping("/detail")
	@Operation(summary = "Item History 상세 정보 조회", description = "Item History 상세 정보 조회")
	public HistoryDetailResDto retrieveHistoryDetail(@RequestParam String objUuid) {
		return uiHistoryService.retrieveHistoryDetail(objUuid);
	}
}...
```

**Chunk 정보**
- 🆔 **ID**: `85f21d0d026f`
- 📍 **라인**: 21-21
- 📊 **토큰**: 33
- 🏷️ **태그**: `class, java, getmapping, requestparam, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **100개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 67 | 67.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 8.0 | 33 | 33.0% |

