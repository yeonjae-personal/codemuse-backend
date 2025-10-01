package com.lgcns.svcp.prod.ui.prod.dto.offer;

import lombok.Data;

@Data
public class GroupedPricePlanAndAddOnUpdateDto {
	private GeneralPricePlanAndAddOnUpdateDto general;
	private AdditionalPricePlanAndAddOnUpdateDto additional;
	
}
