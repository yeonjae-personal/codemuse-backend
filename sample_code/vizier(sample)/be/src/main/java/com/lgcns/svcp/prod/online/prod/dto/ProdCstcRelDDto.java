package com.lgcns.svcp.prod.online.prod.dto;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data

public class ProdCstcRelDDto extends BasePaginationDto {
	private String baseUuid;
	private String trgtUuid;
	private String baseProdItemCd;
	private String trgtProdItemCd;
	private String valdStrtDtm;
	private String valdEndDtm;
	private String prodCstcRelCd;
	private String cstcXclnYn;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
	private String asisBaseProdItemCd;
	private String asisTrgtProdItemCd;
}
