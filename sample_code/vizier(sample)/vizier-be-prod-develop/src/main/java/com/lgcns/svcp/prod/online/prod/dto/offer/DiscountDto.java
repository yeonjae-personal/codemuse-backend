package com.lgcns.svcp.prod.online.prod.dto.offer;

import java.util.List;

import com.lgcns.svcp.prod.online.prod.dto.common.AdditionalColumnsDto;

import lombok.Data;

@Data
public class DiscountDto {
	private String dcUuid;
	private String dcCode;
	private String dcName;
	private String ovwCntn;
	private String dplcTrgtUuid;
	private String validStartDtm;
	private String validEndDtm;
	private String rgstUser;
	private String rgstDtm;
	private String updUser;
	private String updDtm;
	private List<AdditionalColumnsDto> additionalColumns;
}
