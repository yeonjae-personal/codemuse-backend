# 📄 ComponentSearchReq.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/component/list/ComponentSearchReq.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `ComponentSearchReq`
- **도메인**: product_ui
- **목적**: 비즈니스 로직 처리
- **복잡도**: 10
- **관련 파일**: ./sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/component/list/*
- **라인**: 12


## 📑 목차

### 🏗️ 클래스
- [`ComponentSearchReq`](#class-componentsearchreq) - 복잡도: 10

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.ui.prod.enums.ComponentItemCode` • `com.lgcns.svcp.prod.ui.prod.enums.ComponentType` • `lombok.AllArgsConstructor` • `lombok.Data` • `lombok.NoArgsConstructor` | ⚡ **총 복잡도**: 10 |
| 📊 **총 토큰 수**: 73 |  |



## 🏗️ 클래스

### <a id="class-componentsearchreq"></a>🎯 `ComponentSearchReq`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 10 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 10 || 📍 **라인 범위** | 12-12 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ComponentSearchReq {
    private String offerUUID;
    private ComponentType componentType;
    private ComponentItemCode componentSubType;
    private String code;
    private String name;
    private boolean onlyValidDtm;
    private Integer page;
    private Integer size;
}...
```

**Chunk 정보**
- 🆔 **ID**: `9d643df0e1a3`
- 📍 **라인**: 12-12
- 📊 **토큰**: 29
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **73개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 44 | 60.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 10.0 | 29 | 39.7% |

