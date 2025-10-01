package com.lgcns.svcp.prod.ruleengine.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jeasy.rules.api.Facts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.svcp.prod.ruleengine.dto.condition.ConditionDto;
import com.lgcns.svcp.prod.ruleengine.dto.condition.ConditionGroupDto;
import com.lgcns.svcp.prod.ruleengine.dto.rule.EvaluationResult;
import com.lgcns.svcp.prod.ruleengine.dto.rule.RegisteredRuleInfo;
import com.lgcns.svcp.prod.ruleengine.dto.rule.RuleDto;
import com.lgcns.svcp.prod.ruleengine.dto.rule.RuleInput;
import com.lgcns.svcp.prod.ruleengine.dto.rule.RuleInsertRequest;
import com.lgcns.svcp.prod.ruleengine.dto.rule.RuleTestInput;
import com.lgcns.svcp.prod.ruleengine.wrapper.PredicateWithTracking;
import com.lgcns.svcp.prod.ruleengine.wrapper.RuleWrapper;
import com.lgcns.svcp.prod.util.DateUtil;
import com.lgcns.svcp.prod.util.UuidUtil;

import com.lgcns.svcp.prod.dataaccess.CommonDao;
import jakarta.annotation.PostConstruct;

@Service
@Transactional
public class RuleService {

	@Autowired
	private ConditionService conditionService;

	@Autowired
	private CommonDao commonDao;

	// 메모리에 등록된 룰 저장소
	private final Map<String, RuleWrapper> ruleRegistry = new ConcurrentHashMap<>();

	private RuleDto getRuleInfoByUuid(String ruleUuid) {
		return commonDao.select("Rule-Rule.selectRuleByUuid", ruleUuid);
	}

	private List<String> getRuleUuids() {
		return commonDao.selectList("Rule-Rule.selectRuleUuids");
	}

	//룰 등록
	public void registerRule(String ruleUuid) {
		ConditionGroupDto root = conditionService.getRuleConditionTree(ruleUuid);
		RuleDto ruleInfo = getRuleInfoByUuid(ruleUuid);
		PredicateWithTracking predicate = convertGroupToPredicate(root);

		RuleWrapper wrapper = new RuleWrapper(
				ruleUuid,
				ruleInfo.getRuleName(),
				ruleInfo.getRuleMsg(),
				predicate,
				() -> System.out.println("Rule fired: " + ruleUuid),
				root
				);

		ruleRegistry.put(ruleUuid, wrapper);
	}

	//백엔드 실행시 전체 룰 등록
	@PostConstruct
	public void registerAllUrles() {
		List<String> ruleUuids = getRuleUuids();
		for(String ruleUuid : ruleUuids) {
			registerRule(ruleUuid);	
		}
	}

	public EvaluationResult fireRule(String ruleId, Facts facts) {
		RuleWrapper wrapper = ruleRegistry.get(ruleId);
		if (wrapper == null) {
			throw new IllegalArgumentException("등록되지 않은 ruleId: " + ruleId);
		}

		EvaluationResult result = wrapper.evaluate(facts);

		if (!result.isPassed()) {
			result.setPassedMessage(null);
		}

		return result;
	}

	public EvaluationResult testRule(Facts facts, RuleTestInput input) {
		String ruleUuid = UuidUtil.generateRandomUUID();
		ConditionGroupDto root = buildConditionGroup(input.getConditionGroupDto());
		PredicateWithTracking predicate = convertGroupToPredicate(root);

		RuleWrapper wrapper = new RuleWrapper(
				ruleUuid,
				input.getRuleName(),
				input.getRuleMsg(),
				predicate,
				() -> System.out.println("Rule fired: " + ruleUuid),
				root
				);
		ruleRegistry.put(ruleUuid, wrapper);
		EvaluationResult result = fireRule(ruleUuid, facts);
		ruleRegistry.remove(ruleUuid);
		return result;
	}

	private ConditionGroupDto buildConditionGroup(Object structures) {
		ConditionGroupDto result = new ConditionGroupDto();
		List<Object> conditions = new ArrayList<>();
		if (structures instanceof ConditionGroupDto groupDto) {
			result.setLogicType(groupDto.getLogicType());
			for (Object obj : groupDto.getCondition()) {
				if (obj instanceof LinkedHashMap map) {
					if (map.containsKey("logicType")) {
						conditions.add(buildConditionGroup(map));
					} else {
						conditions.add(convertToCondDto(map));
					}
				}
			}
		} 
		else if (structures instanceof LinkedHashMap map) {
			result.setLogicType((String) map.get("logicType"));

			List<Object> conditionRaw = (List<Object>) map.get("condition");
			for (Object obj : conditionRaw) {
				if (obj instanceof LinkedHashMap map1) {
					if (map1.containsKey("logicType")) {
						conditions.add(buildConditionGroup(map1));
					} else {
						conditions.add(convertToCondDto(map1));
					}
				}
			}
		}
		result.setCondition(conditions);
		return result;
	}

