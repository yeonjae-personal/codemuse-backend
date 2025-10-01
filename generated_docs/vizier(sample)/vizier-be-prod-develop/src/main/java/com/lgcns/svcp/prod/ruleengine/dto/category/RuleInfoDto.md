# 📄 RuleInfoDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ruleengine/dto/category/RuleInfoDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`RuleInfoDto`](#class-ruleinfodto) - 복잡도: 20

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.Date` • `com.lgcns.svcp.prod.util.DateUtil` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 20 |
| 📊 **총 토큰 수**: 158 |  |



## 🏗️ 클래스

### <a id="class-ruleinfodto"></a>🎯 `RuleInfoDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 20 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 20 || 📍 **라인 범위** | 12-12 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class RuleInfoDto {
	private String ruleUuid;
	private String ruleName;
	private String ovwCntn;
	private String chgDeptName;
	private String chgUser;
	private String ruleMsg;
	private String useYn;
	private String rgstDtm;
	
	public RuleInfoDto(String ruleUuid, String ruleName, String ovwCntn, String chgDeptName, String chgUser, String ruleMsg, String useYn, Date rgstDtm) {
		this.ruleUuid = ruleUuid;
		this.ruleName = ruleName;
		this.ovwCntn = ovwCntn;
		this.chgDeptName = chgDeptName;
		this.chgUser = chgUser;
		this.ruleMsg = ruleMsg;
		this.useYn = useYn;
		this.rgstDtm = DateUtil.formatDate("yyyy-MM-dd", rgstDtm);
	}
}...
```

**Chunk 정보**
- 🆔 **ID**: `46ed5f3f3826`
- 📍 **라인**: 12-12
- 📊 **토큰**: 73
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **158개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 85 | 53.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 20.0 | 73 | 46.2% |

