package com.lgcns.svcp.prod.online.prod.dto.component.benefit;

import java.util.List;

import com.lgcns.svcp.prod.online.prod.dto.common.AdditionalColumnsDto;

import lombok.Data;

@Data
public class AllowanceDto {
	private String alwncUuid;
	private String alwncCode;
	private String alwncName;
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
