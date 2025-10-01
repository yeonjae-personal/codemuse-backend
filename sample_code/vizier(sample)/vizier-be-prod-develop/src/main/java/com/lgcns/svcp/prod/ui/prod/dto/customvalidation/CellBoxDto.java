package com.lgcns.svcp.prod.ui.prod.dto.customvalidation;

import java.util.Date;

import com.lgcns.svcp.prod.entity.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CellBoxDto extends BaseEntity {
	
	private String validCode;
	private String condItemCode;
	private Integer seqNo;
	private String validCntn;
	private String validStartDtm;
	private String validEndDtm;
	private Date validEndDtmOrigin;
}
