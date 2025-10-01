package com.lgcns.svcp.prod.ui.prod.dto.dashboard.request;

import java.util.ArrayList;
import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.dashboard.CalendarViewDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarViewRequest {
	
	@NotBlank
	private String date;
	
	@NotBlank
	private String dsbdViewUuid;
	
	private List<CalendarViewDto> datas = new ArrayList<>();
}
