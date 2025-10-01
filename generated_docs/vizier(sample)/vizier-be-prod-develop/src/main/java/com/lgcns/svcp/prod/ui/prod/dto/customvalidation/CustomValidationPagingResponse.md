# 📄 CustomValidationPagingResponse.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/customvalidation/CustomValidationPagingResponse.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`CustomValidationPagingResponse`](#class-customvalidationpagingresponse) - 복잡도: 9

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `java.util.Map` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 9 |
| 📊 **총 토큰 수**: 70 |  |



## 🏗️ 클래스

### <a id="class-customvalidationpagingresponse"></a>🎯 `CustomValidationPagingResponse`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 9 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 9 || 📍 **라인 범위** | 11-11 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class CustomValidationPagingResponse {
	
	public CustomValidationPagingResponse(int totalItems, List<Map<String, Object>> data) {
		this.totalItems = totalItems;
		this.data = data;
	}
	
	private int totalPages;
	private int totalItems;
	private List<Map<String, Object>> data;
}...
```

**Chunk 정보**
- 🆔 **ID**: `4a26b056e067`
- 📍 **라인**: 11-11
- 📊 **토큰**: 29
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **70개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 41 | 58.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 9.0 | 29 | 41.4% |

