# 📄 ItemMappingDetailDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/item/detail/ItemMappingDetailDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ItemMappingDetailDto`](#class-itemmappingdetaildto) - 복잡도: 57

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.HashMap` • `java.util.List` • `java.util.Map` • `com.fasterxml.jackson.annotation.JsonIgnore` • `com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto` • `com.lgcns.svcp.prod.ui.prod.dto.common.metadata.GeneralDetailDto` 외 2개 | ⚡ **총 복잡도**: 57 |
| 📊 **총 토큰 수**: 370 |  |



## 🏗️ 클래스

### <a id="class-itemmappingdetaildto"></a>🎯 `ItemMappingDetailDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 57 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 57 || 📍 **라인 범위** | 16-16 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ItemMappingDetailDto {
    private List<GeneralDetailDto> general;
    private List<AdditionalDetailDto> additional;

    @JsonIgnore
    public Map<String, String> getGeneralParam() {
        Map<String, String> params = new HashMap<>();
        if (general != null) {
            for (GeneralDetailDto gnrl : general) {
                String camelKey = snakeToCamel(gnrl.getColName());
                params.put(camelKey, gnrl.getAttrVal());
            }
        }
        return params;
    }

    @JsonIgnore
    public Map<String, String> getGeneralEditYParam() {
        Map<String, String> params = new HashMap<>();
        if (general != null) {
            for (GeneralDetailDto gnrl : general) {
                if ("Y".equals(gnrl.getEditYn())) {
                    String...
```

**Chunk 정보**
- 🆔 **ID**: `f563fc107271`
- 📍 **라인**: 16-16
- 📊 **토큰**: 175
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **370개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 195 | 52.7% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 57.0 | 175 | 47.3% |

