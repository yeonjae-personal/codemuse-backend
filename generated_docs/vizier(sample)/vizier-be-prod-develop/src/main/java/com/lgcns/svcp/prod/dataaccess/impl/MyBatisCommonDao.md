# 📄 MyBatisCommonDao.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/dataaccess/impl/MyBatisCommonDao.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`MyBatisCommonDao`](#class-mybatiscommondao) - 복잡도: 98 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.Collections` • `java.util.List` • `java.util.Map` • `java.util.function.BiConsumer` • `java.util.stream.Collectors` • `org.apache.ibatis.executor.BatchResult` 외 11개 | ⚡ **총 복잡도**: 98 |
| 📊 **총 토큰 수**: 743 |  |



## 🏗️ 클래스

### <a id="class-mybatiscommondao"></a>🎯 `MyBatisCommonDao`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `SqlSessionDaoSupport` |
| ⚡ 복잡도 | 98 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 98 || 📍 **라인 범위** | 24-24 |
| 🏗️ **상속** | `SqlSessionDaoSupport` || 🏷️ **태그** | `class, java, autowired, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class MyBatisCommonDao extends SqlSessionDaoSupport implements CommonDao {
	
	private static final int BATCH_SIZE = 1000;

	@Autowired
	@Override
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
	}

	@Override
	public <T> T select(String queryId) {
		return getSqlSession().selectOne(queryId);
	}

	@Override
	public <T> T select(String queryId, Object parameter) {
		return getSqlSession().selectOne(queryId, parameter);
	}

	@Override
	public <E> List<E> selectList(String queryId) {
		return getSqlSession().selectList(queryId);
	}

	@Override
	public <E> List<E> selectList(String queryId, Object parameter) {
		return getSqlSession().selectList(queryId, parameter);
	}

	@Override
	public <E> PageResult<E> selectPa...
```

**Chunk 정보**
- 🆔 **ID**: `11cdd1d16f7a`
- 📍 **라인**: 24-24
- 📊 **토큰**: 353
- 🏷️ **태그**: `class, java, autowired, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **743개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 390 | 52.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 98.0 | 353 | 47.5% |

