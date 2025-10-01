package com.lgcns.svcp.prod.ui.prod.dto.publish.item;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChngDataListLSearchReqDto extends BasePaginationDto {
	private String chngDataCode;
	private String chngDataCodeName;
	private String chgDeptName;
	private String chgUser;
}
