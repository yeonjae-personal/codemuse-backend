# 📄 CustomValidationExcelEntity.java

> **파일 경로**: `vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/entity/CustomValidationExcelEntity.java`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`CustomValidationExcelEntity`](#class-customvalidationexcelentity) - 복잡도: 19

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.Date` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 19 |
| 📊 **총 토큰 수**: 126 |  |



## 🏗️ 클래스

### <a id="class-customvalidationexcelentity"></a>🎯 `CustomValidationExcelEntity`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseEntity` |
| ⚡ 복잡도 | 19 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 19 || 📍 **라인 범위** | 10-10 |
| 🏗️ **상속** | `BaseEntity` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class CustomValidationExcelEntity extends BaseEntity {
	
	private String validCode;
	private String attrUuid;
	private String condType;
	private Integer attrNo;
	private String validStartDtm;
	private String validEndDtm;
	private String actionItemCode;
	private Integer rangeStartVal;
	private Integer rangeEndVal;
	private Date rangeStartDtm;
	private Date rangeEndDtm;
	private String textCntn;
	private String labelId;
	private String labelName;
	private String fieldTypeCode;
	private String itemCodeName;
	private String itemCode;
}...
```

**Chunk 정보**
- 🆔 **ID**: `726451c4e32f`
- 📍 **라인**: 10-10
- 📊 **토큰**: 58
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **126개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 68 | 54.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 19.0 | 58 | 46.0% |

