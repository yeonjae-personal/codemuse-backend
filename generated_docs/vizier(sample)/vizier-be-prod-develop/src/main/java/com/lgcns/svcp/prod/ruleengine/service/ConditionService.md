# 📄 ConditionService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ruleengine/service/ConditionService.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ConditionService`](#class-conditionservice) - 복잡도: 56 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.HashMap` • `java.util.List` • `java.util.Map` • `org.springframework.beans.factory.annotation.Autowired` • `org.springframework.stereotype.Service` 외 4개 | ⚡ **총 복잡도**: 56 |
| 📊 **총 토큰 수**: 472 |  |



## 🏗️ 클래스

### <a id="class-conditionservice"></a>🎯 `ConditionService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 56 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 56 || 📍 **라인 범위** | 20-20 |
| 🏷️ **태그** | `class, java, autowired, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ConditionService {
	
	@Autowired
	private CommonDao commonDao;
	
	public List<ConditionGroupDto> selectConditionGroupsByRuleId(String ruleUuid) {
		return commonDao.selectList("Rule-Condition.selectConditionGroupsByRuleId", ruleUuid);
	}
	
	public List<ConditionDto> selectConditionsByRuleId(String ruleUuid) {
		return commonDao.selectList("Rule-Condition.selectConditionsByRuleId", ruleUuid);
	}
	
	public ConditionGroupDto getRuleConditionTree(String ruleUuid) {
	    List<ConditionGroupDto> groupRows = selectConditionGroupsByRuleId(ruleUuid);
	    List<ConditionDto> condRows = selectConditionsByRuleId(ruleUuid);
	    Map<String, ConditionGroupDto> groupMap = new HashMap<>();
	    Map<String, String> parentMap = new HashMap<>();

	    // 그룹 먼저 생성
	    for (ConditionGroupDto row ...
```

**Chunk 정보**
- 🆔 **ID**: `8f8a352c7fe0`
- 📍 **라인**: 20-20
- 📊 **토큰**: 224
- 🏷️ **태그**: `class, java, autowired, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **472개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 248 | 52.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 56.0 | 224 | 47.5% |

