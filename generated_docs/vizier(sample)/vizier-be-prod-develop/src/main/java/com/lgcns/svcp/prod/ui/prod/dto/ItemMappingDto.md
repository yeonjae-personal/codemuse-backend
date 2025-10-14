# 📄 ItemMappingDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/ItemMappingDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ItemMappingDto`](#class-itemmappingdto) - 복잡도: 11

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.fasterxml.jackson.annotation.JsonInclude` • `com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto` • `com.lgcns.svcp.prod.ui.prod.dto.common.metadata.GeneralDetailDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 11 |
| 📊 **총 토큰 수**: 72 |  |



## 🏗️ 클래스

### <a id="class-itemmappingdto"></a>🎯 `ItemMappingDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseDto` |
| ⚡ 복잡도 | 11 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 11 || 📍 **라인 범위** | 14-14 |
| 🏗️ **상속** | `BaseDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ItemMappingDto extends BaseDto {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String objUuid;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String objCode;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String objName;

    private String itemCode;
	private String validStartDtm;
	private String validEndDtm;
	
}...
```

**Chunk 정보**
- 🆔 **ID**: `40504adfe9bd`
- 📍 **라인**: 14-14
- 📊 **토큰**: 28
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **72개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 44 | 61.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 11.0 | 28 | 38.9% |

