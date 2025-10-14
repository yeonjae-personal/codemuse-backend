# 📄 StringUtilCustom.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/util/StringUtilCustom.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`StringUtilCustom`](#class-stringutilcustom) - 복잡도: 87

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.math.BigDecimal` • `java.text.NumberFormat` • `java.util.Currency` • `java.util.List` • `java.util.Locale` • `org.apache.commons.lang3.StringUtils` | ⚡ **총 복잡도**: 87 |
| 📊 **총 토큰 수**: 586 |  |



## 🏗️ 클래스

### <a id="class-stringutilcustom"></a>🎯 `StringUtilCustom`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `StringUtils` |
| ⚡ 복잡도 | 87 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 87 || 📍 **라인 범위** | 11-11 |
| 🏗️ **상속** | `StringUtils` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class StringUtilCustom extends StringUtils {

	public static String snakeToCamel(String snakeCase) {
		if (snakeCase == null || snakeCase.isEmpty()) {
			return snakeCase;
		}

		StringBuilder camelCase = new StringBuilder();
		boolean nextCharUpperCase = false;

		for (int i = 0; i < snakeCase.length(); i++) {
			char currentChar = snakeCase.charAt(i);

			if (currentChar == '_') {
				nextCharUpperCase = true;
			} else {
				if (nextCharUpperCase) {
					camelCase.append(Character.toUpperCase(currentChar));
					nextCharUpperCase = false;
				} else {
					camelCase.append(currentChar);
				}
			}
		}

		return camelCase.toString();
	}

	public static String covertToWonWithoutWCharacter(String amount) {
		if (amount == null || amount.isEmpty()) {
			return null;
		}
		Currency kr...
```

**Chunk 정보**
- 🆔 **ID**: `e33b87260982`
- 📍 **라인**: 11-11
- 📊 **토큰**: 286
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **586개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 300 | 51.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 87.0 | 286 | 48.8% |

