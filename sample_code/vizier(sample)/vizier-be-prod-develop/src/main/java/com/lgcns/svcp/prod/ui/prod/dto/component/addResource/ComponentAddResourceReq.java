package com.lgcns.svcp.prod.ui.prod.dto.component.addResource;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComponentAddResourceReq extends BasePaginationDto {
	private String componentUUID;
	private String componentType;
	private String componentSubType;
	private String componentCreateType;
	private String name;
	private String code;
	private String itemCode;
}
