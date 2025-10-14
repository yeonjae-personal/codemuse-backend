# 📄 AttributeAdditionalDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/attribute/AttributeAdditionalDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`AttributeAdditionalDto`](#class-attributeadditionaldto) - 복잡도: 15

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.ui.prod.dto.BaseDto` • `jakarta.validation.constraints.NotEmpty` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 15 |
| 📊 **총 토큰 수**: 100 |  |



## 🏗️ 클래스

### <a id="class-attributeadditionaldto"></a>🎯 `AttributeAdditionalDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseDto` |
| ⚡ 복잡도 | 15 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 15 || 📍 **라인 범위** | 11-11 |
| 🏗️ **상속** | `BaseDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class AttributeAdditionalDto extends BaseDto {
	
	private String attrUuid;
    private String itemCode;
    private String fieldTypeCode;
    private String commGroupCode;
    private Integer sortNo;
    private String useYn;
    private String attrMaxLength;
    private String requiredYn;
    
    @NotEmpty
    private String labelId;
    private String dispTab;
    private String dispCardYn;
    private String advSearchYn;
}...
```

**Chunk 정보**
- 🆔 **ID**: `4de63d80c21d`
- 📍 **라인**: 11-11
- 📊 **토큰**: 44
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **100개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 56 | 56.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 15.0 | 44 | 44.0% |

