# 📄 PubPackageDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/publish/packages/PubPackageDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`PubPackageDto`](#class-pubpackagedto) - 복잡도: 7

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.publish.item.ChngDataListLDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 7 |
| 📊 **총 토큰 수**: 68 |  |



## 🏗️ 클래스

### <a id="class-pubpackagedto"></a>🎯 `PubPackageDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 7 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 7 || 📍 **라인 범위** | 12-12 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class PubPackageDto {
	private String pubRqstTaskCode;
	private PubRqstTaskMDto pubRqstTaskMDto; // General
	private List<ChngDataListLDto> chngDataLstDtos; // Compose
	private PubAprvMDto pubAprvMDto; // Approval
	private PubPrcsTaskMDto pubPrcsTaskMDto; // Publish
}...
```

**Chunk 정보**
- 🆔 **ID**: `dcf3cecc751e`
- 📍 **라인**: 12-12
- 📊 **토큰**: 28
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **68개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 40 | 58.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 7.0 | 28 | 41.2% |

