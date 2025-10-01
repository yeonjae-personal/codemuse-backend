package com.lgcns.svcp.prod.ui.prod.dto.category;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestOfferWithCatgUuidDto extends BasePaginationDto {
	private String ctgrTabUuid;
	private String offerCd;
	private String offerNm;
}
