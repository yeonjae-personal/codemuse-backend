package com.lgcns.svcp.prod.online.prod.dto.characteristic;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Data;

@Data
public class DcntTrgtInfoMDto extends BasePaginationDto {
	
	private String prodUuid;
	private String dcntTrgtInfoCd;
	private String dcntTrgtInfoNm;
	private String susXclnYn;
	private String xclnYn;
	private String sumYn;
	private String aplyYn;
	private String valdEndDtm;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
}