# 📄 DcTrgtDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/multiEntity/detail/DcTrgtDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `DcTrgtDto`
- **도메인**: product_ui
- **목적**: DcTrgtDto 데이터 전송 객체
- **복잡도**: 12
- **관련 파일**: ./sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/multiEntity/detail/*
- **라인**: 10


## 📑 목차

### 🏗️ 클래스
- [`DcTrgtDto`](#class-dctrgtdto) - 복잡도: 12

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.ui.prod.dto.multiEntity.MultiEntityDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 12 |
| 📊 **총 토큰 수**: 84 |  |



## 🏗️ 클래스

### <a id="class-dctrgtdto"></a>🎯 `DcTrgtDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `MultiEntityDto` |
| ⚡ 복잡도 | 12 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 12 || 📍 **라인 범위** | 10-10 |
| 🏗️ **상속** | `MultiEntityDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class DcTrgtDto extends MultiEntityDto {
	private String groupUuid;
	private String offerUuid;
	private String cpntUuid;
	private String rscUuid;
	private String chrgTypeCode;
	private String ovwCntn;
	private String groupName;
	private String offerName;
	private String cpntName;
	private String rscName;
}...
```

**Chunk 정보**
- 🆔 **ID**: `dc495a9ad414`
- 📍 **라인**: 10-10
- 📊 **토큰**: 37
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **84개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 47 | 56.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 12.0 | 37 | 44.0% |

