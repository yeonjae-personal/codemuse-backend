# 📄 AdditionalDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/common/AdditionalDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`AdditionalDto`](#class-additionaldto) - 복잡도: 12

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.fasterxml.jackson.annotation.JsonIgnore` • `com.fasterxml.jackson.annotation.JsonInclude` • `lombok.Data` | ⚡ **총 복잡도**: 12 |
| 📊 **총 토큰 수**: 69 |  |



## 🏗️ 클래스

### <a id="class-additionaldto"></a>🎯 `AdditionalDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 12 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 12 || 📍 **라인 범위** | 11-11 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class AdditionalDto {
	@JsonIgnore
	private String prodUuid;
	private String attrUuid;
	private String attrNm;
	private String attrVal;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String attrDescription;
	private String fieldType;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<Attribute> selectOptions;

}...
```

**Chunk 정보**
- 🆔 **ID**: `82b02b4f483e`
- 📍 **라인**: 11-11
- 📊 **토큰**: 29
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **69개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 40 | 58.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 12.0 | 29 | 42.0% |

