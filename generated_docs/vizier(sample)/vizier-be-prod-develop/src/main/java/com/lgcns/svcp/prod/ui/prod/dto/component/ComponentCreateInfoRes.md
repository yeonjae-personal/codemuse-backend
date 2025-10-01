# 📄 ComponentCreateInfoRes.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/component/ComponentCreateInfoRes.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `ComponentCreateInfoRes`
- **도메인**: user
- **목적**: 비즈니스 로직 처리
- **복잡도**: 3
- **관련 파일**: /Users/roseline/projects/codemuse-backend/sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/component/*
- **라인**: 13


## 📑 목차

### 🏗️ 클래스
- [`ComponentCreateInfoRes`](#class-componentcreateinfores) - 복잡도: 3

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.item.detail.ItemMappingDetailDto` • `com.lgcns.svcp.prod.ui.prod.dto.multiEntity.rel.EntityItemRelResDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 3 |
| 📊 **총 토큰 수**: 34 |  |



## 🏗️ 클래스

### <a id="class-componentcreateinfores"></a>🎯 `ComponentCreateInfoRes`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `ItemMappingDetailDto` |
| ⚡ 복잡도 | 3 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 3 || 📍 **라인 범위** | 13-13 |
| 🏗️ **상속** | `ItemMappingDetailDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ComponentCreateInfoRes extends ItemMappingDetailDto {
    private List<EntityItemRelResDto> entityItemInfos;
}...
```

**Chunk 정보**
- 🆔 **ID**: `cc719e2e9f6b`
- 📍 **라인**: 13-13
- 📊 **토큰**: 10
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **34개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 24 | 70.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 3.0 | 10 | 29.4% |

