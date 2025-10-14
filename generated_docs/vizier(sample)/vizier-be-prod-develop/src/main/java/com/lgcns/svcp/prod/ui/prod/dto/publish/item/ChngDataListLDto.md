# 📄 ChngDataListLDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/publish/item/ChngDataListLDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ChngDataListLDto`](#class-chngdatalistldto) - 복잡도: 14

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.ui.prod.dto.BaseDto` • `com.lgcns.svcp.prod.ui.prod.enums.publish.ChngDataStusCode` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 14 |
| 📊 **총 토큰 수**: 98 |  |



## 🏗️ 클래스

### <a id="class-chngdatalistldto"></a>🎯 `ChngDataListLDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseDto` |
| ⚡ 복잡도 | 14 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 14 || 📍 **라인 범위** | 11-11 |
| 🏗️ **상속** | `BaseDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ChngDataListLDto extends BaseDto {
	private long chngDataSeq;
	private String chngDataCode;
	private String chngDataCodeName;
	private String chngDataTypeCode;
	private String chngDataObjUuid;
	private String chngDataItemCode;
	private ChngDataStusCode chngDataStusCode;
	private String chngDataRqstUser;
	private String callApiUrl;
	private String callApiMethod;
	private String callApiQuery;
	private String callApiBody;
}...
```

**Chunk 정보**
- 🆔 **ID**: `76753cb5b4ed`
- 📍 **라인**: 11-11
- 📊 **토큰**: 43
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **98개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 55 | 56.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 14.0 | 43 | 43.9% |

