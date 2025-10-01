# 📄 ComponentPagingDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/offer/structure/listAddComponent/ComponentPagingDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `ComponentPagingDto`
- **도메인**: offer
- **목적**: ComponentPagingDto 데이터 전송 객체
- **복잡도**: 10
- **관련 파일**: /Users/roseline/projects/codemuse-backend/sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/offer/structure/listAddComponent/*
- **라인**: 10


## 📑 목차

### 🏗️ 클래스
- [`ComponentPagingDto`](#class-componentpagingdto) - 복잡도: 10

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.util.paging.BasePaginationDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 10 |
| 📊 **총 토큰 수**: 72 |  |



## 🏗️ 클래스

### <a id="class-componentpagingdto"></a>🎯 `ComponentPagingDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BasePaginationDto` |
| ⚡ 복잡도 | 10 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 10 || 📍 **라인 범위** | 10-10 |
| 🏗️ **상속** | `BasePaginationDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ComponentPagingDto extends BasePaginationDto {
    private String offerUUID;
    private String objUUID;
    private String objName;
    private String objCode;

    private String itemCode;
    private String validStartDtm;
    private String validEndDtm;
    private String componentType;
}...
```

**Chunk 정보**
- 🆔 **ID**: `b416eea55323`
- 📍 **라인**: 10-10
- 📊 **토큰**: 31
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **72개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 41 | 56.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 10.0 | 31 | 43.1% |

