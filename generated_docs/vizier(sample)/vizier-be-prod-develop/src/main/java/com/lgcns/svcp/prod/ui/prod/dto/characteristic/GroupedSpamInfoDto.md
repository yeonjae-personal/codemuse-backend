# 📄 GroupedSpamInfoDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/characteristic/GroupedSpamInfoDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`GroupedSpamInfoDto`](#class-groupedspaminfodto) - 복잡도: 31

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `lombok.Data` | ⚡ **총 복잡도**: 31 |
| 📊 **총 토큰 수**: 201 |  |



## 🏗️ 클래스

### <a id="class-groupedspaminfodto"></a>🎯 `GroupedSpamInfoDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 31 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 31 || 📍 **라인 범위** | 8-8 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class GroupedSpamInfoDto {
	public GroupedSpamInfoDto(SpamInfoDto spamInfoDto) {
		this.generalDetails = new GeneralDetailFields(spamInfoDto);
		this.additionalParams = new AdditionalParamFields(spamInfoDto);
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String spamCd;
		private String spamNm;
		private String thrsIdfyCd;
		private String lvwuPlcyCd;
//		private List<SpamLvwuPlcyDDto> lvwuPlcyInfoList;
		private String valdStrtDtm;
		private String valdEndDtm;

		public GeneralDetailFields (SpamInfoDto spamInfoDto) {
			this.type = spamInfoDto.getType();
			this.spamCd = spamInfoDto.getSpamCd();
			this.spamNm = spamInfoDto.getSpamNm();
			this.thrsIdfyCd = spamInfoDto.getThrsIdfyCd();
//			this.lv...
```

**Chunk 정보**
- 🆔 **ID**: `058e1ad62dd3`
- 📍 **라인**: 8-8
- 📊 **토큰**: 97
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **201개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 104 | 51.7% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 31.0 | 97 | 48.3% |

