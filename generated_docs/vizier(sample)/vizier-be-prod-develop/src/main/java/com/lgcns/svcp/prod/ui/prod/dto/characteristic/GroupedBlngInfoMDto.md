# 📄 GroupedBlngInfoMDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/characteristic/GroupedBlngInfoMDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`GroupedBlngInfoMDto`](#class-groupedblnginfomdto) - 복잡도: 38

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `lombok.Data` | ⚡ **총 복잡도**: 38 |
| 📊 **총 토큰 수**: 225 |  |



## 🏗️ 클래스

### <a id="class-groupedblnginfomdto"></a>🎯 `GroupedBlngInfoMDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 38 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 38 || 📍 **라인 범위** | 6-6 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class GroupedBlngInfoMDto {
	public GroupedBlngInfoMDto(BlngInfoMDto blngInfoMDto) {
        this.generalDetails = new GeneralDetailFields(blngInfoMDto);
        this.additionalParams = new AdditionalParamFields(blngInfoMDto);
    }
	
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String blngInfoCd;
		private String blngInfoNm;
		private String rtmRtngTrgtDivsCd;
		private String prodBillMthdCd;
		private String atstNtagYn;

		public GeneralDetailFields (BlngInfoMDto blngInfoMDto) {
			this.type = blngInfoMDto.getType();
			this.blngInfoCd = blngInfoMDto.getBlngInfoCd();
			this.blngInfoNm = blngInfoMDto.getBlngInfoNm();
			this.rtmRtngTrgtDivsCd = blngInfoMDto.getRtmRtngTrgtDivsCd();
			this.prodBillM...
```

**Chunk 정보**
- 🆔 **ID**: `fbe42c24425d`
- 📍 **라인**: 6-6
- 📊 **토큰**: 110
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **225개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 115 | 51.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 38.0 | 110 | 48.9% |

