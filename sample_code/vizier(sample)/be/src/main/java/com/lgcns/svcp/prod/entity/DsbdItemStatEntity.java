package com.lgcns.svcp.prod.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DsbdItemStatEntity extends BaseEntity {
	
	private String lctgrItemName;
	private String itemCodeName;
	private Integer itemCnt;
	private double lctgrItemRatio;
	private double itemCodeRatio;
}
