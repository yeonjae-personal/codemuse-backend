package com.lgcns.svcp.prod.ui.prod.dto.history.save;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Data;

@Data
public class GeneralAttributeHistDto extends BaseDto {
	private long workNo;
	private String objUuid;
	private String attrCode;
	private String attrVal;
	private String updUserDeptName;
	private String attrValUpdUser;
    private String firstAttrVal;
}
