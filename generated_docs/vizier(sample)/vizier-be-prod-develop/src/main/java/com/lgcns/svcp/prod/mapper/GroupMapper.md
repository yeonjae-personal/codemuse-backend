# 📄 GroupMapper.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/mapper/GroupMapper.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`GroupMapper`](#interface-groupmapper)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `org.mapstruct.Mapper` • `org.mapstruct.Mapping` • `org.mapstruct.ReportingPolicy` • `com.lgcns.svcp.prod.ui.prod.dto.export.GroupExportDto` • `com.lgcns.svcp.prod.ui.prod.dto.group.search.GroupSearchRes` | ⚡ **총 복잡도**: 5 |
| 📊 **총 토큰 수**: 58 |  |




## 🔌 인터페이스

### <a id="interface-groupmapper"></a>🔌 `GroupMapper`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 11-11 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface GroupMapper {
	
	@Mapping(source = "general", target = "general")
    @Mapping(source = "additional", target = "additional")
    GroupExportDto groupToExportDto(GroupSearchRes dto);
}...
```

**Chunk 정보**
- 🆔 **ID**: `28596e713766`
- 📊 **토큰**: 20

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **58개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 38 | 65.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 5.0 | 20 | 34.5% |

