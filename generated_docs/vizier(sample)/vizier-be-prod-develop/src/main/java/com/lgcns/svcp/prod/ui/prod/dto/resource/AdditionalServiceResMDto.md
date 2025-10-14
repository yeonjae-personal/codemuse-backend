# 📄 AdditionalServiceResMDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/resource/AdditionalServiceResMDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `AdditionalServiceResMDto`
- **도메인**: product_ui
- **목적**: AdditionalServiceResMDto 비즈니스 로직 처리
- **복잡도**: 12
- **관련 파일**: ./sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/resource/*
- **라인**: 6


## 📑 목차

### 🏗️ 클래스
- [`AdditionalServiceResMDto`](#class-additionalserviceresmdto) - 복잡도: 12

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `lombok.Data` | ⚡ **총 복잡도**: 12 |
| 📊 **총 토큰 수**: 73 |  |



## 🏗️ 클래스

### <a id="class-additionalserviceresmdto"></a>🎯 `AdditionalServiceResMDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 12 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 12 || 📍 **라인 범위** | 6-6 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class AdditionalServiceResMDto {
	private String swtcPrmtnm;
	private String swtcPrmtDelNm;
	private String saSwtcPrmtNm;
	private String saSwtcPrmtDelNm;
	
	public AdditionalServiceResMDto(RawServiceResMDto rawServiceResMDto) {
        this.swtcPrmtnm = rawServiceResMDto.getSwtcPrmtnm();
        this.swtcPrmtDelNm = rawServiceResMDto.getSwtcPrmtDelNm();
        this.saSwtcPrmtNm = rawServiceResMDto.getSaSwtcPrmtDelNm();
        this.saSwtcPrmtDelNm = rawServiceResMDto.getSwtcPrmtDelNm();
    }
}...
```

**Chunk 정보**
- 🆔 **ID**: `cd1d5a54f013`
- 📍 **라인**: 6-6
- 📊 **토큰**: 34
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **73개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 39 | 53.4% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 12.0 | 34 | 46.6% |

