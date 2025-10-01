# 📄 OfferMapper.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/mapper/OfferMapper.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`OfferMapper`](#interface-offermapper)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.text.DecimalFormat` • `org.mapstruct.Mapper` • `org.mapstruct.Mapping` • `org.mapstruct.ReportingPolicy` • `org.mapstruct.factory.Mappers` • `com.lgcns.svcp.prod.ui.prod.dto.export.OfferExportDto` 외 1개 | ⚡ **총 복잡도**: 7 |
| 📊 **총 토큰 수**: 80 |  |




## 🔌 인터페이스

### <a id="interface-offermapper"></a>🔌 `OfferMapper`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 14-14 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface OfferMapper {

    OfferMapper INSTANCE = Mappers.getMapper(OfferMapper.class);
    DecimalFormat df = new DecimalFormat("0.00");

    @Mapping(source = "general", target = "general")
    @Mapping(source = "additional", target = "additional")
    OfferExportDto offerToExportDto(OfferSearchRes dto);
}...
```

**Chunk 정보**
- 🆔 **ID**: `c281de643529`
- 📊 **토큰**: 29

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **80개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 51 | 63.7% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 7.0 | 29 | 36.2% |

