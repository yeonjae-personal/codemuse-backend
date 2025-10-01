package com.lgcns.svcp.prod.ui.prod.dto.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class AdditionalDto {
	@JsonIgnore
	private String prodUuid;
	private String attrUuid;
	private String attrNm;
	private String attrVal;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String attrDescription;
	private String fieldType;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<Attribute> selectOptions;

}
