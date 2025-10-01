package com.lgcns.svcp.prod.ui.prod.service.dashboard;

import com.lgcns.svcp.prod.ui.prod.dto.Sorting;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.SubscribeTop10SearchPagingDto;

import jakarta.servlet.http.HttpServletResponse;

public interface UiSubscriberTop10Service {
	Object getSubscribeTop10(SubscribeTop10SearchPagingDto searchPaging, Integer page, Integer size);
	void exportExcel(String type, String searchBy, String searchValue, Sorting sorting, HttpServletResponse response);
}
