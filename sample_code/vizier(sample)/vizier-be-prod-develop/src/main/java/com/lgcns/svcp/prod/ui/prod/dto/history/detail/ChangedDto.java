package com.lgcns.svcp.prod.ui.prod.dto.history.detail;

import lombok.Data;

@Data
public abstract class ChangedDto {
	private long workNo;
	private String workDate;
	private String changeTypeName;
	private String chgDeptName;
	private String chgUser;
}
