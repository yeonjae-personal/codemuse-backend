# 📄 ComponentUpdateReq.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/component/update/ComponentUpdateReq.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `ComponentUpdateReq`
- **도메인**: product_ui
- **목적**: 비즈니스 로직 처리
- **복잡도**: 5
- **관련 파일**: ./sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/component/update/*
- **라인**: 13


## 📑 목차

### 🏗️ 클래스
- [`ComponentUpdateReq`](#class-componentupdatereq) - 복잡도: 5

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.item.detail.ItemMappingDetailDto` • `com.lgcns.svcp.prod.ui.prod.dto.multiEntity.rel.SaveEntityObjRelReqDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 5 |
| 📊 **총 토큰 수**: 46 |  |



## 🏗️ 클래스

### <a id="class-componentupdatereq"></a>🎯 `ComponentUpdateReq`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `ItemMappingDetailDto` |
| ⚡ 복잡도 | 5 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 5 || 📍 **라인 범위** | 13-13 |
| 🏗️ **상속** | `ItemMappingDetailDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ComponentUpdateReq extends ItemMappingDetailDto {
    private List<ComponentUpdateResourceReq> resources;
    private List<SaveEntityObjRelReqDto> insertEntityObjRels;
    private List<SaveEntityObjRelReqDto> updateEntityObjRels;
}...
```

**Chunk 정보**
- 🆔 **ID**: `06cae8de3404`
- 📍 **라인**: 13-13
- 📊 **토큰**: 16
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **46개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 30 | 65.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 5.0 | 16 | 34.8% |

