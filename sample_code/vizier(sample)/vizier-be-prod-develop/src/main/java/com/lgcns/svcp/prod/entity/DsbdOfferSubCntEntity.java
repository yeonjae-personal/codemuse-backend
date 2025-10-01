package com.lgcns.svcp.prod.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DsbdOfferSubCntEntity extends BaseEntity {
	
	private String offerCode;
	private String offerName;
	private String offerTypeName;
	private Integer subCnt;
	private Date saleValidStartDtm;
	private Date saleValidEndDtm;
	private Date batchRunDtm;
}
