# 📄 UiDashboardServiceImpl.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/dashboard/impl/UiDashboardServiceImpl.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiDashboardServiceImpl`](#class-uidashboardserviceimpl) - 복잡도: 109 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.HashMap` • `java.util.List` • `java.util.Map` • `java.util.stream.Collectors` • `java.util.stream.Stream` 외 27개 | ⚡ **총 복잡도**: 109 |
| 📊 **총 토큰 수**: 756 |  |



## 🏗️ 클래스

### <a id="class-uidashboardserviceimpl"></a>🎯 `UiDashboardServiceImpl`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 109 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 109 || 📍 **라인 범위** | 42-42 |
| 🏷️ **태그** | `class, java, transactional, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiDashboardServiceImpl implements UiDashboardService {
	
	private final CommonDao commonDao;
	
	private final DsbdViewPstMapper dsbdViewPstMapper;
	
	private final DsbdViewMapper dsbdViewMapper;
	
	private final DsbdMonthlyOfferMapper dsbdMonthlyOfferMapper;
	
	private final DsbdMonthlyUserGroupOfferMapper dsbdMonthlyUserGroupOfferMapper; 
	
	private final DsbdListViewMapper dsbdListViewMapper;
	
	@Override
	public Map<String, Object> initData(String userId) {
		Map<String, Object> results = new HashMap<>();
		List<DsbdListViewEntity> dsbdListViewEntities = commonDao.selectList("ui-dsbd-view-m.findListDsbdView", userId);
		List<DsbdListViewDto> dsbdListViewDtos = dsbdListViewEntities.stream().map(item -> dsbdListViewMapper.entityToDto(item)).toList();
		Map<String, Object> par...
```

**Chunk 정보**
- 🆔 **ID**: `76b1f4a460c1`
- 📍 **라인**: 42-42
- 📊 **토큰**: 343
- 🏷️ **태그**: `class, java, transactional, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **756개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 413 | 54.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 109.0 | 343 | 45.4% |

