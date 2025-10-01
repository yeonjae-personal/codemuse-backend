# 📄 AdditionalBlngResMDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/resource/AdditionalBlngResMDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`AdditionalBlngResMDto`](#class-additionalblngresmdto) - 복잡도: 14

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `lombok.Data` | ⚡ **총 복잡도**: 14 |
| 📊 **총 토큰 수**: 85 |  |



## 🏗️ 클래스

### <a id="class-additionalblngresmdto"></a>🎯 `AdditionalBlngResMDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 14 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 14 || 📍 **라인 범위** | 6-6 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class AdditionalBlngResMDto {
	private String usePlcyYn;
	private String rawSvcFctrCd;
	private String svcFctrKdDetlCd;
	private String svcFctrKdCd;
	private String rtmSysAplyYn;

	public AdditionalBlngResMDto(RawBlngResMDto rawBlngResMDto) {
        this.usePlcyYn = rawBlngResMDto.getUsePlcyYn();
        this.rawSvcFctrCd = rawBlngResMDto.getRawSvcFctrCd();
        this.svcFctrKdDetlCd = rawBlngResMDto.getSvcFctrCallKdDetlCd();
        this.svcFctrKdCd = rawBlngResMDto.getRawSvcFctrCd();
        this.rtmSysAplyYn = rawBlngResMDto.getRtmSysAplyYn();
    }
}...
```

**Chunk 정보**
- 🆔 **ID**: `eeace5a9f01d`
- 📍 **라인**: 6-6
- 📊 **토큰**: 40
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **85개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 45 | 52.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 14.0 | 40 | 47.1% |

