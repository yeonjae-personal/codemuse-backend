package com.lgcns.svcp.prod.ui.prod.dto.history.detail;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListChanged {
	private String workDate;
	private List<ChangedResDto> records;
}
