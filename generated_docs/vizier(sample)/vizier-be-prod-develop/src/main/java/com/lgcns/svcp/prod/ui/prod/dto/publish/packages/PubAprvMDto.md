# 📄 PubAprvMDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/publish/packages/PubAprvMDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`PubAprvMDto`](#class-pubaprvmdto) - 복잡도: 10

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.BaseDto` • `com.lgcns.svcp.prod.ui.prod.enums.YesNo` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 10 |
| 📊 **총 토큰 수**: 76 |  |



## 🏗️ 클래스

### <a id="class-pubaprvmdto"></a>🎯 `PubAprvMDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseDto` |
| ⚡ 복잡도 | 10 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 10 || 📍 **라인 범위** | 13-13 |
| 🏗️ **상속** | `BaseDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class PubAprvMDto extends BaseDto {
	private String pubAprvUuid;
	private String pubRqstTaskCode;
	private String pubAprvDscr;
	private YesNo useYn;
	private String aprvFlowTmptCode;
	private String aprvFlowTmptName;
	private String pubAprvRqsttDtm;

	private List<PubAprvStepLDto> pubAprvStepLDtos;
}...
```

**Chunk 정보**
- 🆔 **ID**: `ffc451b021d4`
- 📍 **라인**: 13-13
- 📊 **토큰**: 31
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **76개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 45 | 59.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 10.0 | 31 | 40.8% |

