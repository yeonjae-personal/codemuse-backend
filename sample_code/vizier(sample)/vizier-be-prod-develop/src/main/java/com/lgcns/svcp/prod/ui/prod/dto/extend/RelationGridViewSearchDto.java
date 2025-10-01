package com.lgcns.svcp.prod.ui.prod.dto.extend;

import java.util.List;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelationGridViewSearchDto extends BasePaginationDto {
	
	private List<String> objUuids;
	private String offerCode;
	private String offerName;
	private String groupCode;
	private String groupName;
	private String relationCode;
	private String relationName;
	private String searchByCode;
	private String searchByName;
}
