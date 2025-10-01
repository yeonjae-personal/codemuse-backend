package com.lgcns.svcp.prod.ui.prod.dto.offer;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.common.AdditionalUpdateDto;

import lombok.Data;

@Data
public class AdditionalPricePlanAndAddOnUpdateDto {
	private List<AdditionalUpdateDto> additional;
	private String prodUuid;
}
