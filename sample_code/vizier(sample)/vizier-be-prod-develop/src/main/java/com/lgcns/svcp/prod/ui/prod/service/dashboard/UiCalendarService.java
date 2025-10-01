package com.lgcns.svcp.prod.ui.prod.service.dashboard;

import java.util.List;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.request.CalendarViewRequest;
import com.lgcns.svcp.prod.ui.prod.dto.dashboard.response.CalendarViewRespone;

public interface UiCalendarService {
	void saveCalendar(CalendarViewRequest request);
	List<CalendarViewRespone> getCalendar(String dsbdViewUuid, String dateInput);
}
