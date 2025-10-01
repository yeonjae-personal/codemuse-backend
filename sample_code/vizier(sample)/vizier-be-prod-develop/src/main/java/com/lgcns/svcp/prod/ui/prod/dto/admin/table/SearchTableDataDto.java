package com.lgcns.svcp.prod.ui.prod.dto.admin.table;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.common.search.DynamicFieldReq;
import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchTableDataDto extends BasePaginationDto {
	private String tableName;
	private List<String> selectColumns;
	private List<DynamicFieldReq> fieldSearchs;
	private String orderByClause;
}
