package com.lgcns.svcp.prod.ui.prod.dto.publish.packages;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.BaseDto;
import com.lgcns.svcp.prod.ui.prod.enums.YesNo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PubAprvMDto extends BaseDto {
	private String pubAprvUuid;
	private String pubRqstTaskCode;
	private String pubAprvDscr;
	private YesNo useYn;
	private String aprvFlowTmptCode;
	private String aprvFlowTmptName;
	private String pubAprvRqsttDtm;

	private List<PubAprvStepLDto> pubAprvStepLDtos;
}