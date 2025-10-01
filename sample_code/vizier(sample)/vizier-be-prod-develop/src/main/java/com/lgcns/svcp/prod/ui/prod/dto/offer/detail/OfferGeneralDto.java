package com.lgcns.svcp.prod.ui.prod.dto.offer.detail;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Data;

@Data
public abstract class OfferGeneralDto extends BaseDto {
	private String objUuid;
	private String objCode;
	private String objName;
	private String offerTypeCode;
	private String dplcTrgtUuid;
	private String validStartDtm;
	private String validEndDtm;
	private String itemCodeName;
}
