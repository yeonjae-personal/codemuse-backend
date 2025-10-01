package com.lgcns.svcp.prod.ui.prod.dto.customvalidation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustValidValHistoryDto {
	
	private String workNo;
	private String validCode;
	private String condType;
	private String attrUuid;
	private String attrVal;
	private String updUserDeptName;
	private String attrValUpdUser;
	private String rgstUser;
	private String rgstDtm;
	private String updUser;
	private String updDtm;
}
