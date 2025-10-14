# 📄 DsbdViewTypeEnums.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/enums/DsbdViewTypeEnums.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 📋 열거형
- [`DsbdViewTypeEnums`](#enum-dsbdviewtypeenums)


## 📋 파일 개요

| | |
|--|--|
| ⚡ **총 복잡도**: 11 | 📊 **총 토큰 수**: 50 |





## 📋 열거형

### <a id="enum-dsbdviewtypeenums"></a>📋 `DsbdViewTypeEnums`


<details>
<summary>🔍 코드 미리보기</summary>

```java
public enum DsbdViewTypeEnums {
	
	S("satistics"),
	P("personalized");
	
	private String value;
	
	DsbdViewTypeEnums(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}...
```

**Chunk 정보**
- 🆔 **ID**: `d525b4991580`
- 📍 **라인**: 3-3

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **50개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 26 | 52.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| enum | 1 | 11.0 | 24 | 48.0% |

