package com.lgcns.svcp.prod.ruleengine.dto.rule;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RuleInput {
	
	private String ruleUuid;
	private String ruleName;
	private String ruleCtgrUuid;
	private Integer sortNo;
	private String useYn;
	private String chgDeptName;
	private String chgUser;
	private String ovwCntn;
	private String rgstUser;
	private String rgstDtm;
	private String updUser;
	private String updDtm;
}
