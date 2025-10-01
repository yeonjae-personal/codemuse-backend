package com.lgcns.svcp.prod.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPocketEntity extends BaseEntity {
	
	private String userId;
	private String objUuid;
	private Date validStartDtm;
	private Date validEndDtm;
}
