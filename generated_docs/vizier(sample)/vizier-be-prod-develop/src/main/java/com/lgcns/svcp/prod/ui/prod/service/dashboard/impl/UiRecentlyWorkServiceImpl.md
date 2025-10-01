# 📄 UiRecentlyWorkServiceImpl.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/dashboard/impl/UiRecentlyWorkServiceImpl.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiRecentlyWorkServiceImpl`](#class-uirecentlyworkserviceimpl) - 복잡도: 133

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.HashMap` • `java.util.List` • `java.util.Map` • `org.springframework.context.MessageSource` • `org.springframework.context.i18n.LocaleContextHolder` 외 22개 | ⚡ **총 복잡도**: 133 |
| 📊 **총 토큰 수**: 952 |  |



## 🏗️ 클래스

### <a id="class-uirecentlyworkserviceimpl"></a>🎯 `UiRecentlyWorkServiceImpl`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 133 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 133 || 📍 **라인 범위** | 37-37 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiRecentlyWorkServiceImpl implements UiRecentlyWorkService {
	
	private final CommonDao commonDao;
	private final DsbdRecentWorkMapper recentWorkMapper; 
	private final RecentWorkExcelHelper excelHelper;
	private final MessageSource messageSource;
	
	@Override
	public Object getRecentlyWork(RecentlyWorkSearchPagingDto searchPaging, Integer page, Integer size) {
		RecentlyWorkSearchPagingDto properties = buildProperties(searchPaging, page, size);
		PageResult<DsbdRecentlyWorkEntity> pageResultRecentlyWorkEntities = commonDao.selectPagedList("ui-dsbd-recently-work-d.findProperties", properties);
		List<DsbdRecentlyWorkEntity> recentlyWorkEntities = pageResultRecentlyWorkEntities.getElements();
		if (searchPaging.getView().equals("simple")) {
			return recentlyWorkEntities.stream...
```

**Chunk 정보**
- 🆔 **ID**: `8aae9ba6441e`
- 📍 **라인**: 37-37
- 📊 **토큰**: 446
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **952개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 506 | 53.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 133.0 | 446 | 46.8% |

