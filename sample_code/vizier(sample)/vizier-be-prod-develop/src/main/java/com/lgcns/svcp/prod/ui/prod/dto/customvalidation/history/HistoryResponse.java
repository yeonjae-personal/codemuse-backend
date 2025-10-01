package com.lgcns.svcp.prod.ui.prod.dto.customvalidation.history;

import java.util.List;

import com.lgcns.svcp.prod.ui.prod.dto.history.detail.EventDateDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryResponse {
	
	private EventDateDto created;
	private List<HistoryChangeDto> changed;
	private EventDateDto ended;
}
