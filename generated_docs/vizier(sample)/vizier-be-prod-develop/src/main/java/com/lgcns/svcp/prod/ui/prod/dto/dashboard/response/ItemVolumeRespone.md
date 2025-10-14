# 📄 ItemVolumeRespone.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/dashboard/response/ItemVolumeRespone.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`ItemVolumeRespone`](#class-itemvolumerespone) - 복잡도: 10

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.dashboard.ItemVolumeGroupDto` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 10 |
| 📊 **총 토큰 수**: 76 |  |



## 🏗️ 클래스

### <a id="class-itemvolumerespone"></a>🎯 `ItemVolumeRespone`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 10 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 10 || 📍 **라인 범위** | 13-13 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class ItemVolumeRespone {
	
	public ItemVolumeRespone() {
	}
	
	public ItemVolumeRespone(int total, List<ItemVolumeGroupDto> data) {
		this.total = total;
		this.data = data;
	}
	
	private int total;
	private List<ItemVolumeGroupDto> data = new ArrayList<>(); 
}...
```

**Chunk 정보**
- 🆔 **ID**: `c13e80d03e08`
- 📍 **라인**: 13-13
- 📊 **토큰**: 31
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **76개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 45 | 59.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 10.0 | 31 | 40.8% |

