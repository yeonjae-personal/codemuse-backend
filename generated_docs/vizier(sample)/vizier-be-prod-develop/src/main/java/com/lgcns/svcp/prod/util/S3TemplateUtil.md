# 📄 S3TemplateUtil.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/util/S3TemplateUtil.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`S3TemplateUtil`](#class-s3templateutil) - 복잡도: 22 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.io.InputStream` • `org.springframework.beans.factory.annotation.Value` • `org.springframework.stereotype.Component` • `io.awspring.cloud.s3.S3Resource` • `io.awspring.cloud.s3.S3Template` | ⚡ **총 복잡도**: 22 |
| 📊 **총 토큰 수**: 139 |  |



## 🏗️ 클래스

### <a id="class-s3templateutil"></a>🎯 `S3TemplateUtil`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 22 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 22 || 📍 **라인 범위** | 12-12 |
| 🏷️ **태그** | `class, java, value, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class S3TemplateUtil {
	
	@Value("${spring.cloud.aws.region.bucket-name}")
    private String bucketName;
	
	@Value("${spring.cloud.aws.region.object-url}")
    private String objectUrl;
	
	private final S3Template s3Template;
	
	public S3TemplateUtil(S3Template s3Template) {
        this.s3Template = s3Template;
    }
	
	public void createObject(String path, InputStream inputStream) {
		s3Template.upload(bucketName, path, inputStream);
	}
	
	public void deleteObject(String path) {
		s3Template.deleteObject(bucketName, path);
	}
	
	public S3Resource getObject(String path) {
		return s3Template.download(bucketName, path);
	}
	
	public String getObjectUrl(String path) {
		return objectUrl + path;
	}
}...
```

**Chunk 정보**
- 🆔 **ID**: `dc0d152cdc67`
- 📍 **라인**: 12-12
- 📊 **토큰**: 63
- 🏷️ **태그**: `class, java, value, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **139개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 76 | 54.7% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 22.0 | 63 | 45.3% |

