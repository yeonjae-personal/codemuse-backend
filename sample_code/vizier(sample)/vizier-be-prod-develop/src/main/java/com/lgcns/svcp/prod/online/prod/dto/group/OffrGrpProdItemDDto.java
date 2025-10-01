package com.lgcns.svcp.prod.online.prod.dto.group;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OffrGrpProdItemDDto extends BasePaginationDto {
	private String offrGrpUuid;
	private String prodItemOffrTmplUuid;
	private String offrGrpCd;
	private String prodItemCd;
	private String valdStrtDtm;
	private String valdEndDtm;
	private String offrTmplYn;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;

}