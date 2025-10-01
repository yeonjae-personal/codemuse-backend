# 📄 TomcatRelaxedCharsCustomizer.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/config/TomcatRelaxedCharsCustomizer.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`TomcatRelaxedCharsCustomizer`](#class-tomcatrelaxedcharscustomizer) - 복잡도: 10

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory` • `org.springframework.boot.web.server.WebServerFactoryCustomizer` • `org.springframework.stereotype.Component` | ⚡ **총 복잡도**: 10 |
| 📊 **총 토큰 수**: 53 |  |



## 🏗️ 클래스

### <a id="class-tomcatrelaxedcharscustomizer"></a>🎯 `TomcatRelaxedCharsCustomizer`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 10 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 10 || 📍 **라인 범위** | 8-8 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class TomcatRelaxedCharsCustomizer
        implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addConnectorCustomizers(connector -> {
            connector.setProperty("relaxedPathChars", "`|{}[]^<>");
            connector.setProperty("relaxedQueryChars", "`|{}[]^<>");
        });
    }
}...
```

**Chunk 정보**
- 🆔 **ID**: `94dc661386be`
- 📍 **라인**: 8-8
- 📊 **토큰**: 22
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **53개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 31 | 58.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 10.0 | 22 | 41.5% |

