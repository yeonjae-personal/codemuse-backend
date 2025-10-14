# 📄 Sorting.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/Sorting.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`Sorting`](#class-sorting) - 복잡도: 11

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.Arrays` • `java.util.List` | ⚡ **총 복잡도**: 11 |
| 📊 **총 토큰 수**: 64 |  |



## 🏗️ 클래스

### <a id="class-sorting"></a>🎯 `Sorting`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 11 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 11 || 📍 **라인 범위** | 6-6 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class Sorting {
	
	private List<String> properties;
	
	public Sorting(String... sortProperties) {
		if (sortProperties.length != 0) {
			this.properties = Arrays.asList(sortProperties);
		} 
	}

	public List<String> getProperties() {
		return properties;
	}
}...
```

**Chunk 정보**
- 🆔 **ID**: `a106f538fbfc`
- 📍 **라인**: 6-6
- 📊 **토큰**: 29
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **64개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 35 | 54.7% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 11.0 | 29 | 45.3% |

