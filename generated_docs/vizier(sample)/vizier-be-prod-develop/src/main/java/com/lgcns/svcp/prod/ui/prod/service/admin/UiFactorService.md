# 📄 UiFactorService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/admin/UiFactorService.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiFactorService`](#class-uifactorservice) - 복잡도: 92 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.Collections` • `java.util.HashSet` • `java.util.Iterator` • `java.util.List` • `java.util.Map` 외 17개 | ⚡ **총 복잡도**: 92 |
| 📊 **총 토큰 수**: 618 |  |



## 🏗️ 클래스

### <a id="class-uifactorservice"></a>🎯 `UiFactorService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 92 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 92 || 📍 **라인 범위** | 32-32 |
| 🏷️ **태그** | `class, java, transactional, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiFactorService {
	private final CommonDao commonDao;
	private final MessageSource messageSource;

	public PageResult<?> searchFactorType(SearchFactorTypeReqDto reqDto) {
		return commonDao.selectPagedList("Ui-factor.searchFactorType", reqDto);
	}

	public PageResult<?> searchFactor(SearchFactorReqDto reqDto) {
		return commonDao.selectPagedList("Ui-factor.searchFactor", reqDto);
	}

	public FactorTypeDto retrieveFactorType(SearchFactorReqDto reqDto) {
		FactorTypeDto resulTypeDto = commonDao.select("Ui-factor.retrieveFactorType", reqDto.getFactorTypeCode());
		if (resulTypeDto != null) {
			PageResult<?> factorSearchLst = searchFactor(reqDto);
			List<FactorDto> factorLst = commonDao.selectList("Ui-factor.searchFactor", reqDto);
			resulTypeDto.setFactorSearchLst(factorSearch...
```

**Chunk 정보**
- 🆔 **ID**: `5c8c961f723a`
- 📍 **라인**: 32-32
- 📊 **토큰**: 284
- 🏷️ **태그**: `class, java, transactional, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **618개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 334 | 54.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 92.0 | 284 | 46.0% |

