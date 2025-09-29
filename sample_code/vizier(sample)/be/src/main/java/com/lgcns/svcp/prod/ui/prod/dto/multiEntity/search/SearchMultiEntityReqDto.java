package com.lgcns.svcp.prod.ui.prod.dto.multiEntity.search;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchMultiEntityReqDto extends BasePaginationDto {
	private String itemCode;
	private String entityTypeCode;
	private String multiEntityCode;
	private String multiEntityName;
	private boolean onlyValidDtm;
}
