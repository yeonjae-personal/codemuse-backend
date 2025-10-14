# 📄 CategoryPathExportDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/export/CategoryPathExportDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`CategoryPathExportDto`](#class-categorypathexportdto) - 복잡도: 18 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.util.excel.annotation.CustomTitleHeader` • `com.lgcns.svcp.prod.util.excel.annotation.Value` • `lombok.Builder` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 18 |
| 📊 **총 토큰 수**: 106 |  |



## 🏗️ 클래스

### <a id="class-categorypathexportdto"></a>🎯 `CategoryPathExportDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 18 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 18 || 📍 **라인 범위** | 14-14 |
| 🏷️ **태그** | `class, java, value, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class CategoryPathExportDto {
	
	@Value(name ="category.offers.excel.index")
	private Integer index;
	
	@Value(name ="category.offers.excel.level1")
	private String level1;
	
	@Value(name ="category.offers.excel.level2")
	private String level2;
	
	@Value(name ="category.offers.excel.level3")
	private String level3;
	
	@Value(name ="category.offers.excel.level4")
	private String level4;
	
	@Value(name ="category.offers.excel.level5")
	private String level5;
	
	@Value(name ="category.offers.excel.offercode")
	private String offerCd;
	
	@Value(name ="category.offers.excel.offername")
	private String offerNm;

}...
```

**Chunk 정보**
- 🆔 **ID**: `1f0c2264d80c`
- 📍 **라인**: 14-14
- 📊 **토큰**: 45
- 🏷️ **태그**: `class, java, value, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **106개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 61 | 57.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 18.0 | 45 | 42.5% |

