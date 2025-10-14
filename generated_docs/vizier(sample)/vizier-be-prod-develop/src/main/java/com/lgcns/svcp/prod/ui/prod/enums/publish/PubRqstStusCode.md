# 📄 PubRqstStusCode.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/enums/publish/PubRqstStusCode.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 📋 열거형
- [`PubRqstStusCode`](#enum-pubrqststuscode)


## 📋 파일 개요

| | |
|--|--|
| ⚡ **총 복잡도**: 17 | 📊 **총 토큰 수**: 70 |





## 📋 열거형

### <a id="enum-pubrqststuscode"></a>📋 `PubRqstStusCode`


<details>
<summary>🔍 코드 미리보기</summary>

```java
public enum PubRqstStusCode {
	C("Created"),
	M("Composed"),
	V("Validation"),
	I("In Progress"),
	D("Delay"),
	P("Publish Complete"),
	O("Prod Transfer"),
	E("Expire");

	private String value;

	private PubRqstStusCode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}...
```

**Chunk 정보**
- 🆔 **ID**: `eda85135da22`
- 📍 **라인**: 3-3

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **70개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 36 | 51.4% |
| package | 1 | 0.0 | 0 | 0.0% |
| enum | 1 | 17.0 | 34 | 48.6% |

