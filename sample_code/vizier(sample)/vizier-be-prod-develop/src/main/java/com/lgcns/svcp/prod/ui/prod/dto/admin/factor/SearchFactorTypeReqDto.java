package com.lgcns.svcp.prod.ui.prod.dto.admin.factor;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchFactorTypeReqDto extends BasePaginationDto {
	private String factorTypeCode;
	private String factorTypeName;
	private String useYn;
}
