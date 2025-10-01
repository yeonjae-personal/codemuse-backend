package com.lgcns.svcp.prod.ruleengine.dto.rule;

import java.util.Date;

import lombok.Data;

@Data
public class RuleDto {
	private String ruleUuid;
	private String ruleName;
	private String ruleMsg;
	private Date rgstDtm;
}
