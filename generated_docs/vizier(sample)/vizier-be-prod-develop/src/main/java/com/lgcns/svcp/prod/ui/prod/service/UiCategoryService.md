# 📄 UiCategoryService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/UiCategoryService.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`UiCategoryService`](#interface-uicategoryservice)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.category.CategoryDescriptionDto` • `com.lgcns.svcp.prod.ui.prod.dto.category.CategoryPathDto` • `com.lgcns.svcp.prod.ui.prod.dto.category.CategoryTreeDto` • `com.lgcns.svcp.prod.ui.prod.dto.category.CatgMDto` • `com.lgcns.svcp.prod.ui.prod.dto.category.RequestOfferWithCatgUuidDto` 외 6개 | ⚡ **총 복잡도**: 12 |
| 📊 **총 토큰 수**: 98 |  |




## 🔌 인터페이스

### <a id="interface-uicategoryservice"></a>🔌 `UiCategoryService`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 18-18 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface UiCategoryService {

	List<OfferOfLeafNodeDto> retrieveProdMList(CatgMDto catgMDto);

	List<CategoryTreeDto> retrieveCategoryTreeWithOfferCounts(String ctgrTabUuid);

	PageResult<?> retrieveCategoryPathWithOffer(CategoryPathDto categoryPathDto);

	CategoryDescriptionDto retrieveCategoryDescription(String ctgrTabUuid);

	PageResult<?> retrieveOffersWithCategoryList(RequestOfferWithCatgUuidDto requestOfferWithCatgUuidDto);

	PageResult<?> retrieveOffersOfCatgegory(OffersOfCatgegoryReqDto req);

	void exportExcel(CategoryPathDto categoryPathDto, HttpServletResponse response);

	L...
```

**Chunk 정보**
- 🆔 **ID**: `f6c9bf79ae73`
- 📊 **토큰**: 36

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **98개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 62 | 63.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 12.0 | 36 | 36.7% |

