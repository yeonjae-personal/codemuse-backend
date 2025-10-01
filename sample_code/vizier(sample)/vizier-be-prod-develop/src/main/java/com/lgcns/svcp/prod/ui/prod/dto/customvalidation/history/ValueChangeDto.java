package com.lgcns.svcp.prod.ui.prod.dto.customvalidation.history;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValueChangeDto {
	
	private String workNo;
	private String validCode;
	private String condType;
	private String attrUuid;
	private String attrVal;
	private String beforeValue;
	private String afterValue;
	private String labelId;
	private String itemCodeName;
}
