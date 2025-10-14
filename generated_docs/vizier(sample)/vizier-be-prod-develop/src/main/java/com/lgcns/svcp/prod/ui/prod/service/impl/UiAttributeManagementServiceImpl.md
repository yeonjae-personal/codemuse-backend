# 📄 UiAttributeManagementServiceImpl.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/impl/UiAttributeManagementServiceImpl.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiAttributeManagementServiceImpl`](#class-uiattributemanagementserviceimpl) - 복잡도: 263 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.Comparator` • `java.util.HashMap` • `java.util.List` • `java.util.Map` • `java.util.stream.Collectors` 외 24개 | ⚡ **총 복잡도**: 263 |
| 📊 **총 토큰 수**: 1,646 |  |



## 🏗️ 클래스

### <a id="class-uiattributemanagementserviceimpl"></a>🎯 `UiAttributeManagementServiceImpl`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 263 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 263 || 📍 **라인 범위** | 39-39 |
| 🏷️ **태그** | `class, java, transactional, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiAttributeManagementServiceImpl implements UiAttributeManagementService {
	
	private final CommonDao commonDao;
	private final CtgrNodeMapper ctgrNodeMapper; 

	@Override
	public List<AttributeViewDto> getData() {
		List<AttributeViewDto> results = new ArrayList<>();
		List<Item> items = commonDao.selectList("Ui-item.getListAttributeInAdmin");
		Map<String, List<Item>> mapLargeItems = items.stream().collect(Collectors.groupingBy(Item::getLargeItemCode));
		for(Map.Entry<String, List<Item>> entry : mapLargeItems.entrySet()) {
			Item largeItem = entry.getValue().get(0);
			AttributeViewDto attributeViewDto = new AttributeViewDto();
			attributeViewDto.setName(largeItem.getLargeItemName());
			attributeViewDto.setCode(largeItem.getLargeItemCode());
			attributeViewDto.setSortNo...
```

**Chunk 정보**
- 🆔 **ID**: `f40b0d94dffe`
- 📍 **라인**: 39-39
- 📊 **토큰**: 791
- 🏷️ **태그**: `class, java, transactional, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **1,646개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 855 | 51.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 263.0 | 791 | 48.1% |

