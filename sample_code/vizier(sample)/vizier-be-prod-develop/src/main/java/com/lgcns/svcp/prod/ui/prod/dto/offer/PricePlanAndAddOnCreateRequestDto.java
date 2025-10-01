package com.lgcns.svcp.prod.ui.prod.dto.offer;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.ProdStruDDto;

import lombok.Data;

@Data
public class PricePlanAndAddOnCreateRequestDto {
	private PricePlanAndAddOnDto general;
	private AdditionalPricePlanAndAddOnDto additional;
	private List<ProdStruDDto> structure;
}
