# 📄 UiUserImageService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/dashboard/UiUserImageService.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`UiUserImageService`](#interface-uiuserimageservice)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.dashboard.UserImageDto` • `com.lgcns.svcp.prod.ui.prod.dto.dashboard.request.UserImageSaveRequest` | ⚡ **총 복잡도**: 4 |
| 📊 **총 토큰 수**: 30 |  |




## 🔌 인터페이스

### <a id="interface-uiuserimageservice"></a>🔌 `UiUserImageService`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 8-8 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface UiUserImageService {
	List<UserImageDto> saveUserImage(UserImageSaveRequest request);
	List<UserImageDto> findUserImageByUser(String uuid);
}...
```

**Chunk 정보**
- 🆔 **ID**: `e68a17259dd6`
- 📊 **토큰**: 11

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **30개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 19 | 63.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 4.0 | 11 | 36.7% |

