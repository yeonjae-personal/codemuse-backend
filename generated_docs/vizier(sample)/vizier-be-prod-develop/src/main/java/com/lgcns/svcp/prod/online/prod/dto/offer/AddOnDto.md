# 📄 AddOnDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/dto/offer/AddOnDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`AddOnDto`](#class-addondto) - 복잡도: 16

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `java.util.Map` • `com.lgcns.svcp.prod.online.prod.dto.common.AdditionalColumnsDto` • `lombok.Data` | ⚡ **총 복잡도**: 16 |
| 📊 **총 토큰 수**: 105 |  |



## 🏗️ 클래스

### <a id="class-addondto"></a>🎯 `AddOnDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 16 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 16 || 📍 **라인 범위** | 11-11 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class AddOnDto {
	private String addonUuid;
	private String addonCode;
	private String addonName;
	private String ovwCntn;
	private String dplcTrgtUuid;
	private String validStartDtm;
	private String validEndDtm;
	private String saleValidStartDtm;
	private String saleValidEndDtm;
	private String rgstUser;
	private String rgstDtm;
	private String updUser;
	private String updDtm;
	private List<AdditionalColumnsDto> additionalColumns;
}...
```

**Chunk 정보**
- 🆔 **ID**: `b2d90f4d3b74`
- 📍 **라인**: 11-11
- 📊 **토큰**: 47
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **105개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 58 | 55.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 16.0 | 47 | 44.8% |

