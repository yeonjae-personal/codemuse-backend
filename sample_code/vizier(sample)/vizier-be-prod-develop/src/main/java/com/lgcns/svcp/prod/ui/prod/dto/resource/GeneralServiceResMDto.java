package com.lgcns.svcp.prod.ui.prod.dto.resource;

import lombok.Data;

@Data
public class GeneralServiceResMDto {
	private String prodUuid;
	private String svcFctrCd;
	private String svcFctrNm;
	private String svcFctrClssCd;
	private String svcFctrKdCd;
	private String swtcSbgnYn;
	private String swtcCd;
	private String valdEndDtm;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
	
	 public GeneralServiceResMDto(RawServiceResMDto rawServiceResMDto) {
	        this.prodUuid = rawServiceResMDto.getProdUuid();
	        this.svcFctrCd = rawServiceResMDto.getSvcFctrCd();
	        this.svcFctrNm = rawServiceResMDto.getSvcFctrNm();
	        this.svcFctrClssCd = rawServiceResMDto.getSvcFctrClssCd();
	        this.svcFctrKdCd = rawServiceResMDto.getSvcFctrKdCd();
	        this.swtcSbgnYn = rawServiceResMDto.getSwtcSbgnYn();
	        this.swtcCd = rawServiceResMDto.getSwtcCd();
	        this.rgstUsr = rawServiceResMDto.getRgstUsr();
	        this.rgstDtm = rawServiceResMDto.getRgstDtm();
	        this.updUsr = rawServiceResMDto.getUpdUsr();
	        this.updDtm = rawServiceResMDto.getUpdDtm();
	    }
}
