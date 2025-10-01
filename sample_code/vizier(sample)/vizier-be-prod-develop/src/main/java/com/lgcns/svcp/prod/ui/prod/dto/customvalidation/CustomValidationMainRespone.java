package com.lgcns.svcp.prod.ui.prod.dto.customvalidation;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomValidationMainRespone {
	
	private String validCode;
	private String validCntn;
	private Integer seqNo;
	private List<AttributeDto> datas = new ArrayList<>();
}
