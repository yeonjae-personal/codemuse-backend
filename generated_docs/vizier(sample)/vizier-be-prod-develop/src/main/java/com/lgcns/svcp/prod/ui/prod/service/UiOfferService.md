# 📄 UiOfferService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/UiOfferService.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiOfferService`](#class-uiofferservice) - 복잡도: 394 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.constant.SystemConstant.CODE` • `com.lgcns.svcp.prod.constant.SystemConstant.CTGR_NODE_UUID` • `com.lgcns.svcp.prod.constant.SystemConstant.ITEM_CODE` • `com.lgcns.svcp.prod.constant.SystemConstant.LANG_CODE` • `com.lgcns.svcp.prod.constant.SystemConstant.OBJ_CODE` • `com.lgcns.svcp.prod.constant.SystemConstant.OBJ_UUID` 외 57개 | ⚡ **총 복잡도**: 394 |
| 📊 **총 토큰 수**: 2,499 |  |



## 🏗️ 클래스

### <a id="class-uiofferservice"></a>🎯 `UiOfferService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 394 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 394 || 📍 **라인 범위** | 73-73 |
| 🏷️ **태그** | `class, java, autowired, transactional, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiOfferService {
	private final static String FILE_NAME = "Offer Details";

	private final CommonDao commonDao;
	private final CommonCodeService commonCodeService;
	private final UiCommonService uiCommonService;
	private final OfferMapper offerMapper;
	private final UIHistoryService uiHistoryService;
	private final UiTableService uiTableService;	

	private final OfferListExcelHelper excelHelper;
	
	@Autowired
	private MessageSource messageSource;

	public ProdMDto retrieveProdM(ProdMDto prodMDto) {
		return commonDao.select("Ui-offer.retrieveProdM", prodMDto);
	}

	public List<ProdStruDWithItemsMDto> retrieveProdStruDWithItemsMListWithPartiotion(
			ProdStruDWithItemsMDto prodStruDWithItemsMDto) {
		return commonDao.selectList("Ui-offer.retrieveProdStruDWithItemsMListWithParti...
```

**Chunk 정보**
- 🆔 **ID**: `7876037f29f8`
- 📍 **라인**: 73-73
- 📊 **토큰**: 1181
- 🏷️ **태그**: `class, java, autowired, transactional, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **2,499개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 1,318 | 52.7% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 394.0 | 1,181 | 47.3% |

