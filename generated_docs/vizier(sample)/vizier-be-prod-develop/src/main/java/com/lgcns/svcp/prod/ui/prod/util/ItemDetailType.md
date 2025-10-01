# 📄 ItemDetailType.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/util/ItemDetailType.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 📋 열거형
- [`ItemDetailType`](#enum-itemdetailtype)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.ui.prod.dto.characteristic.DcntCstcMDto` • `com.lgcns.svcp.prod.ui.prod.dto.characteristic.BlngInfoMDto` • `com.lgcns.svcp.prod.ui.prod.dto.characteristic.DcntTrgtInfoMDto` • `com.lgcns.svcp.prod.ui.prod.dto.characteristic.LobMDto` • `com.lgcns.svcp.prod.ui.prod.dto.characteristic.QosMDto` • `com.lgcns.svcp.prod.ui.prod.dto.characteristic.SlinInfoMDto` 외 9개 | ⚡ **총 복잡도**: 56 |
| 📊 **총 토큰 수**: 444 |  |





## 📋 열거형

### <a id="enum-itemdetailtype"></a>📋 `ItemDetailType`


<details>
<summary>🔍 코드 미리보기</summary>

```java
public enum ItemDetailType {
	BASE_FEE("RC", "basfCd", MfMDto.class, "uiComponentService", "getGroupedMfM"),
    USE_FEE("UC", "usfeCd", UsfeMDto.class, "uiComponentService", "getGroupedUsfeM"),
    VOICE("VO", "pdspCd", PdspTossLkgeDDto.class, "uiComponentService", "getGroupedPdspTossLkgeD"),
    MESSAGE("MS", "pdspCd", PdspTossLkgeDDto.class, "uiComponentService", "getGroupedPdspTossLkgeD"),
    ADDITIONAL("AD", "pdspCd", PdspTossLkgeDDto.class, "uiComponentService", "getGroupedPdspTossLkgeD"),
    ALLOWANCE("AW", "alowCd", AlowMDto.class, "uiComponentService", "getGroupedAlowM"),
    RTNG_D...
```

**Chunk 정보**
- 🆔 **ID**: `65c5e0e9cb0d`
- 📍 **라인**: 19-19

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **444개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 238 | 53.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| enum | 1 | 56.0 | 206 | 46.4% |

