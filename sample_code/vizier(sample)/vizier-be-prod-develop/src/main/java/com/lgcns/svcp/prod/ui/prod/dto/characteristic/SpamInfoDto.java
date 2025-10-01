package com.lgcns.svcp.prod.ui.prod.dto.characteristic;

import java.util.List;

import lombok.Data;

@Data
public class SpamInfoDto {
	private String prodUuid;
	private String spamCd;
	private String spamNm;
	private String thrsIdfyCd;
	private String valdStrtDtm;
	private String valdEndDtm;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
	private List<SpamLvwuPlcyDDto> spamLvwuPlcyDDto;
	private String type;
}
