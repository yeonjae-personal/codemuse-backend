# 📄 DsbdRecentWorkMapper.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/mapper/DsbdRecentWorkMapper.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`DsbdRecentWorkMapper`](#interface-dsbdrecentworkmapper)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `org.mapstruct.Mapper` • `org.mapstruct.Mapping` • `com.lgcns.svcp.prod.entity.DsbdRecentlyWorkEntity` • `com.lgcns.svcp.prod.ui.prod.dto.dashboard.DsbdRecentlyWorkExportDto` • `com.lgcns.svcp.prod.ui.prod.dto.dashboard.RecentWorkDetailViewDto` • `com.lgcns.svcp.prod.ui.prod.dto.dashboard.RecentWorkSimpleViewDto` | ⚡ **총 복잡도**: 13 |
| 📊 **총 토큰 수**: 141 |  |




## 🔌 인터페이스

### <a id="interface-dsbdrecentworkmapper"></a>🔌 `DsbdRecentWorkMapper`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 12-12 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface DsbdRecentWorkMapper {
	
	@Mapping(target = "workTypeCode", source = "entity.workType")
	RecentWorkSimpleViewDto entityToSimpleViewDto(DsbdRecentlyWorkEntity entity);
	
	@Mapping(target = "category", source = "entity.lctgrItemName")
	@Mapping(target = "workTypeCode", source = "entity.workType")
	@Mapping(target = "responsibleDept", source = "entity.updUserDeptName")
	@Mapping(target = "responsibleUser", source = "entity.attrValUpdUser")
	RecentWorkDetailViewDto entityToDetailViewDto(DsbdRecentlyWorkEntity entity);
	
	@Mapping(target = "category", source = "entity.lctgrItemName...
```

**Chunk 정보**
- 🆔 **ID**: `bb71a951ce38`
- 📊 **토큰**: 62

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **141개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 79 | 56.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 13.0 | 62 | 44.0% |

