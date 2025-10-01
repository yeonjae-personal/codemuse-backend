# 📄 ServiceElementDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/dto/resource/ServiceElementDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `ServiceElementDto`
- **도메인**: user
- **목적**: ServiceElementDto 비즈니스 로직 처리
- **복잡도**: 15
- **관련 파일**: /Users/roseline/projects/codemuse-backend/sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/dto/resource/*
- **라인**: 10


## 📑 목차

### 🏗️ 클래스
- [`ServiceElementDto`](#class-serviceelementdto) - 복잡도: 15

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.online.prod.dto.common.AdditionalColumnsDto` • `lombok.Data` | ⚡ **총 복잡도**: 15 |
| 📊 **총 토큰 수**: 97 |  |



## 🏗️ 클래스

### <a id="class-serviceelementdto"></a>🎯 `ServiceElementDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 15 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 15 || 📍 **라인 범위** | 10-10 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ServiceElementDto {
	private String objUuid;
	private String objCode;
	private String svcRscTypeCode;
	private String prvsnActvtYn;
	private String prvsnCode;
	private String prvsnPrmtrName;
	private String svcRscOvwCntn;
	private String dplcTrgtUuid;
	private String rgstUser;
	private String rgstDtm;
	private String updUser;
	private String updDtm;
	private List<AdditionalColumnsDto> additionalColumns;
}...
```

**Chunk 정보**
- 🆔 **ID**: `96838deb7d80`
- 📍 **라인**: 10-10
- 📊 **토큰**: 44
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **97개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 53 | 54.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 15.0 | 44 | 45.4% |

