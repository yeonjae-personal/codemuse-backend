# 📄 MetadataServiceImpl.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/impl/MetadataServiceImpl.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`MetadataServiceImpl`](#class-metadataserviceimpl) - 복잡도: 15

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.HashMap` • `java.util.List` • `java.util.Map` • `org.springframework.stereotype.Service` • `com.lgcns.svcp.prod.dataaccess.CommonDao` • `com.lgcns.svcp.prod.entity.MiddleItemEntity` 외 5개 | ⚡ **총 복잡도**: 15 |
| 📊 **총 토큰 수**: 120 |  |



## 🏗️ 클래스

### <a id="class-metadataserviceimpl"></a>🎯 `MetadataServiceImpl`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 15 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 15 || 📍 **라인 범위** | 19-19 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class MetadataServiceImpl implements MetadataService {
	
	private final CommonDao commonDao;
	private final MetadataMapper metadataMapper;
	
	@Override
	public List<MiddleItemDto> getMiddleItem(String largeItemCode) {
		Map<String, Object> maps = new HashMap<>();
		maps.put("largeItemCode", largeItemCode);
		List<MiddleItemEntity> middleItemEntities = commonDao.selectList("Ui-item.getMiddleItem", maps);
		return middleItemEntities.stream().map(item -> metadataMapper.middleItemEntityToDto(item)).toList();
	}

	@Override
	public List<LargeItemDto> getLargeItem() {
		return commonDao.selectList("Ui-item.getLargeItem");
	}
}...
```

**Chunk 정보**
- 🆔 **ID**: `35e2ffe2735b`
- 📍 **라인**: 19-19
- 📊 **토큰**: 47
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **120개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 73 | 60.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 15.0 | 47 | 39.2% |

