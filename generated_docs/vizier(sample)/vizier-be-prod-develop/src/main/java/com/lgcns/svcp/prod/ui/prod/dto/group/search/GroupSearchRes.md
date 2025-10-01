# 📄 GroupSearchRes.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/group/search/GroupSearchRes.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`GroupSearchRes`](#class-groupsearchres) - 복잡도: 12

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.common.metadata.AdditionalDetailDto` • `com.lgcns.svcp.prod.ui.prod.dto.common.metadata.GeneralDetailDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 12 |
| 📊 **총 토큰 수**: 84 |  |



## 🏗️ 클래스

### <a id="class-groupsearchres"></a>🎯 `GroupSearchRes`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 12 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 12 || 📍 **라인 범위** | 13-13 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class GroupSearchRes {
	
	private String objUuid;
	private String objCode;
	private String objName;
    private String itemCode;
	private String validStartDtm;
	private String validEndDtm;
	
	private List<GeneralDetailDto> general;
	private List<AdditionalDetailDto> additional;

	private String mctgrItemCode;
	private String lctgrItemCode;
}...
```

**Chunk 정보**
- 🆔 **ID**: `4068347f45c4`
- 📍 **라인**: 13-13
- 📊 **토큰**: 35
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **84개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 49 | 58.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 12.0 | 35 | 41.7% |

