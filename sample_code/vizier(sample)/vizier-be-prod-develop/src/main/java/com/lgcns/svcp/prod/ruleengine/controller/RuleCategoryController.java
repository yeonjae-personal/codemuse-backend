package com.lgcns.svcp.prod.ruleengine.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.svcp.prod.ruleengine.dto.category.RuleCategoryTreeDto;
import com.lgcns.svcp.prod.ruleengine.service.RuleCategoryService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Hidden
@CrossOrigin
@RestController
@RequestMapping("rule-engine/categories")
@Tag(name = "rule-engine-category-controller", description = "룰엔진 카테고리 컨트롤러")
/**
 * 
 * @Author	: 이훈민(A76485@cnspartners.com)
 * @Date	: 2025. 4. 23.
 */
public class RuleCategoryController {
	
	@Autowired
	private RuleCategoryService ruleCategoryService;
	
	@GetMapping
	@Operation(summary = "룰 카테고리 기본 전체 조회 API", description = "룰 카테고리 기본 정보 전체 조회 API")
	public List<RuleCategoryTreeDto> retrieveRuleCategoryList() {
		List<RuleCategoryTreeDto> response = ruleCategoryService.retrieveRuleCategoryList();
		return response;
	}
	
	@GetMapping(value = "/tree")
	@Operation(summary = "get category tree", description = "get category tree")
	public List<Map<String, Object>> retrieveRuleCatgoryTreeList(@RequestParam(required = false) String searchBy, @RequestParam(required = false) String name) {
		List<RuleCategoryTreeDto> results = new ArrayList<>();
		if (StringUtils.isNotBlank(name) && searchBy.equals("category")) {
			List<RuleCategoryTreeDto> categories = ruleCategoryService.getCategory(name.toLowerCase().trim());
			List<String> categoryStrs = new ArrayList<>();
			if (categories != null && !categories.isEmpty()) {
				categoryStrs = categories.stream().map(RuleCategoryTreeDto::getRuleCtgrUuid).toList();
			}
			List<RuleCategoryTreeDto> newCategories = new ArrayList<>();
			List<String> newCategoryStrs = new ArrayList<>();
			List<RuleCategoryTreeDto> categoryBelows = ruleCategoryService.getCategoryBelow();
			List<RuleCategoryTreeDto> newCategoryBelows = new ArrayList<>();
			for (RuleCategoryTreeDto item: categoryBelows) {
				if (!categoryStrs.contains(item.getHpstRuleCtgrUuid())) {
					if (item.getRuleCtgrName().toLowerCase().contains(name.toLowerCase())) {
						if (!newCategoryStrs.contains(item.getHpstRuleCtgrUuid())) {
							RuleCategoryTreeDto category = ruleCategoryService.getCategoryByUuid(item.getHpstRuleCtgrUuid());
							newCategories.add(category);
							newCategoryStrs.add(item.getHpstRuleCtgrUuid());
						}
						newCategoryBelows.add(item);
					}
				} else {
					if (!newCategoryStrs.contains(item.getHpstRuleCtgrUuid())) {
						RuleCategoryTreeDto category = ruleCategoryService.getCategoryByUuid(item.getHpstRuleCtgrUuid());
						newCategories.add(category);
						newCategoryStrs.add(item.getHpstRuleCtgrUuid());
					}
					newCategoryBelows.add(item);
				}
			}
			results.addAll(newCategories);
			results.addAll(newCategoryBelows);
			Map<String, List<RuleCategoryTreeDto>> ruleCategoryMap = results.stream()
					.collect(Collectors.groupingBy(dto -> dto.getHpstRuleCtgrUuid() != null ? dto.getHpstRuleCtgrUuid() : ""));
			List<Map<String, Object>> tree = ruleCategoryService.buildTree(ruleCategoryMap, "");
			return tree;
		} else if (StringUtils.isNotBlank(name) && searchBy.equals("rule")) {
			results = ruleCategoryService.retrieveRuleCategoryList();
			Map<String, List<RuleCategoryTreeDto>> ruleCategoryMap = results.stream()
					.collect(Collectors.groupingBy(dto -> dto.getHpstRuleCtgrUuid() != null ? dto.getHpstRuleCtgrUuid() : ""));
			List<Map<String, Object>> tree = ruleCategoryService.buildTreeByRuleName(ruleCategoryMap, "", name);
			return tree;
		} else {
			List<RuleCategoryTreeDto> response = ruleCategoryService.retrieveRuleCategoryList();
			Map<String, List<RuleCategoryTreeDto>> ruleCategoryMap = response.stream()
					.collect(Collectors.groupingBy(dto -> dto.getHpstRuleCtgrUuid() != null ? dto.getHpstRuleCtgrUuid() : ""));
			List<Map<String, Object>> tree = ruleCategoryService.buildTree(ruleCategoryMap, "");
			return tree;
		}
	}

	@PostMapping
	@Operation(summary = "save category", description = "save category")
	public void saveCategory(@RequestBody RuleCategoryTreeDto ruleCategory) {
		ruleCategoryService.saveChildCategory(ruleCategory);
	}
	
	@DeleteMapping("/{uuid}")
	@Operation(summary = "delete category below", description = "delete category below")
	public void deleteCategoryBelow(@PathVariable String uuid) {
		ruleCategoryService.deleteCategoryBelow(uuid);
	}
}
