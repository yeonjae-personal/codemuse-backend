package com.lgcns.svcp.prod.ui.prod.dto.customvalidation;

import com.lgcns.svcp.prod.util.excel.annotation.CustomTitleHeader;
import com.lgcns.svcp.prod.util.excel.annotation.Value;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@CustomTitleHeader
public class CustomValidationExportDto {
	
	@Value(name ="customvalidation.excel.number")
	private String number;
	
	@Value(name ="customvalidation.excel.condition.item")
	private String itemCondition;
	
	@Value(name ="customvalidation.excel.condition.attribute")
	private String attributeCondition;
	
	@Value(name ="customvalidation.excel.condition.validation")
	private String validationCondition;
	
	@Value(name ="customvalidation.excel.action.item")
	private String itemAction;
	
	@Value(name ="customvalidation.excel.action.attribute")
	private String attributeAction;
	
	@Value(name ="customvalidation.excel.action.validation")
	private String validationAction;
	
	@Value(name ="customvalidation.excel.registeruser")
	private String registerUser;
	
	@Value(name ="customvalidation.excel.registerdate")
	private String registerDate;
	
	@Value(name ="customvalidation.excel.modifieduser")
	private String modifiedUser;
	
	@Value(name ="customvalidation.excel.modifieddate")
	private String modifiedDate;
}
