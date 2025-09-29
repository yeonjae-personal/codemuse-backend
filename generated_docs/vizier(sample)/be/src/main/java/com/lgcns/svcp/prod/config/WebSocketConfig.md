# 📄 WebSocketConfig.java

> **파일 경로**: `vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/config/WebSocketConfig.java`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`WebSocketConfig`](#class-websocketconfig) - 복잡도: 11

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `org.springframework.context.annotation.Configuration` • `org.springframework.messaging.simp.config.MessageBrokerRegistry` • `org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker` • `org.springframework.web.socket.config.annotation.StompEndpointRegistry` • `org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer` • `com.lgcns.svcp.prod.filters.CORSFilter` | ⚡ **총 복잡도**: 11 |
| 📊 **총 토큰 수**: 66 |  |



## 🏗️ 클래스

### <a id="class-websocketconfig"></a>🎯 `WebSocketConfig`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 11 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 11 || 📍 **라인 범위** | 13-13 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws").setAllowedOrigins(CORSFilter.ALLOWED_ORIGINS.toArray(new String[0])).withSockJS();
	}
}...
```

**Chunk 정보**
- 🆔 **ID**: `86c7461f110f`
- 📍 **라인**: 13-13
- 📊 **토큰**: 25
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **66개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 41 | 62.1% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 11.0 | 25 | 37.9% |

