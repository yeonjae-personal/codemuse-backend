# 📄 RuleCategoryTreeDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ruleengine/dto/category/RuleCategoryTreeDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`RuleCategoryTreeDto`](#class-rulecategorytreedto) - 복잡도: 15

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.fasterxml.jackson.annotation.JsonInclude` • `lombok.Data` | ⚡ **총 복잡도**: 15 |
| 📊 **총 토큰 수**: 93 |  |



## 🏗️ 클래스

### <a id="class-rulecategorytreedto"></a>🎯 `RuleCategoryTreeDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 15 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 15 || 📍 **라인 범위** | 10-10 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class RuleCategoryTreeDto {
	private String ruleCtgrUuid;
	private String ruleCtgrName;
	private String hpstRuleCtgrUuid;
	private String overview;
	private String tclsCtgrYn;
	private String useYn;
	private String rgstUser;
	private String rgstDtm;
	private String updUser;
	private String updDtm;
	private List<RuleCategoryTreeDto> children;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<RuleInfoDto> rules;
}...
```

**Chunk 정보**
- 🆔 **ID**: `53ddaa0939a6`
- 📍 **라인**: 10-10
- 📊 **토큰**: 42
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **93개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 51 | 54.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 15.0 | 42 | 45.2% |

