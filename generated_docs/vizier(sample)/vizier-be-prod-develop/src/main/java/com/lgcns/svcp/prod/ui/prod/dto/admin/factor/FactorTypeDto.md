# 📄 FactorTypeDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/admin/factor/FactorTypeDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`FactorTypeDto`](#class-factortypedto) - 복잡도: 8

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.BaseDto` • `com.lgcns.svcp.prod.util.paging.PageResult` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 8 |
| 📊 **총 토큰 수**: 64 |  |



## 🏗️ 클래스

### <a id="class-factortypedto"></a>🎯 `FactorTypeDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseDto` |
| ⚡ 복잡도 | 8 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 8 || 📍 **라인 범위** | 13-13 |
| 🏗️ **상속** | `BaseDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class FactorTypeDto extends BaseDto {
    private String factorTypeCode;
    private String factorTypeName;
    private String useYn;
    private int sortNo;
    private List<FactorDto> factorLst;
    private PageResult<?> factorSearchLst;
}...
```

**Chunk 정보**
- 🆔 **ID**: `7bf550db32a9`
- 📍 **라인**: 13-13
- 📊 **토큰**: 25
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **64개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 39 | 60.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 8.0 | 25 | 39.1% |

