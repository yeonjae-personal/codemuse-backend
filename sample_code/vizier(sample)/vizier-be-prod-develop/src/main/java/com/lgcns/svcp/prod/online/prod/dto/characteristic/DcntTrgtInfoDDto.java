package com.lgcns.svcp.prod.online.prod.dto.characteristic;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Data;

@Data
public class DcntTrgtInfoDDto extends BasePaginationDto {
	
	private String dcntTrgtInfoCd;
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
	private String dcntTrgtChrgKidCd;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
}