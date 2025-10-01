package com.lgcns.svcp.prod.ruleengine.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.svcp.prod.dataaccess.CommonDao;
import com.lgcns.svcp.prod.exception.BusinessException;
import com.lgcns.svcp.prod.ruleengine.dto.category.RuleCategoryTreeDto;
import com.lgcns.svcp.prod.ruleengine.dto.category.RuleInfoDto;

@Service
@Transactional
public class RuleCategoryService {
	
	@Autowired
	private CommonDao commonDao;

	public List<RuleCategoryTreeDto> retrieveRuleCategoryList() {
		return commonDao.selectList("Rule-Category.retrieveRuleCategoryDto");
	}
	
	public List<RuleCategoryTreeDto> getCategory(String name) {
		return commonDao.selectList("Rule-Category.getCategory", formatNameWithSpecialText(name));
	}
	
	public List<RuleCategoryTreeDto> getCategoryByName(String sortBy, String name) {
		if (StringUtils.isNotBlank(sortBy) && sortBy.equals("category")) {
			Map<String, Object> params = new HashMap<>();
			params.put("ruleCtgrName", name.toLowerCase().trim());
			return commonDao.selectList("Rule-Category.retrieveRuleCategoryByName", params);
		} else {
			return commonDao.selectList("Rule-Category.retrieveRuleCategoryDto");
		}
	}

	public List<RuleInfoDto> retrieveRuleInfoList() {
		return commonDao.selectList("Rule-Category.retrieveRuleInfoDto");
	}

	public List<Map<String, Object>> selectAllCategories() {
		return commonDao.selectList("Rule-Category.selectAllCategories");
	}

	public List<String> selectRuleUuidsByCategoryId(Map<String, Object> params) {
		return commonDao.selectList("Rule-Category.selectRuleUuidsByCategoryIdAndRuleName", params);
	}

	public List<Map<String, Object>> selectRulesByUuids(Map<String, Object> uuids) {
		return commonDao.selectList("Rule-Category.selectRulesByUuids", uuids);
	}

	/**
	 * 
	 * @Author	: 이훈민(A76485@cnspartners.com)
	 * @Date	: 2024. 5. 9.
	 * @MethodName	: buildTree
	 * @Method 설명	: 카테고리 트리 형태 메소드
	 * @Param	:
	 * @return	: List<Map<String,Object>>
	 */
	public List<Map<String, Object>> buildTreeByRuleName(Map<String, List<RuleCategoryTreeDto>> categoryMap, String parentId, String name) {
		List<Map<String, Object>> result = new ArrayList<>();
		List<RuleCategoryTreeDto> subCategories = categoryMap.get(parentId);
		if (subCategories != null) {
			for (RuleCategoryTreeDto category : subCategories) {
				boolean added = true;
				LinkedHashMap<String, Object> node = new LinkedHashMap<>();
				node.put("ruleCtgrUuid", category.getRuleCtgrUuid());
				node.put("ruleCtgrName", category.getRuleCtgrName());

				List<Map<String, Object>> children = buildTreeByRuleName(categoryMap, category.getRuleCtgrUuid(), name);
				if (!children.isEmpty()) {
					node.put("children", children);
				} else if (children.isEmpty()) {
					Map<String, Object> params = new HashMap<>();
					params.put("ruleCtgrUuid", category.getRuleCtgrUuid());
					params.put("ruleName", formatNameWithSpecialText(name.toLowerCase().trim()));
					List<String> ruleUuids = selectRuleUuidsByCategoryId(params);
					if (ruleUuids != null && !ruleUuids.isEmpty()) {
						Map<String, Object> param = new HashMap<>();
						param.put("ruleUuids", ruleUuids);
						List<Map<String, Object>> rules = selectRulesByUuids(param);
						List<RuleInfoDto> ruleDtos = rules.stream()
								.map(r -> new RuleInfoDto(
										(String) r.get("ruleUuid"),
										(String) r.get("ruleName"),
										(String) r.get("ovwCntn"),
										(String) r.get("chgDeptName"),
										(String) r.get("chgUser"),
										(String) r.get("ruleMsg"),
										(String) r.get("useYn"),
										(Date) r.get("rgstDtm")
										)).toList();
						node.put("rules", ruleDtos);
					} else {
						added = false;
					}
				}
				if (added) {
					result.add(node);
				}
			}
		}
		return result;
	}
	
	private String formatNameWithSpecialText(String input) {
		return input
		        .replace("\\", "\\\\")
		        .replace("%", "\\%")
		        .replace("_", "\\_");
	}

	public List<Map<String, Object>> buildTree(Map<String, List<RuleCategoryTreeDto>> categoryMap, String parentId) {
		List<Map<String, Object>> result = new ArrayList<>();
		
		List<RuleCategoryTreeDto> subCategories = categoryMap.get(parentId);

		if (subCategories != null) {
			for (RuleCategoryTreeDto category : subCategories) {
				
				LinkedHashMap<String, Object> node = new LinkedHashMap<>();
				node.put("ruleCtgrUuid", category.getRuleCtgrUuid());
				node.put("ruleCtgrName", category.getRuleCtgrName());

				List<Map<String, Object>> children = buildTree(categoryMap, category.getRuleCtgrUuid());
				if (!children.isEmpty()) {
					node.put("children", children);
				} else if (children.isEmpty()) {
					Map<String, Object> params = new HashMap<>();
					params.put("ruleCtgrUuid", category.getRuleCtgrUuid());
					List<String> ruleUuids = selectRuleUuidsByCategoryId(params);
					if (ruleUuids != null && !ruleUuids.isEmpty()) {
						Map<String, Object> param = new HashMap<>();
						param.put("ruleUuids", ruleUuids);
						List<Map<String, Object>> rules = selectRulesByUuids(param);
						List<RuleInfoDto> ruleDtos = rules.stream()
								.map(r -> new RuleInfoDto(
										(String) r.get("ruleUuid"),
										(String) r.get("ruleName"),
										(String) r.get("ovwCntn"),
										(String) r.get("chgDeptName"),
										(String) r.get("chgUser"),
										(String) r.get("ruleMsg"),
										(String) r.get("useYn"),
										(Date) r.get("rgstDtm")
										)).toList();
						node.put("rules", ruleDtos);
					} else {
						node.put("rules", new ArrayList<>());
					}
				}
				result.add(node);
			}
		}
		return result;
	}

	public void saveChildCategory(RuleCategoryTreeDto category) {
		if (StringUtils.isNotBlank(category.getRuleCtgrUuid())) {
			commonDao.update("Rule-Category.updateChildCategory", category);
		} else {
			category.setTclsCtgrYn("N");
			category.setUseYn("Y");
			commonDao.insert("Rule-Category.insertChildCategory", category);
		}
	}

	public void deleteCategoryBelow(String categoryUuid) {
		int count1 = commonDao.select("Rule-Category.checkCategoryParent", categoryUuid);
		if (count1 > 0) {
			throw new BusinessException("Please don't remove category parent");
		}
		int count2 = commonDao.select("Rule-Category.countRuleByCategoryUuid", categoryUuid);
		if (count2 > 0) {
			throw new BusinessException("Category has rule. Please don't remove");
		}
		commonDao.delete("Rule-Category.deleteCategoryBelow", categoryUuid);
	}

	public List<RuleCategoryTreeDto> getCategoryBelow() {
		return commonDao.selectList("Rule-Category.getCategoryBelow");
	}

	public RuleCategoryTreeDto getCategoryByUuid(String uuid) {
		return commonDao.select("Rule-Category.getCategoryByUuid", uuid);
	}
}
