# 📄 UserPocketMapper.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/mapper/UserPocketMapper.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`UserPocketMapper`](#interface-userpocketmapper)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `org.mapstruct.Mapper` • `org.mapstruct.Mapping` • `com.lgcns.svcp.prod.entity.external.UserPocketCustomEntity` • `com.lgcns.svcp.prod.ui.prod.dto.userpocket.UserPocketDto` | ⚡ **총 복잡도**: 8 |
| 📊 **총 토큰 수**: 89 |  |




## 🔌 인터페이스

### <a id="interface-userpocketmapper"></a>🔌 `UserPocketMapper`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 10-10 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface UserPocketMapper {
	
	@Mapping(target = "name", source = "entity.objName")
	@Mapping(target = "code", source = "entity.objCode")
	@Mapping(target = "type", source = "entity.itemCode")
	@Mapping(target = "uuid", source = "entity.objUuid")
	@Mapping(target = "middleType", source = "entity.mctgrItemCode")
	UserPocketDto customEntityToDto(UserPocketCustomEntity entity);
}...
```

**Chunk 정보**
- 🆔 **ID**: `50434ca2fc5f`
- 📊 **토큰**: 38

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **89개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 51 | 57.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 8.0 | 38 | 42.7% |

