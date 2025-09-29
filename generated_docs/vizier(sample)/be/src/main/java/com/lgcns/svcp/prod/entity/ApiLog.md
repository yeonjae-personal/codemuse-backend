# 📄 ApiLog.java

> **파일 경로**: `vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/entity/ApiLog.java`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `ApiLog`
- **도메인**: general
- **목적**: 비즈니스 로직 처리
- **복잡도**: 11
- **관련 파일**: ./sample_code/vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/entity/*
- **라인**: 9


## 📑 목차

### 🏗️ 클래스
- [`ApiLog`](#class-apilog) - 복잡도: 11

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.time.LocalDateTime` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 11 |
| 📊 **총 토큰 수**: 74 |  |



## 🏗️ 클래스

### <a id="class-apilog"></a>🎯 `ApiLog`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 11 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 11 || 📍 **라인 범위** | 9-9 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ApiLog {
	
    private Long id;
    private String url;
    private String method;
    private String query;
    private String body;
    private String objUuid;
    private String objCode;
    private String userId;
    private LocalDateTime timestamp;
}...
```

**Chunk 정보**
- 🆔 **ID**: `af9b083e6d42`
- 📍 **라인**: 9-9
- 📊 **토큰**: 32
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **74개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 42 | 56.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 11.0 | 32 | 43.2% |

