# 📄 AdditionalCreateDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/common/AdditionalCreateDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`AdditionalCreateDto`](#class-additionalcreatedto) - 복잡도: 7

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.fasterxml.jackson.annotation.JsonInclude` • `lombok.Data` | ⚡ **총 복잡도**: 7 |
| 📊 **총 토큰 수**: 45 |  |



## 🏗️ 클래스

### <a id="class-additionalcreatedto"></a>🎯 `AdditionalCreateDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 7 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 7 || 📍 **라인 범위** | 10-10 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class AdditionalCreateDto {
	private String attrUuid;
	private String attrNm;
	private String attrFieldCd;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<Attribute> selectOptions;
}...
```

**Chunk 정보**
- 🆔 **ID**: `ca2dc8bf44c8`
- 📍 **라인**: 10-10
- 📊 **토큰**: 18
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **45개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 27 | 60.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 7.0 | 18 | 40.0% |

