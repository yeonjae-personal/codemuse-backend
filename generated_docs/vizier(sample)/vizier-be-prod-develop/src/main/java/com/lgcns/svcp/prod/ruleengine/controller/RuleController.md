# 📄 RuleController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ruleengine/controller/RuleController.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`RuleController`](#class-rulecontroller) - 복잡도: 23 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.jeasy.rules.api.Facts` • `org.springframework.beans.factory.annotation.Autowired` • `org.springframework.web.bind.annotation.CrossOrigin` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.PostMapping` 외 10개 | ⚡ **총 복잡도**: 23 |
| 📊 **총 토큰 수**: 167 |  |



## 🏗️ 클래스

### <a id="class-rulecontroller"></a>🎯 `RuleController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 23 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 23 || 📍 **라인 범위** | 27-27 |
| 🏷️ **태그** | `class, java, autowired, getmapping, postmapping, requestbody, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class RuleController {
	
	@Autowired
	private RuleService ruleService;
	
	@PostMapping("/test")
	public EvaluationResult testRule(@RequestBody RuleTestInput input) {
		Facts facts = new Facts();
		input.getFactsData().forEach(facts::put);
		EvaluationResult result = ruleService.testRule(facts, input);
		return result;
	}

	// 등록된 룰 확인용
	@GetMapping("/registered-details")
	public List<RegisteredRuleInfo> listRegisteredRulesWithConditions() {
		return ruleService.listRegisteredRulesWithConditions();
	}
	
	@PostMapping
	public RuleInput saveRule(@RequestBody RuleInput ruleInput) {
		return ruleService.saveRule(ruleInput);
	}
	
	@PostMapping("/structure")
	public void saveRuleStructure(@RequestBody RuleInsertRequest request) {
	    ruleService.insertRuleWithTree(request);
	}
}...
```

**Chunk 정보**
- 🆔 **ID**: `7c73698a40d7`
- 📍 **라인**: 27-27
- 📊 **토큰**: 61
- 🏷️ **태그**: `class, java, autowired, getmapping, postmapping...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **167개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 106 | 63.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 23.0 | 61 | 36.5% |

