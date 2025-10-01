package com.lgcns.svcp.prod.ui.prod.dto.dashboard;

import java.util.List;

import com.lgcns.svcp.prod.util.paging.BasePaginationDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecentlyWorkSearchPagingDto extends BasePaginationDto {
	
	private String view;
	private String category;
	private String type;
	private String sorting;
	private String searchBy;
	private String searchValue;
	private String objName;
	private String objCode;
	private List<String> sortingList;
}
