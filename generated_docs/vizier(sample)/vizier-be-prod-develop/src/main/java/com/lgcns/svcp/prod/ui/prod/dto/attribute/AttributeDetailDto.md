# 📄 AttributeDetailDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/attribute/AttributeDetailDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`AttributeDetailDto`](#class-attributedetaildto) - 복잡도: 7 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.List` • `jakarta.validation.Valid` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 7 |
| 📊 **총 토큰 수**: 52 |  |



## 🏗️ 클래스

### <a id="class-attributedetaildto"></a>🎯 `AttributeDetailDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 7 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 7 || 📍 **라인 범위** | 12-12 |
| 🏷️ **태그** | `class, java, valid, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class AttributeDetailDto {
	
	@Valid
	private AttributeGeneralDto general;
	
	@Valid
	private List<AttributeAdditionalDto> additionals = new ArrayList<>();
	
	private TreeViewDto treeView;
}...
```

**Chunk 정보**
- 🆔 **ID**: `b1b7db18b7de`
- 📍 **라인**: 12-12
- 📊 **토큰**: 19
- 🏷️ **태그**: `class, java, valid, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **52개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 33 | 63.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 7.0 | 19 | 36.5% |

