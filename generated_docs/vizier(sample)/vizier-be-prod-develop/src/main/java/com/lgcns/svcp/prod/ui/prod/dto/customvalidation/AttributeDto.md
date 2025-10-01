# 📄 AttributeDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/customvalidation/AttributeDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`AttributeDto`](#class-attributedto) - 복잡도: 34

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.Date` • `com.fasterxml.jackson.annotation.JsonFormat` • `com.lgcns.svcp.prod.entity.BaseEntity` • `jakarta.validation.constraints.NotBlank` • `jakarta.validation.constraints.NotNull` • `lombok.Getter` 외 1개 | ⚡ **총 복잡도**: 34 |
| 📊 **총 토큰 수**: 240 |  |



## 🏗️ 클래스

### <a id="class-attributedto"></a>🎯 `AttributeDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseEntity` |
| ⚡ 복잡도 | 34 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 34 || 📍 **라인 범위** | 15-15 |
| 🏗️ **상속** | `BaseEntity` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class AttributeDto extends BaseEntity {
	
	private String validCode;
	
	@NotBlank
	private String attrUuid;
	
	@NotBlank
	private String condType;
	
	@NotNull
	private Integer attrNo;
	
	@NotNull
	private String validStartDtm;
	
	private String actionItemCode;
	
	private Integer rangeStartVal;
	private Integer rangeEndVal;
	
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date rangeStartDtm;
	
	private String rangeStartDtmStr;
	
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date rangeEndDtm;
	
	private String rangeEndDtmStr;
	
	private String textCntn;
	
	private String[] multipleValues = new String[] {};
	
	private String validEndDtm;
	
	private String labelId;
	
	@NotBlank
	private String fieldTy...
```

**Chunk 정보**
- 🆔 **ID**: `0d528e7b9805`
- 📍 **라인**: 15-15
- 📊 **토큰**: 111
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **240개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 129 | 53.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 34.0 | 111 | 46.2% |

