# 📄 UIHistoryService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/UIHistoryService.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UIHistoryService`](#class-uihistoryservice) - 복잡도: 223

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.constant.SystemConstant.CHG_DEPT_NAME` • `com.lgcns.svcp.prod.constant.SystemConstant.CHG_USER` • `com.lgcns.svcp.prod.constant.SystemConstant.OBJ_UUID` • `com.lgcns.svcp.prod.constant.SystemConstant.RGST_DTM` • `java.util.ArrayList` • `java.util.Collections` 외 28개 | ⚡ **총 복잡도**: 223 |
| 📊 **총 토큰 수**: 1,481 |  |



## 🏗️ 클래스

### <a id="class-uihistoryservice"></a>🎯 `UIHistoryService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 223 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 223 || 📍 **라인 범위** | 45-45 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UIHistoryService {

    private final CommonDao commonDao;

    public HistoryDetailResDto retrieveHistoryDetail(String objUuid) {
        // Get item info
        ItemCodeInfo itemCodeInfo = commonDao.select("Ui-item.retrieveItemCodeInfoByUuid", objUuid);

        // Get history info
        EventDateDto created = commonDao.select("Ui-history.retrieveCreatedDate", itemCodeInfo);
        EventDateDto ended = commonDao.select("Ui-history.retrieveEndedDate", itemCodeInfo);

        List<AttributeChangeDto> addtionalChanged = commonDao.selectList("Ui-history.retrieveAddtionalChanged",
                itemCodeInfo);
        List<AttributeChangeDto> generalChanged = commonDao.selectList("Ui-history.retrieveGeneralChanged",
                itemCodeInfo);
        generalChanged.forEa...
```

**Chunk 정보**
- 🆔 **ID**: `45d8ed7be061`
- 📍 **라인**: 45-45
- 📊 **토큰**: 702
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **1,481개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 779 | 52.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 223.0 | 702 | 47.4% |

