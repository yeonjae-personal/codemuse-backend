package com.lgcns.svcp.prod.ui.prod.dto.dashboard.response;

import java.util.ArrayList;
import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.dashboard.CalendarViewDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarViewRespone {
	
	private String date;
	private List<CalendarViewDto> datas = new ArrayList<>();
}
