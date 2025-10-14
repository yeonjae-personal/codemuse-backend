# 📄 UiDashboardService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/dashboard/UiDashboardService.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`UiDashboardService`](#interface-uidashboardservice)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `java.util.Map` • `com.lgcns.svcp.prod.ui.prod.dto.dashboard.DsbdViewDto` • `com.lgcns.svcp.prod.ui.prod.dto.dashboard.request.DsbdViewPstRequest` • `com.lgcns.svcp.prod.ui.prod.dto.dashboard.response.DsbdMonthlyOfferResponse` • `com.lgcns.svcp.prod.ui.prod.dto.dashboard.response.DsbdMonthlyUserGroupOfferResponse` 외 1개 | ⚡ **총 복잡도**: 9 |
| 📊 **총 토큰 수**: 66 |  |




## 🔌 인터페이스

### <a id="interface-uidashboardservice"></a>🔌 `UiDashboardService`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 12-12 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface UiDashboardService {
	Map<String, Object> initData(String userId);
	DsbdViewDto findViewByUuid(String dsbdViewUuid);
	void saveListView(List<DsbdViewPstRequest> requests);
	ItemVolumeRespone getItemsVolume();
	Map<String, List<DsbdMonthlyUserGroupOfferResponse>> getMonthlyReportAboutUsers();
	Map<String, List<DsbdMonthlyOfferResponse>> getMonthlyReportAboutItems();
	List<String> getOfferType();
}...
```

**Chunk 정보**
- 🆔 **ID**: `195534ff5224`
- 📊 **토큰**: 25

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **66개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 41 | 62.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 9.0 | 25 | 37.9% |

