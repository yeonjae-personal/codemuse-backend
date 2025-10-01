package com.lgcns.svcp.prod.online.prod.dto.resource;

import java.util.List;

import com.lgcns.svcp.prod.online.prod.dto.common.AdditionalColumnsDto;

import lombok.Data;

@Data
public class RatingElementDto {
	private String objUuid;
	private String objCode;
	private String rtngRscTypeCode;
	private String blngDivCode;
	private String acntSalesCode;
	private String pymntCmsnApplyYn;
	private String vatApplyYn;
	private String rtngRscOvwCntn;
	private String dplcTrgtUuid;
	private String chgDeptName;
	private String chgUser;
	private String rgstUser;
	private String rgstDtm;
	private String updUser;
	private String updDtm;
	private List<AdditionalColumnsDto> additionalColumns;
}
