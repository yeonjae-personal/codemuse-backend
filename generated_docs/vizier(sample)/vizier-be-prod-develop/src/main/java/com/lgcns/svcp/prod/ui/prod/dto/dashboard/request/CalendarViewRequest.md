# 📄 CalendarViewRequest.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/dashboard/request/CalendarViewRequest.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`CalendarViewRequest`](#class-calendarviewrequest) - 복잡도: 7

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.dashboard.CalendarViewDto` • `jakarta.validation.constraints.NotBlank` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 7 |
| 📊 **총 토큰 수**: 54 |  |



## 🏗️ 클래스

### <a id="class-calendarviewrequest"></a>🎯 `CalendarViewRequest`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 7 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 7 || 📍 **라인 범위** | 14-14 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class CalendarViewRequest {
	
	@NotBlank
	private String date;
	
	@NotBlank
	private String dsbdViewUuid;
	
	private List<CalendarViewDto> datas = new ArrayList<>();
}...
```

**Chunk 정보**
- 🆔 **ID**: `ff11d4c67c50`
- 📍 **라인**: 14-14
- 📊 **토큰**: 19
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **54개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 35 | 64.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 7.0 | 19 | 35.2% |

