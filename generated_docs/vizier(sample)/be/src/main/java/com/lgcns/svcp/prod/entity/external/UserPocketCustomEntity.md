# 📄 UserPocketCustomEntity.java

> **파일 경로**: `vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/entity/external/UserPocketCustomEntity.java`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UserPocketCustomEntity`](#class-userpocketcustomentity) - 복잡도: 13

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.Date` • `com.lgcns.svcp.prod.entity.BaseEntity` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 13 |
| 📊 **총 토큰 수**: 92 |  |



## 🏗️ 클래스

### <a id="class-userpocketcustomentity"></a>🎯 `UserPocketCustomEntity`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BaseEntity` |
| ⚡ 복잡도 | 13 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 13 || 📍 **라인 범위** | 12-12 |
| 🏗️ **상속** | `BaseEntity` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UserPocketCustomEntity extends BaseEntity {
	
	private String objUuid;
	private String objCode;
	private String objName;
	private String itemCode;
	private String lctgrItemCode;
	private String lctgrItemName;
	private String mctgrItemCode;
	private Date itemValidStartDtm;
	private Date itemValidEndDtm;
	private String offerGroupTypeCode;
	private String subType;
}...
```

**Chunk 정보**
- 🆔 **ID**: `3cba59bac434`
- 📍 **라인**: 12-12
- 📊 **토큰**: 40
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **92개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 52 | 56.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 13.0 | 40 | 43.5% |

