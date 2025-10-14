# 📄 TomcatErrorController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/tomcatError/controller/TomcatErrorController.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`TomcatErrorController`](#class-tomcaterrorcontroller) - 복잡도: 8 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.advice.ErrorResponseBody` • `org.springframework.http.HttpStatus` • `org.springframework.http.ResponseEntity` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.RequestMapping` • `org.springframework.web.bind.annotation.RestController` | ⚡ **총 복잡도**: 8 |
| 📊 **총 토큰 수**: 58 |  |



## 🏗️ 클래스

### <a id="class-tomcaterrorcontroller"></a>🎯 `TomcatErrorController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 8 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 8 || 📍 **라인 범위** | 12-12 |
| 🏷️ **태그** | `class, java, getmapping, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class TomcatErrorController {

    @GetMapping()
    public ResponseEntity<?> handleException() {
        ErrorResponseBody body = new ErrorResponseBody();
        body.setErrorCode("403");
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

}...
```

**Chunk 정보**
- 🆔 **ID**: `e510b409387b`
- 📍 **라인**: 12-12
- 📊 **토큰**: 21
- 🏷️ **태그**: `class, java, getmapping, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **58개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 37 | 63.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 8.0 | 21 | 36.2% |

