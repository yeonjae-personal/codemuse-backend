package com.lgcns.svcp.prod.ruleengine.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.svcp.prod.ruleengine.dto.condition.ConditionDto;
import com.lgcns.svcp.prod.ruleengine.dto.condition.ConditionGroupDto;

import com.lgcns.svcp.prod.dataaccess.CommonDao;


@Service
@Transactional
public class ConditionService {
	
	@Autowired
	private CommonDao commonDao;
	
	public List<ConditionGroupDto> selectConditionGroupsByRuleId(String ruleUuid) {
		return commonDao.selectList("Rule-Condition.selectConditionGroupsByRuleId", ruleUuid);
	}
	
	public List<ConditionDto> selectConditionsByRuleId(String ruleUuid) {
		return commonDao.selectList("Rule-Condition.selectConditionsByRuleId", ruleUuid);
	}
	
	public ConditionGroupDto getRuleConditionTree(String ruleUuid) {
	    List<ConditionGroupDto> groupRows = selectConditionGroupsByRuleId(ruleUuid);
	    List<ConditionDto> condRows = selectConditionsByRuleId(ruleUuid);
	    Map<String, ConditionGroupDto> groupMap = new HashMap<>();
	    Map<String, String> parentMap = new HashMap<>();

	    // 그룹 먼저 생성
	    for (ConditionGroupDto row : groupRows) {
	        String groupId = row.getCondGroupUuid();
	        String parentId = row.getHpstCondGroupUuid();
	        if(parentId == null) parentId = "";
	        String logicType = row.getLogicType();

	        ConditionGroupDto group = new ConditionGroupDto();
	        group.setLogicType(logicType);
	        group.setCondition(new ArrayList<>());

	        groupMap.put(groupId, group);
	        parentMap.put(groupId, parentId);
	    }

	    // 조건 추가
	    for (ConditionDto row : condRows) {
	        String groupId = row.getCondGroupUuid();
	        
	        ConditionGroupDto group = groupMap.get(groupId);
	        if (group != null) {
	            group.getCondition().add(row);
	        } 
	        else {
//	            log.warn("조건이 소속된 그룹이 존재하지 않습니다. groupId={}, ruleUuid={}", groupId, ruleUuid);
	        }
	    }

	    // 그룹 트리 구성
	    String rootGroupId = null;
	    for (String groupId : groupMap.keySet()) {
	        String parentId = parentMap.get(groupId);
	        
	        if (parentMap.get(groupId).isEmpty()) {
	            rootGroupId = groupId;
	        }
	        else {
	            ConditionGroupDto parent = groupMap.get(parentId);
	            ConditionGroupDto child = groupMap.get(groupId);
	            if (parent != null) {
	                parent.getCondition().add(child);
	            }
	            else {
//	                log.warn("부모 그룹이 존재하지 않아 연결할 수 없습니다. parentId={}, childId={}", parentId, groupId);
	            }
	        }
	    }
	    if (rootGroupId == null || !groupMap.containsKey(rootGroupId)) {
	        throw new IllegalStateException("루트 그룹을 찾을 수 없습니다. ruleUuid=" + ruleUuid);
	    }

	    return groupMap.get(rootGroupId);
	}
	
	//TODO : RuleService에 있는 컨디션 DB Insert를 여기에다 따로 분리하여 사용하는게 맞지 않을까
}
