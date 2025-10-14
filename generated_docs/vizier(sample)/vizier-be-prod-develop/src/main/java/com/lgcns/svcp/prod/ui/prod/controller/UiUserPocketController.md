# 📄 UiUserPocketController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/UiUserPocketController.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiUserPocketController`](#class-uiuserpocketcontroller) - 복잡도: 21 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.springframework.http.HttpStatus` • `org.springframework.http.ResponseEntity` • `org.springframework.validation.annotation.Validated` • `org.springframework.web.bind.annotation.CrossOrigin` • `org.springframework.web.bind.annotation.DeleteMapping` 외 15개 | ⚡ **총 복잡도**: 21 |
| 📊 **총 토큰 수**: 222 |  |



## 🏗️ 클래스

### <a id="class-uiuserpocketcontroller"></a>🎯 `UiUserPocketController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 21 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 21 || 📍 **라인 범위** | 35-35 |
| 🏷️ **태그** | `class, java, getmapping, postmapping, deletemapping, requestparam, requestbody, valid, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiUserPocketController {

	private final UiUserPocketService userPocketService;
	
	@GetMapping
	@Operation(summary = "show list user pocket", description = "show list user pocket")
	public List<UserPocketRespone> getUserPocket() {
		String userId = UserContext.getCurrentUser();
		return userPocketService.getUserPocket(userId);
	}
	
	@PostMapping
	@Operation(summary = "drag user pocket", description = "drag user pocket")
	public void saveUserPocket(@Valid @RequestBody UserPocketRequest request) {
		String userId = UserContext.getCurrentUser();
		userPocketService.save(request, userId);
	}
	
	@DeleteMapping
	@Operation(summary = "delete user pocket", description = "delete user pocket")
	public void deleteUserPocket(@RequestParam String uuid) {
		String userId = UserContext.getCu...
```

**Chunk 정보**
- 🆔 **ID**: `2c63c0b86867`
- 📍 **라인**: 35-35
- 📊 **토큰**: 82
- 🏷️ **태그**: `class, java, getmapping, postmapping, deletemapping...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **222개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 140 | 63.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 21.0 | 82 | 36.9% |

