package com.lgcns.svcp.prod.ui.prod.dto.attribute;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TreeViewDto {
	
	private String ctgrNodeUuid;
    private String ctgrTabUuid;
    private String ctgrNodeName;
    private String chgDeptName;
    private String chgUser;
    private String ctgrOvwCntn;
    private String rgstDtm;
    private Date rgstDtmOrigin;
}
