# 📄 RegisteredRuleInfo.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ruleengine/dto/rule/RegisteredRuleInfo.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`RegisteredRuleInfo`](#class-registeredruleinfo) - 복잡도: 24

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.ruleengine.dto.condition.ConditionGroupDto` | ⚡ **총 복잡도**: 24 |
| 📊 **총 토큰 수**: 140 |  |



## 🏗️ 클래스

### <a id="class-registeredruleinfo"></a>🎯 `RegisteredRuleInfo`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 24 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 24 || 📍 **라인 범위** | 5-5 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class RegisteredRuleInfo {
    private String ruleUuid;
    private String ruleName;
    private String ruleMsg;
    private ConditionGroupDto conditionTree;

    public RegisteredRuleInfo(String ruleUuid, String ruleName, String ruleMsg, ConditionGroupDto conditionTree) {
        this.ruleUuid = ruleUuid;
        this.ruleName = ruleName;
        this.ruleMsg = ruleMsg;
        this.conditionTree = conditionTree;
    }

    public String getRuleUuid() {
        return ruleUuid;
    }

    public String getRuleName() {
        return ruleName;
    }
    
    public String getRuleMsg() {
        return ruleMsg;
    }

    public ConditionGroupDto getConditionTree() {
        return conditionTree;
    }
}...
```

**Chunk 정보**
- 🆔 **ID**: `a47b49175039`
- 📍 **라인**: 5-5
- 📊 **토큰**: 68
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **140개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 72 | 51.4% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 24.0 | 68 | 48.6% |

