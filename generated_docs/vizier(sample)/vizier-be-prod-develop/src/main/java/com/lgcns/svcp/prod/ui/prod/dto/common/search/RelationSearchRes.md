# 📄 RelationSearchRes.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/common/search/RelationSearchRes.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`RelationSearchRes`](#class-relationsearchres) - 복잡도: 10

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto` • `com.lgcns.svcp.prod.ui.prod.dto.common.metadata.GeneralDetailDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 10 |
| 📊 **총 토큰 수**: 72 |  |



## 🏗️ 클래스

### <a id="class-relationsearchres"></a>🎯 `RelationSearchRes`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 10 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 10 || 📍 **라인 범위** | 13-13 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class RelationSearchRes {
	
	private String objUuid;
	private String objCode;
	private String objName;
    private String itemCode;
	private String validStartDtm;
	private String validEndDtm;
	
	private List<GeneralDetailDto> general;
	private List<AdditionalDetailDto> additional;
}...
```

**Chunk 정보**
- 🆔 **ID**: `29996cc06d5a`
- 📍 **라인**: 13-13
- 📊 **토큰**: 29
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **72개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 43 | 59.7% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 10.0 | 29 | 40.3% |

