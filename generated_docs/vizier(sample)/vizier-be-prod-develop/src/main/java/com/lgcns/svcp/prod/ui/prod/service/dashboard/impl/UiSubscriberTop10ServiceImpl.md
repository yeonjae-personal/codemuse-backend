# 📄 UiSubscriberTop10ServiceImpl.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/dashboard/impl/UiSubscriberTop10ServiceImpl.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiSubscriberTop10ServiceImpl`](#class-uisubscribertop10serviceimpl) - 복잡도: 132

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.Date` • `java.util.HashMap` • `java.util.List` • `java.util.Map` • `org.springframework.context.MessageSource` 외 21개 | ⚡ **총 복잡도**: 132 |
| 📊 **총 토큰 수**: 932 |  |



## 🏗️ 클래스

### <a id="class-uisubscribertop10serviceimpl"></a>🎯 `UiSubscriberTop10ServiceImpl`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 132 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 132 || 📍 **라인 범위** | 36-36 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiSubscriberTop10ServiceImpl implements UiSubscriberTop10Service {
	
	private final CommonDao commonDao;
	
	private final MessageSource messageSource;
	
	private final DsbdOfferSubCntMapper dsbdOfferSubCntMapper;
	
	private final SubscriberTop10ExcelHelper excelHelper;
	
	@Override
	public Object getSubscribeTop10(SubscribeTop10SearchPagingDto searchPaging, Integer page, Integer size) {
		SubscribeTop10SearchPagingDto properties = buildProperties(searchPaging, page, size);
		PageResult<DsbdOfferSubCntEntity> pageResultCntEntities = commonDao.selectPagedList("ui-dsbd-offer-sub-cnt.findProperties", properties);
		List<DsbdOfferSubCntEntity> cntEntities = pageResultCntEntities.getElements();
		if (searchPaging.getView().equals("simple")) {
			return cntEntities.stream().map(item ...
```

**Chunk 정보**
- 🆔 **ID**: `4eac98bb898d`
- 📍 **라인**: 36-36
- 📊 **토큰**: 437
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **932개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 495 | 53.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 132.0 | 437 | 46.9% |

