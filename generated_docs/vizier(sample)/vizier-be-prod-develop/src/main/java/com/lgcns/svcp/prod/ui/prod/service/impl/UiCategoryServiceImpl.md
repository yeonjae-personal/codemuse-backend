# 📄 UiCategoryServiceImpl.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/impl/UiCategoryServiceImpl.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiCategoryServiceImpl`](#class-uicategoryserviceimpl) - 복잡도: 130 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.Comparator` • `java.util.List` • `java.util.Map` • `java.util.stream.Collectors` • `java.util.stream.IntStream` 외 26개 | ⚡ **총 복잡도**: 130 |
| 📊 **총 토큰 수**: 788 |  |



## 🏗️ 클래스

### <a id="class-uicategoryserviceimpl"></a>🎯 `UiCategoryServiceImpl`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 130 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 130 || 📍 **라인 범위** | 41-41 |
| 🏷️ **태그** | `class, java, transactional, param, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiCategoryServiceImpl implements UiCategoryService {
	private final CommonDao commonDao;

	private final ExcelHelper excelHelper;

	private final static String FILE_NAME = "Tree-View";

	private int getCatgMCount(CatgMDto catgMDto) {
		return commonDao.select("Ui-category.getCountCatgM", catgMDto);
	}

	private boolean isLeafCategoryNode(CatgMDto catgMDto) {
		if (getCatgMCount(catgMDto) > 0) {
			return false;
		}
		return true;
	}

	@Override
	public List<OfferOfLeafNodeDto> retrieveProdMList(CatgMDto catgMDto) {
		if (isLeafCategoryNode(catgMDto)) {
			return commonDao.selectList("Ui-category.retrieveProdMByCatgMUuid", catgMDto);
		}
		throw new BusinessException("카테고리 노드가 아닙니다.");
	}

	/**
	 *
	 * @Author : 이훈민(A76485@cnspartners.com)
	 * @Date : 2024. 7. 2.
	 * @MethodNam...
```

**Chunk 정보**
- 🆔 **ID**: `7125c4cbcd10`
- 📍 **라인**: 41-41
- 📊 **토큰**: 360
- 🏷️ **태그**: `class, java, transactional, param, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **788개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 428 | 54.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 130.0 | 360 | 45.7% |

