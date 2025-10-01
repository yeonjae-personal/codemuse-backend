package com.lgcns.svcp.prod.ui.prod.dto.category.update;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryOfferRelDto extends BaseDto {
    private String oldCtgrNodeUuid;
    private String ctgrNodeUuid;
    private String objUuid;
	private String validStartDtm;
	private String validEndDtm;
}
