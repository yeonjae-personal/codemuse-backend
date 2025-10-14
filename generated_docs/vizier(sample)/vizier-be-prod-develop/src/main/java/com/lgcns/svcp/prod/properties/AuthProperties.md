# 📄 AuthProperties.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/properties/AuthProperties.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`AuthProperties`](#class-authproperties) - 복잡도: 22

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.Arrays` • `java.util.List` | ⚡ **총 복잡도**: 22 |
| 📊 **총 토큰 수**: 116 |  |



## 🏗️ 클래스

### <a id="class-authproperties"></a>🎯 `AuthProperties`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 22 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 22 || 📍 **라인 범위** | 6-6 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class AuthProperties {

    public final static List<String> ALLOW_URIS = Arrays.asList(
            "/prod/swagger-ui/index.html",
            "/prod/ui/admin/label/language",
            "/prod/ui/admin/label/i18n",
            "/prod/error"
    );

    public final static List<String> WEBSOCKET_URIS = Arrays.asList(
            "/prod/ws"
    );

    public final static List<String> ALLOW_INCLUDED_URIS = Arrays.asList(
            "/export",
            "/import",
            "/swagger-ui",
            "/api-docs"
    );

    public final static String FILE_UPLOAD = "/import";

    public final static List<String> ALLOW_FILES = Arrays.asList(
            "",
            ""
    );

}...
```

**Chunk 정보**
- 🆔 **ID**: `b518b14f03aa`
- 📍 **라인**: 6-6
- 📊 **토큰**: 55
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **116개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 61 | 52.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 22.0 | 55 | 47.4% |

