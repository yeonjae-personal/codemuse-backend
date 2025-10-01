package com.lgcns.svcp.prod.ui.prod.dto.resource;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupedBlngResMDto {
	private GeneralBlngResMDto general;
	private AdditionalBlngResMDto additional;
}
