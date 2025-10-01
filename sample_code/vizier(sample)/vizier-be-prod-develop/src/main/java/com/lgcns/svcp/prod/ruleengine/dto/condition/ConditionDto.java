package com.lgcns.svcp.prod.ruleengine.dto.condition;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ConditionDto {
	@JsonIgnore
	private String condGroupUuid;
	private String condUuid;
	private String keyName;
    private String dispName;
    private String operator;
    private String value;
    private String fieldDataType;
    private String fieldUuid;
    private Integer sortNo;
}
