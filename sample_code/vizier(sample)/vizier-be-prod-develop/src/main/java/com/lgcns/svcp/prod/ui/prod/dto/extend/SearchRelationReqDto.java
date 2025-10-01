package com.lgcns.svcp.prod.ui.prod.dto.extend;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRelationReqDto extends BasePaginationDto {
	private String dpdcRelCd;
	private String dpdcRelNm;
}
