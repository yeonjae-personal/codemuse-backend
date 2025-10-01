package com.lgcns.svcp.prod.ui.prod.dto.offer.detail;

import lombok.Data;

@Data
public class DiscountGeneralDto extends OfferGeneralDto {
	private String dcTypeCode;
	private String pnltOcrcYn;
	private String dcPriorRank;
	private String dcOvwCntn;

}
