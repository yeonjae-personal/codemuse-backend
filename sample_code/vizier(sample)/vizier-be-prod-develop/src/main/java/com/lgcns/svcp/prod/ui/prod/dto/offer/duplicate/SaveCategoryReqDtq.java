package com.lgcns.svcp.prod.ui.prod.dto.offer.duplicate;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveCategoryReqDtq extends BaseDto {
    private String ctgrNodeUuid;
    private String objUuid;
	private String validEndDtm;
}
