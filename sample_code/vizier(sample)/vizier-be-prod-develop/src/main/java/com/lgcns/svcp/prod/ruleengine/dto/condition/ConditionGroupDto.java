package com.lgcns.svcp.prod.ruleengine.dto.condition;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class ConditionGroupDto {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String condGroupUuid;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String hpstCondGroupUuid;
	private String logicType;
	private Integer sortNo;
	private List<Object> condition;
}
