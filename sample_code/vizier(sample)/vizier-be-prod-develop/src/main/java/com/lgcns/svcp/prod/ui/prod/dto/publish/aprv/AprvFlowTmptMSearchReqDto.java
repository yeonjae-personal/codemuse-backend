package com.lgcns.svcp.prod.ui.prod.dto.publish.aprv;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AprvFlowTmptMSearchReqDto extends BasePaginationDto {
	private String aprvFlowTmptCode;
	private String aprvFlowTmptName;
	private String aprvUser;
}
