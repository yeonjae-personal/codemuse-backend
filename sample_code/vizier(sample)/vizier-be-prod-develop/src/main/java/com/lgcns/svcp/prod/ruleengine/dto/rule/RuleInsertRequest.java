package com.lgcns.svcp.prod.ruleengine.dto.rule;

import com.lgcns.svcp.prod.ruleengine.dto.condition.ConditionGroupDto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RuleInsertRequest {
    private String ruleUuid;
    private String ruleName;
    private String ruleMsg;
    private ConditionGroupDto conditionTree;
}
