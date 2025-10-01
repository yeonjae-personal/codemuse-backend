# 📄 GroupedDcntMDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/offer/GroupedDcntMDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`GroupedDcntMDto`](#class-groupeddcntmdto) - 복잡도: 46

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `lombok.Data` | ⚡ **총 복잡도**: 46 |
| 📊 **총 토큰 수**: 273 |  |



## 🏗️ 클래스

### <a id="class-groupeddcntmdto"></a>🎯 `GroupedDcntMDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 46 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 46 || 📍 **라인 범위** | 5-5 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class GroupedDcntMDto {
	public GroupedDcntMDto(DcntMDto dcntMDto) {
		this.generalDetails = new GeneralDetailFields(dcntMDto);
		this.additionalParams = new AdditionalParamFields(dcntMDto);
		this.overView = dcntMDto.getOverView();
		this.comment = dcntMDto.getComment();
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String dcntCd;
		private String dcntNm;
		private String prty;
		private String prodKdCd;
		private String dcntValdStrtDtm;
		private String dcntValdEndDtm;
		private String rgstUsr;
		private String rgstDtm;
		private String updUsr;
		private String updDtm;

		public GeneralDetailFields (DcntMDto dcntMDto) {
			this.type = dcntMDto.getType();
			this.dcntCd = dcntMDto.getDcntCd();
			...
```

**Chunk 정보**
- 🆔 **ID**: `ceeb078c759a`
- 📍 **라인**: 5-5
- 📊 **토큰**: 134
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **273개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 139 | 50.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 46.0 | 134 | 49.1% |

