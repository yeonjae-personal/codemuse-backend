# 📄 ComponentAddResourceDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/component/addResource/ComponentAddResourceDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `ComponentAddResourceDto`
- **도메인**: user
- **목적**: ComponentAddResourceDto 데이터 전송 객체
- **복잡도**: 14
- **관련 파일**: /Users/roseline/projects/codemuse-backend/sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/component/addResource/*
- **라인**: 10


## 📑 목차

### 🏗️ 클래스
- [`ComponentAddResourceDto`](#class-componentaddresourcedto) - 복잡도: 14

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.util.paging.BasePaginationDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 14 |
| 📊 **총 토큰 수**: 96 |  |



## 🏗️ 클래스

### <a id="class-componentaddresourcedto"></a>🎯 `ComponentAddResourceDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BasePaginationDto` |
| ⚡ 복잡도 | 14 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 14 || 📍 **라인 범위** | 10-10 |
| 🏗️ **상속** | `BasePaginationDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ComponentAddResourceDto extends BasePaginationDto {
    private String componentUUID;
    private String uuid;
    private String name;
    private String code;
    private String itemCode;
    private String rscTypeCode;
    private String startDate;
    private String endDate;
    private String relationStartDate;
    private String relationEndDate;
    private String overview;
    private String componentCreateType;
}...
```

**Chunk 정보**
- 🆔 **ID**: `0fb2d70ce946`
- 📍 **라인**: 10-10
- 📊 **토큰**: 43
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **96개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 53 | 55.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 14.0 | 43 | 44.8% |

