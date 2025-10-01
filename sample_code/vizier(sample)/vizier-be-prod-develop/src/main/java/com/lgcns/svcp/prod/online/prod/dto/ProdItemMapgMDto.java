package com.lgcns.svcp.prod.online.prod.dto;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProdItemMapgMDto extends BasePaginationDto {
	private String prodUuid;
	private String prodItemCd;
	private String prodItemNm;
	private String entiTypeNm;
	private String entiDetlTypeNm;
	private String rgstUsr;
	private String rgstDtm;
	private String updUsr;
	private String updDtm;
	private String asisProdItemCd;
	private String asisProdItemNm;
}
