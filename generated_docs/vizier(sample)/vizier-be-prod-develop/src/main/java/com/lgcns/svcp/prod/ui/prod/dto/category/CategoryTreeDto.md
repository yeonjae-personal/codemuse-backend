# 📄 CategoryTreeDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/category/CategoryTreeDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`CategoryTreeDto`](#class-categorytreedto) - 복잡도: 16

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.BaseDto` • `lombok.Data` | ⚡ **총 복잡도**: 16 |
| 📊 **총 토큰 수**: 111 |  |



## 🏗️ 클래스

### <a id="class-categorytreedto"></a>🎯 `CategoryTreeDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseDto` |
| ⚡ 복잡도 | 16 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 16 || 📍 **라인 범위** | 10-10 |
| 🏗️ **상속** | `BaseDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class CategoryTreeDto extends BaseDto {
    private String ctgrNodeUuid;
    private String ctgrTabUuid;
    private String ctgrNodeName;
    private String hpstCtgrNodeUuid;
    private String tclsCtgrYn;
    private String chgDeptName;
    private String chgUser;
    private String ctgrOvwCntn;
    private String useYn;

    private String isLeafCategoryNode;
    private int totalOfferCount;
    private String level;
    private Boolean showChilderen = false;
    private List<CategoryTreeDto> children;
}...
```

**Chunk 정보**
- 🆔 **ID**: `4665d921635d`
- 📍 **라인**: 10-10
- 📊 **토큰**: 51
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **111개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 60 | 54.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 16.0 | 51 | 45.9% |

