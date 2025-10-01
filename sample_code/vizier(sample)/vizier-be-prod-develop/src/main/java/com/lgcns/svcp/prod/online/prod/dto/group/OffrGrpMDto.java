package com.lgcns.svcp.prod.online.prod.dto.group;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OffrGrpMDto extends BasePaginationDto {
	private String prodUuid;
	private String offrGrpCd;
	private String offrGrpNm;
	private String valdStrtDtm;
	private String valdEndDtm;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
	private String asisOffrGrpCd;
	private String asisOffrGrpNm;
}