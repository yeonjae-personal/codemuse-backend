# 📄 UIRelationService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/UIRelationService.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UIRelationService`](#class-uirelationservice) - 복잡도: 82 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.constant.SystemConstant.ITEM_CODE` • `com.lgcns.svcp.prod.constant.SystemConstant.LANG_CODE` • `com.lgcns.svcp.prod.constant.SystemConstant.OBJ_CODE` • `com.lgcns.svcp.prod.constant.SystemConstant.OBJ_UUID` • `java.util.ArrayList` • `java.util.List` 외 16개 | ⚡ **총 복잡도**: 82 |
| 📊 **총 토큰 수**: 580 |  |



## 🏗️ 클래스

### <a id="class-uirelationservice"></a>🎯 `UIRelationService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 82 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 82 || 📍 **라인 범위** | 32-32 |
| 🏷️ **태그** | `class, java, transactional, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UIRelationService {
	private final CommonDao commonDao;
	private final UiCommonService uiCommonService;
	private final UIExtendsService uiExtendsService;
	private final UIHistoryService uiHistoryService;
	private final UiTableService uiTableService;

	public PageResult<?> retrieveRelationsAdvanced(SearchAdvancedReq searchAdvancedReq) {
		return commonDao.selectPagedList("Ui-relation.searchRelationsAdvanced", searchAdvancedReq);
	}

	public ItemMappingDetailDto retrieveRelationCreateInfo(String itemCode, String langCode) {
		Map<String, String> param = Map.of(ITEM_CODE, itemCode, LANG_CODE, langCode);

		ItemMappingDetailDto response = new ItemMappingDetailDto();
		List<GeneralDetailDto> general = commonDao.selectList("Ui-item.retrieveGeneral", param);
		List<AdditionalDetailDt...
```

**Chunk 정보**
- 🆔 **ID**: `8b54f3cf37a3`
- 📍 **라인**: 32-32
- 📊 **토큰**: 264
- 🏷️ **태그**: `class, java, transactional, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **580개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 316 | 54.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 82.0 | 264 | 45.5% |

