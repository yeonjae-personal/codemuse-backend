# 📄 FieldMapper.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/mapper/ruleengine/FieldMapper.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`FieldMapper`](#interface-fieldmapper)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.mapstruct.Mapper` • `com.lgcns.svcp.prod.ruleengine.dto.field.FieldDto` • `com.lgcns.svcp.prod.ruleengine.entity.FieldEntity` | ⚡ **총 복잡도**: 5 |
| 📊 **총 토큰 수**: 41 |  |




## 🔌 인터페이스

### <a id="interface-fieldmapper"></a>🔌 `FieldMapper`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 10-10 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface FieldMapper {
	
	FieldDto convertEntityToDto(FieldEntity entity);
	FieldEntity convertDtoToEntity(FieldDto dto);
	List<FieldDto> convertListEntityToDto(List<FieldEntity> entities);	
}...
```

**Chunk 정보**
- 🆔 **ID**: `1383699656bd`
- 📊 **토큰**: 14

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **41개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 27 | 65.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 5.0 | 14 | 34.1% |

