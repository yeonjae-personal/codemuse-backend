# 📄 ControllerSupport.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/advice/ControllerSupport.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ControllerSupport`](#class-controllersupport) - 복잡도: 67

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.Collections` • `java.util.List` • `javax.security.sasl.AuthenticationException` • `org.springframework.http.HttpHeaders` • `org.springframework.http.HttpStatus` 외 4개 | ⚡ **총 복잡도**: 67 |
| 📊 **총 토큰 수**: 427 |  |



## 🏗️ 클래스

### <a id="class-controllersupport"></a>🎯 `ControllerSupport`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 67 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 67 || 📍 **라인 범위** | 18-18 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ControllerSupport {
	
	private static final String PACKAGE_PREFIX = "com.lgcns";
    private static final int MAX_LINES = 10;
    
    @ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception ex){
    	HttpHeaders headers = new HttpHeaders();
		headers.add("BIZError", "N");
		ErrorResponseBody body = new ErrorResponseBody();
		body.setErrorCode("500");
		body.setErrorMsg(ex.getMessage());
		body.setErrorDetail(getExceptionMessage(ex));
		body.setErrorStack(getErrorStack(ex));
		return new ResponseEntity<>(body, headers, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<?> handleAuthenticationException(Exception ex){
		HttpHeaders headers = new HttpHeaders();
		headers.add("AuthError", "...
```

**Chunk 정보**
- 🆔 **ID**: `dcb1e237a3eb`
- 📍 **라인**: 18-18
- 📊 **토큰**: 202
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **427개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 225 | 52.7% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 67.0 | 202 | 47.3% |

