# 📄 ItemVolumeGroupDto.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/dashboard/ItemVolumeGroupDto.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ItemVolumeGroupDto`](#class-itemvolumegroupdto) - 복잡도: 6

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.List` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 6 |
| 📊 **총 토큰 수**: 54 |  |



## 🏗️ 클래스

### <a id="class-itemvolumegroupdto"></a>🎯 `ItemVolumeGroupDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 6 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 6 || 📍 **라인 범위** | 11-11 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ItemVolumeGroupDto {
	
	private String name;
	private double ratio;
	private Integer total = 0;
	List<ItemVolumeDto> items = new ArrayList<>();
}...
```

**Chunk 정보**
- 🆔 **ID**: `984ab25a59e5`
- 📍 **라인**: 11-11
- 📊 **토큰**: 21
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **54개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 33 | 61.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 6.0 | 21 | 38.9% |

