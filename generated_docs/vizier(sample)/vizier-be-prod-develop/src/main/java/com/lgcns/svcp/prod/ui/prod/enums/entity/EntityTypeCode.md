# 📄 EntityTypeCode.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/enums/entity/EntityTypeCode.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 📋 열거형
- [`EntityTypeCode`](#enum-entitytypecode)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `lombok.AllArgsConstructor` • `lombok.Getter` | ⚡ **총 복잡도**: 27 |
| 📊 **총 토큰 수**: 186 |  |





## 📋 열거형

### <a id="enum-entitytypecode"></a>📋 `EntityTypeCode`


<details>
<summary>🔍 코드 미리보기</summary>

```java
public enum EntityTypeCode {
	EBL("EBL"),
	EDT("EDT"),
	ESC("ESC"),
	ETC("EUC"),
	UNKNOWN(null);
	
	private final String prefix;

	private static boolean isFixedCode(String code) {
		for (EntityTypeCode type : values()) {
			if (type.name().equals(code) && EntityTypeCode.valueOf(code) != EntityTypeCode.ETC) {
				return true;
			}
		}
		return false;
	}

	private static boolean isValidDynamicCode(String code) {
		return code != null && code.matches("^ETC\\d{5}$");
	}

//	public static boolean isValidCode(String code) {
//		return isFixedCode(code) || isValidDynamicCode(code);
//	}

	public sta...
```

**Chunk 정보**
- 🆔 **ID**: `15cad1749ac2`
- 📍 **라인**: 8-8

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **186개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 97 | 52.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| enum | 1 | 27.0 | 89 | 47.8% |

