# 📄 MetadataService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/MetadataService.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`MetadataService`](#interface-metadataservice)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.item.LargeItemDto` • `com.lgcns.svcp.prod.ui.prod.dto.item.MiddleItemDto` | ⚡ **총 복잡도**: 4 |
| 📊 **총 토큰 수**: 28 |  |




## 🔌 인터페이스

### <a id="interface-metadataservice"></a>🔌 `MetadataService`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 8-8 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface MetadataService {
	List<MiddleItemDto> getMiddleItem(String largeItemCode);
	List<LargeItemDto> getLargeItem();
}...
```

**Chunk 정보**
- 🆔 **ID**: `1b6299c9fbe7`
- 📊 **토큰**: 10

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **28개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 18 | 64.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 4.0 | 10 | 35.7% |

