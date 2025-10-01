# 📄 MapUtil.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/util/MapUtil.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`MapUtil`](#class-maputil) - 복잡도: 30

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.LinkedHashMap` • `java.util.List` • `java.util.Map` • `java.util.stream.Collectors` • `com.lgcns.svcp.prod.online.prod.dto.common.AdditionalColumnsDto` | ⚡ **총 복잡도**: 30 |
| 📊 **총 토큰 수**: 280 |  |



## 🏗️ 클래스

### <a id="class-maputil"></a>🎯 `MapUtil`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 30 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 30 || 📍 **라인 범위** | 10-10 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class MapUtil {
	public static Map<String, Object> mapToFlatMapWithNullHandling(Map<String, Object> result, List<AdditionalColumnsDto> additionalColumns) {
		// 모든 키-값 쌍을 그대로 복사
		Map<String, Object> flatMap = result.entrySet().stream()
				.collect(Collectors.toMap(
						entry -> toCamelCase(entry.getKey()), 
						Map.Entry::getValue, 
						(oldValue, newValue) -> oldValue, 
						LinkedHashMap::new));
		// additionalColumns에서 컬럼 이름 가져와 처리
		additionalColumns.forEach(column -> {
			String columnName = toCamelCase(column.getAttrName());
			flatMap.putIfAbsent(columnName, null); // 값이 없으면 null로 초기화
		});

		return flatMap;
	}

	public static String toCamelCase(String input) {
		//공백과 _로 구분된 단어들을 분리
		String[] words = input.split("[ _]+");

		StringBuilder camelCaseString = new Str...
```

**Chunk 정보**
- 🆔 **ID**: `3f8478cfad12`
- 📍 **라인**: 10-10
- 📊 **토큰**: 134
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **280개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 146 | 52.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 30.0 | 134 | 47.9% |

