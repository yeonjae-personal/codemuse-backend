package com.lgcns.svcp.prod.ruleengine.dto.rule;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class RuleFireResult {
    private boolean success;
    private String passedMessage;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> failedConditions;

    public RuleFireResult(boolean success, String passedMessage, List<String> failedConditions) {
        this.success = success;
        this.passedMessage = passedMessage;
        this.failedConditions = failedConditions;
    }
}
