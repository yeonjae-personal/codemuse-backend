package com.lgcns.svcp.prod.ui.prod.dto.label;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabelSearchPagingDto extends BasePaginationDto {

	private String type;
	private String value;
	private String langCode;
	private String labelId;
	private String labelName;
}
