package com.lgcns.svcp.prod.ui.prod.dto.customvalidation.history;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryChangeDto {
	
	private String workDate;
	private List<HistoryChangeResDto> records;
}
