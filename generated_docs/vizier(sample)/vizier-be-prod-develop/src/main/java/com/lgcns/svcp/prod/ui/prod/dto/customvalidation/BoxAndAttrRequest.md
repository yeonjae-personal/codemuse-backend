# 📄 BoxAndAttrRequest.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/customvalidation/BoxAndAttrRequest.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`BoxAndAttrRequest`](#class-boxandattrrequest) - 복잡도: 11 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.List` • `jakarta.validation.Valid` • `jakarta.validation.constraints.NotBlank` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 11 |
| 📊 **총 토큰 수**: 76 |  |



## 🏗️ 클래스

### <a id="class-boxandattrrequest"></a>🎯 `BoxAndAttrRequest`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 11 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 11 || 📍 **라인 범위** | 13-13 |
| 🏷️ **태그** | `class, java, valid, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class BoxAndAttrRequest {
	
	@NotBlank
	private String item;
	
	@NotBlank
	private String type;
	
	private String subType;
	
	//@NotBlank
	private String chgDeptName;
	
	//@NotBlank
	private String chgUser;
	
	@Valid
	List<SaveCusSearchDto> datas = new ArrayList<>();
}...
```

**Chunk 정보**
- 🆔 **ID**: `05f8343fa521`
- 📍 **라인**: 13-13
- 📊 **토큰**: 30
- 🏷️ **태그**: `class, java, valid, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **76개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 46 | 60.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 11.0 | 30 | 39.5% |

