package com.lgcns.svcp.prod.ui.prod.dto.history.detail;

import java.util.List;

import lombok.Data;

@Data
public class HistoryDetailResDto {
	private EventDateDto created;
	private List<ListChanged> changed;
	private EventDateDto ended;
}
