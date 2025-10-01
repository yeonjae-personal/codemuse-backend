# 📄 RelationViewResDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/extend/RelationViewResDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`RelationViewResDto`](#class-relationviewresdto) - 복잡도: 24

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 24 |
| 📊 **총 토큰 수**: 152 |  |



## 🏗️ 클래스

### <a id="class-relationviewresdto"></a>🎯 `RelationViewResDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 24 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 24 || 📍 **라인 범위** | 10-10 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class RelationViewResDto {
    private String parentUuid;
	private String dpdcRelUuid;
	private String dpdcRelCode;
	private String dpdcRelName;
	private String relationValidStartDate;
	private String relationValidEndDate;
	private String targetUuid;
	private String targetCode;
	private String targetName;
	private String itemCode;
	private String itemCodeName;
	private String lctgrItemName;
	private String validStartDtm;
	private String validEndDtm;
	private String itemValidStartDtm;
	private String itemValidEndDtm;
	private List<String> referenceUuids;
	private List<ItemOffrResDto> childOffr;
	private String offerGroupUuid;
	private String referenceUuid;
	private String leaderCode;
	private String leaderName;
}...
```

**Chunk 정보**
- 🆔 **ID**: `b9096f970292`
- 📍 **라인**: 10-10
- 📊 **토큰**: 71
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **152개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 81 | 53.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 24.0 | 71 | 46.7% |

