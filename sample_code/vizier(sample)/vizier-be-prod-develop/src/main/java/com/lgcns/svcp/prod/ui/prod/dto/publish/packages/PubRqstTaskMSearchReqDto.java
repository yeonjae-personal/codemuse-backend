package com.lgcns.svcp.prod.ui.prod.dto.publish.packages;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PubRqstTaskMSearchReqDto extends BasePaginationDto {
	private String pubRqstTaskCode;
	private String pubRqstTaskCodeName;
	private String pubRqstTaskPubr;
	private String pubRqstStusCode;
}
