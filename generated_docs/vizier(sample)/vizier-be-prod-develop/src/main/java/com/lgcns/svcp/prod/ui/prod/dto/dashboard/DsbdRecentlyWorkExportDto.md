# 📄 DsbdRecentlyWorkExportDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/dashboard/DsbdRecentlyWorkExportDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`DsbdRecentlyWorkExportDto`](#class-dsbdrecentlyworkexportdto) - 복잡도: 18 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.util.excel.annotation.CustomTitleHeader` • `com.lgcns.svcp.prod.util.excel.annotation.Value` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 18 |
| 📊 **총 토큰 수**: 103 |  |



## 🏗️ 클래스

### <a id="class-dsbdrecentlyworkexportdto"></a>🎯 `DsbdRecentlyWorkExportDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 18 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 18 || 📍 **라인 범위** | 12-12 |
| 🏷️ **태그** | `class, java, value, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class DsbdRecentlyWorkExportDto {
	
	@Value(name ="dashboard.recentlywork.excel.category")
	private String category;
	
	@Value(name ="dashboard.recentlywork.excel.type")
	private String type;
	
	@Value(name ="dashboard.recentlywork.excel.objname")
	private String objName;
	
	@Value(name ="dashboard.recentlywork.excel.objcode")
	private String objCode;
	
	@Value(name ="dashboard.recentlywork.excel.worktypename")
	private String workTypeName;
	
	@Value(name ="dashboard.recentlywork.excel.responsibledept")
	private String responsibleDept;
	
	@Value(name ="dashboard.recentlywork.excel.responsibleuser")
	private String responsibleUser;
	
	@Value(name ="dashboard.recentlywork.excel.workdate")
	private String workDate;
}...
```

**Chunk 정보**
- 🆔 **ID**: `e5ee3a4f0775`
- 📍 **라인**: 12-12
- 📊 **토큰**: 45
- 🏷️ **태그**: `class, java, value, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **103개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 58 | 56.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 18.0 | 45 | 43.7% |

