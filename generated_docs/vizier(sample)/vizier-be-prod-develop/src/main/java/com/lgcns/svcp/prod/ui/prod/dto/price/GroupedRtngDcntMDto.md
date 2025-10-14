# 📄 GroupedRtngDcntMDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/price/GroupedRtngDcntMDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`GroupedRtngDcntMDto`](#class-groupedrtngdcntmdto) - 복잡도: 40

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `lombok.Data` | ⚡ **총 복잡도**: 40 |
| 📊 **총 토큰 수**: 237 |  |



## 🏗️ 클래스

### <a id="class-groupedrtngdcntmdto"></a>🎯 `GroupedRtngDcntMDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 40 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 40 || 📍 **라인 범위** | 6-6 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class GroupedRtngDcntMDto {
	public GroupedRtngDcntMDto(RtngDcntMDto rtngDcntMDto) {
		this.generalDetails = new GeneralDetailFields(rtngDcntMDto);
		this.additionalParams = new AdditionalParamFields(rtngDcntMDto);
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String rtngDcntCd;
		private String rtngDcntNm;
		private String ratDivsCd;
		private String rtngDcntRatAplyUval;
		private String rtngDcntRat;
		private String prty;
		private String valdStrtDtm;
		private String valdEndDtm;

		public GeneralDetailFields (RtngDcntMDto rtngDcntMDto) {
			this.type = rtngDcntMDto.getType();
			this.rtngDcntCd = rtngDcntMDto.getRtngDcntCd();
			this.rtngDcntNm = rtngDcntMDto.getRtngDcntNm();
			this.ratDivsCd =...
```

**Chunk 정보**
- 🆔 **ID**: `e09fda950d87`
- 📍 **라인**: 6-6
- 📊 **토큰**: 116
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **237개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 121 | 51.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 40.0 | 116 | 48.9% |

