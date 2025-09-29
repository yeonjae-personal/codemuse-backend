# 📄 RelationService.java

> **파일 경로**: `vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/online/prod/service/RelationService.java`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`RelationService`](#class-relationservice) - 복잡도: 8 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.springframework.beans.factory.annotation.Autowired` • `org.springframework.stereotype.Component` • `com.lgcns.svcp.prod.online.prod.dto.ProdCstcRelDDto` • `com.lgcns.svcp.prod.online.prod.dto.ProdDpndRelDDto` • `com.lgcns.svcp.prod.dataaccess.CommonDao` | ⚡ **총 복잡도**: 8 |
| 📊 **총 토큰 수**: 201 |  |



## 🏗️ 클래스

### <a id="class-relationservice"></a>🎯 `RelationService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 8 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 8 || 📍 **라인 범위** | 14-14 |
| 🏷️ **태그** | `class, java, autowired, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class RelationService {
	@Autowired
	private CommonDao commonDao;
	
	public List<ProdDpndRelDDto> retrieveAllProdDpndRelDList(ProdDpndRelDDto prodDpndRelDDto) {
		List<ProdDpndRelDDto> resultList = commonDao.selectList("Relation.retrieveAllProdDpndRelDList", prodDpndRelDDto);
		return resultList;
	}
	
//	public List<ProdDpndRelDDto> retrieveProdDpndRelDList(ProdDpndRelDDto prodDpndRelDDto) {
//		List<ProdDpndRelDDto> resultList = commonDao.selectPagedList("Relation.retrieveProdDpndRelDList", prodDpndRelDDto);
//		return resultList;
//	}
//	
//	public List<ProdDpndRelDDto> retrieveProdDpndRelD(ProdDpndRelDDto prodDpndRelDDto) {
//		List<ProdDpndRelDDto> resultList = commonDao.selectList("Relation.retrieveProdDpndRelD", prodDpndRelDDto);
//		return resultList;
//	}
//	
//	public List<...
```

**Chunk 정보**
- 🆔 **ID**: `703519f29493`
- 📍 **라인**: 14-14
- 📊 **토큰**: 93
- 🏷️ **태그**: `class, java, autowired, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **201개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 108 | 53.7% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 8.0 | 93 | 46.3% |

