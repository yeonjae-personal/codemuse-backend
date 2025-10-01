package com.lgcns.svcp.prod.ruleengine.dto.rule;

import com.lgcns.svcp.prod.ruleengine.dto.condition.ConditionGroupDto;

public class RegisteredRuleInfo {
    private String ruleUuid;
    private String ruleName;
    private String ruleMsg;
    private ConditionGroupDto conditionTree;

    public RegisteredRuleInfo(String ruleUuid, String ruleName, String ruleMsg, ConditionGroupDto conditionTree) {
        this.ruleUuid = ruleUuid;
        this.ruleName = ruleName;
        this.ruleMsg = ruleMsg;
        this.conditionTree = conditionTree;
    }

    public String getRuleUuid() {
        return ruleUuid;
    }

    public String getRuleName() {
        return ruleName;
    }
    
    public String getRuleMsg() {
        return ruleMsg;
    }

    public ConditionGroupDto getConditionTree() {
        return conditionTree;
    }
}

