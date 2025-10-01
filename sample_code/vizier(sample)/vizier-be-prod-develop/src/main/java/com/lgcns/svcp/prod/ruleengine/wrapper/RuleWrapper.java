package com.lgcns.svcp.prod.ruleengine.wrapper;

import java.util.ArrayList;
import java.util.List;

import org.jeasy.rules.api.Facts;

import com.lgcns.svcp.prod.ruleengine.dto.condition.ConditionGroupDto;
import com.lgcns.svcp.prod.ruleengine.dto.rule.EvaluationResult;

public class RuleWrapper {
    private final String ruleUuid;
    private final String ruleName;
    private final String ruleMsg;
    private final PredicateWithTracking predicate;
    private final Runnable thenAction;
    private final ConditionGroupDto conditionTree; // 추가

    public RuleWrapper(String ruleUuid, String ruleName, String ruleMsg, PredicateWithTracking predicate, 
    		Runnable thenAction, ConditionGroupDto conditionTree) {
        this.ruleUuid = ruleUuid;
        this.ruleName = ruleName;
        this.ruleMsg = ruleMsg;
        this.predicate = predicate;
        this.thenAction = thenAction;
        this.conditionTree = conditionTree;
    }

    public EvaluationResult evaluate(Facts facts) {
        List<String> failed = new ArrayList<>();
        List<String> passed = new ArrayList<>();

        boolean result = predicate.test(facts, failed, passed);

        return new EvaluationResult(result, ruleMsg, failed, passed);
    }

    public void execute() {
        thenAction.run();
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
