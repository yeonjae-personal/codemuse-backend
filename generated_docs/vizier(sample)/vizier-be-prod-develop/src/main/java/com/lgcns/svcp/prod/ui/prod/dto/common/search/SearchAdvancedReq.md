# 📄 SearchAdvancedReq.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/common/search/SearchAdvancedReq.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`SearchAdvancedReq`](#class-searchadvancedreq) - 복잡도: 9

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.util.paging.BasePaginationDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 9 |
| 📊 **총 토큰 수**: 76 |  |



## 🏗️ 클래스

### <a id="class-searchadvancedreq"></a>🎯 `SearchAdvancedReq`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BasePaginationDto` |
| ⚡ 복잡도 | 9 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 9 || 📍 **라인 범위** | 12-12 |
| 🏗️ **상속** | `BasePaginationDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class SearchAdvancedReq extends BasePaginationDto {
    private String objCode;
    private String objName;
    private String itemCode;
    private String mctgrItemCode;
    private boolean onlyValidDtm;
    //    private List<DynamicFieldReq> general;
    private List<DynamicFieldReq> additional;
    private String language;
}...
```

**Chunk 정보**
- 🆔 **ID**: `8ffcb4ccbf71`
- 📍 **라인**: 12-12
- 📊 **토큰**: 32
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **76개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 44 | 57.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 9.0 | 32 | 42.1% |

