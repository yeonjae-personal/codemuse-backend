# 📄 HistoryResponse.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/customvalidation/history/HistoryResponse.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`HistoryResponse`](#class-historyresponse) - 복잡도: 5

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.history.detail.EventDateDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 5 |
| 📊 **총 토큰 수**: 40 |  |



## 🏗️ 클래스

### <a id="class-historyresponse"></a>🎯 `HistoryResponse`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 5 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 5 || 📍 **라인 범위** | 12-12 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class HistoryResponse {
	
	private EventDateDto created;
	private List<HistoryChangeDto> changed;
	private EventDateDto ended;
}...
```

**Chunk 정보**
- 🆔 **ID**: `ed89e391d333`
- 📍 **라인**: 12-12
- 📊 **토큰**: 14
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **40개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 26 | 65.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 5.0 | 14 | 35.0% |

