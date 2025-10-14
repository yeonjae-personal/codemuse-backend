# 📄 FileUploadController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/FileUploadController.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`FileUploadController`](#class-fileuploadcontroller) - 복잡도: 7 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.springframework.http.MediaType` • `org.springframework.web.bind.annotation.PostMapping` • `org.springframework.web.bind.annotation.RequestMapping` • `org.springframework.web.bind.annotation.RestController` • `com.lgcns.svcp.prod.ui.prod.dto.admin.matrix.MatrixMeasureMDto` 외 5개 | ⚡ **총 복잡도**: 7 |
| 📊 **총 토큰 수**: 82 |  |



## 🏗️ 클래스

### <a id="class-fileuploadcontroller"></a>🎯 `FileUploadController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 7 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 7 || 📍 **라인 범위** | 23-23 |
| 🏷️ **태그** | `class, java, postmapping, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class FileUploadController {
    private final UiMatrixService uiMatrixService;

    @PostMapping(path = "matrix-management/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<MatrixMeasureMDto> importMatrix(HttpServletRequest request) {
        return uiMatrixService.importMatrix(request);
    }

}...
```

**Chunk 정보**
- 🆔 **ID**: `f8643accfc36`
- 📍 **라인**: 23-23
- 📊 **토큰**: 23
- 🏷️ **태그**: `class, java, postmapping, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **82개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 59 | 72.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 7.0 | 23 | 28.0% |

