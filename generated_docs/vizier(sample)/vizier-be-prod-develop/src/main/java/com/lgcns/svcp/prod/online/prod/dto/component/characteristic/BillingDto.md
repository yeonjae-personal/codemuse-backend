# 📄 BillingDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/dto/component/characteristic/BillingDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `BillingDto`
- **도메인**: billing
- **목적**: BillingDto 데이터 전송 객체
- **복잡도**: 14
- **관련 파일**: /Users/roseline/projects/codemuse-backend/sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/dto/component/characteristic/*
- **라인**: 10


## 📑 목차

### 🏗️ 클래스
- [`BillingDto`](#class-billingdto) - 복잡도: 14

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.online.prod.dto.common.AdditionalColumnsDto` • `lombok.Data` | ⚡ **총 복잡도**: 14 |
| 📊 **총 토큰 수**: 91 |  |



## 🏗️ 클래스

### <a id="class-billingdto"></a>🎯 `BillingDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 14 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 14 || 📍 **라인 범위** | 10-10 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class BillingDto {
	private String blngUuid;
	private String blngCode;
	private String blngName;
	private String dplcTrgtUuid;
	private String ovwCntn;
	private String validStartDtm;
	private String validEndDtm;
	private String rgstUser;
	private String rgstDtm;
	private String updUser;
	private String updDtm;
	private List<AdditionalColumnsDto> additionalColumns;
}...
```

**Chunk 정보**
- 🆔 **ID**: `e891c2b9e428`
- 📍 **라인**: 10-10
- 📊 **토큰**: 41
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **91개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 50 | 54.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 14.0 | 41 | 45.1% |

