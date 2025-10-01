package com.lgcns.svcp.prod.ui.prod.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class AdditionalUpdateDto {
	@JsonIgnore
	private String prodUuid; 
	private String attrUuid;
	private String attrVal;
}
