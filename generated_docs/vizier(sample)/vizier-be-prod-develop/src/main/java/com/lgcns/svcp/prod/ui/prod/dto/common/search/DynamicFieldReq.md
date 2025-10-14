# 📄 DynamicFieldReq.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/common/search/DynamicFieldReq.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`DynamicFieldReq`](#class-dynamicfieldreq) - 복잡도: 30

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.List` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 30 |
| 📊 **총 토큰 수**: 236 |  |



## 🏗️ 클래스

### <a id="class-dynamicfieldreq"></a>🎯 `DynamicFieldReq`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 30 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 30 || 📍 **라인 범위** | 11-11 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class DynamicFieldReq {
    private String fieldName;
    private String fieldType;
    private String fieldValue;
    private String fieldValueMin;
    private String fieldValueMax;
    private List<String> fieldValues;

    public List<String> getFieldValues() {
        if ((fieldValues == null || fieldValues.isEmpty())
                && ("DL".equalsIgnoreCase(this.fieldType) || "DM".equalsIgnoreCase(this.fieldType))) {
            if (fieldValue == null || fieldValue.trim().isEmpty() || "[]".equals(fieldValue.trim()))
                return null;
            String trimmed = fieldValue.trim();
            if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
                trimmed = trimmed.substring(1, trimmed.length() - 1);
            }
//            if (trimmed.trim().isE...
```

**Chunk 정보**
- 🆔 **ID**: `8de7e4683f04`
- 📍 **라인**: 11-11
- 📊 **토큰**: 112
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **236개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 124 | 52.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 30.0 | 112 | 47.5% |

