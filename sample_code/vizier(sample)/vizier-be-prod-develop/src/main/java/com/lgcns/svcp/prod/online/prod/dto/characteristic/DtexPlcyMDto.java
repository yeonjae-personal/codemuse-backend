package com.lgcns.svcp.prod.online.prod.dto.characteristic;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Data;

@Data
public class DtexPlcyMDto extends BasePaginationDto {
	private String prodUuid;
	private String dtexPlcyCd;
	private String dtexPlcyNm;
	private String dtexPsblYn;
	private String dtsnGenPsblTmsc;
	private String dtrcGenPsblTmsc;
	private String dtsnFamPsblTmsc;
	private String dtrcFamPsblTmsc;
	private String rstcMttrYn1;
	private String rstcMttrYn2;
	private String rstcMttrYn3;
	private String rstcMttrYn4;
	private String valdEndDtm;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;

}