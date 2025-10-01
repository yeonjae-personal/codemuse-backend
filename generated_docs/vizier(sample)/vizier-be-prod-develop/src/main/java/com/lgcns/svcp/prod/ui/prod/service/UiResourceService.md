# 📄 UiResourceService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/UiResourceService.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiResourceService`](#class-uiresourceservice) - 복잡도: 218 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.constant.SystemConstant.ITEM_CODE` • `com.lgcns.svcp.prod.constant.SystemConstant.LANG_CODE` • `com.lgcns.svcp.prod.constant.SystemConstant.OBJ_CODE` • `com.lgcns.svcp.prod.constant.SystemConstant.OBJ_UUID` • `com.lgcns.svcp.prod.constant.SystemConstant.VALID_END_DTM` • `java.util.ArrayList` 외 49개 | ⚡ **총 복잡도**: 218 |
| 📊 **총 토큰 수**: 1,555 |  |



## 🏗️ 클래스

### <a id="class-uiresourceservice"></a>🎯 `UiResourceService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 218 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 218 || 📍 **라인 범위** | 65-65 |
| 🏷️ **태그** | `class, java, transactional, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiResourceService {
	
	private final CommonDao commonDao;
	private final ResourceMapper resourceMapper;
	private final UiCommonService uiCommonService;
	private final UIMultiEntityService uiMultiEntityService;
	private final UIHistoryService uiHistoryService;
	private final UiTableService uiTableService;
	private final ResourceExcelHelper excelHelper;
	private final MessageSource messageSource;


	public GroupedServiceResMDto retrieveGroupedServiceResM(RawServiceResMDto rawServiceResMDto) {

		RawServiceResMDto raw = commonDao.select("Ui-resource.retrieveServiceResM", rawServiceResMDto);
		GeneralServiceResMDto general = new GeneralServiceResMDto(raw);
		AdditionalServiceResMDto additional = new AdditionalServiceResMDto(raw);

		return GroupedServiceResMDto.builder().general(g...
```

**Chunk 정보**
- 🆔 **ID**: `068b8544b4b3`
- 📍 **라인**: 65-65
- 📊 **토큰**: 718
- 🏷️ **태그**: `class, java, transactional, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **1,555개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 837 | 53.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 218.0 | 718 | 46.2% |

