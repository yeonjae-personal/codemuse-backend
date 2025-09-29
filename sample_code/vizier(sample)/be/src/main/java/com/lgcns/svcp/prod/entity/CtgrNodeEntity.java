package com.lgcns.svcp.prod.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CtgrNodeEntity extends BaseEntity {
	
	private String ctgrNodeUuid;
    private String ctgrTabUuid;
    private String ctgrNodeName;
    private String chgDeptName;
    private String chgUser;
    private String ctgrOvwCntn;
    private String useYn;
    private String tclsCtgrYn;
    private String hpstCtgrNodeUuid;
}
