# 📄 RelationManagerMapper.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/mapper/RelationManagerMapper.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`RelationManagerMapper`](#interface-relationmanagermapper)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.mapstruct.Mapper` • `org.mapstruct.Mapping` • `com.lgcns.svcp.prod.ui.prod.dto.extend.RelationGridViewDto` • `com.lgcns.svcp.prod.ui.prod.dto.extend.RelationGridViewEntity` • `com.lgcns.svcp.prod.ui.prod.dto.extend.RelationGridViewExportDto` 외 1개 | ⚡ **총 복잡도**: 10 |
| 📊 **총 토큰 수**: 101 |  |




## 🔌 인터페이스

### <a id="interface-relationmanagermapper"></a>🔌 `RelationManagerMapper`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 14-14 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface RelationManagerMapper {
	
	@Mapping(target = "relationCode", source = "relationViewResDto.dpdcRelCode")
	@Mapping(target = "relationName", source = "relationViewResDto.dpdcRelName")
	@Mapping(target = "followerCode", source = "relationViewResDto.targetCode")
	@Mapping(target = "followerName", source = "relationViewResDto.targetName")
	RelationGridViewDto viewResToGridViewDto(RelationViewResDto relationViewResDto);
	
	RelationGridViewExportDto convertToExcelExport(RelationGridViewDto dtos);
	RelationGridViewDto convertToDto(RelationGridViewEntity entity);
	RelationGridViewExpor...
```

**Chunk 정보**
- 🆔 **ID**: `d96c803643ee`
- 📊 **토큰**: 41

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **101개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 60 | 59.4% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 10.0 | 41 | 40.6% |

