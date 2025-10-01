# 📄 CreateComponentReq.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/component/CreateComponentReq.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `CreateComponentReq`
- **도메인**: user
- **목적**: 비즈니스 로직 처리
- **복잡도**: 4
- **관련 파일**: /Users/roseline/projects/codemuse-backend/sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/component/*
- **라인**: 13


## 📑 목차

### 🏗️ 클래스
- [`CreateComponentReq`](#class-createcomponentreq) - 복잡도: 4

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.item.detail.ItemMappingDetailDto` • `com.lgcns.svcp.prod.ui.prod.dto.multiEntity.rel.SaveEntityObjRelReqDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 4 |
| 📊 **총 토큰 수**: 40 |  |



## 🏗️ 클래스

### <a id="class-createcomponentreq"></a>🎯 `CreateComponentReq`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `ItemMappingDetailDto` |
| ⚡ 복잡도 | 4 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 4 || 📍 **라인 범위** | 13-13 |
| 🏗️ **상속** | `ItemMappingDetailDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class CreateComponentReq extends ItemMappingDetailDto {
    private List<ComponentResourceReq> resourceList;
    private List<SaveEntityObjRelReqDto> insertEntityObjRels;
}...
```

**Chunk 정보**
- 🆔 **ID**: `1f491346e7f3`
- 📍 **라인**: 13-13
- 📊 **토큰**: 13
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **40개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 27 | 67.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 4.0 | 13 | 32.5% |

