# 📄 UIPublishService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/UIPublishService.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UIPublishService`](#class-uipublishservice) - 복잡도: 355 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.time.LocalDateTime` • `java.util.ArrayList` • `java.util.Collection` • `java.util.Collections` • `java.util.Comparator` • `java.util.HashMap` 외 39개 | ⚡ **총 복잡도**: 355 |
| 📊 **총 토큰 수**: 2,336 |  |



## 🏗️ 클래스

### <a id="class-uipublishservice"></a>🎯 `UIPublishService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 355 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 355 || 📍 **라인 범위** | 54-54 |
| 🏷️ **태그** | `class, java, transactional, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UIPublishService {
	private final CommonDao commonDao;
	private final UINotificationService uiNotificationService;

	public PageResult<?> searchChngDataDetail(ChngDataListLSearchReqDto reqDto) {
		return commonDao.selectPagedList("Ui-publish.searchChngDataDetail", reqDto);
	}

	public PageResult<?> searchPubRqstTaskMaster(PubRqstTaskMSearchReqDto reqDto) {
		// Trigger updatePubRqstTaskStatus
		commonDao.update("Ui-publish.updatePubRqstTaskStatus");

		return commonDao.selectPagedList("Ui-publish.searchPubRqstTaskMaster", reqDto);
	}

	public PubPackageDto retrievePackage(String pubRqstTaskCode) {
		Map<String, String> params = new HashMap<>();
		params.put("pubRqstTaskCode", pubRqstTaskCode);

		// Trigger updatePubRqstTaskStatus
		commonDao.update("Ui-publish.updatePubRqstTa...
```

**Chunk 정보**
- 🆔 **ID**: `024f850d85ff`
- 📍 **라인**: 54-54
- 📊 **토큰**: 1121
- 🏷️ **태그**: `class, java, transactional, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **2,336개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 1,215 | 52.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 355.0 | 1,121 | 48.0% |

