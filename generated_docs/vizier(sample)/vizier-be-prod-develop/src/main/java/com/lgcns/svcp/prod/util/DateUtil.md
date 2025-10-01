# 📄 DateUtil.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/util/DateUtil.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`DateUtil`](#class-dateutil) - 복잡도: 96

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.text.ParseException` • `java.text.SimpleDateFormat` • `java.time.LocalDate` • `java.time.LocalDateTime` • `java.time.format.DateTimeFormatter` • `java.util.Calendar` 외 4개 | ⚡ **총 복잡도**: 96 |
| 📊 **총 토큰 수**: 686 |  |



## 🏗️ 클래스

### <a id="class-dateutil"></a>🎯 `DateUtil`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 96 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 96 || 📍 **라인 범위** | 15-15 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class DateUtil {

	public static final DateTimeFormatter SOURCE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static final DateTimeFormatter TARGET_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	private static final DateTimeFormatter WORKNO_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	public static String formatDate(String dateStr) {
		if (dateStr == null || dateStr.isEmpty()) {
			return null;
		}
		LocalDateTime dateTime = LocalDateTime.parse(dateStr, SOURCE_FORMATTER);
		return dateTime.format(TARGET_FORMATTER);
	}

	public static String formatDate(String format, Date date) {
		if (date != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			return simpleDateFormat.format(date);
		}
		return null;...
```

**Chunk 정보**
- 🆔 **ID**: `19a0d34569bb`
- 📍 **라인**: 15-15
- 📊 **토큰**: 332
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **686개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 354 | 51.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 96.0 | 332 | 48.4% |

