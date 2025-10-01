package com.lgcns.svcp.prod.online.prod.dto.characteristic;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Data;

@Data
public class LimMDto extends BasePaginationDto {
	private String prodUuid;
	private String limCd;
	private String limNm;
	private String limAmt;
	private String limKidCd;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;

}