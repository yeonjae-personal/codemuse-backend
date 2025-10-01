package com.lgcns.svcp.prod.ui.prod.dto.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class SelectOptionDto {
	private String value;
	private String label;
	private String cmcdDetlId;
	private String cmcdDetlNm;
	private int sortNo;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<SelectOptionDto> subOptions;
}
