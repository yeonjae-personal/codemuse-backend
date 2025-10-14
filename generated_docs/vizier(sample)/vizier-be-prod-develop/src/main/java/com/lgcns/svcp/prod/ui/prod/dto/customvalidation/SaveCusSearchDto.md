# 📄 SaveCusSearchDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/customvalidation/SaveCusSearchDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`SaveCusSearchDto`](#class-savecussearchdto) - 복잡도: 14 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.List` • `jakarta.validation.Valid` • `jakarta.validation.constraints.NotBlank` • `jakarta.validation.constraints.NotNull` • `lombok.Getter` 외 1개 | ⚡ **총 복잡도**: 14 |
| 📊 **총 토큰 수**: 98 |  |



## 🏗️ 클래스

### <a id="class-savecussearchdto"></a>🎯 `SaveCusSearchDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 14 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 14 || 📍 **라인 범위** | 14-14 |
| 🏷️ **태그** | `class, java, valid, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class SaveCusSearchDto {
	
	private String validCode;
	
	private String validCntn;
	
	@NotNull
	private Integer seqNo;
	
	private String condItemCode;
	
	@NotBlank
	private String validStartDtm;
	
	private String validEndDtm;
	
	private boolean isCreated = false;
	
	private boolean isUpdated;
	
	@Valid
	private List<AttributeDto> attributes = new ArrayList<>();
}...
```

**Chunk 정보**
- 🆔 **ID**: `9ad547715a62`
- 📍 **라인**: 14-14
- 📊 **토큰**: 40
- 🏷️ **태그**: `class, java, valid, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **98개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 58 | 59.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 14.0 | 40 | 40.8% |

