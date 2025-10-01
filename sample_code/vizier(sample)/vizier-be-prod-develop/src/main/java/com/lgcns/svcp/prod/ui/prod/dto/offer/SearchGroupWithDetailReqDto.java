package com.lgcns.svcp.prod.ui.prod.dto.offer;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Data;

@Data
public class SearchGroupWithDetailReqDto extends BasePaginationDto {
	private String offerType;
	private String offerCode;
	private String offerName;
}
