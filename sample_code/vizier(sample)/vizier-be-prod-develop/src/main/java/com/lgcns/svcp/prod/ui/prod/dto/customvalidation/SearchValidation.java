package com.lgcns.svcp.prod.ui.prod.dto.customvalidation;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchValidation extends BasePaginationDto {
	private String itemCode;
	private String largeItemCode;
	private String middleItemCode;
}
