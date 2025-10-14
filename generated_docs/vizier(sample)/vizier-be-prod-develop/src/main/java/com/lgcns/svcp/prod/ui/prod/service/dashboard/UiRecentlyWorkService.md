# 📄 UiRecentlyWorkService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/dashboard/UiRecentlyWorkService.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`UiRecentlyWorkService`](#interface-uirecentlyworkservice)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.ui.prod.dto.Sorting` • `com.lgcns.svcp.prod.ui.prod.dto.dashboard.RecentlyWorkSearchPagingDto` • `jakarta.servlet.http.HttpServletResponse` | ⚡ **총 복잡도**: 4 |
| 📊 **총 토큰 수**: 58 |  |




## 🔌 인터페이스

### <a id="interface-uirecentlyworkservice"></a>🔌 `UiRecentlyWorkService`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 8-8 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface UiRecentlyWorkService {
	Object getRecentlyWork(RecentlyWorkSearchPagingDto searchPaging, Integer page, Integer size);
	void exportExcel(String category, String type, String searchBy, String searchValue, Sorting sorting, HttpServletResponse response);
}...
```

**Chunk 정보**
- 🆔 **ID**: `39445be3d131`
- 📊 **토큰**: 25

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **58개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 33 | 56.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 4.0 | 25 | 43.1% |

