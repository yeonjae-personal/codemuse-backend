# 📄 SearchMatrixResDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/admin/matrix/SearchMatrixResDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`SearchMatrixResDto`](#class-searchmatrixresdto) - 복잡도: 12

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 12 |
| 📊 **총 토큰 수**: 86 |  |



## 🏗️ 클래스

### <a id="class-searchmatrixresdto"></a>🎯 `SearchMatrixResDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 12 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 12 || 📍 **라인 범위** | 8-8 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class SearchMatrixResDto {
    private String matrixCode;
    private String matrixCodeName;
    private String useYn;
    private Object factorCodes;

    public String[] getFactorCodes() {
        if (factorCodes instanceof String factorCodesString) {
            return factorCodesString.trim().isEmpty() ? new String[0] : factorCodesString.split(",");
        }
        return new String[0];
    }
}...
```

**Chunk 정보**
- 🆔 **ID**: `d336add9dbe0`
- 📍 **라인**: 8-8
- 📊 **토큰**: 39
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **86개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 47 | 54.7% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 12.0 | 39 | 45.3% |

