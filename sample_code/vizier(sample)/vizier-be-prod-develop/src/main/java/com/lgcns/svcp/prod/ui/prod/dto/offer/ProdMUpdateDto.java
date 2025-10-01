package com.lgcns.svcp.prod.ui.prod.dto.offer;

import lombok.Data;

@Data
public class ProdMUpdateDto {
	private String type;
	private String prodCd;
	private String prodNm;
	private String prodKdCd;
	private String custKdCd;
	private String prodAgeDivsCd;
	private String saleValdStrtDtm;
	private String saleValdEndDtm;
	
	private String smsNotiYn;
	private String prodGrdCd;
	private String expyImpsbTermDays;
	private String rjnImpsbTermDays;
	private String mbspDcntRqstPsblYn;
	
	private String overView;
	private String comment;
}
