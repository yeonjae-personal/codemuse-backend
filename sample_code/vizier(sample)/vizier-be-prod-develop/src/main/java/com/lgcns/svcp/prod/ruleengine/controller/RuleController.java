package com.lgcns.svcp.prod.ruleengine.controller;

import java.util.List;

import org.jeasy.rules.api.Facts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.ruleengine.dto.rule.EvaluationResult;
import com.lgcns.svcp.prod.ruleengine.dto.rule.RegisteredRuleInfo;
import com.lgcns.svcp.prod.ruleengine.dto.rule.RuleInput;
import com.lgcns.svcp.prod.ruleengine.dto.rule.RuleInsertRequest;
import com.lgcns.svcp.prod.ruleengine.dto.rule.RuleTestInput;
import com.lgcns.svcp.prod.ruleengine.service.RuleService;

import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@RestController
@RequestMapping("rule-engine/rule")
@Tag(name = "rule-engine-rule-controller", description = "룰엔진 조건 컨트롤러")
public class RuleController {
	
	@Autowired
	private RuleService ruleService;
	
	@PostMapping("/test")
	public EvaluationResult testRule(@RequestBody RuleTestInput input) {
		Facts facts = new Facts();
		input.getFactsData().forEach(facts::put);
		EvaluationResult result = ruleService.testRule(facts, input);
		return result;
	}

	// 등록된 룰 확인용
	@GetMapping("/registered-details")
	public List<RegisteredRuleInfo> listRegisteredRulesWithConditions() {
		return ruleService.listRegisteredRulesWithConditions();
	}
	
	@PostMapping
	public RuleInput saveRule(@RequestBody RuleInput ruleInput) {
		return ruleService.saveRule(ruleInput);
	}
	
	@PostMapping("/structure")
	public void saveRuleStructure(@RequestBody RuleInsertRequest request) {
	    ruleService.insertRuleWithTree(request);
	}
}
