# 📄 OfferComponentStructureReq.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/offer/structure/update/OfferComponentStructureReq.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `OfferComponentStructureReq`
- **도메인**: offer
- **목적**: 비즈니스 로직 처리
- **복잡도**: 7
- **관련 파일**: ./sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/offer/structure/update/*
- **라인**: 10


## 📑 목차

### 🏗️ 클래스
- [`OfferComponentStructureReq`](#class-offercomponentstructurereq) - 복잡도: 7

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.ui.prod.dto.BaseDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 7 |
| 📊 **총 토큰 수**: 54 |  |



## 🏗️ 클래스

### <a id="class-offercomponentstructurereq"></a>🎯 `OfferComponentStructureReq`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseDto` |
| ⚡ 복잡도 | 7 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 7 || 📍 **라인 범위** | 10-10 |
| 🏗️ **상속** | `BaseDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class OfferComponentStructureReq extends BaseDto {
    private String offerUuid;
    private String objUuid;
    private String relationValidStartDtm;
    private String relationValidEndDtm;
    private String workTypeCode;
}...
```

**Chunk 정보**
- 🆔 **ID**: `dfb25b48ff16`
- 📍 **라인**: 10-10
- 📊 **토큰**: 22
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **54개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 32 | 59.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 7.0 | 22 | 40.7% |

