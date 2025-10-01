# 📄 DsbdListViewMapper.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/mapper/DsbdListViewMapper.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`DsbdListViewMapper`](#interface-dsbdlistviewmapper)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `org.mapstruct.Mapper` • `org.mapstruct.Mapping` • `com.lgcns.svcp.prod.entity.external.DsbdListViewEntity` • `com.lgcns.svcp.prod.ui.prod.dto.dashboard.DsbdListViewDto` | ⚡ **총 복잡도**: 4 |
| 📊 **총 토큰 수**: 41 |  |




## 🔌 인터페이스

### <a id="interface-dsbdlistviewmapper"></a>🔌 `DsbdListViewMapper`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 10-10 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface DsbdListViewMapper {
	
	@Mapping(target = "dsbdViewDscrCntn", source = "entity.dsbdViewDscr")
	DsbdListViewDto entityToDto(DsbdListViewEntity entity);
}...
```

**Chunk 정보**
- 🆔 **ID**: `271b1186c7ae`
- 📊 **토큰**: 14

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **41개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 27 | 65.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 4.0 | 14 | 34.1% |

