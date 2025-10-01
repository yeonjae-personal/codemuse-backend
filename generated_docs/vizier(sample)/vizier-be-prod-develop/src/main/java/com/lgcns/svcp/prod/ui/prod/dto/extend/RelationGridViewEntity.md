# 📄 RelationGridViewEntity.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/extend/RelationGridViewEntity.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---


## 🗄️ 데이터베이스 엔티티

### `RelationGridViewEntity`
- **테이블**: `relation_grid_view_entity`
- **주요 필드**: 
- **관계**: 
- **라인**: 8


## 💼 비즈니스 로직

### `RelationGridViewEntity`
- **도메인**: user
- **목적**: RelationGridViewEntity 데이터 모델 정의
- **복잡도**: 19
- **관련 파일**: /Users/roseline/projects/codemuse-backend/sample_code/vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/extend/*
- **라인**: 8


## 📑 목차

### 🏗️ 클래스
- [`RelationGridViewEntity`](#class-relationgridviewentity) - 복잡도: 19

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 19 |
| 📊 **총 토큰 수**: 120 |  |



## 🏗️ 클래스

### <a id="class-relationgridviewentity"></a>🎯 `RelationGridViewEntity`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 19 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 19 || 📍 **라인 범위** | 8-8 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class RelationGridViewEntity {
	
	private Integer no;
	private String leaderName;
	private String leaderCode;
	private String followerName;
	private String followerCode;
	private String relationName;
	private String relationCode;
	private String relationStartDate;
	private String relationEndDate;
	private String groupName;
	private String groupCode;
	private String groupStartDate;
	private String groupEndDate;
	private String followerCodeGroup;
	private String followerNameGroup;
	private String largeItemCode;
	private String offerGroupUuid;
}...
```

**Chunk 정보**
- 🆔 **ID**: `8b4296dfc206`
- 📍 **라인**: 8-8
- 📊 **토큰**: 56
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **120개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 64 | 53.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 19.0 | 56 | 46.7% |

