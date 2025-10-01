# 📄 DateFormatUtil.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/util/DateFormatUtil.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`DateFormatUtil`](#class-dateformatutil) - 복잡도: 8

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.text.SimpleDateFormat` | ⚡ **총 복잡도**: 8 |
| 📊 **총 토큰 수**: 52 |  |



## 🏗️ 클래스

### <a id="class-dateformatutil"></a>🎯 `DateFormatUtil`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 8 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 8 || 📍 **라인 범위** | 5-5 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class DateFormatUtil {
	
	public static SimpleDateFormat getDatabaseDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public static SimpleDateFormat getResponseDateFormat() {
		return new SimpleDateFormat("yyyy/MM/dd");
	}
}...
```

**Chunk 정보**
- 🆔 **ID**: `a11dbccf8116`
- 📍 **라인**: 5-5
- 📊 **토큰**: 24
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **52개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 28 | 53.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 8.0 | 24 | 46.2% |

