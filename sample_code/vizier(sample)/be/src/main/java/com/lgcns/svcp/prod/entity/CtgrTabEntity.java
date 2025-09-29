package com.lgcns.svcp.prod.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CtgrTabEntity extends BaseEntity {
	
    private String ctgrTabUuid;
	private String ctgrTabName;
    private String useYn;
    private Integer sortNo;
}
