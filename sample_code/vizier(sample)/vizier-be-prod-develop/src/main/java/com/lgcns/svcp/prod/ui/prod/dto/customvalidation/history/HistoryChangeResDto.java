package com.lgcns.svcp.prod.ui.prod.dto.customvalidation.history;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryChangeResDto {
	
	private String changeTypeName;
	private String chgDeptName;
	private String chgUser;
	private List<ValueChangeDto> values;
	private List<AttributeChangeDto> attributes;
}
