# 📄 PubRqstTaskDetlStusCode.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/enums/publish/PubRqstTaskDetlStusCode.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 📋 열거형
- [`PubRqstTaskDetlStusCode`](#enum-pubrqsttaskdetlstuscode)


## 📋 파일 개요

| | |
|--|--|
| ⚡ **총 복잡도**: 12 | 📊 **총 토큰 수**: 54 |





## 📋 열거형

### <a id="enum-pubrqsttaskdetlstuscode"></a>📋 `PubRqstTaskDetlStusCode`


<details>
<summary>🔍 코드 미리보기</summary>

```java
public enum PubRqstTaskDetlStusCode {
	SAVED("Saved"),
	PACKED("Packed"),
	PUBLISHED("Published");

	private String value;

	private PubRqstTaskDetlStusCode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}...
```

**Chunk 정보**
- 🆔 **ID**: `39b091c129a2`
- 📍 **라인**: 3-3

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **54개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 28 | 51.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| enum | 1 | 12.0 | 26 | 48.1% |

