package com.lgcns.svcp.prod.ui.prod.dto.admin.table;

import java.util.List;
import java.util.Map;

import com.lgcns.svcp.prod.ui.prod.dto.common.search.DynamicFieldReq;
import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchTableDataReqDto extends BasePaginationDto {
	private List<DynamicFieldReq> fieldSearchs;
	private Map<String, String> fieldSorts;
}
