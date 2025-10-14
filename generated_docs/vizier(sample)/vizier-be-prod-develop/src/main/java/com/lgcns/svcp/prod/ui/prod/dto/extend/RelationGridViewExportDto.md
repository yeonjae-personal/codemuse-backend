# 📄 RelationGridViewExportDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/extend/RelationGridViewExportDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`RelationGridViewExportDto`](#class-relationgridviewexportdto) - 복잡도: 28 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.util.excel.annotation.CustomTitleHeader` • `com.lgcns.svcp.prod.util.excel.annotation.Value` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 28 |
| 📊 **총 토큰 수**: 153 |  |



## 🏗️ 클래스

### <a id="class-relationgridviewexportdto"></a>🎯 `RelationGridViewExportDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 28 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 28 || 📍 **라인 범위** | 12-12 |
| 🏷️ **태그** | `class, java, value, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class RelationGridViewExportDto {
	
	@Value(name ="relationmanager.excel.no")
	private Integer no;
	
	@Value(name ="relationmanager.excel.leadercode")
	private String leaderCode;
	
	@Value(name ="relationmanager.excel.leadername")
	private String leaderName;
	
	@Value(name ="relationmanager.excel.followercode")
	private String followerCode;
	
	@Value(name ="relationmanager.excel.followername")
	private String followerName;
	
	@Value(name ="relationmanager.excel.relationcode")
	private String relationCode;
	
	@Value(name ="relationmanager.excel.relationname")
	private String relationName;
	
	@Value(name ="relationmanager.excel.relationstartdate")
	private String relationStartDate;
	
	@Value(name ="relationmanager.excel.relationenddate")
	private String relationEndDate;
	
	@Value(name...
```

**Chunk 정보**
- 🆔 **ID**: `f532532a31c6`
- 📍 **라인**: 12-12
- 📊 **토큰**: 70
- 🏷️ **태그**: `class, java, value, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **153개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 83 | 54.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 28.0 | 70 | 45.8% |

