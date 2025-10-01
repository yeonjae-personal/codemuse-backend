# 📄 UiAttributeManagementService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/UiAttributeManagementService.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`UiAttributeManagementService`](#interface-uiattributemanagementservice)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.attribute.AttributeDetailDto` • `com.lgcns.svcp.prod.ui.prod.dto.attribute.AttributeUpperLowerItemsDto` • `com.lgcns.svcp.prod.ui.prod.dto.attribute.AttributeViewDto` | ⚡ **총 복잡도**: 6 |
| 📊 **총 토큰 수**: 42 |  |




## 🔌 인터페이스

### <a id="interface-uiattributemanagementservice"></a>🔌 `UiAttributeManagementService`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 9-9 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface UiAttributeManagementService {
	List<AttributeViewDto> getData();
	AttributeDetailDto getDetail(String itemCode);
	AttributeUpperLowerItemsDto getUpperOrLowerItems(String largeItem);
	void save(AttributeDetailDto request);
}...
```

**Chunk 정보**
- 🆔 **ID**: `e6d339d24102`
- 📊 **토큰**: 16

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **42개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 26 | 61.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 6.0 | 16 | 38.1% |

