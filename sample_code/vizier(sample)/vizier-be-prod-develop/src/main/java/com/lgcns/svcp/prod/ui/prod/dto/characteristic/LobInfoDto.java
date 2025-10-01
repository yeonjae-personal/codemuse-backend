package com.lgcns.svcp.prod.ui.prod.dto.characteristic;

import java.util.List;

import lombok.Data;

@Data
public class LobInfoDto {
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
	private List<LobMrktRelDDto> lobMrktRelD;
	private String type;
}
