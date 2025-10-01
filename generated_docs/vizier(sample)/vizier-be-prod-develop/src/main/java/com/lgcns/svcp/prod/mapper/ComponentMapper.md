# 📄 ComponentMapper.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/mapper/ComponentMapper.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`ComponentMapper`](#interface-componentmapper)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.mapstruct.Mapper` • `org.mapstruct.Mapping` • `org.mapstruct.ReportingPolicy` • `com.lgcns.svcp.prod.ui.prod.dto.component.addResource.ComponentAddResourceDto` • `com.lgcns.svcp.prod.ui.prod.dto.component.addResource.ComponentAddResourceRes` 외 2개 | ⚡ **총 복잡도**: 6 |
| 📊 **총 토큰 수**: 70 |  |




## 🔌 인터페이스

### <a id="interface-componentmapper"></a>🔌 `ComponentMapper`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 15-15 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface ComponentMapper {
    List<ComponentAddResourceRes> dtoToResList(List<ComponentAddResourceDto> componentAddResourceDtoList);
    
    @Mapping(source = "general", target = "general")
    @Mapping(source = "additional", target = "additional")
    ComponentExportDto componentToExportDto(ComponentSearchRes dto);
}...
```

**Chunk 정보**
- 🆔 **ID**: `3bb2a12febb2`
- 📊 **토큰**: 23

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **70개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 47 | 67.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 6.0 | 23 | 32.9% |

