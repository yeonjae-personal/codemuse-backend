package com.lgcns.svcp.prod.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DsbdUserSetEntity extends BaseEntity {
	
	private String userId;
	private String dsbdViewUuid;
	private Integer seqNo;
	private String attrKey;
	private String attrVal;
}
