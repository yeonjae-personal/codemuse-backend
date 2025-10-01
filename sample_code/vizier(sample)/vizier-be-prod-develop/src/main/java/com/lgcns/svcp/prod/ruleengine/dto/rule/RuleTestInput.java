package com.lgcns.svcp.prod.ruleengine.dto.rule;

import java.util.Map;

import com.lgcns.svcp.prod.ruleengine.dto.condition.ConditionGroupDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RuleTestInput {
	
	private String ruleName;
	private String ruleMsg;
	private Map<String, Object> factsData;
	private ConditionGroupDto conditionGroupDto; 
}
