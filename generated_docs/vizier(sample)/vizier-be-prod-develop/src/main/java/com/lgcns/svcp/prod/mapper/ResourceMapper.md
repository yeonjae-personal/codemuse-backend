# 📄 ResourceMapper.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/mapper/ResourceMapper.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`ResourceMapper`](#interface-resourcemapper)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.mapstruct.Mapper` • `org.mapstruct.Mapping` • `org.mapstruct.ReportingPolicy` • `com.lgcns.svcp.prod.ui.prod.dto.export.ResourceExportDto` • `com.lgcns.svcp.prod.ui.prod.dto.resource.ResourceSearchReq` 외 3개 | ⚡ **총 복잡도**: 7 |
| 📊 **총 토큰 수**: 78 |  |




## 🔌 인터페이스

### <a id="interface-resourcemapper"></a>🔌 `ResourceMapper`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 16-16 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface ResourceMapper {

    List<ResourceGeneralRes> resourceDtoListToRes(List<ResourceSearchDto> generalDtoList);

    ResourceSearchDto resourceSearchToDto(ResourceSearchReq searchReq);
    
    @Mapping(source = "general", target = "general")
    @Mapping(source = "additional", target = "additional")
    ResourceExportDto resourceToExportDto(ResoureSearchRes dto);
}...
```

**Chunk 정보**
- 🆔 **ID**: `62d216e5c270`
- 📊 **토큰**: 26

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **78개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 52 | 66.7% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 7.0 | 26 | 33.3% |

