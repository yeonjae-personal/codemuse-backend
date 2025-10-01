package com.lgcns.svcp.prod.ui.prod.dto.dashboard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecentWorkDetailViewDto {
	
	private String objUuid;
	private String category;
	private String type;
	private String objName;
	private String objCode;
	private String workTypeName;
	private String workTypeCode;
	private String responsibleDept;
	private String responsibleUser;
	private String workDate;
}
