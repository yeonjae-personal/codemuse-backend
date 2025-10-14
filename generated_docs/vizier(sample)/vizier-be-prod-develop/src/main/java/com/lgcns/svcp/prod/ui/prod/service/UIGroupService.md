# 📄 UIGroupService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/UIGroupService.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UIGroupService`](#class-uigroupservice) - 복잡도: 202 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.constant.SystemConstant.CHG_DEPT_NAME` • `com.lgcns.svcp.prod.constant.SystemConstant.CHG_USER` • `com.lgcns.svcp.prod.constant.SystemConstant.ITEM_CODE` • `com.lgcns.svcp.prod.constant.SystemConstant.LANG_CODE` • `com.lgcns.svcp.prod.constant.SystemConstant.OBJ_CODE` • `com.lgcns.svcp.prod.constant.SystemConstant.OBJ_UUID` 외 40개 | ⚡ **총 복잡도**: 202 |
| 📊 **총 토큰 수**: 1,422 |  |



## 🏗️ 클래스

### <a id="class-uigroupservice"></a>🎯 `UIGroupService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 202 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 202 || 📍 **라인 범위** | 56-56 |
| 🏷️ **태그** | `class, java, transactional, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UIGroupService {
	private static final String OFFER_GROUP_UUID = "offerGroupUuid";

	private final CommonDao commonDao;
	private final UIHistoryService uiHistoryService;
	private final UiCommonService uiCommonService;
	private final UiTableService uiTableService;
	private final GroupExcelHelper excelHelper;
	private final GroupMapper groupMapper;
	private final MessageSource messageSource;


	public Object searchOfferGroup(SearchGroupReqDto searchOffrGrpReqDto) {
		if (searchOffrGrpReqDto.isPaged()) {
			return commonDao.selectPagedList("Ui-group.retrieveOffrGrpList", searchOffrGrpReqDto);
		}
		return commonDao.selectList("Ui-group.retrieveOffrGrpList", searchOffrGrpReqDto);

	}

	@Transactional
	public void updateGroup(SaveGroupReqDto req) {
		Map<String, String> generalPara...
```

**Chunk 정보**
- 🆔 **ID**: `0c9fc0a3ac27`
- 📍 **라인**: 56-56
- 📊 **토큰**: 660
- 🏷️ **태그**: `class, java, transactional, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **1,422개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 762 | 53.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 202.0 | 660 | 46.4% |

