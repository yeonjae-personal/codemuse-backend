package com.lgcns.svcp.prod.ui.prod.dto.category.detail;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OffersOfCatgegoryReqDto extends BasePaginationDto {
	private String ctgrNodeUuid;
}
