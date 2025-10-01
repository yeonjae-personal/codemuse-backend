# 📄 PricePlanAndComponentDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/dto/offer/PricePlanAndComponentDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `PricePlanAndComponentDto`
- **도메인**: offer
- **목적**: PricePlanAndComponentDto 데이터 전송 객체
- **복잡도**: 4
- **관련 파일**: /Users/roseline/projects/codemuse-backend/sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/dto/offer/*
- **라인**: 9


## 📑 목차

### 🏗️ 클래스
- [`PricePlanAndComponentDto`](#class-priceplanandcomponentdto) - 복잡도: 4

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `java.util.Map` • `lombok.Data` | ⚡ **총 복잡도**: 4 |
| 📊 **총 토큰 수**: 67 |  |



## 🏗️ 클래스

### <a id="class-priceplanandcomponentdto"></a>🎯 `PricePlanAndComponentDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 4 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 4 || 📍 **라인 범위** | 9-9 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class PricePlanAndComponentDto {
	private List<Map<String, Object>> pricePlan;
	private Map<String, List<String>> componentList;
//	private List<String> benefit;
//	private List<String> characteristics;
//	private List<String> price;
//	private List<String> service;
}...
```

**Chunk 정보**
- 🆔 **ID**: `3eb59caaba6e`
- 📍 **라인**: 9-9
- 📊 **토큰**: 29
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **67개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 38 | 56.7% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 4.0 | 29 | 43.3% |

