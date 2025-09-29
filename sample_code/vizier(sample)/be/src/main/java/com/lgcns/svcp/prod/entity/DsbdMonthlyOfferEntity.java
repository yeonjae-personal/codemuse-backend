package com.lgcns.svcp.prod.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DsbdMonthlyOfferEntity extends BaseEntity {
	
	private String yearMonth;
	private String offerTypeName;
	private Integer offerCnt;
}
