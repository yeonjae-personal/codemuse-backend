# 📄 AprvFlowTmptStepLDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/publish/aprv/AprvFlowTmptStepLDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`AprvFlowTmptStepLDto`](#class-aprvflowtmptstepldto) - 복잡도: 8

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.BaseDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 8 |
| 📊 **총 토큰 수**: 62 |  |



## 🏗️ 클래스

### <a id="class-aprvflowtmptstepldto"></a>🎯 `AprvFlowTmptStepLDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseDto` |
| ⚡ 복잡도 | 8 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 8 || 📍 **라인 범위** | 12-12 |
| 🏗️ **상속** | `BaseDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class AprvFlowTmptStepLDto extends BaseDto {
	private String aprvFlowTmptCode;
	private long sortNo;
	private String aprvStepCode;
	private long lmtTm;
	private String useYn;

	private List<AprvFlowTmptSubStepLDto> aprvFlowTmptSubStepLs;
}...
```

**Chunk 정보**
- 🆔 **ID**: `cf7427de8040`
- 📍 **라인**: 12-12
- 📊 **토큰**: 25
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **62개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 37 | 59.7% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 8.0 | 25 | 40.3% |

