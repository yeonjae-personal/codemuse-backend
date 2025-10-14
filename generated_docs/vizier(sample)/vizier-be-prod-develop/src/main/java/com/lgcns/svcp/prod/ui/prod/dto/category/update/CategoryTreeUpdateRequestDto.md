# 📄 CategoryTreeUpdateRequestDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/category/update/CategoryTreeUpdateRequestDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`CategoryTreeUpdateRequestDto`](#class-categorytreeupdaterequestdto) - 복잡도: 63

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.List` • `java.util.stream.Collectors` • `com.fasterxml.jackson.annotation.JsonIgnore` • `com.lgcns.svcp.prod.ui.prod.dto.category.CategoryDescriptionDto` • `com.lgcns.svcp.prod.ui.prod.dto.category.CtgrLevelDto` 외 3개 | ⚡ **총 복잡도**: 63 |
| 📊 **총 토큰 수**: 348 |  |



## 🏗️ 클래스

### <a id="class-categorytreeupdaterequestdto"></a>🎯 `CategoryTreeUpdateRequestDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 63 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 63 || 📍 **라인 범위** | 17-17 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class CategoryTreeUpdateRequestDto {
    private String ctgrTabUuid;
    private CategoryDescriptionDto description;
    private List<CategorySaveDto> categoryTree;

    @JsonIgnore
    public List<CtgrLevelDto> getCtgrLevels() {
        List<CtgrLevelDto> ctgrLevels = new ArrayList<>();
        ctgrLevels.add(convertLevel("1", description.getLevel1()));
        ctgrLevels.add(convertLevel("2", description.getLevel2()));
        ctgrLevels.add(convertLevel("3", description.getLevel3()));
        ctgrLevels.add(convertLevel("4", description.getLevel4()));
        ctgrLevels.add(convertLevel("5", description.getLevel5()));
        return ctgrLevels;
    }

    @JsonIgnore
    public List<CategorySaveDto> getCategoryTreeFlat() {
        return flattenCategoryTree(categoryTree);
    }

...
```

**Chunk 정보**
- 🆔 **ID**: `18c3d83cbd48`
- 📍 **라인**: 17-17
- 📊 **토큰**: 163
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **348개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 185 | 53.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 63.0 | 163 | 46.8% |

