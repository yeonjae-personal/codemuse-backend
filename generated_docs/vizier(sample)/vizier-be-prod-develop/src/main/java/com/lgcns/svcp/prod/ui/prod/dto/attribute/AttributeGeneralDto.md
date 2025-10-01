# 📄 AttributeGeneralDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/attribute/AttributeGeneralDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`AttributeGeneralDto`](#class-attributegeneraldto) - 복잡도: 18

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.item.Item` • `jakarta.validation.constraints.NotEmpty` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 18 |
| 📊 **총 토큰 수**: 110 |  |



## 🏗️ 클래스

### <a id="class-attributegeneraldto"></a>🎯 `AttributeGeneralDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 18 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 18 || 📍 **라인 범위** | 14-14 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class AttributeGeneralDto {
	
	@NotEmpty
	private String itemCode;
	@NotEmpty
	private String itemName;
	private String useYn;
	@NotEmpty
	private String largeItemCode;
	@NotEmpty
	private String largeItemName;
	@NotEmpty
	private String middleItemCode;
	@NotEmpty
	private String middleItemName;
	private Integer sortNo;
	private List<Item> upperItems = new ArrayList<>();
	private List<Item> lowerItems = new ArrayList<>();
}...
```

**Chunk 정보**
- 🆔 **ID**: `7d96e80e4d24`
- 📍 **라인**: 14-14
- 📊 **토큰**: 47
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **110개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 63 | 57.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 18.0 | 47 | 42.7% |

