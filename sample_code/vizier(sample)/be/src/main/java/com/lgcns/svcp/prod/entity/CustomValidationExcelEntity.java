package com.lgcns.svcp.prod.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomValidationExcelEntity extends BaseEntity {
	
	private String validCode;
	private String attrUuid;
	private String condType;
	private Integer attrNo;
	private String validStartDtm;
	private String validEndDtm;
	private String actionItemCode;
	private Integer rangeStartVal;
	private Integer rangeEndVal;
	private Date rangeStartDtm;
	private Date rangeEndDtm;
	private String textCntn;
	private String labelId;
	private String labelName;
	private String fieldTypeCode;
	private String itemCodeName;
	private String itemCode;
}
