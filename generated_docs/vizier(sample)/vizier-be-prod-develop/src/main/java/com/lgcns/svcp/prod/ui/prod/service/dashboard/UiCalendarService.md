# 📄 UiCalendarService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/dashboard/UiCalendarService.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`UiCalendarService`](#interface-uicalendarservice)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.dashboard.request.CalendarViewRequest` • `com.lgcns.svcp.prod.ui.prod.dto.dashboard.response.CalendarViewRespone` | ⚡ **총 복잡도**: 4 |
| 📊 **총 토큰 수**: 34 |  |




## 🔌 인터페이스

### <a id="interface-uicalendarservice"></a>🔌 `UiCalendarService`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 7-7 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface UiCalendarService {
	void saveCalendar(CalendarViewRequest request);
	List<CalendarViewRespone> getCalendar(String dsbdViewUuid, String dateInput);
}...
```

**Chunk 정보**
- 🆔 **ID**: `531ddc2bb9c0`
- 📊 **토큰**: 13

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **34개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 21 | 61.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 4.0 | 13 | 38.2% |

