# 📄 AttributeNewDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/customvalidation/AttributeNewDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`AttributeNewDto`](#class-attributenewdto) - 복잡도: 14

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.entity.BaseEntity` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 14 |
| 📊 **총 토큰 수**: 104 |  |



## 🏗️ 클래스

### <a id="class-attributenewdto"></a>🎯 `AttributeNewDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseEntity` |
| ⚡ 복잡도 | 14 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 14 || 📍 **라인 범위** | 10-10 |
| 🏗️ **상속** | `BaseEntity` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class AttributeNewDto extends BaseEntity {
	
	private String attrUuid;
	private String itemCode;
	private String fieldTypeCode;
	private Integer sortNo;
	private String labelId;
	private String commGroupCode;
	private String itemCodeName;
	private String largeItemCode;
	private String requiredYn;
	private String dispTab;
	private String[] types = new String[] {};
	private String attrMaxLength;
}...
```

**Chunk 정보**
- 🆔 **ID**: `f351b2ed0cc7`
- 📍 **라인**: 10-10
- 📊 **토큰**: 47
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **104개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 57 | 54.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 14.0 | 47 | 45.2% |

