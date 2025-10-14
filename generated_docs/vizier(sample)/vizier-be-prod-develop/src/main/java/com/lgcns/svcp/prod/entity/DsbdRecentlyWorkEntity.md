# 📄 DsbdRecentlyWorkEntity.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/entity/DsbdRecentlyWorkEntity.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`DsbdRecentlyWorkEntity`](#class-dsbdrecentlyworkentity) - 복잡도: 13

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.Date` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 13 |
| 📊 **총 토큰 수**: 90 |  |



## 🏗️ 클래스

### <a id="class-dsbdrecentlyworkentity"></a>🎯 `DsbdRecentlyWorkEntity`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseEntity` |
| ⚡ 복잡도 | 13 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 13 || 📍 **라인 범위** | 10-10 |
| 🏗️ **상속** | `BaseEntity` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class DsbdRecentlyWorkEntity extends BaseEntity {
	
	private String workNo;
	private String objUuid;
	private String lctgrItemName;
	private String itemCode;
	private String objCode;
	private String objName;
	private String workType;
	private String updUserDeptName;
	private String attrValUpdUser;
	private Date objWorkDtm;
	private Date batchRunDtm;
}...
```

**Chunk 정보**
- 🆔 **ID**: `5d13db0f8692`
- 📍 **라인**: 10-10
- 📊 **토큰**: 40
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **90개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 50 | 55.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 13.0 | 40 | 44.4% |

