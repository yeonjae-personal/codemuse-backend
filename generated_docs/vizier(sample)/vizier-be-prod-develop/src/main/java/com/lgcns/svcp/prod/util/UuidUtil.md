# 📄 UuidUtil.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/util/UuidUtil.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UuidUtil`](#class-uuidutil) - 복잡도: 7

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.context.RequestContextHolder` • `java.util.UUID` | ⚡ **총 복잡도**: 7 |
| 📊 **총 토큰 수**: 42 |  |



## 🏗️ 클래스

### <a id="class-uuidutil"></a>🎯 `UuidUtil`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 7 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 7 || 📍 **라인 범위** | 7-7 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UuidUtil {
	public static String generateRandomUUID() {
        String randomUuid = UUID.randomUUID().toString();
        RequestContextHolder.setUuid(randomUuid);
        return randomUuid;
    }
}...
```

**Chunk 정보**
- 🆔 **ID**: `33b6f4d4d7f0`
- 📍 **라인**: 7-7
- 📊 **토큰**: 18
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **42개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 24 | 57.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 7.0 | 18 | 42.9% |

