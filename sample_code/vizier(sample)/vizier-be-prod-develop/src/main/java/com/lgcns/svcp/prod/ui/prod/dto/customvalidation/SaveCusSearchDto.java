package com.lgcns.svcp.prod.ui.prod.dto.customvalidation;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveCusSearchDto {
	
	private String validCode;
	
	private String validCntn;
	
	@NotNull
	private Integer seqNo;
	
	private String condItemCode;
	
	@NotBlank
	private String validStartDtm;
	
	private String validEndDtm;
	
	private boolean isCreated = false;
	
	private boolean isUpdated;
	
	@Valid
	private List<AttributeDto> attributes = new ArrayList<>();
}
