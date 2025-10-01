# 📄 AttributeUtil.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/util/AttributeUtil.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`AttributeUtil`](#class-attributeutil) - 복잡도: 16

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.ui.prod.dto.common.Attribute` • `java.util.regex.Pattern` | ⚡ **총 복잡도**: 16 |
| 📊 **총 토큰 수**: 88 |  |



## 🏗️ 클래스

### <a id="class-attributeutil"></a>🎯 `AttributeUtil`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 16 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 16 || 📍 **라인 범위** | 7-7 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class AttributeUtil {
    public static Attribute createAttribute(String value) {
        return new Attribute(value, null);
    }

    public static String camelToSnake(String str) {
        return Pattern.compile("([a-z])([A-Z]+)")
                .matcher(str)
                .replaceAll("$1_$2")
                .toLowerCase();
    }

    public static String snakeToCamel(String str) {
        return Pattern.compile("_([a-z])")
                .matcher(str)
                .replaceAll(m -> m.group(1).toUpperCase());
    }
}...
```

**Chunk 정보**
- 🆔 **ID**: `fce9922395c0`
- 📍 **라인**: 7-7
- 📊 **토큰**: 41
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **88개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 47 | 53.4% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 16.0 | 41 | 46.6% |

