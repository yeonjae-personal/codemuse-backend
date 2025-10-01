# 📄 CategoryService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/service/CategoryService.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`CategoryService`](#class-categoryservice) - 복잡도: 18 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.springframework.beans.factory.annotation.Autowired` • `org.springframework.stereotype.Component` • `com.lgcns.svcp.prod.online.prod.dto.category.CtgrNodeMDto` • `com.lgcns.svcp.prod.dataaccess.CommonDao` | ⚡ **총 복잡도**: 18 |
| 📊 **총 토큰 수**: 115 |  |



## 🏗️ 클래스

### <a id="class-categoryservice"></a>🎯 `CategoryService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 18 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 18 || 📍 **라인 범위** | 13-13 |
| 🏷️ **태그** | `class, java, autowired, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class CategoryService {
	@Autowired
	private CommonDao commonDao;

	public CtgrNodeMDto retrieveCtgrNodeMByCtgrNodeUuid(CtgrNodeMDto catgNodeMDto) {
		return commonDao.select("Category.retrieveCtgrNodeMByCtgrNodeUuid", catgNodeMDto);
	}

	public List<CtgrNodeMDto> retrieveCtgrNodeMList() {
		List<CtgrNodeMDto> resultList = commonDao.selectList("Category.retrieveCtgrNodeMList");
		return resultList;
	}
	
	public CtgrNodeMDto retrieveCtgrNodeMByObjUuid(CtgrNodeMDto catgNodeMDto) {
		return commonDao.select("Category.retrieveCtgrNodeMByObjUuid", catgNodeMDto);
	}
	
	public List<CtgrNodeMDto> retrieveCatgTreeList(CtgrNodeMDto catgNodeMDto) {
		List<CtgrNodeMDto> resultList = commonDao.selectList("Category.retrieveCtgrNodeMList", catgNodeMDto);
		return resultList;
	}
}...
```

**Chunk 정보**
- 🆔 **ID**: `9b85ab581c69`
- 📍 **라인**: 13-13
- 📊 **토큰**: 51
- 🏷️ **태그**: `class, java, autowired, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **115개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 64 | 55.7% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 18.0 | 51 | 44.3% |

