# 📄 GroupedSlinInfoMDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/characteristic/GroupedSlinInfoMDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`GroupedSlinInfoMDto`](#class-groupedslininfomdto) - 복잡도: 28

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `lombok.Data` | ⚡ **총 복잡도**: 28 |
| 📊 **총 토큰 수**: 165 |  |



## 🏗️ 클래스

### <a id="class-groupedslininfomdto"></a>🎯 `GroupedSlinInfoMDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 28 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 28 || 📍 **라인 범위** | 6-6 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class GroupedSlinInfoMDto {
	public GroupedSlinInfoMDto(SlinInfoMDto slinInfoMDto) {
		this.generalDetails = new GeneralDetailFields(slinInfoMDto);
		this.additionalParams = new AdditionalParamFields(slinInfoMDto);
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String slinInfoCd;
		private String slinInfoNm;
		private String slinMgmtUnitCd;
		private String valdEndDtm;

		public GeneralDetailFields (SlinInfoMDto slinInfoMDto) {
			this.type = slinInfoMDto.getType();
			this.slinInfoCd = slinInfoMDto.getSlinInfoCd();
			this.slinInfoNm = slinInfoMDto.getSlinInfoNm();
			this.slinMgmtUnitCd = slinInfoMDto.getSlinMgmtUnitCd();
			this.valdEndDtm = slinInfoMDto.getValdEndDtm();
		}
	}


	private Additio...
```

**Chunk 정보**
- 🆔 **ID**: `55fd6bc138a4`
- 📍 **라인**: 6-6
- 📊 **토큰**: 80
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **165개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 85 | 51.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 28.0 | 80 | 48.5% |

