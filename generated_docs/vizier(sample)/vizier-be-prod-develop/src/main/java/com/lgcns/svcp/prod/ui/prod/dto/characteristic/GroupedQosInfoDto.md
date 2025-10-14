# 📄 GroupedQosInfoDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/characteristic/GroupedQosInfoDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`GroupedQosInfoDto`](#class-groupedqosinfodto) - 복잡도: 33

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `lombok.Data` | ⚡ **총 복잡도**: 33 |
| 📊 **총 토큰 수**: 213 |  |



## 🏗️ 클래스

### <a id="class-groupedqosinfodto"></a>🎯 `GroupedQosInfoDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 33 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 33 || 📍 **라인 범위** | 8-8 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class GroupedQosInfoDto {
	public GroupedQosInfoDto(QosInfoDto qosInfoDto) {
		this.generalDetails = new GeneralDetailFields(qosInfoDto);
		this.additionalParams = new AdditionalParamFields(qosInfoDto);
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String qosCd;
		private String qosNm;
		private String qosPlcyCd;
		private String qosPlcyGrpCd;
		private String thrsIdfyCd;
//		private List<QosPlcyRelDDto> qosPlcyList;

		private String valdStrtDtm;
		private String valdEndDtm;


		public GeneralDetailFields (QosInfoDto qosInfoDto) {
			this.type = qosInfoDto.getType();
			this.qosCd = qosInfoDto.getQosCd();
			this.qosNm = qosInfoDto.getQosNm();
//			this.qosPlcyList = qosInfoDto.getQosPlcyRelDDto()...
```

**Chunk 정보**
- 🆔 **ID**: `91ca96924ada`
- 📍 **라인**: 8-8
- 📊 **토큰**: 103
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **213개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 110 | 51.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 33.0 | 103 | 48.4% |

