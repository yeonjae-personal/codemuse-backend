package com.lgcns.svcp.prod.online.prod.dto.resource;

import java.util.List;

import com.lgcns.svcp.prod.online.prod.dto.common.AdditionalColumnsDto;

import lombok.Data;

@Data
public class ServiceElementDto {
	private String objUuid;
	private String objCode;
	private String svcRscTypeCode;
	private String prvsnActvtYn;
	private String prvsnCode;
	private String prvsnPrmtrName;
	private String svcRscOvwCntn;
	private String dplcTrgtUuid;
	private String rgstUser;
	private String rgstDtm;
	private String updUser;
	private String updDtm;
	private List<AdditionalColumnsDto> additionalColumns;
}
