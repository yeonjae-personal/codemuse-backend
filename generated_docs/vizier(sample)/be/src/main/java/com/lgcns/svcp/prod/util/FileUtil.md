# 📄 FileUtil.java

> **파일 경로**: `vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/util/FileUtil.java`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`FileUtil`](#class-fileutil) - 복잡도: 51

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.io.ByteArrayInputStream` • `java.io.IOException` • `java.io.InputStream` • `java.util.regex.Pattern` • `org.apache.commons.codec.binary.Base64` • `org.apache.commons.lang3.StringUtils` 외 2개 | ⚡ **총 복잡도**: 51 |
| 📊 **총 토큰 수**: 339 |  |



## 🏗️ 클래스

### <a id="class-fileutil"></a>🎯 `FileUtil`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 51 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 51 || 📍 **라인 범위** | 15-15 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class FileUtil {

	public static String formatFileName(String name) {
		name = name.replaceAll("\\s", "-");
		name = name.toLowerCase();
		return name;
	}

	public static boolean checkFileFormat(String patternStr, String fileName) {
		if (StringUtils.isNotBlank(fileName)) {
			Pattern pattern = Pattern.compile(patternStr);
			return PatternUtil.matches(pattern, fileName);
		}
		return false;
	}

	public static boolean checkFileSize(int size, int sizeFile) {
		if (sizeFile > 0 && sizeFile <= size) {
			return true;
		}
		return false;
	}

	public static int getFileSize(InputStream inputStream) {
		try {
			return inputStream.available();
		} catch (IOException e) {
			log.error("Error: "+e.getMessage());
			return 0;
		}
	}

	public static byte[] getByteFromBase64(String base64) {
		...
```

**Chunk 정보**
- 🆔 **ID**: `98dbb76efa61`
- 📍 **라인**: 15-15
- 📊 **토큰**: 160
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **339개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 179 | 52.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 51.0 | 160 | 47.2% |

