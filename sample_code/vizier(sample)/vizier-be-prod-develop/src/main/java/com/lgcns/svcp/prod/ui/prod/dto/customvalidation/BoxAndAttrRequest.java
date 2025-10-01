package com.lgcns.svcp.prod.ui.prod.dto.customvalidation;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoxAndAttrRequest {
	
	@NotBlank
	private String item;
	
	@NotBlank
	private String type;
	
	private String subType;
	
	//@NotBlank
	private String chgDeptName;
	
	//@NotBlank
	private String chgUser;
	
	@Valid
	List<SaveCusSearchDto> datas = new ArrayList<>();
}
