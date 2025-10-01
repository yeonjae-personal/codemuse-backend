# 📄 AdditionalDetailDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/common/metadata/AdditionalDetailDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`AdditionalDetailDto`](#class-additionaldetaildto) - 복잡도: 33

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.fasterxml.jackson.annotation.JsonIgnore` • `com.fasterxml.jackson.annotation.JsonInclude` • `com.fasterxml.jackson.annotation.JsonInclude.Include` • `com.lgcns.svcp.prod.ui.prod.dto.BaseDto` • `com.lgcns.svcp.prod.ui.prod.dto.admin.table.ref.TableColumnKeyValue` 외 3개 | ⚡ **총 복잡도**: 33 |
| 📊 **총 토큰 수**: 206 |  |



## 🏗️ 클래스

### <a id="class-additionaldetaildto"></a>🎯 `AdditionalDetailDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseDto` |
| ⚡ 복잡도 | 33 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 33 || 📍 **라인 범위** | 17-17 |
| 🏗️ **상속** | `BaseDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class AdditionalDetailDto extends BaseDto {
	private String attrUuid;
	private String itemCode;
	private String fieldTypeCode;
	private String commGroupCode;
	private long sortNo;
	private String useYn;
	private String attrMaxLength;
	private String requiredYn;
	private String labelId;
	private String dispTab;
	private String dispCardYn;
	private String advSearchYn;
	private String attrVal;
	private String attrRefTableName;

	@JsonInclude(Include.NON_NULL)
	private List<TableColumnKeyValue> tableColumns;

	@JsonIgnore
	private String objUuid;
	private String obName;
	private String labelName;
	private String labelDscr;

	public String getAttrVal() {
		if (StringUtilCustom.isEmpty(attrVal) || (!"NF".equals(fieldTypeCode) && !"RF".equals(fieldTypeCode))) {
			return attrVal;
		}
		if ...
```

**Chunk 정보**
- 🆔 **ID**: `e43c717f4928`
- 📍 **라인**: 17-17
- 📊 **토큰**: 92
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **206개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 114 | 55.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 33.0 | 92 | 44.7% |

