package com.lgcns.svcp.prod.ui.prod.dto.dashboard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecentWorkSimpleViewDto {
	
	private String objUuid;
	private String type;
	private String objName;
	private String workTypeName;
	private String workTypeCode;
	private String workDate;
}
