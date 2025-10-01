package com.lgcns.svcp.prod.ui.prod.dto.offer.detail;

import lombok.Data;

@Data
public class AddonGeneralDto extends OfferGeneralDto {
	private String custTypeCode;
	private String ageDivCode;
	private String addonOvwCntn;
	private String saleValidStartDtm;
	private String saleValidEndDtm;
}
