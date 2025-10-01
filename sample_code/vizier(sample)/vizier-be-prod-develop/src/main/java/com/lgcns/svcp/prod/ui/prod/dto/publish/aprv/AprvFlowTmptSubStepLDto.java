package com.lgcns.svcp.prod.ui.prod.dto.publish.aprv;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AprvFlowTmptSubStepLDto extends BaseDto {
	private String aprvFlowTmptCode;
	private long sortNo;
	private long subSortNo;
	private String aprvUser;
	private String aprvUserDeptCd;
	private String useYn;
}
