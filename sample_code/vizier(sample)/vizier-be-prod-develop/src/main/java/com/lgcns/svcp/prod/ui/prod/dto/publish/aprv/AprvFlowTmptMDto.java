package com.lgcns.svcp.prod.ui.prod.dto.publish.aprv;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AprvFlowTmptMDto extends BaseDto {
	private String aprvFlowTmptCode;
	private String aprvFlowTmptName;
	private String aprvFlowTmptDscr;
	private String aprvFlowTmptTypeCode;
	private String useYn;

	private List<AprvFlowTmptStepLDto> aprvFlowTmptStepLs;
}
