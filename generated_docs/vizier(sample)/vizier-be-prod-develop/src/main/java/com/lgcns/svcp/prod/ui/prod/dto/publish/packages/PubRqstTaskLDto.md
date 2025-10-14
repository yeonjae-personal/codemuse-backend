# 📄 PubRqstTaskLDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/publish/packages/PubRqstTaskLDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`PubRqstTaskLDto`](#class-pubrqsttaskldto) - 복잡도: 6

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.ui.prod.dto.BaseDto` • `com.lgcns.svcp.prod.ui.prod.enums.YesNo` • `com.lgcns.svcp.prod.ui.prod.enums.publish.PubRqstTaskDetlStusCode` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 6 |
| 📊 **총 토큰 수**: 52 |  |



## 🏗️ 클래스

### <a id="class-pubrqsttaskldto"></a>🎯 `PubRqstTaskLDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseDto` |
| ⚡ 복잡도 | 6 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 6 || 📍 **라인 범위** | 12-12 |
| 🏗️ **상속** | `BaseDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class PubRqstTaskLDto extends BaseDto {
	private String pubRqstTaskCode;
	private long chngDataSeq;
	private PubRqstTaskDetlStusCode pubRqstDetlStusCode;
	private YesNo vldateFnshYn;
}...
```

**Chunk 정보**
- 🆔 **ID**: `980ef47413a2`
- 📍 **라인**: 12-12
- 📊 **토큰**: 19
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **52개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 33 | 63.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 6.0 | 19 | 36.5% |

