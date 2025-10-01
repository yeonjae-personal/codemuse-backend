package com.lgcns.svcp.prod.ui.prod.dto.offer.detail;

import lombok.Data;

@Data
public class PricePlanGeneralDto extends OfferGeneralDto {
	private String custTypeCode;
	private String ageDivCode;
	private String ppOvwCntn;
	private String saleValidStartDtm;
	private String saleValidEndDtm;
}
