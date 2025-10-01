package com.lgcns.svcp.prod.ui.prod.dto.customvalidation;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoxAndAttrRespone {
	
	private List<AttributeNewDto> attrConditions = new ArrayList<>();
	private List<AttributeNewDto> attrActions = new ArrayList<>();
	private List<SaveCusSearchDto> validations = new ArrayList<>();
}
