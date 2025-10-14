# 📄 LabelItemDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/label/LabelItemDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`LabelItemDto`](#class-labelitemdto) - 복잡도: 10

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `jakarta.validation.constraints.NotEmpty` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 10 |
| 📊 **총 토큰 수**: 56 |  |



## 🏗️ 클래스

### <a id="class-labelitemdto"></a>🎯 `LabelItemDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 10 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 10 || 📍 **라인 범위** | 9-9 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class LabelItemDto {
	
	@NotEmpty
	private String langCode;
	
	@NotEmpty
	private String regionCode;
	
	@NotEmpty
	private String labelName;
	
	private String labelDscr;
	
	private String labelCode;
}...
```

**Chunk 정보**
- 🆔 **ID**: `bc1ee83f7ef0`
- 📍 **라인**: 9-9
- 📊 **토큰**: 23
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **56개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 33 | 58.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 10.0 | 23 | 41.1% |

