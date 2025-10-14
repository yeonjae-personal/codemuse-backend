# 📄 SearchGroupWithDetailResDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/offer/SearchGroupWithDetailResDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`SearchGroupWithDetailResDto`](#class-searchgroupwithdetailresdto) - 복잡도: 8

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto` • `com.lgcns.svcp.prod.ui.prod.dto.common.metadata.GeneralDetailDto` • `lombok.Data` | ⚡ **총 복잡도**: 8 |
| 📊 **총 토큰 수**: 57 |  |



## 🏗️ 클래스

### <a id="class-searchgroupwithdetailresdto"></a>🎯 `SearchGroupWithDetailResDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 8 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 8 || 📍 **라인 범위** | 11-11 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class SearchGroupWithDetailResDto {
	private String offerUuid;
	private String offerCode;
	private String offerName;
	private String offerType;
    private List<GeneralDetailDto> general;
    private List<AdditionalDetailDto> additional;

}...
```

**Chunk 정보**
- 🆔 **ID**: `a9ef5d342fb6`
- 📍 **라인**: 11-11
- 📊 **토큰**: 23
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **57개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 34 | 59.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 8.0 | 23 | 40.4% |

