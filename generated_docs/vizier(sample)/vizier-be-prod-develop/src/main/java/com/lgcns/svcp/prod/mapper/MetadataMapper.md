# 📄 MetadataMapper.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/mapper/MetadataMapper.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`MetadataMapper`](#interface-metadatamapper)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `org.mapstruct.Mapper` • `org.mapstruct.Mapping` • `com.lgcns.svcp.prod.entity.MiddleItemEntity` • `com.lgcns.svcp.prod.ui.prod.dto.item.MiddleItemDto` | ⚡ **총 복잡도**: 5 |
| 📊 **총 토큰 수**: 53 |  |




## 🔌 인터페이스

### <a id="interface-metadatamapper"></a>🔌 `MetadataMapper`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 10-10 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface MetadataMapper {
	
	@Mapping(target = "code", source = "entity.itemCode")
	@Mapping(target = "name", source = "entity.itemCodeName")
	MiddleItemDto middleItemEntityToDto(MiddleItemEntity entity);
}...
```

**Chunk 정보**
- 🆔 **ID**: `1a21b85f21f0`
- 📊 **토큰**: 20

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **53개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 33 | 62.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 5.0 | 20 | 37.7% |

