package com.lgcns.svcp.prod.online.prod.dto.component.service;

import java.util.List;

import com.lgcns.svcp.prod.online.prod.dto.common.AdditionalColumnsDto;

import lombok.Data;

@Data
public class MessageDto {
	private String msgSvcUuid;
	private String msgSvcCode;
	private String msgSvcName;
	private String dplcTrgtUuid;
	private String ovwCntn;
	private String validStartDtm;
	private String validEndDtm;
	private String rgstUser;
	private String rgstDtm;
	private String updUser;
	private String updDtm;
	private List<AdditionalColumnsDto> additionalColumns;
}
