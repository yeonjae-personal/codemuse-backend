# 📄 BaseFeeAndResourceDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/dto/component/price/BaseFeeAndResourceDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `BaseFeeAndResourceDto`
- **도메인**: price
- **목적**: BaseFeeAndResourceDto 데이터 전송 객체
- **복잡도**: 4
- **관련 파일**: ./sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/dto/component/price/*
- **라인**: 9


## 📑 목차

### 🏗️ 클래스
- [`BaseFeeAndResourceDto`](#class-basefeeandresourcedto) - 복잡도: 4

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `java.util.Map` • `lombok.Data` | ⚡ **총 복잡도**: 4 |
| 📊 **총 토큰 수**: 35 |  |



## 🏗️ 클래스

### <a id="class-basefeeandresourcedto"></a>🎯 `BaseFeeAndResourceDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 4 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 4 || 📍 **라인 범위** | 9-9 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class BaseFeeAndResourceDto {
	private List<Map<String, Object>> baseFee;
	private Map<String, List<String>> resourceList;
}...
```

**Chunk 정보**
- 🆔 **ID**: `c8ad94242dd5`
- 📍 **라인**: 9-9
- 📊 **토큰**: 13
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **35개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 22 | 62.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 4.0 | 13 | 37.1% |

