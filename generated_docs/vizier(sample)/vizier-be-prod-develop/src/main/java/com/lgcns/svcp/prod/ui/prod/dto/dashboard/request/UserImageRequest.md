# 📄 UserImageRequest.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/dashboard/request/UserImageRequest.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UserImageRequest`](#class-userimagerequest) - 복잡도: 15

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `org.apache.commons.lang3.StringUtils` • `com.lgcns.svcp.prod.validator.annotation.UserImageValidate` • `jakarta.validation.constraints.NotNull` • `lombok.Getter` • `lombok.Setter` | ⚡ **총 복잡도**: 15 |
| 📊 **총 토큰 수**: 106 |  |



## 🏗️ 클래스

### <a id="class-userimagerequest"></a>🎯 `UserImageRequest`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 15 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 15 || 📍 **라인 범위** | 13-13 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UserImageRequest {
	
	@NotNull(message = "Field is not null")
	@UserImageValidate
	private String imageBase64;
	
	@NotNull(message = "Field is not null")
	private String imageName;
	
	@NotNull(message = "Field is not null")
	private Integer imageSeq;
	
	public String getImageBase64() {
		if (StringUtils.isNotBlank(imageBase64)) {
            return imageBase64.split(",")[1];
        }
        return imageBase64;
	}
}...
```

**Chunk 정보**
- 🆔 **ID**: `d3a3c1184050`
- 📍 **라인**: 13-13
- 📊 **토큰**: 46
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **106개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 60 | 56.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 15.0 | 46 | 43.4% |

