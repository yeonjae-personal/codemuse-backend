# 📄 UiImpactAnalysisService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/UiImpactAnalysisService.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiImpactAnalysisService`](#class-uiimpactanalysisservice) - 복잡도: 66

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `java.util.Map` • `java.util.stream.Collectors` • `java.util.stream.IntStream` • `org.springframework.stereotype.Service` • `com.lgcns.svcp.prod.constant.DateConstant` 외 14개 | ⚡ **총 복잡도**: 66 |
| 📊 **총 토큰 수**: 414 |  |



## 🏗️ 클래스

### <a id="class-uiimpactanalysisservice"></a>🎯 `UiImpactAnalysisService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 66 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 66 || 📍 **라인 범위** | 29-29 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiImpactAnalysisService {
	private final CommonDao commonDao;
	private final ExcelHelper excelHelper;

	public ImpactAnalysisResponseDto getImpactAnalysisResponseDto(ItemDto itemDto) {
		ItemDto parent = commonDao.select("Ui-impact.getParentProdM", itemDto);
		List<ItemDto> siblings = commonDao.selectList("Ui-impact.getSiblingsProdM", itemDto);

		return ImpactAnalysisResponseDto.builder().parent(parent).siblings(siblings).build();
	}

	public PageResult<?> retrieveItemsPagedList(ItemDto itemDto) {
		return commonDao.selectPagedList("Ui-impact.retrieveItemsPagedList", itemDto);
	}

	public PageResult<?> retrieveProductStructureListView(ProductStructureDto productStructureDto) {
		return commonDao.selectPagedList("Ui-impact.retrieveProductStructureListView", productStructureDto...
```

**Chunk 정보**
- 🆔 **ID**: `2fc58e789b58`
- 📍 **라인**: 29-29
- 📊 **토큰**: 185
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **414개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 229 | 55.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 66.0 | 185 | 44.7% |

