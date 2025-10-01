# 📄 CellBoxDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/customvalidation/CellBoxDto.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`CellBoxDto`](#class-cellboxdto) - 복잡도: 9

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.Date` • `com.lgcns.svcp.prod.entity.BaseEntity` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 9 |
| 📊 **총 토큰 수**: 68 |  |



## 🏗️ 클래스

### <a id="class-cellboxdto"></a>🎯 `CellBoxDto`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseEntity` |
| ⚡ 복잡도 | 9 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 9 || 📍 **라인 범위** | 12-12 |
| 🏗️ **상속** | `BaseEntity` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class CellBoxDto extends BaseEntity {
	
	private String validCode;
	private String condItemCode;
	private Integer seqNo;
	private String validCntn;
	private String validStartDtm;
	private String validEndDtm;
	private Date validEndDtmOrigin;
}...
```

**Chunk 정보**
- 🆔 **ID**: `9048cd32dbe1`
- 📍 **라인**: 12-12
- 📊 **토큰**: 28
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **68개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 40 | 58.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 9.0 | 28 | 41.2% |

