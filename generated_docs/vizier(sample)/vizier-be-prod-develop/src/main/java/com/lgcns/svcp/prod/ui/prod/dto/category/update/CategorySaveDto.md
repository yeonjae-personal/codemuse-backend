# 📄 CategorySaveDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/category/update/CategorySaveDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`CategorySaveDto`](#class-categorysavedto) - 복잡도: 4

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.category.CatgMDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 4 |
| 📊 **총 토큰 수**: 38 |  |



## 🏗️ 클래스

### <a id="class-categorysavedto"></a>🎯 `CategorySaveDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `CatgMDto` |
| ⚡ 복잡도 | 4 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 4 || 📍 **라인 범위** | 12-12 |
| 🏗️ **상속** | `CatgMDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class CategorySaveDto extends CatgMDto {
    private List<CategorySaveDto> children;
    private List<CategoryOfferRelDto> offerRel;
}...
```

**Chunk 정보**
- 🆔 **ID**: `14b6b30d8c90`
- 📍 **라인**: 12-12
- 📊 **토큰**: 13
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **38개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 25 | 65.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 4.0 | 13 | 34.2% |

