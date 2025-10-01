package com.lgcns.svcp.prod.online.prod.dto.characteristic;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;
import lombok.Data;

@Data
public class SlinInfoMDto extends BasePaginationDto {
	private String prodUuid;
	private String slinInfoCd;
	private String slinInfoNm;
	private String slinDtrbYn;
	private String pdspCd;
	private String slinMintMagt;
	private String unitPflsCd;
	private String slinMgmtUnitCd;
	private String dwIfKidCd;
	private String valdEndDtm;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;

}