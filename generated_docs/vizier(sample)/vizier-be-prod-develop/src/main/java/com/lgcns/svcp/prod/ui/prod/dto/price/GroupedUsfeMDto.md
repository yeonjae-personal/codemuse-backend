# 📄 GroupedUsfeMDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/price/GroupedUsfeMDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`GroupedUsfeMDto`](#class-groupedusfemdto) - 복잡도: 50

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `lombok.Data` | ⚡ **총 복잡도**: 50 |
| 📊 **총 토큰 수**: 297 |  |



## 🏗️ 클래스

### <a id="class-groupedusfemdto"></a>🎯 `GroupedUsfeMDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 50 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 50 || 📍 **라인 범위** | 6-6 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class GroupedUsfeMDto {

	public GroupedUsfeMDto(UsfeMDto usfeMDto) {
		this.generalDetails = new GeneralDetailFields(usfeMDto);
		this.additionalParams = new AdditionalParamFields(usfeMDto);
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String usfeCd;
		private String usfeNm;
		private String ratDivsCd;
		private String ioclDivsCd;
		private String usfeAplyUval;
		private String ratAplyUnitCd;
		private String useRat;
		private String ratAplyKdCd;
		private String valdStrtDtm;
		private String valdEndDtm;

		public GeneralDetailFields (UsfeMDto usfeMDto) {
			this.type = usfeMDto.getType();
			this.usfeCd = usfeMDto.getUsfeCd();
			this.usfeNm = usfeMDto.getUsfeNm();
			this.ratDivsCd = usfeMDto.g...
```

**Chunk 정보**
- 🆔 **ID**: `6b7cd10af0d2`
- 📍 **라인**: 6-6
- 📊 **토큰**: 146
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **297개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 151 | 50.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 50.0 | 146 | 49.2% |

