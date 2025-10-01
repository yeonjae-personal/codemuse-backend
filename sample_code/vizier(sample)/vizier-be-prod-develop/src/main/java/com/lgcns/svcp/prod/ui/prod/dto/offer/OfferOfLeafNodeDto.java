package com.lgcns.svcp.prod.ui.prod.dto.offer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfferOfLeafNodeDto {
    private String ctgrNodeUuid;
	private String prodUuid;
	private String prodCd;
	private String prodNm;
	private String validStartDtm;
	private String validEndDtm;
}
