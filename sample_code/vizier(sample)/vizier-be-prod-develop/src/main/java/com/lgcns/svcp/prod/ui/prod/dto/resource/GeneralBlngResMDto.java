package com.lgcns.svcp.prod.ui.prod.dto.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class GeneralBlngResMDto {
	@JsonIgnore
	private String svcFctrCd;
	private String svcFctrNm;
	private String svcFctrClssCd;
	private String svcFctrClssDetlCd;
	private String svcFctrLnwlEtcCd;
	private String svcFctrCallKdCd;
	private String svcFctrCallKdDetlCd;
	private String valdEndDtm;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
	
	public GeneralBlngResMDto(RawBlngResMDto rawBlngResMDto) {
        this.svcFctrCd = rawBlngResMDto.getSvcFctrCd();
        this.svcFctrNm = rawBlngResMDto.getSvcFctrNm();
        this.svcFctrClssDetlCd = rawBlngResMDto.getSvcFctrClssDetlCd();
        this.svcFctrLnwlEtcCd = rawBlngResMDto.getSvcFctrLnwlEtcCd();
        this.svcFctrCallKdCd = rawBlngResMDto.getSvcFctrCallKdCd();
        this.svcFctrCallKdDetlCd = rawBlngResMDto.getSvcFctrCallKdDetlCd();
        this.valdEndDtm = rawBlngResMDto.getValdEndDtm();
        this.rgstUsr = rawBlngResMDto.getRgstUsr();
        this.rgstDtm = rawBlngResMDto.getRgstDtm();
        this.updUsr = rawBlngResMDto.getUpdUsr();
        this.updDtm = rawBlngResMDto.getUpdDtm();
    }
}
