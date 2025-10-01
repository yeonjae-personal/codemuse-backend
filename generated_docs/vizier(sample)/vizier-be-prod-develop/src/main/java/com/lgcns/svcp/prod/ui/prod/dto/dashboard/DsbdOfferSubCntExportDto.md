# 📄 DsbdOfferSubCntExportDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/dashboard/DsbdOfferSubCntExportDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`DsbdOfferSubCntExportDto`](#class-dsbdoffersubcntexportdto) - 복잡도: 18 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.util.excel.annotation.CustomTitleHeader` • `com.lgcns.svcp.prod.util.excel.annotation.Value` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 18 |
| 📊 **총 토큰 수**: 103 |  |



## 🏗️ 클래스

### <a id="class-dsbdoffersubcntexportdto"></a>🎯 `DsbdOfferSubCntExportDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 18 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 18 || 📍 **라인 범위** | 12-12 |
| 🏷️ **태그** | `class, java, value, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class DsbdOfferSubCntExportDto {
	
	@Value(name ="dashboard.subscribertop10.excel.offercode")
	private String code;
	
	@Value(name ="dashboard.subscribertop10.excel.offertype")
	private String type;
	
	@Value(name ="dashboard.subscribertop10.excel.offername")
	private String name;
	
	@Value(name ="dashboard.subscribertop10.excel.noofsubscriber")
	private Integer subscriber;
	
	@Value(name ="dashboard.subscribertop10.excel.status")
	private String status;
	
	@Value(name ="dashboard.subscribertop10.excel.startdate")
	private String startDate;
	
	@Value(name ="dashboard.subscribertop10.excel.enddate")
	private String endDate;
	
	@Value(name ="dashboard.subscribertop10.excel.duration")
	private Long duration;
}...
```

**Chunk 정보**
- 🆔 **ID**: `453b82495970`
- 📍 **라인**: 12-12
- 📊 **토큰**: 45
- 🏷️ **태그**: `class, java, value, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **103개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 58 | 56.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 18.0 | 45 | 43.7% |

