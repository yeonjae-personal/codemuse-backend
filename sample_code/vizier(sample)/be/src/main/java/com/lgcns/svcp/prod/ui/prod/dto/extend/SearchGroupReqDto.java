package com.lgcns.svcp.prod.ui.prod.dto.extend;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchGroupReqDto extends BasePaginationDto {
	private String offrGrpCd;
	private String offrGrpNm;
	private String itemCode;
	private String childOffrUuid;
	private boolean onlyValidDtm;
	private boolean isPaged;
	private String language;
}
