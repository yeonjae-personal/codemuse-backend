# 📄 LargeItemCode.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/enums/LargeItemCode.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 📋 열거형
- [`LargeItemCode`](#enum-largeitemcode)


## 📋 파일 개요

| | |
|--|--|
| ⚡ **총 복잡도**: 13 | 📊 **총 토큰 수**: 56 |





## 📋 열거형

### <a id="enum-largeitemcode"></a>📋 `LargeItemCode`


<details>
<summary>🔍 코드 미리보기</summary>

```java
public enum LargeItemCode {
	
	O("Offer"),
    G("Group"),
    C("Component"),
    R("Resource");
    
    private String value;
	
    private LargeItemCode(String value) {
    	this.value = value;
    }

	public String getValue() {
		return value;
	}
}...
```

**Chunk 정보**
- 🆔 **ID**: `4deb10be5c55`
- 📍 **라인**: 3-3

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **56개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 29 | 51.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| enum | 1 | 13.0 | 27 | 48.2% |

