# 📄 ErrorResponseBody.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/advice/ErrorResponseBody.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ErrorResponseBody`](#class-errorresponsebody) - 복잡도: 7

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.io.Serializable` • `lombok.Data` | ⚡ **총 복잡도**: 7 |
| 📊 **총 토큰 수**: 57 |  |



## 🏗️ 클래스

### <a id="class-errorresponsebody"></a>🎯 `ErrorResponseBody`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 7 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 7 || 📍 **라인 범위** | 8-8 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ErrorResponseBody implements Serializable{
	
	private static final long serialVersionUID = -3998297831937665656L;
	
	private String errorCode;
	private String errorMsg;
	private String errorStack;
	private String errorDetail;
}...
```

**Chunk 정보**
- 🆔 **ID**: `4957d65c07c5`
- 📍 **라인**: 8-8
- 📊 **토큰**: 25
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **57개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 32 | 56.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 7.0 | 25 | 43.9% |

