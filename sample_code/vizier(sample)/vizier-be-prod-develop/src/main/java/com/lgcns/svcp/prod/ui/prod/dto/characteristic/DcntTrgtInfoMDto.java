package com.lgcns.svcp.prod.ui.prod.dto.characteristic;

import lombok.Data;

@Data
public class DcntTrgtInfoMDto {
	private String prodUuid;
	private String dcntTrgtInfoCd;
	private String dcntTrgtInfoNm;
	private String dcntTrgtItemSno;
	private String valdStrtDtm;
	private String valdEndDtm;
	private String offrGrpCd;
	private String prodCd;
	private String ratCd;
	private String svcFctrCd;
	private String billItemLclsCd;
	private String billItemMclsCd;
	private String billItemCd;
	private String dcntTrgtChrgKdCd;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
	private String type;
}