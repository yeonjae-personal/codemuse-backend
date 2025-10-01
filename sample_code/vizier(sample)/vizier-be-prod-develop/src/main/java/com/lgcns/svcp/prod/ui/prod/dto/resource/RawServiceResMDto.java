package com.lgcns.svcp.prod.ui.prod.dto.resource;

import lombok.Data;

@Data
public class RawServiceResMDto {
	private String prodUuid;
	private String svcFctrCd;
	private String svcFctrNm;
	private String svcFctrClssCd;
	private String svcFctrKdCd;
	private String swtcSbgnYn;
	private String swtcCd;
	private String swtcPrmtnm;
	private String swtcPrmtDelNm;
	private String saSwtcPrmtNm;
	private String saSwtcPrmtDelNm;
	private String valdEndDtm;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
}
