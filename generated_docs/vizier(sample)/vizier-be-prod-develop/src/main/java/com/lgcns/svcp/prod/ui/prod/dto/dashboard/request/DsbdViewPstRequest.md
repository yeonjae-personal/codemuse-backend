# 📄 DsbdViewPstRequest.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/dashboard/request/DsbdViewPstRequest.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`DsbdViewPstRequest`](#class-dsbdviewpstrequest) - 복잡도: 8

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `jakarta.validation.constraints.NotBlank` • `jakarta.validation.constraints.NotNull` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 8 |
| 📊 **총 토큰 수**: 46 |  |



## 🏗️ 클래스

### <a id="class-dsbdviewpstrequest"></a>🎯 `DsbdViewPstRequest`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 8 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 8 || 📍 **라인 범위** | 11-11 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class DsbdViewPstRequest {
	
	@NotBlank
	private String dsbdViewUuid;
	
	@NotNull
	private Integer posX;
	
	@NotNull
	private Integer posY;
}...
```

**Chunk 정보**
- 🆔 **ID**: `8755d8f48024`
- 📍 **라인**: 11-11
- 📊 **토큰**: 17
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **46개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 29 | 63.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 8.0 | 17 | 37.0% |

