package com.lgcns.svcp.prod.ruleengine.dto.category;

import java.util.Date;

import com.lgcns.svcp.prod.util.DateUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RuleInfoDto {
	private String ruleUuid;
	private String ruleName;
	private String ovwCntn;
	private String chgDeptName;
	private String chgUser;
	private String ruleMsg;
	private String useYn;
	private String rgstDtm;
	
	public RuleInfoDto(String ruleUuid, String ruleName, String ovwCntn, String chgDeptName, String chgUser, String ruleMsg, String useYn, Date rgstDtm) {
		this.ruleUuid = ruleUuid;
		this.ruleName = ruleName;
		this.ovwCntn = ovwCntn;
		this.chgDeptName = chgDeptName;
		this.chgUser = chgUser;
		this.ruleMsg = ruleMsg;
		this.useYn = useYn;
		this.rgstDtm = DateUtil.formatDate("yyyy-MM-dd", rgstDtm);
	}
}
