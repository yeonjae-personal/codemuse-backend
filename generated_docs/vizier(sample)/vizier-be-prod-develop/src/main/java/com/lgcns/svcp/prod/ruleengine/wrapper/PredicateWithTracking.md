# 📄 PredicateWithTracking.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ruleengine/wrapper/PredicateWithTracking.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`PredicateWithTracking`](#interface-predicatewithtracking)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.jeasy.rules.api.Facts` | ⚡ **총 복잡도**: 3 |
| 📊 **총 토큰 수**: 31 |  |




## 🔌 인터페이스

### <a id="interface-predicatewithtracking"></a>🔌 `PredicateWithTracking`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 8-8 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface PredicateWithTracking {
	boolean test(Facts facts, List<String> failedConditions, List<String> passedConditions);
}...
```

**Chunk 정보**
- 🆔 **ID**: `1cc831f7cff7`
- 📊 **토큰**: 12

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **31개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 19 | 61.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 3.0 | 12 | 38.7% |

