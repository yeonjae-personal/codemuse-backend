# 📄 ComponentAddResourceReq.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/component/addResource/ComponentAddResourceReq.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---



## 💼 비즈니스 로직

### `ComponentAddResourceReq`
- **도메인**: user
- **목적**: 비즈니스 로직 처리
- **복잡도**: 9
- **관련 파일**: /Users/roseline/projects/codemuse-backend/sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/component/addResource/*
- **라인**: 10


## 📑 목차

### 🏗️ 클래스
- [`ComponentAddResourceReq`](#class-componentaddresourcereq) - 복잡도: 9

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.util.paging.BasePaginationDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 9 |
| 📊 **총 토큰 수**: 66 |  |



## 🏗️ 클래스

### <a id="class-componentaddresourcereq"></a>🎯 `ComponentAddResourceReq`

![상속](https://img.shields.io/badge/상속-1개-blue)

| 속성 | 값 |
|------|----|
| 🧬 상속 | `BasePaginationDto` |
| ⚡ 복잡도 | 9 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 9 || 📍 **라인 범위** | 10-10 |
| 🏗️ **상속** | `BasePaginationDto` || 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ComponentAddResourceReq extends BasePaginationDto {
	private String componentUUID;
	private String componentType;
	private String componentSubType;
	private String componentCreateType;
	private String name;
	private String code;
	private String itemCode;
}...
```

**Chunk 정보**
- 🆔 **ID**: `23f49dbaec3f`
- 📍 **라인**: 10-10
- 📊 **토큰**: 28
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **66개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 38 | 57.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 9.0 | 28 | 42.4% |

