# 📄 UiUserPocketServiceImpl.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/impl/UiUserPocketServiceImpl.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiUserPocketServiceImpl`](#class-uiuserpocketserviceimpl) - 복잡도: 95 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.time.LocalDate` • `java.util.ArrayList` • `java.util.Comparator` • `java.util.Date` • `java.util.HashMap` • `java.util.List` 외 20개 | ⚡ **총 복잡도**: 95 |
| 📊 **총 토큰 수**: 650 |  |



## 🏗️ 클래스

### <a id="class-uiuserpocketserviceimpl"></a>🎯 `UiUserPocketServiceImpl`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 95 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 95 || 📍 **라인 범위** | 35-35 |
| 🏷️ **태그** | `class, java, transactional, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiUserPocketServiceImpl implements UiUserPocketService {
	
	private final CommonDao commonDao;
	private final UserPocketMapper userPocketMapper;
	private final MessageSource messageSource;
	
	@Override
	public List<UserPocketRespone> getUserPocket(String userId) {
		List<UserPocketRespone> results = new ArrayList<>();
		List<UserPocketCustomEntity> userPocketCustomEntities = commonDao.selectList("ui-user-pocket-m.getAll", userId);
		Map<String, List<UserPocketCustomEntity>> mapEntities = userPocketCustomEntities.stream()
								.collect(Collectors.groupingBy(UserPocketCustomEntity::getLctgrItemCode));
		for(Map.Entry<String, List<UserPocketCustomEntity>> entry : mapEntities.entrySet()) { 
			UserPocketRespone userPocketRespone = new UserPocketRespone();
			userPocketRespone.s...
```

**Chunk 정보**
- 🆔 **ID**: `1a39a9b77300`
- 📍 **라인**: 35-35
- 📊 **토큰**: 297
- 🏷️ **태그**: `class, java, transactional, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **650개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 353 | 54.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 95.0 | 297 | 45.7% |

