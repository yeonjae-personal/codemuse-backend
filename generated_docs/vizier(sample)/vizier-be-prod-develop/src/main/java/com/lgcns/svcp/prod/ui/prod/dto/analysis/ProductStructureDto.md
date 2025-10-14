# 📄 ProductStructureDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/analysis/ProductStructureDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ProductStructureDto`](#class-productstructuredto) - 복잡도: 18

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.util.paging.BasePaginationDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 18 |
| 📊 **총 토큰 수**: 120 |  |



## 🏗️ 클래스

### <a id="class-productstructuredto"></a>🎯 `ProductStructureDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BasePaginationDto` |
| ⚡ 복잡도 | 18 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 18 || 📍 **라인 범위** | 10-10 |
| 🏗️ **상속** | `BasePaginationDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ProductStructureDto extends BasePaginationDto {
	private String offrCd;
	private String offrUuid;
	private String offrNm;
	private String cmpCd;
	private String cmpUuid;
	private String cmpNm;
	private String cmpValdStrtDtm;
	private String cmpValdEndDtm;
	private String svcCd;
	private String svcUuid;
	private String svcNm;
	private String svcValdStrtDtm;
	private String svcValdEndDtm;

	private String objCode;
	private String objName;
	private String lctgrItemCode;
}...
```

**Chunk 정보**
- 🆔 **ID**: `a59494e573ec`
- 📍 **라인**: 10-10
- 📊 **토큰**: 55
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **120개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 65 | 54.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 18.0 | 55 | 45.8% |

