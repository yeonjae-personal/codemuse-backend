# 📄 GeneralServiceResMDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/resource/GeneralServiceResMDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `GeneralServiceResMDto`
- **도메인**: user
- **목적**: GeneralServiceResMDto 비즈니스 로직 처리
- **복잡도**: 27
- **관련 파일**: /Users/roseline/projects/codemuse-backend/sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/resource/*
- **라인**: 6


## 📑 목차

### 🏗️ 클래스
- [`GeneralServiceResMDto`](#class-generalserviceresmdto) - 복잡도: 27

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `lombok.Data` | ⚡ **총 복잡도**: 27 |
| 📊 **총 토큰 수**: 163 |  |



## 🏗️ 클래스

### <a id="class-generalserviceresmdto"></a>🎯 `GeneralServiceResMDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 27 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 27 || 📍 **라인 범위** | 6-6 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class GeneralServiceResMDto {
	private String prodUuid;
	private String svcFctrCd;
	private String svcFctrNm;
	private String svcFctrClssCd;
	private String svcFctrKdCd;
	private String swtcSbgnYn;
	private String swtcCd;
	private String valdEndDtm;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
	
	 public GeneralServiceResMDto(RawServiceResMDto rawServiceResMDto) {
	        this.prodUuid = rawServiceResMDto.getProdUuid();
	        this.svcFctrCd = rawServiceResMDto.getSvcFctrCd();
	        this.svcFctrNm = rawServiceResMDto.getSvcFctrNm();
	        this.svcFctrClssCd = rawServiceResMDto.getSvcFctrClssCd();
	        this.svcFctrKdCd = rawServiceResMDto.getSvcFctrKdCd();
	        this.swtcSbgnYn = rawServiceResMDto.getSwtcSbgnYn();
	...
```

**Chunk 정보**
- 🆔 **ID**: `53a034a9ad9c`
- 📍 **라인**: 6-6
- 📊 **토큰**: 79
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **163개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 84 | 51.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 27.0 | 79 | 48.5% |

