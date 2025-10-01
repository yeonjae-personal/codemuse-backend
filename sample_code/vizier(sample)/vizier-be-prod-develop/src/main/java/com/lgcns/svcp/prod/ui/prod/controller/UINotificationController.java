package com.lgcns.svcp.prod.ui.prod.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.ui.prod.dto.notifications.NotificationDto;
import com.lgcns.svcp.prod.ui.prod.service.UINotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "UI-notification-controller", description = "Notification Controller")
@RequiredArgsConstructor
@RestController
@RequestMapping("/notifications")
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
//				message.getLinkUrl(), message.getImageUrl(), null);
//	}
}