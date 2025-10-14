# 📄 ConditionGroupDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ruleengine/dto/condition/ConditionGroupDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ConditionGroupDto`](#class-conditiongroupdto) - 복잡도: 9

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.fasterxml.jackson.annotation.JsonInclude` • `lombok.Data` | ⚡ **총 복잡도**: 9 |
| 📊 **총 토큰 수**: 53 |  |



## 🏗️ 클래스

### <a id="class-conditiongroupdto"></a>🎯 `ConditionGroupDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 9 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 9 || 📍 **라인 범위** | 10-10 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ConditionGroupDto {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String condGroupUuid;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String hpstCondGroupUuid;
	private String logicType;
	private Integer sortNo;
	private List<Object> condition;
}...
```

**Chunk 정보**
- 🆔 **ID**: `058d433b2ac2`
- 📍 **라인**: 10-10
- 📊 **토큰**: 22
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **53개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 31 | 58.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 9.0 | 22 | 41.5% |

