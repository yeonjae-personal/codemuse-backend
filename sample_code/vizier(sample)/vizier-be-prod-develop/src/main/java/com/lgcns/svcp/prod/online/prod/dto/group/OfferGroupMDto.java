package com.lgcns.svcp.prod.online.prod.dto.group;

import lombok.Data;

@Data
public class OfferGroupMDto {
	private String objUuid;
	private String objCode;
	private String objName;
	private String offerGroupTypeCode;
	private String prpsCntn;
	private String offerGroupOvwCntn;
	private String rqstDeptName;
	private String rqstUser;
	private String dplcTrgtUuid;
	private String chgDeptName;
	private String chgUser;
	private String rgstUser;
	private String rgstDtm;
	private String updUser;
	private String updDtm;
}
