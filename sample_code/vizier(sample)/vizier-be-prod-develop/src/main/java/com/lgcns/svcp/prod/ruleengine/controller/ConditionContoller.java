package com.lgcns.svcp.prod.ruleengine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.ruleengine.dto.condition.ConditionGroupDto;
import com.lgcns.svcp.prod.ruleengine.service.ConditionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@RestController
@RequestMapping("rule-engine/condition")
@Tag(name = "rule-engine-condition-controller", description = "룰엔진 조건 컨트롤러")
/**
 * 
 * @Author	: 이훈민(A76485@cnspartners.com)
 * @Date	: 2025. 4. 23.
 */
public class ConditionContoller {
	@Autowired
	private ConditionService conditionService;
	
	@GetMapping
	@Operation(summary = "조건 기본 전체 조회 API", description = "룰 아이디로 조건 트리 조회")
	public ConditionGroupDto getRuleConditionTree(@RequestParam("ruleUuid") String ruleUuid) {
	    ConditionGroupDto tree = conditionService.getRuleConditionTree(ruleUuid);
	    return tree;
	}
}
