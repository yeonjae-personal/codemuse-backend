package com.lgcns.svcp.prod.ui.prod.dto.admin.table;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearcTableStrcReqDto extends BasePaginationDto {
	private String tableTypeCode;
	private String tableName;
	private String useYn;
}
