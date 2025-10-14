# 📄 QosInfoDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/characteristic/QosInfoDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`QosInfoDto`](#class-qosinfodto) - 복잡도: 15

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `lombok.Data` | ⚡ **총 복잡도**: 15 |
| 📊 **총 토큰 수**: 95 |  |



## 🏗️ 클래스

### <a id="class-qosinfodto"></a>🎯 `QosInfoDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 15 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 15 || 📍 **라인 범위** | 8-8 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class QosInfoDto {
	private String prodUuid;
	private String qosCd;
	private String qosNm;
	private String qosPlcyGrpCd;
	private String thrsIdfyCd;
	private String valdStrtDtm;
	private String valdEndDtm;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
	private List<QosPlcyRelDDto> qosPlcyRelDDto;
	private String type;
}...
```

**Chunk 정보**
- 🆔 **ID**: `a418edf1f111`
- 📍 **라인**: 8-8
- 📊 **토큰**: 44
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **95개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 51 | 53.7% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 15.0 | 44 | 46.3% |

