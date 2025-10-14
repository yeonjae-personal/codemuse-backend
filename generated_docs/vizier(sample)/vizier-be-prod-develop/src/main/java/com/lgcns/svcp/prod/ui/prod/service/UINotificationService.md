# 📄 UINotificationService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/UINotificationService.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UINotificationService`](#class-uinotificationservice) - 복잡도: 26

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.Collections` • `java.util.HashMap` • `java.util.List` • `java.util.Map` • `org.springframework.messaging.simp.SimpMessagingTemplate` • `org.springframework.stereotype.Service` 외 5개 | ⚡ **총 복잡도**: 26 |
| 📊 **총 토큰 수**: 174 |  |



## 🏗️ 클래스

### <a id="class-uinotificationservice"></a>🎯 `UINotificationService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 26 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 26 || 📍 **라인 범위** | 20-20 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UINotificationService {
	private final SimpMessagingTemplate template;
	private final CommonDao commonDao;

	public void sendNotification(String userId, String message, String notiType, String linkUrl, String imageUrl,
			String rgstUser) {
		NotificationDto notification = new NotificationDto();
		notification.setUserNotiUuid(UuidUtil.generateRandomUUID());
		notification.setUserId(userId);
		notification.setNotiMsgLabelId(message);
		notification.setNotiType(notiType);
		notification.setNotiReadYn(YesNo.N);
		notification.setNotiReadDtm(null);
		notification.setLinkUrl(linkUrl);
		notification.setImageUrl(imageUrl);

		commonDao.insert("Ui-notification.insertNotification", notification);
		template.convertAndSend("/topic/user/" + userId, notification);
	}

	public List<Notifi...
```

**Chunk 정보**
- 🆔 **ID**: `1b72e9ea650a`
- 📍 **라인**: 20-20
- 📊 **토큰**: 74
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **174개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 100 | 57.5% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 26.0 | 74 | 42.5% |

