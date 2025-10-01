package com.lgcns.svcp.prod.ui.prod.dto.attribute;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeItemStrcDto extends BaseDto {
	
	private String baseItemCode; 
	private String trgtItemCode; 
	private String strcTypeCode;
	private String validEndDtm;
}
