package com.lgcns.svcp.prod.ui.prod.dto.offer;

import lombok.Data;

@Data
public class DcntMUpdateDto {
	private String type;
	private String dcntCd;
	private String dcntNm;
	private String prty;
	private String prodKdCd;
	private String dcntValdStrtDtm;
	private String dcntValdEndDtm;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
	
	private String dcntGrpRepKdCd;
	
	private String overview;
	private String comment;
}
