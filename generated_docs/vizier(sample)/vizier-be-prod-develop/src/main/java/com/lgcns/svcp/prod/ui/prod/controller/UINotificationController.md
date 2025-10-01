# 📄 UINotificationController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/UINotificationController.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UINotificationController`](#class-uinotificationcontroller) - 복잡도: 13 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.PathVariable` • `org.springframework.web.bind.annotation.PutMapping` • `org.springframework.web.bind.annotation.RequestMapping` • `org.springframework.web.bind.annotation.RequestParam` 외 6개 | ⚡ **총 복잡도**: 13 |
| 📊 **총 토큰 수**: 174 |  |



## 🏗️ 클래스

### <a id="class-uinotificationcontroller"></a>🎯 `UINotificationController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 13 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 13 || 📍 **라인 범위** | 23-23 |
| 🏷️ **태그** | `class, java, getmapping, putmapping, pathvariable, requestparam, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UINotificationController {
	private final UINotificationService uiNotificationService;

	@GetMapping()
	@Operation(summary = "Get all notifications", description = "Get all notifications of User")
	public List<NotificationDto> getAllNotifications(@RequestParam String userId) {
		return uiNotificationService.getAllNotifications(userId);
	}

	@PutMapping("/read/{userNotiUuid}")
	@Operation(summary = "Mark as read", description = "Mark as read")
	public void markAsRead(@PathVariable String userNotiUuid) {
		uiNotificationService.markAsRead(userNotiUuid);
	}

//	@MessageMapping("/sendNotification")
//	public void handleNotification(@Payload NotificationDto message) {
//		notificationService.sendNotification(message.getUserId(), message.getNotiMsgLabelId(), message.getNotiType(),
/...
```

**Chunk 정보**
- 🆔 **ID**: `52d2b86d2770`
- 📍 **라인**: 23-23
- 📊 **토큰**: 69
- 🏷️ **태그**: `class, java, getmapping, putmapping, pathvariable...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **174개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 105 | 60.3% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 13.0 | 69 | 39.7% |

