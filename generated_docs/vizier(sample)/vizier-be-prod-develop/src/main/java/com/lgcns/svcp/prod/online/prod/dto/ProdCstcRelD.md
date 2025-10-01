# 📄 ProdCstcRelD.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/dto/ProdCstcRelD.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ProdCstcRelD`](#class-prodcstcreld) - 복잡도: 86

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.util.paging.BasePaginationDto` • `lombok.Data` | ⚡ **총 복잡도**: 86 |
| 📊 **총 토큰 수**: 501 |  |



## 🏗️ 클래스

### <a id="class-prodcstcreld"></a>🎯 `ProdCstcRelD`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BasePaginationDto` |
| ⚡ 복잡도 | 86 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 86 || 📍 **라인 범위** | 7-7 |
| 🏗️ **상속** | `BasePaginationDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ProdCstcRelD extends BasePaginationDto {
	private String baseUuid;
	private String trgtUuid;
	private String baseProdItemCd;
	private String trgtProdItemCd;
	private String valdStrtDtm;
	private String valdEndDtm;
	private String prodCstcRelCd;
	private String cstcXclnYn;
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
	public String setBasePro...
```

**Chunk 정보**
- 🆔 **ID**: `8b939bece054`
- 📍 **라인**: 7-7
- 📊 **토큰**: 247
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **501개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 254 | 50.7% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 86.0 | 247 | 49.3% |

