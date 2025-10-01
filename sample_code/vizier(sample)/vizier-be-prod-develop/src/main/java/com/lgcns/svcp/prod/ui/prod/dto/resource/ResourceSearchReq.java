package com.lgcns.svcp.prod.ui.prod.dto.resource;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceSearchReq extends BasePaginationDto {
	private String objUuid;
	private String itemCode;
	private String objName;
	private String objCode;
	private boolean onlyValidDtm;
}
