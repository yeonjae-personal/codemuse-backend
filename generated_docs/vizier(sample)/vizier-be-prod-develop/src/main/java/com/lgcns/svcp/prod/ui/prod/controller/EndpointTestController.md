# 📄 EndpointTestController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/EndpointTestController.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`EndpointTestController`](#class-endpointtestcontroller) - 복잡도: 12 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `lombok.extern.slf4j.Slf4j` • `org.springframework.http.ResponseEntity` • `org.springframework.web.bind.annotation.PostMapping` • `org.springframework.web.bind.annotation.RequestBody` • `org.springframework.web.bind.annotation.RequestMapping` • `org.springframework.web.bind.annotation.RestController` 외 2개 | ⚡ **총 복잡도**: 12 |
| 📊 **총 토큰 수**: 113 |  |



## 🏗️ 클래스

### <a id="class-endpointtestcontroller"></a>🎯 `EndpointTestController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 12 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 12 || 📍 **라인 범위** | 16-16 |
| 🏷️ **태그** | `class, java, postmapping, requestbody, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class EndpointTestController {

    @PostMapping("/test")
    public ResponseEntity<Map<String, Object>> receivePost(@RequestBody Map<String, Object> body) {
        String userId = (String) body.get("userId");

        log.info("Endpoint TEST!!! POST 요청 수신: userId = {}", userId);

        // 응답 구성
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("receivedUserId", userId);
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(response);
    }
}...
```

**Chunk 정보**
- 🆔 **ID**: `8b09ccfe03be`
- 📍 **라인**: 16-16
- 📊 **토큰**: 46
- 🏷️ **태그**: `class, java, postmapping, requestbody, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **113개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 67 | 59.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 12.0 | 46 | 40.7% |

