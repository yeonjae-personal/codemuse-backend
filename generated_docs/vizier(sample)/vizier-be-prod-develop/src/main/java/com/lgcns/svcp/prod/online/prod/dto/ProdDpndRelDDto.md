# 📄 ProdDpndRelDDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/dto/ProdDpndRelDDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ProdDpndRelDDto`](#class-proddpndrelddto) - 복잡도: 79

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.util.paging.BasePaginationDto` • `lombok.Data` | ⚡ **총 복잡도**: 79 |
| 📊 **총 토큰 수**: 461 |  |



## 🏗️ 클래스

### <a id="class-proddpndrelddto"></a>🎯 `ProdDpndRelDDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BasePaginationDto` |
| ⚡ 복잡도 | 79 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 79 || 📍 **라인 범위** | 7-7 |
| 🏗️ **상속** | `BasePaginationDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ProdDpndRelDDto extends BasePaginationDto {
	private String baseUuid;
	private String trgtUuid;
	private String baseProdItemCd;
	private String trgtProdItemCd;
	private String dpndRelDivsCd;
	private String valdStrtDtm;
	private String valdEndDtm;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
	
	public String getBaseUuid() {
	        return this.baseUuid;
	}
	public String setBaseUuid(String baseUuid) {
	        return this.baseUuid = baseUuid;
	}
	public String getTrgtUuid() {
	        return this.trgtUuid;
	}
	public String setTrgtUuid(String trgtUuid) {
	        return this.trgtUuid = trgtUuid;
	}
	public String getBaseProdItemCd() {
	        return this.baseProdItemCd;
	}
	public String setBaseProdItemCd(String baseProdIt...
```

**Chunk 정보**
- 🆔 **ID**: `1a944e494c2e`
- 📍 **라인**: 7-7
- 📊 **토큰**: 227
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **461개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 234 | 50.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 79.0 | 227 | 49.2% |

