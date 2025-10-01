package com.lgcns.svcp.prod.ui.prod.service.dashboard;

import com.lgcns.svcp.prod.ui.prod.dto.Sorting;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.RecentlyWorkSearchPagingDto;

import jakarta.servlet.http.HttpServletResponse;

public interface UiRecentlyWorkService {
	Object getRecentlyWork(RecentlyWorkSearchPagingDto searchPaging, Integer page, Integer size);
	void exportExcel(String category, String type, String searchBy, String searchValue, Sorting sorting, HttpServletResponse response);
}
