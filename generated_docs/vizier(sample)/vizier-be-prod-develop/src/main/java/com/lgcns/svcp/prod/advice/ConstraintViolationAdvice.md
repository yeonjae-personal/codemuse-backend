# 📄 ConstraintViolationAdvice.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/advice/ConstraintViolationAdvice.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ConstraintViolationAdvice`](#class-constraintviolationadvice) - 복잡도: 26

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `org.springframework.http.HttpStatus` • `org.springframework.http.ResponseEntity` • `org.springframework.validation.BindException` • `org.springframework.web.bind.MethodArgumentNotValidException` • `org.springframework.web.bind.annotation.ControllerAdvice` • `org.springframework.web.bind.annotation.ExceptionHandler` 외 1개 | ⚡ **총 복잡도**: 26 |
| 📊 **총 토큰 수**: 163 |  |



## 🏗️ 클래스

### <a id="class-constraintviolationadvice"></a>🎯 `ConstraintViolationAdvice`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `ControllerSupport` |
| ⚡ 복잡도 | 26 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 26 || 📍 **라인 범위** | 13-13 |
| 🏗️ **상속** | `ControllerSupport` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ConstraintViolationAdvice extends ControllerSupport {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleNotValidException(MethodArgumentNotValidException ex) {
		ErrorResponseBody body = new ErrorResponseBody();
		body.setErrorMsg("Error constraint violation exception");
		body.setErrorDetail(getExceptionMessage(ex));
		body.setErrorStack(getErrorStack(ex));
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex) {
		ErrorResponseBody body = new ErrorResponseBody();
		body.setErrorMsg("Error constraint violation exception");
		body.setErrorDetail(getExceptionMessage(ex))...
```

**Chunk 정보**
- 🆔 **ID**: `c45f85e7a100`
- 📍 **라인**: 13-13
- 📊 **토큰**: 73
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **163개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 90 | 55.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 26.0 | 73 | 44.8% |

