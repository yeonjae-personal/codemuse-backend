package com.lgcns.svcp.prod.ui.prod.dto.customvalidation;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomValidationPagingResponse {
	
	public CustomValidationPagingResponse(int totalItems, List<Map<String, Object>> data) {
		this.totalItems = totalItems;
		this.data = data;
	}
	
	private int totalPages;
	private int totalItems;
	private List<Map<String, Object>> data;
}
