package com.lgcns.svcp.prod.entity.external;

import java.util.Date;

import com.lgcns.svcp.prod.entity.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPocketCustomEntity extends BaseEntity {
	
	private String objUuid;
	private String objCode;
	private String objName;
	private String itemCode;
	private String lctgrItemCode;
	private String lctgrItemName;
	private String mctgrItemCode;
	private Date itemValidStartDtm;
	private Date itemValidEndDtm;
	private String offerGroupTypeCode;
	private String subType;
}
