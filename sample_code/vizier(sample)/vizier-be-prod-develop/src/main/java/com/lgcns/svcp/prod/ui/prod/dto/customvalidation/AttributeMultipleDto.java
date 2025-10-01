package com.lgcns.svcp.prod.ui.prod.dto.customvalidation;

import com.lgcns.svcp.prod.entity.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeMultipleDto extends BaseEntity {
	
	private String validCode;
	private String attrUuid;
	private Integer attrSeq;
	private String attrVal;
}
