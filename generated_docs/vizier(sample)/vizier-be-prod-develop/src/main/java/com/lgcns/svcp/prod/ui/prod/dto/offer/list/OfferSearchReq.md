# 📄 OfferSearchReq.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/offer/list/OfferSearchReq.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`OfferSearchReq`](#class-offersearchreq) - 복잡도: 13

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `lombok.AllArgsConstructor` • `lombok.Data` • `lombok.NoArgsConstructor` | ⚡ **총 복잡도**: 13 |
| 📊 **총 토큰 수**: 93 |  |



## 🏗️ 클래스

### <a id="class-offersearchreq"></a>🎯 `OfferSearchReq`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 13 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 13 || 📍 **라인 범위** | 10-10 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class OfferSearchReq {
    private String objCode;
    private String objName;
    private String itemCode;
    private boolean onlyValidDtm;
    private Integer page;
    private Integer size;

    public OfferSearchReq(String objCode, String objName, String itemCode) {
        this.objCode = objCode;
        this.objName = objName;
        this.itemCode = itemCode;
    }
}...
```

**Chunk 정보**
- 🆔 **ID**: `6bc008af60a8`
- 📍 **라인**: 10-10
- 📊 **토큰**: 41
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **93개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 52 | 55.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 13.0 | 41 | 44.1% |

