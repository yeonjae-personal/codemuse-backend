package com.lgcns.svcp.prod.ui.prod.dto.offer;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lgcns.svcp.prod.ui.prod.dto.common.AdditionalDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdditionalPricePlanAndAddOnDto {
	List<AdditionalDto> additional;
	private String prodUuid;
}
