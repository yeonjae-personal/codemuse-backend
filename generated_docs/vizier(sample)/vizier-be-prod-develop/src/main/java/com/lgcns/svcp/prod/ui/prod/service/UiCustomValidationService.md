# 📄 UiCustomValidationService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/UiCustomValidationService.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`UiCustomValidationService`](#interface-uicustomvalidationservice)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.customvalidation.BoxAndAttrRequest` • `com.lgcns.svcp.prod.ui.prod.dto.customvalidation.BoxAndAttrRespone` • `com.lgcns.svcp.prod.ui.prod.dto.customvalidation.CustomValidationItemResponse` • `com.lgcns.svcp.prod.ui.prod.dto.customvalidation.CustomValidationMainRespone` • `com.lgcns.svcp.prod.ui.prod.dto.customvalidation.SaveCusSearchDto` 외 4개 | ⚡ **총 복잡도**: 9 |
| 📊 **총 토큰 수**: 152 |  |




## 🔌 인터페이스

### <a id="interface-uicustomvalidationservice"></a>🔌 `UiCustomValidationService`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 16-16 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface UiCustomValidationService {
	List<CustomValidationMainRespone> getListMain(String item, String type, String subType, String attrUuid);
	BoxAndAttrRespone getListAdmin(String item, String type, String subType, String action);
	List<SaveCusSearchDto> save(@Valid BoxAndAttrRequest request);
	HistoryResponse getHistory(String validCode);
	CustomValidationPagingResponse getData(int page, int size, String searchBy, String item, String type, String subType, String language);
	void exportExcel(String searchBy, String item, String type, String subType, String language, HttpServletRespo...
```

**Chunk 정보**
- 🆔 **ID**: `f970c3977156`
- 📊 **토큰**: 65

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **152개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 87 | 57.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 9.0 | 65 | 42.8% |

