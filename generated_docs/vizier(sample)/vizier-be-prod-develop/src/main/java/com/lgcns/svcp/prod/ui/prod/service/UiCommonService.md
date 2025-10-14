# 📄 UiCommonService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/UiCommonService.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiCommonService`](#class-uicommonservice) - 복잡도: 18

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.constant.SystemConstant.ITEM_CODE` • `com.lgcns.svcp.prod.constant.SystemConstant.MCTGR_ITEM_CODE` • `java.util.Collections` • `java.util.HashMap` • `java.util.List` • `java.util.Map` 외 5개 | ⚡ **총 복잡도**: 18 |
| 📊 **총 토큰 수**: 166 |  |



## 🏗️ 클래스

### <a id="class-uicommonservice"></a>🎯 `UiCommonService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 18 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 18 || 📍 **라인 범위** | 21-21 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiCommonService {
    private final CommonDao commonDao;

    public String generateNextItemCode(Map<String, String> params) {
        return generateNextItemCode(params.get(ITEM_CODE));
    }

    public String generateNextItemCode(String itemCode) {
        String objCode = commonDao.select("Ui-common.generateNextItemCode",
                Collections.singletonMap(ITEM_CODE, itemCode));
//        if (objCode == null) {
//            throw new BusinessException("Item code not found!");
//        }
        RequestContextHolder.setCode(objCode);
        return objCode;
    }

	public List<ItemStructureDto> retreiveItemStructure(String itemCode, String mctgrItemCode) {
        Map<String, String> param = new HashMap<>();
        param.put(ITEM_CODE, itemCode);
        param.put(...
```

**Chunk 정보**
- 🆔 **ID**: `bfdd64d11e58`
- 📍 **라인**: 21-21
- 📊 **토큰**: 69
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **166개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 97 | 58.4% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 18.0 | 69 | 41.6% |

