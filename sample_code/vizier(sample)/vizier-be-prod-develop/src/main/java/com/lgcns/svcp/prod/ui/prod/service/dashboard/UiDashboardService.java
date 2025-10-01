package com.lgcns.svcp.prod.ui.prod.service.dashboard;

import java.util.List;
import java.util.Map;

import com.lgcns.svcp.prod.ui.prod.dto.dashboard.DsbdViewDto;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.request.DsbdViewPstRequest;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.response.DsbdMonthlyOfferResponse;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.response.DsbdMonthlyUserGroupOfferResponse;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.response.ItemVolumeRespone;

public interface UiDashboardService {
	Map<String, Object> initData(String userId);
	DsbdViewDto findViewByUuid(String dsbdViewUuid);
	void saveListView(List<DsbdViewPstRequest> requests);
	ItemVolumeRespone getItemsVolume();
	Map<String, List<DsbdMonthlyUserGroupOfferResponse>> getMonthlyReportAboutUsers();
	Map<String, List<DsbdMonthlyOfferResponse>> getMonthlyReportAboutItems();
	List<String> getOfferType();
}
