package com.lgcns.svcp.prod.ui.prod.dto.offer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupedPricePlanAndAddOnCreateDto {
	private String type;
	private String prodUuid;
	private String prodCd;
	private String prodKdCd;
	private GeneralPricePlanAndAddOnCreateDto general;
	private AdditionalPricePlanAndAddOnCreateDto additional;
}
