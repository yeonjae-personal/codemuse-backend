# 📄 RuleWrapper.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ruleengine/wrapper/RuleWrapper.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`RuleWrapper`](#class-rulewrapper) - 복잡도: 38

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.List` • `org.jeasy.rules.api.Facts` • `com.lgcns.svcp.prod.ruleengine.dto.condition.ConditionGroupDto` • `com.lgcns.svcp.prod.ruleengine.dto.rule.EvaluationResult` | ⚡ **총 복잡도**: 38 |
| 📊 **총 토큰 수**: 264 |  |



## 🏗️ 클래스

### <a id="class-rulewrapper"></a>🎯 `RuleWrapper`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 38 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 38 || 📍 **라인 범위** | 11-11 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class RuleWrapper {
    private final String ruleUuid;
    private final String ruleName;
    private final String ruleMsg;
    private final PredicateWithTracking predicate;
    private final Runnable thenAction;
    private final ConditionGroupDto conditionTree; // 추가

    public RuleWrapper(String ruleUuid, String ruleName, String ruleMsg, PredicateWithTracking predicate, 
    		Runnable thenAction, ConditionGroupDto conditionTree) {
        this.ruleUuid = ruleUuid;
        this.ruleName = ruleName;
        this.ruleMsg = ruleMsg;
        this.predicate = predicate;
        this.thenAction = thenAction;
        this.conditionTree = conditionTree;
    }

    public EvaluationResult evaluate(Facts facts) {
        List<String> failed = new ArrayList<>();
        List<String> passe...
```

**Chunk 정보**
- 🆔 **ID**: `42c81535d72d`
- 📍 **라인**: 11-11
- 📊 **토큰**: 126
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **264개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 138 | 52.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 38.0 | 126 | 47.7% |

