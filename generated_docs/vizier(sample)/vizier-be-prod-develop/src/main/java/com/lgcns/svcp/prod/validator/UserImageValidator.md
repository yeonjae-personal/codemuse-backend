# 📄 UserImageValidator.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/validator/UserImageValidator.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UserImageValidator`](#class-userimagevalidator) - 복잡도: 18

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.io.InputStream` • `org.apache.commons.lang3.StringUtils` • `com.lgcns.svcp.prod.constant.DashboardConstant` • `com.lgcns.svcp.prod.util.FileUtil` • `com.lgcns.svcp.prod.validator.annotation.UserImageValidate` • `jakarta.validation.ConstraintValidator` 외 1개 | ⚡ **총 복잡도**: 18 |
| 📊 **총 토큰 수**: 122 |  |



## 🏗️ 클래스

### <a id="class-userimagevalidator"></a>🎯 `UserImageValidator`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 18 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 18 || 📍 **라인 범위** | 14-14 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UserImageValidator implements ConstraintValidator<UserImageValidate, String> {
	
	private static String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpeg|jpg|png|gif|bmp))$)";

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return false;
		}
		if (StringUtils.isNotEmpty(value)) {
			InputStream inputStream = FileUtil.getInputStreamFromBase64(value.split(",")[1]);
			if (FileUtil.checkFileFormat(IMAGE_PATTERN, ".."+FileUtil.getImageType(inputStream)) &&
					FileUtil.checkFileSize(DashboardConstant.USER_IMAGE_MAX_FILE_SIZE, FileUtil.getFileSize(inputStream))) {
				return true;
			}
			return false;
		}
		return true;
	}
}...
```

**Chunk 정보**
- 🆔 **ID**: `8039f0e861da`
- 📍 **라인**: 14-14
- 📊 **토큰**: 53
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **122개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 69 | 56.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 18.0 | 53 | 43.4% |

