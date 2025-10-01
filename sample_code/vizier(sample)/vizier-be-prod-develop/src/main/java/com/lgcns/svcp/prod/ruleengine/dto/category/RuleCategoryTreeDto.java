package com.lgcns.svcp.prod.ruleengine.dto.category;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class RuleCategoryTreeDto {
	private String ruleCtgrUuid;
	private String ruleCtgrName;
	private String hpstRuleCtgrUuid;
	private String overview;
	private String tclsCtgrYn;
	private String useYn;
	private String rgstUser;
	private String rgstDtm;
	private String updUser;
	private String updDtm;
	private List<RuleCategoryTreeDto> children;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<RuleInfoDto> rules;
}