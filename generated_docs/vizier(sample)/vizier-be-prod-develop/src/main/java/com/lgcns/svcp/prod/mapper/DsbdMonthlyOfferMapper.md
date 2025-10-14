# 📄 DsbdMonthlyOfferMapper.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/mapper/DsbdMonthlyOfferMapper.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`DsbdMonthlyOfferMapper`](#interface-dsbdmonthlyoffermapper)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.mapstruct.Mapper` • `com.lgcns.svcp.prod.entity.DsbdMonthlyOfferEntity` • `com.lgcns.svcp.prod.ui.prod.dto.dashboard.response.DsbdMonthlyOfferResponse` | ⚡ **총 복잡도**: 3 |
| 📊 **총 토큰 수**: 29 |  |




## 🔌 인터페이스

### <a id="interface-dsbdmonthlyoffermapper"></a>🔌 `DsbdMonthlyOfferMapper`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 11-11 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface DsbdMonthlyOfferMapper {
	
	List<DsbdMonthlyOfferResponse> convertListEntityToResponse(List<DsbdMonthlyOfferEntity> entity);
}...
```

**Chunk 정보**
- 🆔 **ID**: `72553572351e`
- 📊 **토큰**: 8

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **29개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 21 | 72.4% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 3.0 | 8 | 27.6% |

