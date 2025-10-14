# 📄 UiUserImageServiceImpl.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/dashboard/impl/UiUserImageServiceImpl.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiUserImageServiceImpl`](#class-uiuserimageserviceimpl) - 복잡도: 100 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.io.InputStream` • `java.util.HashMap` • `java.util.List` • `java.util.Map` • `org.apache.commons.lang3.StringUtils` • `org.springframework.context.MessageSource` 외 16개 | ⚡ **총 복잡도**: 100 |
| 📊 **총 토큰 수**: 690 |  |



## 🏗️ 클래스

### <a id="class-uiuserimageserviceimpl"></a>🎯 `UiUserImageServiceImpl`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 100 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 100 || 📍 **라인 범위** | 31-31 |
| 🏷️ **태그** | `class, java, transactional, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiUserImageServiceImpl implements UiUserImageService {
	
	private final CommonDao commonDao;
	private final S3TemplateUtil s3Template;
	private final UserImageMapper userImageMapper;
	private final MessageSource messageSource;
	
	@Override
	@Transactional
	public List<UserImageDto> saveUserImage(UserImageSaveRequest request) {
		String userId = UserContext.getCurrentUser();
		String uuid = request.getDsbdViewUuid();
		for (UserImageRequest item: request.getRequests()) {
			if (StringUtils.isNotBlank(item.getImageBase64()) && StringUtils.isNotBlank(item.getImageName()) 
											&& item.getImageSeq() != null) {
				InputStream inputStream = FileUtil.getInputStreamFromBase64(item.getImageBase64());
				String path = buildImagePath(item.getImageName());
				Map<String, Object> ...
```

**Chunk 정보**
- 🆔 **ID**: `7810969e193a`
- 📍 **라인**: 31-31
- 📊 **토큰**: 321
- 🏷️ **태그**: `class, java, transactional, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **690개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 369 | 53.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 100.0 | 321 | 46.5% |

