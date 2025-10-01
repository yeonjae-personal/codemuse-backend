package com.lgcns.svcp.prod.ui.prod.dto.offer;

import lombok.Data;

@Data
public class DcntMDto {
	private String prodUuid;
	private String dcntCd;
	private String dcntNm;
	private String dcntGrpRepKdCd;
//	private String offrDcntKdCd;
	private String prty;
//	private String dcntPrtyAplyDivsCd;
//	private String dcntPrtyGrpCd;
//	private String prodDivsCd;
	private String prodKdCd;
//	private String prodRelDivsCd;
	private String dcntValdStrtDtm;
	private String dcntValdEndDtm;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
	private String type;
	private String overView;
	private String comment;
}