	//TODO: 전체 등록된 룰에 모두 실행
	//	public void fireAllRules(Facts facts) {
	//		for (RuleWrapper wrapper : ruleRegistry.values()) {
	//			List<String> failedConditions = new ArrayList<>();
	//			if (wrapper.evaluate(facts, failedConditions)) {
	//				wrapper.execute();
	//			}
	//		}

	private PredicateWithTracking convertGroupToPredicate(ConditionGroupDto group) {
		if (group.getCondition() == null || group.getCondition().isEmpty()) {
			return (facts, failedConditions, passedCondtions) -> {
				failedConditions.add("조건이 없는 그룹 (invalid group)");
				return false;
			};
		}	    

		List<PredicateWithTracking> predicates = new ArrayList<>();

		// 개별 조건들 처리 - UI 순서대로만 처리
		for (Object child : group.getCondition()) {
			if (child instanceof ConditionGroupDto) {
				ConditionGroupDto subGroup = (ConditionGroupDto) child;
				predicates.add(convertGroupToPredicate(subGroup));
				// List<PredicateWithTracking> subPredicates = convertGroupToIndividualPredicates(subGroup);
				// predicates.addAll(subPredicates);
			} else if (child instanceof ConditionDto) {
				ConditionDto cond = (ConditionDto) child;
				predicates.add(buildSingleConditionPredicate(cond));
			} else {
				throw new IllegalStateException("조건 타입 오류: " + child.getClass());
			}
		}

		// 각 그룹의 로직 타입에 따라 다르게 처리
		if ("AND".equalsIgnoreCase(group.getLogicType())) {
			return (facts, failedConditions, passedConditions) -> {
				List<String> tempFailedConditions = new ArrayList<>();
				boolean allPass = true;

				for (int i = 0; i < predicates.size(); i++) {
					PredicateWithTracking p = predicates.get(i);
					List<String> tempEachFailed = new ArrayList<>();
					List<String> tempEachPassed = new ArrayList<>();

					boolean result = p.test(facts, tempEachFailed, tempEachPassed);

					// 개별 조건 결과를 항상 상위로 전달
					passedConditions.addAll(tempEachPassed);
					tempFailedConditions.addAll(tempEachFailed);

					if (result) {
						//System.out.println("AND 그룹 통과: " + tempEachPassed);
					} else {
//						System.out.println("AND 그룹 실패: " + tempEachFailed);
						allPass = false;

						// AND에서 실패 시 즉시 중단
//						System.out.println("AND 그룹에서 실패 발생 - 중단");
						break;
					}
				}

				failedConditions.addAll(tempFailedConditions);
//				System.out.println("=== AND 그룹 평가 완료: " + allPass + " ===");
				return allPass;
			};

		} else { // OR 로직
			return (facts, failedConditions, passedConditions) -> {
				List<String> tempFailedConditions = new ArrayList<>();
				boolean anyPass = false;

				// OR에서는 무조건 모든 분기 평가
				for (int i = 0; i < predicates.size(); i++) {
					PredicateWithTracking p = predicates.get(i);
					List<String> tempEachFailed = new ArrayList<>();
					List<String> tempEachPassed = new ArrayList<>();

//					System.out.println("OR 그룹 - predicate " + (i+1) + "/" + predicates.size() + " 평가 시작");
					boolean result = p.test(facts, tempEachFailed, tempEachPassed);
//					System.out.println("OR 그룹 - predicate " + (i+1) + " 결과: " + result);

					// 개별 조건 결과를 항상 상위로 전달
					passedConditions.addAll(tempEachPassed);
					tempFailedConditions.addAll(tempEachFailed);

					if (result) {
//						System.out.println("OR 그룹 통과: " + tempEachPassed);
						anyPass = true;
					} else {
//						System.out.println("OR 그룹 실패: " + tempEachFailed);
					}
					// OR에서는 break 하지 않음
				}

				failedConditions.addAll(tempFailedConditions);
//				System.out.println("=== OR 그룹 평가 완료: " + anyPass + " ===");
				return anyPass;
			};
		}
	}

