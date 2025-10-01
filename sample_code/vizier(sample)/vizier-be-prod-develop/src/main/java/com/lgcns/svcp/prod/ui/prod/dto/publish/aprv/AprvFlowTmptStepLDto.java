package com.lgcns.svcp.prod.ui.prod.dto.publish.aprv;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AprvFlowTmptStepLDto extends BaseDto {
	private String aprvFlowTmptCode;
	private long sortNo;
	private String aprvStepCode;
	private long lmtTm;
	private String useYn;

	private List<AprvFlowTmptSubStepLDto> aprvFlowTmptSubStepLs;
}
