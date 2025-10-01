package com.lgcns.svcp.prod.online.prod.dto.offer;

import java.util.List;
import java.util.Map;

import com.lgcns.svcp.prod.online.prod.dto.common.AdditionalColumnsDto;

import lombok.Data;

@Data
public class PricePlanDto {
	private String ppUuid;
	private String ppCode;
	private String ppName;
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
