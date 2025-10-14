# 📄 UiUserPocketService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/UiUserPocketService.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`UiUserPocketService`](#interface-uiuserpocketservice)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.userpocket.UserPocketRequest` • `com.lgcns.svcp.prod.ui.prod.dto.userpocket.UserPocketRespone` | ⚡ **총 복잡도**: 5 |
| 📊 **총 토큰 수**: 44 |  |




## 🔌 인터페이스

### <a id="interface-uiuserpocketservice"></a>🔌 `UiUserPocketService`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 8-8 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface UiUserPocketService {
	List<UserPocketRespone> getUserPocket(String userId);
	void save(UserPocketRequest request, String userId);
	void delete(String uuid, String userId);
}...
```

**Chunk 정보**
- 🆔 **ID**: `0ed8710b097b`
- 📊 **토큰**: 18

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **44개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 26 | 59.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 5.0 | 18 | 40.9% |

