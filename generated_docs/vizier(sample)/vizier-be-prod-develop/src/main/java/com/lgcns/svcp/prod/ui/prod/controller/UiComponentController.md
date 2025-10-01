# 📄 UiComponentController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/UiComponentController.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 🌐 API 엔드포인트

### `GET /component/export`
- **설명**: unknown 메서드
- **파라미터**: 
- **응답**: void
- **인증**: False
- **라인**: 95



## 💼 비즈니스 로직

### `UiComponentController`
- **도메인**: user
- **목적**: UiComponentController API 엔드포인트 제공
- **복잡도**: 86
- **관련 파일**: /Users/roseline/projects/codemuse-backend/sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/*
- **라인**: 40


## 📑 목차

### 🏗️ 클래스
- [`UiComponentController`](#class-uicomponentcontroller) - 복잡도: 86 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `java.util.Map` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.ModelAttribute` • `org.springframework.web.bind.annotation.PostMapping` • `org.springframework.web.bind.annotation.PutMapping` 외 23개 | ⚡ **총 복잡도**: 86 |
| 📊 **총 토큰 수**: 727 |  |



## 🏗️ 클래스

### <a id="class-uicomponentcontroller"></a>🎯 `UiComponentController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 86 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 86 || 📍 **라인 범위** | 40-40 |
| 🏷️ **태그** | `class, java, getmapping, postmapping, putmapping, requestparam, requestbody, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiComponentController {

    private final UiComponentService uiComponentService;

    @GetMapping(value = "component/create-info")
    @Operation(summary = "Get component create information")
	public ComponentCreateInfoRes getComponentCreateForm(@RequestParam String itemCode) {
		return uiComponentService.getCreateComponentForm(itemCode);
    }

    @PostMapping(value = "component")
    @Operation(summary = "Create Component")
	public Map<String, String> createComponent(@RequestBody CreateComponentReq req) {
        return uiComponentService.createComponent(req);
    }

    @GetMapping(value = "/component/by-resource")
    @Operation(summary = "Get component impact analysis by resource id")
	public List<ComponentGeneralDto> getComponentImpactAnalysis(@RequestParam String reso...
```

**Chunk 정보**
- 🆔 **ID**: `16184edb7b5c`
- 📍 **라인**: 40-40
- 📊 **토큰**: 329
- 🏷️ **태그**: `class, java, getmapping, postmapping, putmapping...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **727개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 398 | 54.7% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 86.0 | 329 | 45.3% |

