# 📄 ConditionContoller.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ruleengine/controller/ConditionContoller.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ConditionContoller`](#class-conditioncontoller) - 복잡도: 10 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `org.springframework.beans.factory.annotation.Autowired` • `org.springframework.web.bind.annotation.CrossOrigin` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.RequestMapping` • `org.springframework.web.bind.annotation.RequestParam` • `org.springframework.web.bind.annotation.RestController` 외 4개 | ⚡ **총 복잡도**: 10 |
| 📊 **총 토큰 수**: 120 |  |



## 🏗️ 클래스

### <a id="class-conditioncontoller"></a>🎯 `ConditionContoller`

> 📝 **클래스 설명**  
> /**

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 10 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 10 || 📍 **라인 범위** | 25-25 |
| 🏷️ **태그** | `class, java, autowired, getmapping, requestparam, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |
#### 📚 Javadoc 상세

```
/**
```


<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ConditionContoller {
	@Autowired
	private ConditionService conditionService;
	
	@GetMapping
	@Operation(summary = "조건 기본 전체 조회 API", description = "룰 아이디로 조건 트리 조회")
	public ConditionGroupDto getRuleConditionTree(@RequestParam("ruleUuid") String ruleUuid) {
	    ConditionGroupDto tree = conditionService.getRuleConditionTree(ruleUuid);
	    return tree;
	}
}...
```

**Chunk 정보**
- 🆔 **ID**: `b8a1906b64b4`
- 📍 **라인**: 25-25
- 📊 **토큰**: 37
- 🏷️ **태그**: `class, java, autowired, getmapping, requestparam...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **120개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 83 | 69.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 10.0 | 37 | 30.8% |

