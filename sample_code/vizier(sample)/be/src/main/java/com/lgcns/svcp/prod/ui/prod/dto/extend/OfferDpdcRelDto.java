package com.lgcns.svcp.prod.ui.prod.dto.extend;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfferDpdcRelDto extends BaseDto {
	private String baseUuid;
	private String trgtUuid;
	private String dpdcRelUuid;
	private String validStartDtm;
	private String validEndDtm;
}
