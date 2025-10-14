# 📄 AprvStepCode.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/enums/publish/AprvStepCode.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 📋 열거형
- [`AprvStepCode`](#enum-aprvstepcode)


## 📋 파일 개요

| | |
|--|--|
| ⚡ **총 복잡도**: 14 | 📊 **총 토큰 수**: 66 |





## 📋 열거형

### <a id="enum-aprvstepcode"></a>📋 `AprvStepCode`


<details>
<summary>🔍 코드 미리보기</summary>

```java
public enum AprvStepCode {
	D("Design"),
	P("Pricing Review"),
	I("IT Review"),
	E("Excute Approval"),
	C("Complete Approval");
	
	private String value;

	private AprvStepCode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}...
```

**Chunk 정보**
- 🆔 **ID**: `3e14c6d40d8a`
- 📍 **라인**: 3-3

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **66개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 34 | 51.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| enum | 1 | 14.0 | 32 | 48.5% |

