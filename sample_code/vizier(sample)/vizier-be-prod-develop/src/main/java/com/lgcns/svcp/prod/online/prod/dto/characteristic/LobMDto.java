package com.lgcns.svcp.prod.online.prod.dto.characteristic;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;
import lombok.Data;

@Data
public class LobMDto extends BasePaginationDto {
	private String prodUuid;
	private String lobCd;
	private String lobNm;
	private String svcCd;
	private String majrUseYn;
	private String valdEndDtm;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;

}