package com.lgcns.svcp.prod.ui.prod.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseDto {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String rgstUser;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String rgstDtm;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String updUser;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String updDtm;
}
