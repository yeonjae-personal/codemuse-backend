# 📄 RuleService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ruleengine/service/RuleService.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`RuleService`](#class-ruleservice) - 복잡도: 350 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.Arrays` • `java.util.Date` • `java.util.HashMap` • `java.util.LinkedHashMap` • `java.util.List` 외 24개 | ⚡ **총 복잡도**: 350 |
| 📊 **총 토큰 수**: 2,592 |  |



## 🏗️ 클래스

### <a id="class-ruleservice"></a>🎯 `RuleService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 350 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 350 || 📍 **라인 범위** | 39-39 |
| 🏷️ **태그** | `class, java, autowired, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class RuleService {

	@Autowired
	private ConditionService conditionService;

	@Autowired
	private CommonDao commonDao;

	// 메모리에 등록된 룰 저장소
	private final Map<String, RuleWrapper> ruleRegistry = new ConcurrentHashMap<>();

	private RuleDto getRuleInfoByUuid(String ruleUuid) {
		return commonDao.select("Rule-Rule.selectRuleByUuid", ruleUuid);
	}

	private List<String> getRuleUuids() {
		return commonDao.selectList("Rule-Rule.selectRuleUuids");
	}

	//룰 등록
	public void registerRule(String ruleUuid) {
		ConditionGroupDto root = conditionService.getRuleConditionTree(ruleUuid);
		RuleDto ruleInfo = getRuleInfoByUuid(ruleUuid);
		PredicateWithTracking predicate = convertGroupToPredicate(root);

		RuleWrapper wrapper = new RuleWrapper(
				ruleUuid,
				ruleInfo.getRuleName(),
				ruleInfo...
```

**Chunk 정보**
- 🆔 **ID**: `df2b1f9a6a45`
- 📍 **라인**: 39-39
- 📊 **토큰**: 1264
- 🏷️ **태그**: `class, java, autowired, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **2,592개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 1,328 | 51.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 350.0 | 1,264 | 48.8% |

