# 📄 RuleFireResult.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ruleengine/dto/rule/RuleFireResult.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`RuleFireResult`](#class-rulefireresult) - 복잡도: 11

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.fasterxml.jackson.annotation.JsonInclude` • `lombok.Data` | ⚡ **총 복잡도**: 11 |
| 📊 **총 토큰 수**: 75 |  |



## 🏗️ 클래스

### <a id="class-rulefireresult"></a>🎯 `RuleFireResult`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 11 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 11 || 📍 **라인 범위** | 10-10 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class RuleFireResult {
    private boolean success;
    private String passedMessage;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> failedConditions;

    public RuleFireResult(boolean success, String passedMessage, List<String> failedConditions) {
        this.success = success;
        this.passedMessage = passedMessage;
        this.failedConditions = failedConditions;
    }
}...
```

**Chunk 정보**
- 🆔 **ID**: `ca2b5e4f4c2f`
- 📍 **라인**: 10-10
- 📊 **토큰**: 33
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **75개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 42 | 56.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 11.0 | 33 | 44.0% |

