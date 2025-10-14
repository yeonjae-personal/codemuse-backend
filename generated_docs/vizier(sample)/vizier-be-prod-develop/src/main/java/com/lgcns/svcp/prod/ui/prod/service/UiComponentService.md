# 📄 UiComponentService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/UiComponentService.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `UiComponentService`
- **도메인**: product_ui
- **목적**: UiComponentService 비즈니스 로직 처리
- **복잡도**: 538
- **관련 파일**: ./sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/*
- **라인**: 96


## 📑 목차

### 🏗️ 클래스
- [`UiComponentService`](#class-uicomponentservice) - 복잡도: 538 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.constant.SystemConstant.CHG_DEPT_NAME` • `com.lgcns.svcp.prod.constant.SystemConstant.CHG_USER` • `com.lgcns.svcp.prod.constant.SystemConstant.ITEM_CODE` • `com.lgcns.svcp.prod.constant.SystemConstant.LANG_CODE` • `com.lgcns.svcp.prod.constant.SystemConstant.OBJ_CODE` • `com.lgcns.svcp.prod.constant.SystemConstant.OBJ_UUID` 외 80개 | ⚡ **총 복잡도**: 538 |
| 📊 **총 토큰 수**: 4,009 |  |



## 🏗️ 클래스

### <a id="class-uicomponentservice"></a>🎯 `UiComponentService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 538 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 538 || 📍 **라인 범위** | 96-96 |
| 🏷️ **태그** | `class, java, transactional, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiComponentService {
	private final CommonDao commonDao;
	private final UiTableService uiTableService;
	private final UiCommonService uiCommonService;
	private final UIMultiEntityService uiMultiEntityService;
	private final UIHistoryService uiHistoryService;
	private final ComponentExcelHelper excelHelper;
	private final ComponentMapper componentMapper;
	private final MessageSource messageSource;

	public MfMDto retrieveMfM(MfMDto mfMDto) {
		return commonDao.select("Ui-component.retrieveMfM", mfMDto);
	}

	public void updateMfM(MfMDto mfMDto) {
		MfMDto existMfM = commonDao.select("Ui-component.retrieveMfM", mfMDto.getBasfCd());
		if (existMfM == null) {
			throw new BusinessException("해당 Id의 상품이 존재하지 않습니다.");
		}
		int result = commonDao.update("Ui-component.updateMfM", mfMD...
```

**Chunk 정보**
- 🆔 **ID**: `3dfdc162663f`
- 📍 **라인**: 96-96
- 📊 **토큰**: 1913
- 🏷️ **태그**: `class, java, transactional, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **4,009개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 2,096 | 52.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 538.0 | 1,913 | 47.7% |

