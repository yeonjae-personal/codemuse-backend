# 📄 UiCalendarServiceImpl.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/dashboard/impl/UiCalendarServiceImpl.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiCalendarServiceImpl`](#class-uicalendarserviceimpl) - 복잡도: 124 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.Comparator` • `java.util.HashMap` • `java.util.List` • `java.util.Map` • `java.util.Set` 외 14개 | ⚡ **총 복잡도**: 124 |
| 📊 **총 토큰 수**: 842 |  |



## 🏗️ 클래스

### <a id="class-uicalendarserviceimpl"></a>🎯 `UiCalendarServiceImpl`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 124 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 124 || 📍 **라인 범위** | 28-28 |
| 🏷️ **태그** | `class, java, transactional, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiCalendarServiceImpl implements UiCalendarService {
	
	private final CommonDao commonDao;
	
	@Override
	public List<CalendarViewRespone> getCalendar(String dsbdViewUuid, String dateInput) {
		String userId = UserContext.getCurrentUser();
		List<CalendarViewRespone> responses = new ArrayList<>();
		Set<String> datesByMonthAndYearSet = getDatesByMonthAndYear(userId, dsbdViewUuid, dateInput);
		for (String item: datesByMonthAndYearSet) {
			CalendarViewRespone calendarViewRespone = new CalendarViewRespone();
			List<DsbdUserSetEntity> dates = getDateBySeqAndSort(userId, dsbdViewUuid, item);
			List<CalendarViewDto> calViewDtoes = new ArrayList<>();
			for (DsbdUserSetEntity date: dates) {
				Map<String, Object> paramsFindCalBySeq = new HashMap<>();
				paramsFindCalBySeq.put("d...
```

**Chunk 정보**
- 🆔 **ID**: `870dea41a983`
- 📍 **라인**: 28-28
- 📊 **토큰**: 399
- 🏷️ **태그**: `class, java, transactional, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **842개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 443 | 52.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 124.0 | 399 | 47.4% |

