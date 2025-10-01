# 📄 PubAprvStepLDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/publish/packages/PubAprvStepLDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`PubAprvStepLDto`](#class-pubaprvstepldto) - 복잡도: 11

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.BaseDto` • `com.lgcns.svcp.prod.ui.prod.enums.YesNo` • `com.lgcns.svcp.prod.ui.prod.enums.publish.AprvStepCode` • `com.lgcns.svcp.prod.ui.prod.enums.publish.AprvStusCode` • `lombok.Getter` 외 1개 | ⚡ **총 복잡도**: 11 |
| 📊 **총 토큰 수**: 86 |  |



## 🏗️ 클래스

### <a id="class-pubaprvstepldto"></a>🎯 `PubAprvStepLDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseDto` |
| ⚡ 복잡도 | 11 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 11 || 📍 **라인 범위** | 15-15 |
| 🏗️ **상속** | `BaseDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class PubAprvStepLDto extends BaseDto {
	private String pubAprvUuid;
	private long sortNo;
	private AprvStepCode pubAprvStepCode;
	private AprvStusCode aprvStusCode;
	private String aprvStusDscr;
	private String aprvDtm;
	private long lmtTm;
	private YesNo useYn;

	private List<PubAprvSubStepLDto> pubAprvSubStepLDtos;
}...
```

**Chunk 정보**
- 🆔 **ID**: `df56d67ff97c`
- 📍 **라인**: 15-15
- 📊 **토큰**: 34
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **86개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 52 | 60.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 11.0 | 34 | 39.5% |

