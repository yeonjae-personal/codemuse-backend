# 📄 ProductStructureDownloadDto.java

> **파일 경로**: `vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/analysis/download/ProductStructureDownloadDto.java`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ProductStructureDownloadDto`](#class-productstructuredownloaddto) - 복잡도: 24 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.util.excel.annotation.CustomTitleHeader` • `com.lgcns.svcp.prod.util.excel.annotation.Value` • `lombok.Data` | ⚡ **총 복잡도**: 24 |
| 📊 **총 토큰 수**: 152 |  |



## 🏗️ 클래스

### <a id="class-productstructuredownloaddto"></a>🎯 `ProductStructureDownloadDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 24 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 24 || 📍 **라인 범위** | 10-10 |
| 🏷️ **태그** | `class, java, value, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ProductStructureDownloadDto {
    @Value(name = "impactAnalysis.excel.no")
    private int no;

    @Value(name = "impactAnalysis.excel.offrCd")
    private String offrCd;
    @Value(name = "impactAnalysis.excel.offrNm")
    private String offrNm;

    @Value(name = "impactAnalysis.excel.cmpCd")
    private String cmpCd;
    @Value(name = "impactAnalysis.excel.cmpNm")
    private String cmpNm;
    @Value(name = "impactAnalysis.excel.cmpValdStrtDtm")
    private String cmpValdStrtDtm;
    @Value(name = "impactAnalysis.excel.cmpValdEndDtm")
    private String cmpValdEndDtm;

    @Value(name = "impactAnalysis.excel.svcCd")
    private String svcCd;
    @Value(name = "impactAnalysis.excel.svcNm")
    private String svcNm;
    @Value(name = "impactAnalysis.excel.svcValdStrtDtm")
  ...
```

**Chunk 정보**
- 🆔 **ID**: `ff92a9de3854`
- 📍 **라인**: 10-10
- 📊 **토큰**: 71
- 🏷️ **태그**: `class, java, value, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **152개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 81 | 53.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 24.0 | 71 | 46.7% |

