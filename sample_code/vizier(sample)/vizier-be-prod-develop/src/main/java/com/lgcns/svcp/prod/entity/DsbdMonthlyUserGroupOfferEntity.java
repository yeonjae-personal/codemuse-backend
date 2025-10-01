package com.lgcns.svcp.prod.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DsbdMonthlyUserGroupOfferEntity extends BaseEntity {
	
	private String yearMonth;
	private String userGroupName;
	private Integer offerCnt;
}
