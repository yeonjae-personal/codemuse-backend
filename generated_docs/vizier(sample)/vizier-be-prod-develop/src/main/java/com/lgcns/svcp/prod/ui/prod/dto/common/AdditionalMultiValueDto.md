# 📄 AdditionalMultiValueDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/common/AdditionalMultiValueDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`AdditionalMultiValueDto`](#class-additionalmultivaluedto) - 복잡도: 24

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.Arrays` • `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.BaseDto` • `com.lgcns.svcp.prod.util.StringUtilCustom` • `lombok.AllArgsConstructor` 외 3개 | ⚡ **총 복잡도**: 24 |
| 📊 **총 토큰 수**: 174 |  |



## 🏗️ 클래스

### <a id="class-additionalmultivaluedto"></a>🎯 `AdditionalMultiValueDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseDto` |
| ⚡ 복잡도 | 24 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 24 || 📍 **라인 범위** | 19-19 |
| 🏗️ **상속** | `BaseDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class AdditionalMultiValueDto extends BaseDto {
    private String objUuid;
    private String attrUuid;
    private int attrSeq;
    private String attrVal;

    public static List<AdditionalMultiValueDto> parseVals(String objUuid, String attrUuid, String attrVals) {
        if (StringUtilCustom.isEmpty(attrVals) || "[]".equals(attrVals)) {
            return null;
        }
        List<String> multiValue = Arrays.stream(attrVals.replaceAll("[\"\\[\\]]", "").split(",")).toList();
        List<AdditionalMultiValueDto> result = new ArrayList<>();
        if (!multiValue.isEmpty()) {
            for (int i = 0; i < multiValue.size(); i++) {
                AdditionalMultiValueDto dto = new AdditionalMultiValueDto();
                dto.setObjUuid(objUuid);
                dto.setAttr...
```

**Chunk 정보**
- 🆔 **ID**: `8c7b7b3c4eab`
- 📍 **라인**: 19-19
- 📊 **토큰**: 75
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **174개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 99 | 56.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 24.0 | 75 | 43.1% |

