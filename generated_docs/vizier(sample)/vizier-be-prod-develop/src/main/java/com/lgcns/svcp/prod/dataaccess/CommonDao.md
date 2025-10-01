# 📄 CommonDao.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/dataaccess/CommonDao.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`CommonDao`](#interface-commondao)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.util.paging.PageResult` | ⚡ **총 복잡도**: 14 |
| 📊 **총 토큰 수**: 134 |  |




## 🔌 인터페이스

### <a id="interface-commondao"></a>🔌 `CommonDao`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 7-7 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface CommonDao {

    <T> T select(String queryId);
    <T> T select(String queryId, Object parameter);
    
    <E> List<E> selectList(String queryId);
    <E> List<E> selectList(String queryId, Object parameter);

    <E> PageResult<E> selectPagedList(String queryId, Object parameter);

    int insert(String queryId, Object parameter);

    int update(String queryId);
    int update(String queryId, Object parameter);

    int delete(String queryId, Object parameter);

    int batchInsert(String queryId, List<?> parameter);

    int batchUpdate(String queryId, List<?> parameter);
...
```

**Chunk 정보**
- 🆔 **ID**: `bffc7439d044`
- 📊 **토큰**: 64

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **134개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 70 | 52.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 14.0 | 64 | 47.8% |

