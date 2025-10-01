package com.lgcns.svcp.prod.ui.prod.dto.offer;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.common.AdditionalCreateDto;
import com.lgcns.svcp.prod.ui.prod.dto.common.AdditionalDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdditionalPricePlanAndAddOnCreateDto {
	private List<AdditionalCreateDto> additional;
}
