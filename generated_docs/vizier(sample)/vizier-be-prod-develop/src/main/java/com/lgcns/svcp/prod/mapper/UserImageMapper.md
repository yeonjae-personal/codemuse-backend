# 📄 UserImageMapper.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/mapper/UserImageMapper.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UserImageMapper`](#class-userimagemapper) - 복잡도: 11

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.nio.file.Paths` • `org.springframework.stereotype.Component` • `com.lgcns.svcp.prod.entity.DsbdUserSetEntity` • `com.lgcns.svcp.prod.ui.prod.dto.dashboard.UserImageDto` • `com.lgcns.svcp.prod.util.S3TemplateUtil` • `lombok.AllArgsConstructor` | ⚡ **총 복잡도**: 11 |
| 📊 **총 토큰 수**: 68 |  |



## 🏗️ 클래스

### <a id="class-userimagemapper"></a>🎯 `UserImageMapper`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 11 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 11 || 📍 **라인 범위** | 15-15 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UserImageMapper {
	
	private final S3TemplateUtil s3Template;
	
	public UserImageDto entityToDto(DsbdUserSetEntity entity) {
		UserImageDto result = new UserImageDto();
		result.setDsbdViewUuid(entity.getDsbdViewUuid());
		result.setImageSeq(entity.getSeqNo());
		result.setImagePath(s3Template.getObjectUrl(entity.getAttrVal()));
		result.setImageName(Paths.get(entity.getAttrVal()).getFileName().toString());
		return result;
	}
}...
```

**Chunk 정보**
- 🆔 **ID**: `85e61451826c`
- 📍 **라인**: 15-15
- 📊 **토큰**: 26
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **68개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 42 | 61.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 11.0 | 26 | 38.2% |

