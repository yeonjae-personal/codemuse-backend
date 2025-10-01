package com.lgcns.svcp.prod.ui.prod.dto.resource.list;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceSearchDto extends BasePaginationDto {
	private String objUuid;
	private String objName;
	private String objCode;
	private String itemCode;
	private String validStartDtm;
	private String validEndDtm;
	private String relationStartDate;
	private String relationEndDate;
	private boolean onlyValidDtm;
}
