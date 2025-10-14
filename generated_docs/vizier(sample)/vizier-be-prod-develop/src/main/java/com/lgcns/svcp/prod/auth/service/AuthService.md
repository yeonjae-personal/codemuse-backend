# 📄 AuthService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/auth/service/AuthService.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`AuthService`](#class-authservice) - 복잡도: 5

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.util.StringUtilCustom` • `org.springframework.stereotype.Service` | ⚡ **총 복잡도**: 5 |
| 📊 **총 토큰 수**: 47 |  |



## 🏗️ 클래스

### <a id="class-authservice"></a>🎯 `AuthService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 5 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 5 || 📍 **라인 범위** | 7-7 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class AuthService {

    public boolean validateUserToken(String token){
        // TODO redis 생성 후 token 인증 연동
        return StringUtilCustom.isBlank(token);
    }

}...
```

**Chunk 정보**
- 🆔 **ID**: `f38356f188e9`
- 📍 **라인**: 7-7
- 📊 **토큰**: 20
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **47개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 27 | 57.4% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 5.0 | 20 | 42.6% |

