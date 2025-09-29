package com.lgcns.svcp.prod.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MiddleItemEntity extends BaseEntity {
	
	private String lctgrItemCode;
	private String lctgrItemName;
	private String largeItemCodeTrgt;
	private String largeItemNameTrgt;
	private String mctgrItemCode;
	private String mctgrItemName;
	private String itemCode;
	private String itemCodeName;
	private String useYn;
	private Integer middleSort;
	private Integer itemSort;
}
