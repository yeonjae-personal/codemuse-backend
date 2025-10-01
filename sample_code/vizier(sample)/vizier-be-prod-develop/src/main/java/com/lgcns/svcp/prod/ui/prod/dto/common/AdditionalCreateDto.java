package com.lgcns.svcp.prod.ui.prod.dto.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class AdditionalCreateDto {
	private String attrUuid;
	private String attrNm;
	private String attrFieldCd;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<Attribute> selectOptions;
}
