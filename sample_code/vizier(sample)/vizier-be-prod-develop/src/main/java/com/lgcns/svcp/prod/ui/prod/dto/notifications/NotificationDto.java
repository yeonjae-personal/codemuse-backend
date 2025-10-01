package com.lgcns.svcp.prod.ui.prod.dto.notifications;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;
import com.lgcns.svcp.prod.ui.prod.enums.YesNo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDto extends BaseDto {
	private String userNotiUuid;
	private String userId;
	private String notiMsgLabelId;
	private String notiType;
	private YesNo notiReadYn;
	private String notiReadDtm;
	private String linkUrl;
	private String imageUrl;
}