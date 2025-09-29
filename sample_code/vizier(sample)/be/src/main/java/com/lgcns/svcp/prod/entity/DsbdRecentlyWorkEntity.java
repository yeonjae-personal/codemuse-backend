package com.lgcns.svcp.prod.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DsbdRecentlyWorkEntity extends BaseEntity {
	
	private String workNo;
	private String objUuid;
	private String lctgrItemName;
	private String itemCode;
	private String objCode;
	private String objName;
	private String workType;
	private String updUserDeptName;
	private String attrValUpdUser;
	private Date objWorkDtm;
	private Date batchRunDtm;
}
