package com.lgcns.svcp.prod.ui.prod.dto.characteristic;

import lombok.Data;

@Data
public class QosMDto {
	private String prodUuid;
	private String qosCd;
	private String qosNm;
	private String qosPlcyGrpCd;
	private String thrsIdfyCd;
	private String valdStrtDtm;
	private String valdEndDtm;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
}