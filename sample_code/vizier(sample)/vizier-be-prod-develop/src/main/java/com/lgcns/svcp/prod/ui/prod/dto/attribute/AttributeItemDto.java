package com.lgcns.svcp.prod.ui.prod.dto.attribute;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeItemDto extends BaseDto {
	
	private String lctgrItemCode; 
	private String mctgrItemCode;
	private String itemCode;
	private String itemCodeName;
	private String useYn;
	private Integer sortNo;
}
