# 📄 BaseDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/BaseDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`BaseDto`](#class-basedto) - 복잡도: 10

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.fasterxml.jackson.annotation.JsonInclude` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 10 |
| 📊 **총 토큰 수**: 52 |  |



## 🏗️ 클래스

### <a id="class-basedto"></a>🎯 `BaseDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 10 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 10 || 📍 **라인 범위** | 10-10 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class BaseDto {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String rgstUser;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String rgstDtm;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String updUser;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String updDtm;
}...
```

**Chunk 정보**
- 🆔 **ID**: `9bd5f648d90e`
- 📍 **라인**: 10-10
- 📊 **토큰**: 21
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **52개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 31 | 59.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 10.0 | 21 | 40.4% |

