package com.lgcns.svcp.prod.ruleengine.dto.field;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldSearchPagingDto extends BasePaginationDto {
	
	private String type;
	private String value;
	private String fieldDispName;
	private String fieldKeyName;
}
