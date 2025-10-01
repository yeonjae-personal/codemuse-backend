# 📄 DsbdOfferSubCntMapper.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/mapper/DsbdOfferSubCntMapper.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`DsbdOfferSubCntMapper`](#interface-dsbdoffersubcntmapper)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `org.mapstruct.Mapper` • `org.mapstruct.Mapping` • `com.lgcns.svcp.prod.entity.DsbdOfferSubCntEntity` • `com.lgcns.svcp.prod.ui.prod.dto.dashboard.DsbdOfferSubCntDto` • `com.lgcns.svcp.prod.ui.prod.dto.dashboard.DsbdOfferSubCntExportDto` • `com.lgcns.svcp.prod.ui.prod.dto.dashboard.response.SubscribeTopSimpleViewResponse` | ⚡ **총 복잡도**: 19 |
| 📊 **총 토큰 수**: 213 |  |




## 🔌 인터페이스

### <a id="interface-dsbdoffersubcntmapper"></a>🔌 `DsbdOfferSubCntMapper`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 13-13 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface DsbdOfferSubCntMapper {
	
	@Mapping(target = "name", source = "entity.offerName")
	@Mapping(target = "subscriber", source = "entity.subCnt")
	SubscribeTopSimpleViewResponse entityToSimpleViewResponse(DsbdOfferSubCntEntity entity);
	
	@Mapping(target = "name", source = "entity.offerName")
	@Mapping(target = "code", source = "entity.offerCode")
	@Mapping(target = "type", source = "entity.offerTypeName")
	@Mapping(target = "subscriber", source = "entity.subCnt")
	@Mapping(target = "startDate", source = "entity.saleValidStartDtm")
	@Mapping(target = "endDate", source = "entity.sal...
```

**Chunk 정보**
- 🆔 **ID**: `4167b1e96800`
- 📊 **토큰**: 98

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **213개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 115 | 54.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 19.0 | 98 | 46.0% |

