# 📄 ConditionDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ruleengine/dto/condition/ConditionDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ConditionDto`](#class-conditiondto) - 복잡도: 12

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.fasterxml.jackson.annotation.JsonIgnore` • `lombok.Data` | ⚡ **총 복잡도**: 12 |
| 📊 **총 토큰 수**: 73 |  |



## 🏗️ 클래스

### <a id="class-conditiondto"></a>🎯 `ConditionDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 12 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 12 || 📍 **라인 범위** | 8-8 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ConditionDto {
	@JsonIgnore
	private String condGroupUuid;
	private String condUuid;
	private String keyName;
    private String dispName;
    private String operator;
    private String value;
    private String fieldDataType;
    private String fieldUuid;
    private Integer sortNo;
}...
```

**Chunk 정보**
- 🆔 **ID**: `e2089a222ce8`
- 📍 **라인**: 8-8
- 📊 **토큰**: 33
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **73개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 40 | 54.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 12.0 | 33 | 45.2% |

