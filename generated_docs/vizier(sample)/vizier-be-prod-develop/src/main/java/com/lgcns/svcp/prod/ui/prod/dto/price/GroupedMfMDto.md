# 📄 GroupedMfMDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/price/GroupedMfMDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`GroupedMfMDto`](#class-groupedmfmdto) - 복잡도: 54

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `lombok.Data` | ⚡ **총 복잡도**: 54 |
| 📊 **총 토큰 수**: 321 |  |



## 🏗️ 클래스

### <a id="class-groupedmfmdto"></a>🎯 `GroupedMfMDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 54 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 54 || 📍 **라인 범위** | 6-6 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class GroupedMfMDto {

	public GroupedMfMDto(MfMDto mfMDto) {
		this.generalDetails = new GeneralDetailFields(mfMDto);
		this.additionalParams = new AdditionalParamFields(mfMDto);
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String basfCd;
		private String basfNm;
		private String ratDivsCd;
		private String basf;
		private String basfAplyCyvl;
		private String basfAplyCyclCd;
		private String daCalcDivsCd;
		private String ratAplyKdCd;
		private String ppayPopyDivsCd;
		private String valdStrtDtm;
		private String valdEndDtm;

		public GeneralDetailFields (MfMDto mfMDto) {
			this.type = mfMDto.getType();
			this.basfCd = mfMDto.getBasfCd();
			this.basfNm = mfMDto.getBasfNm();
			this.ratDivsCd ...
```

**Chunk 정보**
- 🆔 **ID**: `f54b76fe5e51`
- 📍 **라인**: 6-6
- 📊 **토큰**: 158
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **321개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 163 | 50.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 54.0 | 158 | 49.2% |

