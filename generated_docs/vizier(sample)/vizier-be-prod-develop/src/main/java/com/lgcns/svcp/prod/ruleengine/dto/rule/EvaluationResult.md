# 📄 EvaluationResult.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ruleengine/dto/rule/EvaluationResult.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`EvaluationResult`](#class-evaluationresult) - 복잡도: 12

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `lombok.Data` | ⚡ **총 복잡도**: 12 |
| 📊 **총 토큰 수**: 87 |  |



## 🏗️ 클래스

### <a id="class-evaluationresult"></a>🎯 `EvaluationResult`

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
public class EvaluationResult {
    private boolean passed;
    private String passedMessage;
    private List<String> failedCondUuids;
    private List<String> passedCondUuids;

    public EvaluationResult(boolean passed, String passedMessage, List<String> failed, List<String> passedList) {
        this.passed = passed;
        this.passedMessage = passedMessage;
        this.failedCondUuids = failed;
        this.passedCondUuids = passedList;
    }
}...
```

**Chunk 정보**
- 🆔 **ID**: `33a1e5758cc6`
- 📍 **라인**: 8-8
- 📊 **토큰**: 40
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **87개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 47 | 54.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 12.0 | 40 | 46.0% |

