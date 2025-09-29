# 📄 BsnLineDto.java

> **파일 경로**: `vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/multiEntity/detail/BsnLineDto.java`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `BsnLineDto`
- **도메인**: product_ui
- **목적**: BsnLineDto 데이터 전송 객체
- **복잡도**: 4
- **관련 파일**: ./sample_code/vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/multiEntity/detail/*
- **라인**: 8


## 📑 목차

### 🏗️ 클래스
- [`BsnLineDto`](#class-bsnlinedto) - 복잡도: 4

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.ui.prod.dto.multiEntity.MultiEntityDto` • `lombok.Data` | ⚡ **총 복잡도**: 4 |
| 📊 **총 토큰 수**: 33 |  |



## 🏗️ 클래스

### <a id="class-bsnlinedto"></a>🎯 `BsnLineDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `MultiEntityDto` |
| ⚡ 복잡도 | 4 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 4 || 📍 **라인 범위** | 8-8 |
| 🏗️ **상속** | `MultiEntityDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class BsnLineDto extends MultiEntityDto {
	private String bsnLineTypeCode;
	private String ovwCntn;
}...
```

**Chunk 정보**
- 🆔 **ID**: `3b8249005cc5`
- 📍 **라인**: 8-8
- 📊 **토큰**: 13
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **33개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 20 | 60.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 4.0 | 13 | 39.4% |

