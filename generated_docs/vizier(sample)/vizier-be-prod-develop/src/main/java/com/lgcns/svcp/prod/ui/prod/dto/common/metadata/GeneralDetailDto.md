# 📄 GeneralDetailDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/common/metadata/GeneralDetailDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`GeneralDetailDto`](#class-generaldetaildto) - 복잡도: 24

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.ui.prod.dto.BaseDto` • `com.lgcns.svcp.prod.util.StringUtilCustom` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 24 |
| 📊 **총 토큰 수**: 150 |  |



## 🏗️ 클래스

### <a id="class-generaldetaildto"></a>🎯 `GeneralDetailDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseDto` |
| ⚡ 복잡도 | 24 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 24 || 📍 **라인 범위** | 11-11 |
| 🏗️ **상속** | `BaseDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class GeneralDetailDto extends BaseDto {
	private String objUuid;
	private String colName;
	private String fieldTypeCode;
	private String editYn;
	private int sortNo;
	private String useYn;
	private String attrMaxLength;
	private String requiredYn;
	private String labelId;
	private String attrVal;

	private String labelName;
	private String labelDscr;

	public String getAttrVal() {
		if (StringUtilCustom.isEmpty(attrVal) || (!"NF".equals(fieldTypeCode) && !"RF".equals(fieldTypeCode))) {
			return attrVal;
		}
		if (attrVal.matches("0+(\\.0+)?")) {
			return "0";
		}
		return attrVal.replaceFirst("^0+(?!\\.)", "").replaceFirst("(\\.\\d*?)0+$", "$1").replaceFirst("\\.$",
				"");
	}

}...
```

**Chunk 정보**
- 🆔 **ID**: `2dc43465fc31`
- 📍 **라인**: 11-11
- 📊 **토큰**: 69
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **150개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 81 | 54.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 24.0 | 69 | 46.0% |

