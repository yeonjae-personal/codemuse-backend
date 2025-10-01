# 📄 CustomValidationItemResponse.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/customvalidation/CustomValidationItemResponse.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`CustomValidationItemResponse`](#class-customvalidationitemresponse) - 복잡도: 10

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.item.Item` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 10 |
| 📊 **총 토큰 수**: 70 |  |



## 🏗️ 클래스

### <a id="class-customvalidationitemresponse"></a>🎯 `CustomValidationItemResponse`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 10 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 10 || 📍 **라인 범위** | 12-12 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class CustomValidationItemResponse {
	
	private String itemCode;
    private String itemName;
    private String middleItemCode;
    private String middleItemName;
    private String largeItemCode;
    private String largeItemName;   
    private List<Item> types;
    private List<CustomValidationComponentItem> componentItem;
}...
```

**Chunk 정보**
- 🆔 **ID**: `3639731495d5`
- 📍 **라인**: 12-12
- 📊 **토큰**: 29
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **70개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 41 | 58.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 10.0 | 29 | 41.4% |

