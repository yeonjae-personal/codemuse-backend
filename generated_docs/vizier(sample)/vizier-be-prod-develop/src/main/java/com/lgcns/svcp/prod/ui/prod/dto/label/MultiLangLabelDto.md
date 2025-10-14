# 📄 MultiLangLabelDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/label/MultiLangLabelDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`MultiLangLabelDto`](#class-multilanglabeldto) - 복잡도: 6 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.List` • `jakarta.validation.Valid` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 6 |
| 📊 **총 토큰 수**: 50 |  |



## 🏗️ 클래스

### <a id="class-multilanglabeldto"></a>🎯 `MultiLangLabelDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 6 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 6 || 📍 **라인 범위** | 12-12 |
| 🏷️ **태그** | `class, java, valid, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class MultiLangLabelDto {
	
	private String labelId;
	private String labelType;
	@Valid
	private List<LabelItemDto> items = new ArrayList<>();
}...
```

**Chunk 정보**
- 🆔 **ID**: `6b0f199a0b2c`
- 📍 **라인**: 12-12
- 📊 **토큰**: 18
- 🏷️ **태그**: `class, java, valid, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **50개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 32 | 64.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 6.0 | 18 | 36.0% |

