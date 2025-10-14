# 📄 SaveTargetReqDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/extend/SaveTargetReqDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`SaveTargetReqDto`](#class-savetargetreqdto) - 복잡도: 7

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.group.InsertGroupOfferDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 7 |
| 📊 **총 토큰 수**: 52 |  |



## 🏗️ 클래스

### <a id="class-savetargetreqdto"></a>🎯 `SaveTargetReqDto`

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
public class SaveTargetReqDto {
    private String chgDeptName;
    private String chgUser;
	private List<OffrDpdcReqDto> addOffrDpdcLst;
	private List<OffrDpdcReqDto> updateOffrDpdcLst;
    private List<InsertGroupOfferDto> insertGroupOfferLst;
}...
```

**Chunk 정보**
- 🆔 **ID**: `e5a76e86077e`
- 📍 **라인**: 12-12
- 📊 **토큰**: 20
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **52개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 32 | 61.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 7.0 | 20 | 38.5% |

