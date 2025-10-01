package com.lgcns.svcp.prod.ui.prod.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.ui.prod.dto.notifications.NotificationDto;
import com.lgcns.svcp.prod.ui.prod.enums.YesNo;
import com.lgcns.svcp.prod.util.UuidUtil;

import lombok.RequiredArgsConstructor;

@Service("notificationService")
@RequiredArgsConstructor
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

	public List<NotificationDto> getAllNotifications(String userId) {
		return commonDao.selectList("Ui-notification.getAllNotifications", Collections.singletonMap("userId", userId));
	}

	public void markAsRead(String userNotiUuid) {
		Map<String, String> params = new HashMap<>();
		params.put("userNotiUuid", userNotiUuid);
		commonDao.update("Ui-notification.markAsRead", params);
	}
}