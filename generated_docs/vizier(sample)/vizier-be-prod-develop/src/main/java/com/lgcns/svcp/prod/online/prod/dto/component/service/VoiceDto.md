# 📄 VoiceDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/dto/component/service/VoiceDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `VoiceDto`
- **도메인**: product_online
- **목적**: VoiceDto 데이터 전송 객체
- **복잡도**: 14
- **관련 파일**: ./sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/dto/component/service/*
- **라인**: 10


## 📑 목차

### 🏗️ 클래스
- [`VoiceDto`](#class-voicedto) - 복잡도: 14

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.online.prod.dto.common.AdditionalColumnsDto` • `lombok.Data` | ⚡ **총 복잡도**: 14 |
| 📊 **총 토큰 수**: 91 |  |



## 🏗️ 클래스

### <a id="class-voicedto"></a>🎯 `VoiceDto`

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
public class VoiceDto {
	private String vicSvcUuid;
	private String vicSvcCode;
	private String vicSvcName;
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
- 🆔 **ID**: `35003f70013e`
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

