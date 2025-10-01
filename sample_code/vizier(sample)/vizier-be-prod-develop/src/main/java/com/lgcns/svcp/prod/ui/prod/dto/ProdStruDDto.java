package com.lgcns.svcp.prod.ui.prod.dto;

import lombok.Data;

@Data
public class ProdStruDDto {
	private String baseUuid;
	private String trgtUuid;
	private String baseProdItemCd;
	private String trgtProdItemCd;
	private String valdStrtDtm;
	private String valdEndDtm;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
}
