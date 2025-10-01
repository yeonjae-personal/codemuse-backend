package com.lgcns.svcp.prod.ui.prod.dto.dashboard;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscribeTop10SearchPagingDto extends BasePaginationDto {
	private String view;
	private Integer max;
	private String offerName;
	private String offerTypeName;
	private String sorting;
	private String offerCode;
	private String searchBy;
	private String searchValue;
}
