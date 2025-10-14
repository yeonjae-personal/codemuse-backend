# 📄 RuleCategoryService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ruleengine/service/RuleCategoryService.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`RuleCategoryService`](#class-rulecategoryservice) - 복잡도: 156 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.Date` • `java.util.HashMap` • `java.util.LinkedHashMap` • `java.util.List` • `java.util.Map` 외 8개 | ⚡ **총 복잡도**: 156 |
| 📊 **총 토큰 수**: 1,008 |  |



## 🏗️ 클래스

### <a id="class-rulecategoryservice"></a>🎯 `RuleCategoryService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 156 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 156 || 📍 **라인 범위** | 22-22 |
| 🏷️ **태그** | `class, java, autowired, param, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class RuleCategoryService {
	
	@Autowired
	private CommonDao commonDao;

	public List<RuleCategoryTreeDto> retrieveRuleCategoryList() {
		return commonDao.selectList("Rule-Category.retrieveRuleCategoryDto");
	}
	
	public List<RuleCategoryTreeDto> getCategory(String name) {
		return commonDao.selectList("Rule-Category.getCategory", formatNameWithSpecialText(name));
	}
	
	public List<RuleCategoryTreeDto> getCategoryByName(String sortBy, String name) {
		if (StringUtils.isNotBlank(sortBy) && sortBy.equals("category")) {
			Map<String, Object> params = new HashMap<>();
			params.put("ruleCtgrName", name.toLowerCase().trim());
			return commonDao.selectList("Rule-Category.retrieveRuleCategoryByName", params);
		} else {
			return commonDao.selectList("Rule-Category.retrieveRuleCategoryDt...
```

**Chunk 정보**
- 🆔 **ID**: `6ed3cb1f6d0a`
- 📍 **라인**: 22-22
- 📊 **토큰**: 488
- 🏷️ **태그**: `class, java, autowired, param, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **1,008개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 520 | 51.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 156.0 | 488 | 48.4% |

