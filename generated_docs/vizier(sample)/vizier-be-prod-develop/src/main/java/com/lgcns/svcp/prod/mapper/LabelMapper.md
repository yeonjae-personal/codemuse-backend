# 📄 LabelMapper.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/mapper/LabelMapper.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`LabelMapper`](#interface-labelmapper)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `org.mapstruct.Mapper` • `com.lgcns.svcp.prod.entity.MultiLangLabelEntity` • `com.lgcns.svcp.prod.ui.prod.dto.label.MultiLangLabelDto` | ⚡ **총 복잡도**: 3 |
| 📊 **총 토큰 수**: 27 |  |




## 🔌 인터페이스

### <a id="interface-labelmapper"></a>🔌 `LabelMapper`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 9-9 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface LabelMapper {
	
	MultiLangLabelDto entityToDto(MultiLangLabelEntity entity);
	
}...
```

**Chunk 정보**
- 🆔 **ID**: `13006827fe9c`
- 📊 **토큰**: 8

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **27개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 19 | 70.4% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 3.0 | 8 | 29.6% |

