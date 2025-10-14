# 📄 UserContext.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/context/UserContext.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UserContext`](#class-usercontext) - 복잡도: 12

## 📋 파일 개요

| | |
|--|--|
| ⚡ **총 복잡도**: 12 | 📊 **총 토큰 수**: 123 |



## 🏗️ 클래스

### <a id="class-usercontext"></a>🎯 `UserContext`

> 📝 **클래스 설명**  
> /**

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 12 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 12 || 📍 **라인 범위** | 6-6 |
| 🏷️ **태그** | `class, java` |
#### 📚 Javadoc 상세

```
/**
```


<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UserContext {
    private static final ThreadLocal<String> currentUser   = new ThreadLocal<>();

    // Set the current user
    public static void setCurrentUser(String user) {
        currentUser.set(user);
    }

    // Get the current user
    public static String getCurrentUser() {
        return currentUser.get();
    }

    // Clear the current user after request completion
    public static void clear() {
        currentUser.remove();
    }
}...
```

**Chunk 정보**
- 🆔 **ID**: `5b8ab29b7f19`
- 📍 **라인**: 6-6
- 📊 **토큰**: 54
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **123개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 69 | 56.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 12.0 | 54 | 43.9% |

