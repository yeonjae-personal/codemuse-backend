package com.lgcns.svcp.prod.ruleengine.wrapper;

import java.util.List;

import org.jeasy.rules.api.Facts;

@FunctionalInterface
public interface PredicateWithTracking {
	boolean test(Facts facts, List<String> failedConditions, List<String> passedConditions);
}
