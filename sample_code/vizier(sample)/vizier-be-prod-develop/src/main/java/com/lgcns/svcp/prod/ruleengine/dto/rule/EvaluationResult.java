package com.lgcns.svcp.prod.ruleengine.dto.rule;

import java.util.List;

import lombok.Data;

@Data
public class EvaluationResult {
    private boolean passed;
    private String passedMessage;
    private List<String> failedCondUuids;
    private List<String> passedCondUuids;

    public EvaluationResult(boolean passed, String passedMessage, List<String> failed, List<String> passedList) {
        this.passed = passed;
        this.passedMessage = passedMessage;
        this.failedCondUuids = failed;
        this.passedCondUuids = passedList;
    }
}
