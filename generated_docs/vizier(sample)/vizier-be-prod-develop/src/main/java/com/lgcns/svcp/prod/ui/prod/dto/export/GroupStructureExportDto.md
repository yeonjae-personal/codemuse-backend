# 📄 GroupStructureExportDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/export/GroupStructureExportDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`GroupStructureExportDto`](#class-groupstructureexportdto) - 복잡도: 16 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.util.excel.annotation.CustomTitleHeader` • `com.lgcns.svcp.prod.util.excel.annotation.Value` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 16 |
| 📊 **총 토큰 수**: 93 |  |



## 🏗️ 클래스

### <a id="class-groupstructureexportdto"></a>🎯 `GroupStructureExportDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 16 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 16 || 📍 **라인 범위** | 12-12 |
| 🏷️ **태그** | `class, java, value, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class GroupStructureExportDto {
	
	@Value(name ="group.export.no")
	private Integer no;
	
	@Value(name ="group.export.code")
	private String groupCode;
	
	@Value(name ="group.export.name")
	private String groupName;

	@Value(name ="group.export.offercode")
	private String offerCode;
	
	@Value(name ="group.export.offername")
	private String offerName;
	
	@Value(name ="group.export.startdate")
	private String startDate;
	
	@Value(name ="group.export.finishdate")
	private String finishDate;
}...
```

**Chunk 정보**
- 🆔 **ID**: `cef4f2d1ae2a`
- 📍 **라인**: 12-12
- 📊 **토큰**: 40
- 🏷️ **태그**: `class, java, value, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **93개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 53 | 57.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 16.0 | 40 | 43.0% |

