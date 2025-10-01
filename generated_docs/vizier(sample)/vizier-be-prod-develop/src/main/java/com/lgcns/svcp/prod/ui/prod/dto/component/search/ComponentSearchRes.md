# 📄 ComponentSearchRes.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/component/search/ComponentSearchRes.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `ComponentSearchRes`
- **도메인**: user
- **목적**: 비즈니스 로직 처리
- **복잡도**: 20
- **관련 파일**: /Users/roseline/projects/codemuse-backend/sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/component/search/*
- **라인**: 13


## 📑 목차

### 🏗️ 클래스
- [`ComponentSearchRes`](#class-componentsearchres) - 복잡도: 20

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto` • `com.lgcns.svcp.prod.ui.prod.dto.common.metadata.GeneralDetailDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 20 |
| 📊 **총 토큰 수**: 136 |  |



## 🏗️ 클래스

### <a id="class-componentsearchres"></a>🎯 `ComponentSearchRes`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 20 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 20 || 📍 **라인 범위** | 13-13 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ComponentSearchRes {
	// Common
	private String objUuid;
	private String objCode;
	private String objName;
	private String itemCode;
	private String validStartDtm;
	private String validEndDtm;
	private String dplcTrgtUuid;
	private String chgDeptName;
	private String chgUser;
	private String ovwCntn;
	private String rgstUser;
	private String rgstDtm;
	private String updUser;
	private String updDtm;
	
	private List<GeneralDetailDto> general;
	private List<AdditionalDetailDto> additional;

	private String mctgrItemCode;
	private String lctgrItemCode;
}...
```

**Chunk 정보**
- 🆔 **ID**: `5c62b25677c9`
- 📍 **라인**: 13-13
- 📊 **토큰**: 61
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **136개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 75 | 55.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 20.0 | 61 | 44.9% |

