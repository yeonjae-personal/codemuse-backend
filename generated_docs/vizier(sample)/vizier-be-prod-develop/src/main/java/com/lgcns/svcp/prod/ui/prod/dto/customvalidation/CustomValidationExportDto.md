# 📄 CustomValidationExportDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/customvalidation/CustomValidationExportDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`CustomValidationExportDto`](#class-customvalidationexportdto) - 복잡도: 24 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.util.excel.annotation.CustomTitleHeader` • `com.lgcns.svcp.prod.util.excel.annotation.Value` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 24 |
| 📊 **총 토큰 수**: 133 |  |



## 🏗️ 클래스

### <a id="class-customvalidationexportdto"></a>🎯 `CustomValidationExportDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 24 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 24 || 📍 **라인 범위** | 12-12 |
| 🏷️ **태그** | `class, java, value, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class CustomValidationExportDto {
	
	@Value(name ="customvalidation.excel.number")
	private String number;
	
	@Value(name ="customvalidation.excel.condition.item")
	private String itemCondition;
	
	@Value(name ="customvalidation.excel.condition.attribute")
	private String attributeCondition;
	
	@Value(name ="customvalidation.excel.condition.validation")
	private String validationCondition;
	
	@Value(name ="customvalidation.excel.action.item")
	private String itemAction;
	
	@Value(name ="customvalidation.excel.action.attribute")
	private String attributeAction;
	
	@Value(name ="customvalidation.excel.action.validation")
	private String validationAction;
	
	@Value(name ="customvalidation.excel.registeruser")
	private String registerUser;
	
	@Value(name ="customvalidation.excel.registe...
```

**Chunk 정보**
- 🆔 **ID**: `0be2e7478473`
- 📍 **라인**: 12-12
- 📊 **토큰**: 60
- 🏷️ **태그**: `class, java, value, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **133개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 73 | 54.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 24.0 | 60 | 45.1% |

