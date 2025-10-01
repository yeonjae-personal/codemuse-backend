package com.lgcns.svcp.prod.ui.prod.dto.customvalidation;

import com.lgcns.svcp.prod.entity.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeNewDto extends BaseEntity {
	
	private String attrUuid;
	private String itemCode;
	private String fieldTypeCode;
	private Integer sortNo;
	private String labelId;
	private String commGroupCode;
	private String itemCodeName;
	private String largeItemCode;
	private String requiredYn;
	private String dispTab;
	private String[] types = new String[] {};
	private String attrMaxLength;
}