	private PredicateWithTracking buildSingleConditionPredicate(ConditionDto cond) {
		return (facts, failedConditions, passedConditions) -> {
			Object factValue = facts.get(cond.getKeyName());

			if (factValue == null) {
				failedConditions.add(cond.getCondUuid());
				return false;
			}

			String operator = cond.getOperator();
			String condValue = cond.getValue();
			boolean match = false;

			if ("IN".equalsIgnoreCase(operator)) {
				String[] targets = condValue.split(",");
				for (String target : targets) {
					if (factValue.toString().trim().equals(target.trim())) {
						match = true;
						break;
					}
				}
			}
			else if ("NOT IN".equalsIgnoreCase(operator)) {
				List<String> target = Arrays.stream(condValue.split(","))
						.map(String::trim)
						.collect(Collectors.toList());

				if(!target.contains(factValue)) {
					match = true;
				}
			}

			else if (isNumeric(factValue.toString()) && isNumeric(condValue)) {
				double factNum = Double.parseDouble(factValue.toString());
				double condNum = Double.parseDouble(condValue);

				match = switch (operator) {
				case ">" -> factNum > condNum;
				case ">=" -> factNum >= condNum;
				case "<" -> factNum < condNum;
				case "<=" -> factNum <= condNum;
				case "==" -> factNum == condNum;
				case "!=" -> factNum != condNum;
				default -> false;
				};
			}
			else {
				match = switch (operator) {
				case "==" -> factValue.toString().equals(condValue);
				case "!=" -> !factValue.toString().equals(condValue);
				default -> false;
				};
			}
			if (!match) {
				failedConditions.add(cond.getCondUuid());
			}
			else {
				passedConditions.add(cond.getCondUuid());
			}

			return match;
		};
	}


