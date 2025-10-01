# 📄 ComponentUpdateResourceReq.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/component/update/ComponentUpdateResourceReq.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `ComponentUpdateResourceReq`
- **도메인**: user
- **목적**: 비즈니스 로직 처리
- **복잡도**: 7
- **관련 파일**: /Users/roseline/projects/codemuse-backend/sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/component/update/*
- **라인**: 8


## 📑 목차

### 🏗️ 클래스
- [`ComponentUpdateResourceReq`](#class-componentupdateresourcereq) - 복잡도: 7

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.ui.prod.dto.BaseDto` • `lombok.Data` | ⚡ **총 복잡도**: 7 |
| 📊 **총 토큰 수**: 51 |  |



## 🏗️ 클래스

### <a id="class-componentupdateresourcereq"></a>🎯 `ComponentUpdateResourceReq`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseDto` |
| ⚡ 복잡도 | 7 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 7 || 📍 **라인 범위** | 8-8 |
| 🏗️ **상속** | `BaseDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ComponentUpdateResourceReq extends BaseDto {
    private String componentUUID;
    private String resourceUUID;
    private String validStartDtm;
    private String validEndDtm;
    private String workTypeCode;
}...
```

**Chunk 정보**
- 🆔 **ID**: `f1e0dac0df5b`
- 📍 **라인**: 8-8
- 📊 **토큰**: 22
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **51개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 29 | 56.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 7.0 | 22 | 43.1% |

