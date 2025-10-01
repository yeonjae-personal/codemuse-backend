package com.lgcns.svcp.prod.ui.prod.dto.category;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdMWithCatgUuid extends BaseDto {
    private String ctgrNodeUuid;
    private String prodUuid;
    private String prodCd;
    private String prodNm;
	private String validStartDtm;
	private String validEndDtm;
}
