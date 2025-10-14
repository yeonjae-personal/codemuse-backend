# 📄 AuditInterceptor.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/interceptor/AuditInterceptor.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`AuditInterceptor`](#class-auditinterceptor) - 복잡도: 90

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.lang.reflect.Field` • `java.time.LocalDateTime` • `java.util.Map` • `java.util.Properties` • `org.apache.ibatis.executor.Executor` • `org.apache.ibatis.mapping.MappedStatement` 외 7개 | ⚡ **총 복잡도**: 90 |
| 📊 **총 토큰 수**: 1,022 |  |



## 🏗️ 클래스

### <a id="class-auditinterceptor"></a>🎯 `AuditInterceptor`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 90 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 90 || 📍 **라인 범위** | 23-23 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class AuditInterceptor implements Interceptor {

    private static final String UPD_DTM = "updDtm";
    private static final String UPD_USER = "updUser";
    private static final String RGST_DTM = "rgstDtm";
    private static final String RGST_USER = "rgstUser";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1]; // Retrieve parameter object
        if (parameter != null) {
            String currentUser = UserContext.getCurrentUser(); // Retrieve current user from ThreadLocal
            String dtm = LocalDateTime.now().toString();
            SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

...
```

**Chunk 정보**
- 🆔 **ID**: `00dad9e7e920`
- 📍 **라인**: 23-23
- 📊 **토큰**: 485
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **1,022개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 537 | 52.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 90.0 | 485 | 47.5% |

