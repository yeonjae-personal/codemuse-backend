package com.lgcns.svcp.prod.online.prod.dto.group;

import lombok.Data;

@Data
public class OfferGroupWithOfferDto {
	private String offerGroupUuid;
	private String offerGroupCode;
	private String offerGroupName;
	private String offerUuid;
	private String offerCode;
	private String offerName;
	private String validStartDtm;
	private String validEndDtm;
	private String rgstUser;
	private String rgstDtm;
	private String updUser;
	private String updDtm;
}
