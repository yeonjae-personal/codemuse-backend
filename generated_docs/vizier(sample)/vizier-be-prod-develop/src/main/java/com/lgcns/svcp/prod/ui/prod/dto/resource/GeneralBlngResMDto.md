# 📄 GeneralBlngResMDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/resource/GeneralBlngResMDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`GeneralBlngResMDto`](#class-generalblngresmdto) - 복잡도: 28

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.fasterxml.jackson.annotation.JsonIgnore` • `lombok.Data` | ⚡ **총 복잡도**: 28 |
| 📊 **총 토큰 수**: 167 |  |



## 🏗️ 클래스

### <a id="class-generalblngresmdto"></a>🎯 `GeneralBlngResMDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 28 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 28 || 📍 **라인 범위** | 8-8 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class GeneralBlngResMDto {
	@JsonIgnore
	private String svcFctrCd;
	private String svcFctrNm;
	private String svcFctrClssCd;
	private String svcFctrClssDetlCd;
	private String svcFctrLnwlEtcCd;
	private String svcFctrCallKdCd;
	private String svcFctrCallKdDetlCd;
	private String valdEndDtm;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
	
	public GeneralBlngResMDto(RawBlngResMDto rawBlngResMDto) {
        this.svcFctrCd = rawBlngResMDto.getSvcFctrCd();
        this.svcFctrNm = rawBlngResMDto.getSvcFctrNm();
        this.svcFctrClssDetlCd = rawBlngResMDto.getSvcFctrClssDetlCd();
        this.svcFctrLnwlEtcCd = rawBlngResMDto.getSvcFctrLnwlEtcCd();
        this.svcFctrCallKdCd = rawBlngResMDto.getSvcFctrCallKdCd();
        this.svcFct...
```

**Chunk 정보**
- 🆔 **ID**: `2280352cf0ed`
- 📍 **라인**: 8-8
- 📊 **토큰**: 80
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **167개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 87 | 52.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 28.0 | 80 | 47.9% |

