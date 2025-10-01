# 📄 GroupedPdspTossLkgeDDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/price/GroupedPdspTossLkgeDDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`GroupedPdspTossLkgeDDto`](#class-groupedpdsptosslkgeddto) - 복잡도: 34

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `lombok.Data` | ⚡ **총 복잡도**: 34 |
| 📊 **총 토큰 수**: 201 |  |



## 🏗️ 클래스

### <a id="class-groupedpdsptosslkgeddto"></a>🎯 `GroupedPdspTossLkgeDDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 34 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 34 || 📍 **라인 범위** | 6-6 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class GroupedPdspTossLkgeDDto {
	public GroupedPdspTossLkgeDDto(PdspTossLkgeDDto pdspTossLkgeDDto) {
		this.generalDetails = new GeneralDetailFields(pdspTossLkgeDDto);
		this.additionalParams = new AdditionalParamFields(pdspTossLkgeDDto);
	}
	private GeneralDetailFields generalDetails;

	@Data
	public static class GeneralDetailFields {
		private String type;
		private String pdspNm;
		private String pdspCd;
		private String valdEndDtm;

		public GeneralDetailFields (PdspTossLkgeDDto pdspTossLkgeDDto) {
			this.type = pdspTossLkgeDDto.getType();
			this.pdspNm = pdspTossLkgeDDto.getPdspNm();
			this.pdspCd = pdspTossLkgeDDto.getPdspCd();
			this.valdEndDtm = pdspTossLkgeDDto.getValdEndDtm();
		}
	}

	private AdditionalParamFields additionalParams;

	@Data
	public static class Additio...
```

**Chunk 정보**
- 🆔 **ID**: `749d346f4d12`
- 📍 **라인**: 6-6
- 📊 **토큰**: 98
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **201개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 103 | 51.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 34.0 | 98 | 48.8% |

