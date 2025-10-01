# 📄 GroupedDcntRatMDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/price/GroupedDcntRatMDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`GroupedDcntRatMDto`](#class-groupeddcntratmdto) - 복잡도: 58

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `lombok.Data` | ⚡ **총 복잡도**: 58 |
| 📊 **총 토큰 수**: 345 |  |



## 🏗️ 클래스

### <a id="class-groupeddcntratmdto"></a>🎯 `GroupedDcntRatMDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 58 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 58 || 📍 **라인 범위** | 6-6 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class GroupedDcntRatMDto {
	public GroupedDcntRatMDto(DcntRatMDto dcntRatMDto) {
		this.generalDetails = new GeneralDetailFields(dcntRatMDto);
		this.additionalParams = new AdditionalParamFields(dcntRatMDto);
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type; 
		private String dcntRatCd;
		private String dcntRatNm;
		private String ratDivsCd;
		private String dcntRat;
		private String dcntRatAplyCyvl;
		private String dcntRatAplyCyclCd;
		private String daCalcDivsCd;
		private String ratAplyKdCd;
		private String valdStrtDtm;
		private String valdEndDtm;
		private String rgstUsr;
		private String rgstDtm;
		private String updUsr;
		private String updDtm;

		public GeneralDetailFields (DcntRatMDto dcntRatMDto) {
	...
```

**Chunk 정보**
- 🆔 **ID**: `b9ec2712565b`
- 📍 **라인**: 6-6
- 📊 **토큰**: 170
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **345개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 175 | 50.7% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 58.0 | 170 | 49.3% |

