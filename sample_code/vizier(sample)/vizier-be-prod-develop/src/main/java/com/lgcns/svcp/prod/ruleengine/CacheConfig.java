//package com.lgcns.svcp.prod.ruleengine;
//
//import java.util.concurrent.TimeUnit;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//
//import com.github.benmanes.caffeine.cache.Caffeine;
//import com.github.benmanes.caffeine.cache.LoadingCache;
//import com.lgcns.svcp.prod.ruleengine.dto.condition.ConditionGroupDto;
//import com.lgcns.svcp.prod.ruleengine.dto.rule.RuleDto;
//import com.lgcns.svcp.prod.ruleengine.service.ConditionService;
//import com.lgcns.svcp.prod.ruleengine.service.RuleService;
//import com.lgcns.svcp.prod.ruleengine.wrapper.PredicateWithTracking;
//import com.lgcns.svcp.prod.ruleengine.wrapper.RuleWrapper;
//
//@Configuration
//public class CacheConfig {
//
//	@Autowired
//	private ConditionService conditionService;
//	@Autowired
//	@Lazy
//	private RuleService ruleService;
//    @Bean
//    public LoadingCache<String, RuleWrapper> ruleCache() {
//        return Caffeine.newBuilder()
//            .maximumSize(1000)
//            .expireAfterAccess(10, TimeUnit.MINUTES)
//            .recordStats()
//            .build(ruleId -> loadRuleFromDB(ruleId));
//    }
//
//    private RuleWrapper loadRuleFromDB(String ruleUuid) {a
//        // 기존 DB 로딩 로직 그대로 사용
//    	ConditionGroupDto root = conditionService.getRuleConditionTree(ruleUuid);
//		RuleDto ruleInfo = ruleService.getRuleInfoByUuid(ruleUuid);
//		PredicateWithTracking predicate = ruleService.convertGroupToPredicate(root);
//
//		RuleWrapper wrapper = new RuleWrapper(
//				ruleUuid,
//				ruleInfo.getRuleName(),
//				ruleInfo.getRuleMsg(),
//				predicate,
//				() -> System.out.println("Rule fired: " + ruleUuid),
//				root
//				);
//		return wrapper;
//    }
//}