	private boolean isNumeric(String str) {
		if (str == null) return false;
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public List<RegisteredRuleInfo> listRegisteredRulesWithConditions() {
		return ruleRegistry.values().stream()
				.map(wrapper -> new RegisteredRuleInfo(wrapper.getRuleUuid(), wrapper.getRuleName(), wrapper.getRuleMsg(), wrapper.getConditionTree()))
				.toList();
	}

	public void insertRuleWithTree(RuleInsertRequest request) {
		String ruleUuid = request.getRuleUuid();
		Map<String, Object> updateRuleDetail = new HashMap<>();
		updateRuleDetail.put("ruleUuid", ruleUuid);
		updateRuleDetail.put("ruleMsg", request.getRuleMsg());
		commonDao.update("Rule-Rule.updateRuleWithStructure", updateRuleDetail);
		List<String> groupUuids = commonDao.selectList("Rule-Condition.getListGroupUuidByRule", ruleUuid);
		commonDao.batchDelete("Rule-Condition.deleteConditionByGroupUuid", groupUuids);
		commonDao.delete("Rule-Condition.deleteGroupByRuleUuid", ruleUuid);
		AtomicInteger groupSort = new AtomicInteger(1);
		saveGroupRecursive(ruleUuid, null, request.getConditionTree(), groupSort);
		//register rule
		ConditionGroupDto root = buildConditionGroup(request.getConditionTree());
		PredicateWithTracking predicate = convertGroupToPredicate(root);
		RuleWrapper wrapper = new RuleWrapper(
				ruleUuid,
				request.getRuleName(),
				request.getRuleMsg(),
				predicate,
				() -> System.out.println("Rule fired: " + ruleUuid),
				root
				);
		if (ruleRegistry.containsKey(ruleUuid)) {
			ruleRegistry.remove(ruleUuid);
		}
		ruleRegistry.put(ruleUuid, wrapper);
	}

	private void saveGroupRecursive(String ruleUuid, String parentGroupUuid,
			ConditionGroupDto group,
			AtomicInteger groupSort) {

		String groupUuid = UUID.randomUUID().toString();
		Map<String, Object> temp = new HashMap<>();
		temp.put("groupUuid", groupUuid);
		temp.put("ruleUuid", ruleUuid);
		temp.put("parentGroupUuid", parentGroupUuid);
		if (parentGroupUuid == null) {
			temp.put("tclsCondGroupYn", "Y");
		} else {
			temp.put("tclsCondGroupYn", "N");
		}
		temp.put("logicType", group.getLogicType());
		temp.put("sortNo", groupSort.getAndIncrement());
		temp.put("useYn", "Y");
		commonDao.insert("Rule-Rule.insertGroup", temp);

		for (Object obj : group.getCondition()) {
			if (obj instanceof LinkedHashMap map) {
				if (map.containsKey("logicType")) {
					ConditionGroupDto subGroup = convertToGroupDto(map);
					saveGroupRecursive(ruleUuid, groupUuid, subGroup, groupSort);
				} else {
					String condUuid = UUID.randomUUID().toString();

					ConditionDto cond = convertToCondDto(map);
					Map<String, Object> tempCond = new LinkedHashMap<>();
					tempCond.put("condUuid", condUuid);
					tempCond.put("groupUuid", groupUuid);
					tempCond.put("fieldUuid", getFieldUuidByKey(cond.getKeyName()));
					tempCond.put("operatorCode", cond.getOperator());
					tempCond.put("condValue", cond.getValue());
					tempCond.put("sortNo", cond.getSortNo());
					tempCond.put("useYn", "Y");
					commonDao.insert("Rule-Rule.insertCondition", tempCond);
				}
			}
		}
	}

	private ConditionGroupDto convertToGroupDto(Map<String, Object> map) {
		ConditionGroupDto dto = new ConditionGroupDto();

		if (map.get("logicType") == null || map.get("condition") == null) {
			throw new IllegalArgumentException("logicType 또는 condition이 누락된 조건 그룹입니다: " + map);
		}

		dto.setLogicType((String) map.get("logicType"));
		Object conditionRaw = map.get("condition");
		if (conditionRaw instanceof List<?>) {
			dto.setCondition((List<Object>) conditionRaw);
		} else {
			throw new IllegalStateException("condition이 List<Object> 형태가 아닙니다: " + conditionRaw.getClass());
		}

		return dto;
	}

	private ConditionDto convertToCondDto(Map<String, Object> map) {
		ConditionDto dto = new ConditionDto();

		if (map.get("keyName") == null || map.get("operator") == null || map.get("value") == null) {
			throw new IllegalArgumentException("Condition에 필수 항목이 빠졌습니다: " + map);
		}
		if (map.containsKey("condUuid")) {
			dto.setCondUuid((String) map.get("condUuid"));
		}
		dto.setKeyName((String) map.get("keyName"));
		dto.setDispName((String) map.get("dispName")); // dispName은 선택적일 수 있음
		dto.setOperator((String) map.get("operator"));
		dto.setValue((String) map.get("value"));
		if (map.containsKey("sortNo")) {
			Object sortNoStr = map.get("sortNo");
			if (sortNoStr != null) {
				dto.setSortNo(Integer.parseInt(sortNoStr.toString()));
			}
		}
		return dto;
	}

	public String getFieldUuidByKey(String fieldKeyName) {
		return commonDao.select("Rule-Rule.getFieldUuidByKey", fieldKeyName);
	}

	public RuleInput saveRule(RuleInput ruleInput) {
		if (StringUtils.isNotBlank(ruleInput.getRuleUuid())) {
			commonDao.update("Rule-Rule.updateRule", ruleInput);
			RuleDto ruleDetail = commonDao.select("Rule-Rule.selectRuleByUuidIgnoreCase", ruleInput.getRuleUuid());
			ruleInput.setRgstDtm(DateUtil.formatDate("yyyy-MM-dd", ruleDetail.getRgstDtm()));
		} else {
			String ruleUuid = UuidUtil.generateRandomUUID();
			ruleInput.setRuleUuid(ruleUuid);
			ruleInput.setUseYn("Y");
			ruleInput.setSortNo(1);
			commonDao.insert("Rule-Rule.insertRule", ruleInput);
			commonDao.insert("Rule-Category.insertRuleWithCategory", ruleInput);

			String groupUuid = UUID.randomUUID().toString();
			Map<String, Object> condGroup = new HashMap<>();
			condGroup.put("groupUuid", groupUuid);
			condGroup.put("ruleUuid", ruleUuid);
			condGroup.put("parentGroupUuid", null);
			condGroup.put("logicType", "AND");
			condGroup.put("sortNo", 1);
			condGroup.put("useYn", "Y");
			condGroup.put("tclsCondGroupYn", "N");
			commonDao.insert("Rule-Rule.insertGroup", condGroup);
			ruleInput.setRgstDtm(DateUtil.formatDate("yyyy-MM-dd", new Date()));
		}
		return ruleInput;
	}
}
