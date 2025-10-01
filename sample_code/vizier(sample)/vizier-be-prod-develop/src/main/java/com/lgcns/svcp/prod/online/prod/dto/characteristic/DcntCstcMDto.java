package com.lgcns.svcp.prod.online.prod.dto.characteristic;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Data;

@Data
public class DcntCstcMDto extends BasePaginationDto {
	private String prodUuid;
	private String dcntCstcCd;
	private String dcntCstcNm;
	private String dcntRegDivsCd;
	private String dcntKidCd;
	private String dcntKidDetlCd;
	private String prodKidDetlCd;
	private String dcntAplyLvCd;
	private String custKidCd;
	private String prodUseTermMmct;
	private String prodAplyCyclCd;
	private String useTermAplyDivsCd;
	private String duplRegPsblYn;
	private String adjRsnCd;
	private String pnltOccrYn;
	private String dcntStrtDtKidCd;
	private String dcntGrpKidCd;
	private String entrMmddAplyKidCd;
	private String expyMmddAplyKidCd;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
}